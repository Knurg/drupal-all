package eu.wiss_ki.aapi;

import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;
import lebada.marker.Marker;
import lebada.marker.TermOccurence;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import apus.tok.EToken;
import apus.tok.Token;
import apus.tok.TokenUtil;
import de.fau.cs8.mnscholz.util.options.Options;


public class LNAMarker extends Marker {
	
	protected HashSet<String> stopWords;
	protected Pattern stopPattern;
	protected HashSet<String> stopPOS;
	protected boolean caseSensitive;
	protected ExecutorService pool;// = new ThreadPoolExecutor(20,20,10,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	protected int minLookupWordSize;
	protected boolean useWhiteList;
	protected String lookupURL;
	
	protected Logger log;
	
	public LNAMarker(Options o) {
		super(o);
		
		this.log = Logger.getLogger(this.getClass().getCanonicalName() + "#" + this.getName());

		this.stopWords = new HashSet<String>();
		for (String w: options.get("stopWords", "").split("\\s+"))
			stopWords.add(w);
		if (options.exists("stopPattern") && options.get("stopPattern").length() > 0)
			this.stopPattern = Pattern.compile(options.get("stopPattern"));
		else this.stopPattern = null;
		stopPOS = new HashSet<String>(Arrays.asList(options.get("stopPOSList", "ADV APPR APPRART APPO APZR ART CARD ITJ KOUI KOUS KON KOKOM PDS PDAT PIS PIAT PIDAT PPER PPOSS PPOSAT PRELS PRELAT PRF PWS PWAT PWAV PAV PTKZU PTKNEG PTKVZ PTKANT PTKA TRUNC XY $, $. $(").split("\\s+")));
		caseSensitive = "true".equalsIgnoreCase(options.get("caseSensitive", "false"));
		minLookupWordSize = Integer.parseInt(options.get("minLookupWordSize", "3"));
		useWhiteList = "true".equalsIgnoreCase(options.get("useWhiteList", "false"));
		lookupURL = options.get("lookupURL", null);
		if (lookupURL == null) throw new NullPointerException();
		options.set("name", "lna0");
		
	}
	
	
	private boolean stop(String word) {
		return stopWords.contains(word)
			|| (stopPattern != null && stopPattern.matcher(word).matches());
	}
	
	
	public TermOccurence[] markup0(Token[] tokens) {
		return markup0(tokens, null);
	}
	
	
	public TermOccurence[] markup0(Token[] tokens, Set<String> whiteList) {
		
		List<TermOccurence> markup = Collections.synchronizedList(new ArrayList<TermOccurence>());
		log.finest("marking tokens: " + Arrays.toString(tokens));
		log.finest("whitelist: " + whiteList);
		
tokenloop:
		for (int tpos = 0; tpos < tokens.length; tpos++) {
			Token t = tokens[tpos];
			
			// check if this token shall be searched
			if (t.surface.length() < minLookupWordSize) continue;
			if (stop(t.surface)) continue;
			boolean onWhiteList = whiteList == null;
			if (whiteList.contains(t.surface.toLowerCase())) onWhiteList = true;
			String[] lemmata = (t instanceof EToken) ? ((((EToken) t).info != null) ? (String[]) ((EToken) t).info.get("lemmata") : null) : null;
			if (lemmata != null && lemmata.length != 0) {
				for (String l: lemmata) {
					if (l != null && stop(l)) continue tokenloop;
					if (l != null && whiteList.contains(l.toLowerCase())) onWhiteList = true;
				}
			}
			if (!onWhiteList) {
				log.finer("not on whitelist; skip: " + TokenUtil.toString(t));
				continue;
			}
			
			String pos = (t instanceof EToken) ? ((((EToken) t).info != null) ? (String) ((EToken) t).info.get("pos") : null) : null;
			if (stopPOS != null && ! stopPOS.isEmpty() && stopPOS.contains(pos)) continue tokenloop; 
			
			(new TokVocMarker(tokens, tpos, markup)).run();
			
		}
		
		return markup.toArray(new TermOccurence[markup.size()]);
		
	}
	
	
	public TermOccurence[] markup(Token[] tokens, Set<String> whiteList) {
		
		TermOccurence[] tos = markup0(tokens, whiteList);
		
		String name = this.getName();
		
		if (name != null) {
			// add marker name to term occurences
			for (int i = 0; i < tos.length; i++) {
				CFS cfs = FSUtil.newCFS(tos[i]);
				cfs.set("marker", name);
				tos[i] = new TermOccurence(tos[i].start, tos[i].end, cfs);
			}
		}
			
		return tos;

	}
	
	
	private class TokVocMarker implements Runnable {
		
		Token[] ta;
		int tpos;
		List<TermOccurence> l;
		
		TokVocMarker (Token[] ta, int tpos, List<TermOccurence> l) {
			this.ta=ta;
			this.tpos=tpos;
			this.l=l;
		}
		
		@Override
		public void run() {
			Token t = ta[tpos];
			
			Map<String, List<FS>> terms = new HashMap<String, List<FS>>();
			String[] lemmata = (t instanceof EToken) ? ((((EToken) t).info != null) ? (String[]) ((EToken) t).info.get("lemmata") : new String[0]) : new String[0];
			
			// get for surface
			for (FS fs: lookup(t.surface)) {
				if (!terms.containsKey(fs.getString("term"))) terms.put(fs.getString("term"), new LinkedList<FS>()); 
				terms.get(fs.getString("term")).add(fs);
			}

			// get for each lemma if different from surface
			if (lemmata.length > 0) {
				for (String l: lemmata) {
					if (l != null && ! l.equals(t.surface)) {
						for (FS fs: lookup(l)) {
							if (!terms.containsKey(fs.getString("term"))) terms.put(fs.getString("term"), new LinkedList<FS>()); 
							terms.get(fs.getString("term")).add(fs);
						}
					}
				}
			}
			
loop_check_terms:
			for (String term: terms.keySet()) {
				// tokenize the term 
				String[] termTokens = term.split("\\s+");
				
				// too long for remaining tokens
				if (tpos + termTokens.length > ta.length) continue;
				
				// check for each token if matches
loop_check_term_tokens:
				for (int i = 0; i < termTokens.length; i++) {
					Token n = ta[tpos + i];
					if ((caseSensitive && n.surface.equals(termTokens[i])) ||
						(!caseSensitive && n.surface.equalsIgnoreCase(termTokens[i]))) continue loop_check_term_tokens;
					if (lemmata != null && lemmata.length > 0) {
						for (String l: lemmata) {
							if (l != null && ((caseSensitive && l.equals(termTokens[i])) ||
								(!caseSensitive && l.equalsIgnoreCase(termTokens[i])))) {
								continue loop_check_term_tokens;
							}
						}
					}
					// neither surface nor lemmata match term token => not that term
					continue loop_check_terms;
				}
				
				// checks ok, mark!
				for (FS fs: terms.get(term))
					l.add(new TermOccurence(tpos, tpos + termTokens.length, fs));
				
			}
			
		}
		
	}
	
	
	public List<FS> lookup(String term) {
		List<FS> res = new ArrayList<FS>();
		try {
			
			URL url = new URL(lookupURL + URLEncoder.encode(term, "utf-8"));
			log.finest("query " + lookupURL + URLEncoder.encode(term, "utf-8"));
			URLConnection conn = url.openConnection();
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());
			StringWriter w = new StringWriter();
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(w));
			log.finest("result:\n" + w.toString());
			NodeList l = doc.getElementsByTagName("term");
			
			for (int i = l.getLength() - 1; i >= 0; i--) {
				
				Element eterm = (Element) l.item(i);
				int prio = Integer.parseInt(eterm.getAttribute("priority"));
				String tt = eterm.getAttribute("termType");
				String voc = eterm.getAttribute("authority");
				String term1 = eterm.getAttribute("term");
				
				Element eeid = (Element) eterm.getFirstChild(); 
				do {
					res.add(
						FSUtil.newFS(
							"term", term1,
							"entryID", eeid.getTextContent(),
							"vocabulary", voc,
							"type", tt,
							"rank", prio,
							"canonical", term1
						)	
					);
				} while ((eeid = (Element) eeid.getNextSibling()) != null);
				
			}
		} catch (Exception e) {
			log.log(Level.WARNING, "lookup", e);
			return Collections.emptyList();
		}
		
		Collections.sort(res, new Comparator<FS>() {

			@Override
			public int compare(FS o1, FS o2) {
				return o1.getInt("rank") - o2.getInt("rank");
			}
			
		});
		
		return res;
	}
	
}
