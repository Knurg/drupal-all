package lebada.marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.logging.Logger;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;
import lebada.voc.Vocabulary;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.EToken;
import apus.tok.Token;
import apus.tok.TokenUtil;


public class VocabularyMarker extends Marker {
	
	protected HashSet<String> stopWords;
	protected Pattern stopPattern;
	protected HashSet<String> stopPOS;
	protected Vocabulary[] vocs;
	protected boolean caseSensitive;
	protected ExecutorService pool; // = new ThreadPoolExecutor(20,20,10,TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	protected int minLookupWordSize;
	protected boolean useWhiteList;
	
  protected Logger log;
	
	public VocabularyMarker(Options o, Vocabulary[] vocs) {
		super(o);
		
		this.log = Logger.getLogger(this.getClass().getCanonicalName() + "#" + this.getName());

		this.vocs = vocs;
		
		this.stopWords = new HashSet<String>();
		for (String w: options.get("stopWords", "").split("\\s+"))
			stopWords.add(w);
		if (options.exists("stopPattern") && options.get("stopPattern").length() > 0)
			this.stopPattern = Pattern.compile(options.get("stopPattern"));
		else this.stopPattern = null;
		stopPOS = new HashSet<String>((options.exists("stopPOSList"))
          ? Arrays.asList(options.get("stopPOSList", "").split("\\s+"))
          : Arrays.asList("ADV APPR APPRART APPO APZR ART CARD ITJ KOUI KOUS KON KOKOM PDS PDAT PIS PIAT PIDAT PPER PPOSS PPOSAT PRELS PRELAT PRF PWS PWAT PWAV PAV PTKZU PTKNEG PTKVZ PTKANT PTKA TRUNC XY $, $. $(".split(" ")));
		caseSensitive = "true".equalsIgnoreCase(options.get("caseSensitive", "false"));
		minLookupWordSize = Integer.parseInt(options.get("minLookupWordSize", "3"));
		useWhiteList = "true".equalsIgnoreCase(options.get("useWhiteList", "false"));
		
	}
	
	
	private boolean stop(String word) {
		return stopWords.contains(word)
			|| (stopPattern != null && stopPattern.matcher(word).matches());
	}
	
	
	public TermOccurence[] markup0(Token[] tokens) {
		
		List<TermOccurence> markup = Collections.synchronizedList(new ArrayList<TermOccurence>());
		log.finest("marking tokens: " + Arrays.toString(tokens));
		
tokenloop:
		for (int tpos = 0; tpos < tokens.length; tpos++) {
			Token t = tokens[tpos];
			
			// check if this token shall be searched
			if (t.surface.length() < minLookupWordSize) continue;
			if (stop(t.surface)) continue;
			String[] lemmata = (t instanceof EToken) ? ((((EToken) t).info != null) ? (String[]) ((EToken) t).info.get("lemmata") : null) : null;
			if (lemmata != null && lemmata.length != 0) 
				for (String l: lemmata)
					if (l != null && stop(l)) continue tokenloop;
			String pos = (t instanceof EToken) ? ((((EToken) t).info != null) ? (String) ((EToken) t).info.get("pos") : null) : null;
			if (stopPOS != null && ! stopPOS.isEmpty() && stopPOS.contains(pos)) continue tokenloop;
			
			/*ArrayList<Future<?>> jobs = new ArrayList<Future<?>>();*/
			
			for (Vocabulary v: vocs) {
				/*jobs.add(pool.submit*/(new TokVocMarker(tokens, tpos, v, markup)).run()/*)*/;
			}
			
			/*for (Future<?> f: jobs) {
				while (true)
				try {
					f.get();
					break;
				} catch (CancellationException e) {
					break;
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				} 
				
			}*/
			
		}
		
		return markup.toArray(new TermOccurence[markup.size()]);
		
	}
	
	
	public TermOccurence[] markup0(Token[] tokens, Set<String> whiteList) {
		if (whiteList == null) throw new NullPointerException();
		
		List<TermOccurence> markup = Collections.synchronizedList(new ArrayList<TermOccurence>());
		log.finest("marking tokens: " + Arrays.toString(tokens));
		log.finest("whitelist: " + whiteList);
		
tokenloop:
		for (int tpos = 0; tpos < tokens.length; tpos++) {
			Token t = tokens[tpos];
			
			// check if this token shall be searched
			if (t.surface.length() < minLookupWordSize) continue;
			if (stop(t.surface)) continue;
			boolean onWhiteList = false;
			if (whiteList.contains(t.surface.toLowerCase())) onWhiteList = true;
			String[] lemmata = (t instanceof EToken) ? ((((EToken) t).info != null) ? (String[]) ((EToken) t).info.get("lemmata") : null) : null;
			if (lemmata != null && lemmata.length != 0) {
				for (String l: lemmata) {
					if (l != null && stop(l)) continue tokenloop;
					if (whiteList.contains(l.toLowerCase())) onWhiteList = true;
				}
			}
			if (!onWhiteList) {
				log.finer("not on whitelist; skip: " + TokenUtil.toString(t));
				continue;
			}
			
			String pos = (t instanceof EToken) ? ((((EToken) t).info != null) ? (String) ((EToken) t).info.get("pos") : null) : null;
			if (stopPOS != null && ! stopPOS.isEmpty() && stopPOS.contains(pos)) continue tokenloop; 
			
			/*ArrayList<Future<?>> jobs = new ArrayList<Future<?>>();*/
			
			for (Vocabulary v: vocs) {
				/*jobs.add(pool.submit*/(new TokVocMarker(tokens, tpos, v, markup)).run()/*)*/;
			}
			
			/*for (Future<?> f: jobs) {
				while (true)
				try {
					f.get();
					break;
				} catch (CancellationException e) {
					break;
				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
					throw new RuntimeException(e);
				} 
				
			}*/
			
		}
		
		return markup.toArray(new TermOccurence[markup.size()]);
		
	}
	
	
	public TermOccurence[] markup(Token[] tokens, Set<String> whiteList) {
		
		TermOccurence[] tos;
		if (whiteList == null || !useWhiteList) tos = markup0(tokens);			
		else tos = markup0(tokens, whiteList);
		
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
		Vocabulary v;
		List<TermOccurence> l;
		
		TokVocMarker (Token[] ta, int tpos, Vocabulary v, List<TermOccurence> l) {
			this.ta=ta;
			this.tpos=tpos;
			this.v=v;
			this.l=l;
		}
		
		@Override
		public void run() {
			Token t = ta[tpos];
			
			Map<String, List<FS>> terms = new HashMap<String, List<FS>>();
			String[] lemmata = (t instanceof EToken) ? ((((EToken) t).info != null) ? (String[]) ((EToken) t).info.get("lemmata") : new String[0]) : new String[0];
			
			// get for surface
			for (FS fs: v.lookupTermsStartingWith(t.surface)) {
				if (!terms.containsKey(fs.getString("term"))) terms.put(fs.getString("term"), new LinkedList<FS>()); 
				terms.get(fs.getString("term")).add(fs);
			}

			// get for each lemma if different from surface
			if (lemmata.length > 0) {
				for (String l: lemmata) {
					if (l != null && ! l.equals(t.surface)) {
						for (FS fs: v.lookupTermsStartingWith(l)) {
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
	
}
