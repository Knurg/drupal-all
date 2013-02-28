package lebada.marker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import apus.tok.EToken;
import apus.tok.Token;
import de.fau.cs8.mnscholz.util.ArraysUtil;
import de.fau.cs8.mnscholz.util.StringUtil;
import de.fau.cs8.mnscholz.util.options.Options;
import de.fau.cs8.mnscholz.util.xml.DOMUtil;

public class SimpleEventMarker extends Marker {
	
	private static int[] createIntArray(int length, int fill) {
		int[] a = new int[length];
		Arrays.fill(a, fill);
		return a;
	}
	
	protected class Event implements Cloneable {
		
		String eventType;
		String[] words;
		String[][] posConstraints;
		String toBeMarked;
		Event(String eventType, String[] words, String[][] posConstraints,
				String toBeMarked) {
			super();
			this.eventType = eventType;
			this.words = words;
			this.posConstraints = posConstraints;
			this.toBeMarked = toBeMarked;
		}
		
		EventInst makeInstance (String token, int tokenPos) {
			int i = ArraysUtil.indexOf(words, token);
			if (i < 0) return null; 
			EventInst ei = new EventInst(this);
			ei.wordTokens[i] = tokenPos;
			if (toBeMarked.equals(token)) ei.toBeMarkedToken = tokenPos;
			return ei;
		}

		public String toString() {
			return eventType + ": " + Arrays.toString(words) + "/" + toBeMarked;
		}
		
	}
	
	protected class EventInst {
		
		Event event;
		int[] wordTokens;
		int toBeMarkedToken;
		EventInst(Event event) {
			this(event, createIntArray(event.words.length, -1), -1);
		}
		EventInst(Event event, int[] wordTokens, int toBeMarkedToken) {
			super();
			this.event = event;
			this.wordTokens = wordTokens;
			this.toBeMarkedToken = toBeMarkedToken;
		}
		
		EventInst clone(String token, int tokenPos) {
			int i = ArraysUtil.indexOf(event.words, token);
			EventInst ei = new EventInst(event, wordTokens.clone(), toBeMarkedToken);
			ei.wordTokens[i] = tokenPos;
			if (event.toBeMarked.equals(token)) ei.toBeMarkedToken = tokenPos;
			return ei;
		}
		
	}
	
	

	protected Logger log;
	
	protected Map<String, Set<Event>> events = new HashMap<String, Set<Event>>();
	protected Map<String, List<String>> expandPOS = new HashMap<String, List<String>>();
	
	public SimpleEventMarker(Options options) {
		super(options);
		
		log = Logger.getLogger(this.getClass().getCanonicalName() + "#" + this.getName());
		
		expandPOS.put("a", Arrays.asList("ADJA", "ADJD", "ADV"));
		expandPOS.put("n", Arrays.asList("NN", "NE"));
		expandPOS.put("v", Arrays.asList("VVFIN", "VVIMP", "VVINF", "VVIZU", "VVPP", "VAFIN", "VAIMP", "VAINF", "VAPP", "VMFIN", "VMINF", "VMPP"));
		
		loadFromTextFiles();
		loadFromXMLFiles();
		log.finest("# of events loaded: " + events.size());

	}

	
	@Override
	protected TermOccurence[] markup0(Token[] tokens) {
		
		String[] surfaces = new String[tokens.length];
		FS[] infos = new FS[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			surfaces[i] = tokens[i].surface;
			infos[i] = (tokens[i] instanceof EToken) ? ((EToken) tokens[i]).info : FSUtil.EMPTY_FS;
		}
		List<TermOccurence> tos = new ArrayList<TermOccurence>();
		for(FS r: markup(surfaces, infos)) {
			int s = r.getInt("start");
			int e = r.getInt("end");
			CFS cfs = FSUtil.newCFS(r);
			cfs.unset("start");
			cfs.unset("end");
			tos.add(new TermOccurence(s, e, cfs));
		}
		return tos.toArray(new TermOccurence[tos.size()]);
		
	}
	
	public FS[] markup(String[] tokens, FS[] tokenInfos) {

    boolean checkpos = "true".equalsIgnoreCase(this.options.get("checkPOS", "true"));
		boolean includeMatchingTokens = "true".equalsIgnoreCase(this.options.get("considerMatchingTokens", "true")); 
		String[][] forms = new String[tokens.length][];
		String[] poss = new String[tokens.length];
		
		Set<String> tmpset = new TreeSet<String>(); 
		for (int i = 0; i < tokens.length; i++) {
			tmpset.clear();
			tmpset.add(tokens[i].toLowerCase());
			if (tokenInfos != null && tokenInfos[i] != null && tokenInfos[i].hasFeatureName("lemmata") && tokenInfos[i].get("lemmata") != null) {
				for (String l: (String[]) tokenInfos[i].get("lemmata")) 
					if (l != null) tmpset.add(l.toLowerCase());
			}
			forms[i] = tmpset.toArray(new String[tmpset.size()]);
			poss[i] = (tokenInfos != null && tokenInfos[i] != null && tokenInfos[i].hasFeatureName("pos")) ? tokenInfos[i].getString("pos") : null;
		}
		
		Set<EventInst> insts = new HashSet<EventInst>();
		Set<EventInst> tmpinst = new HashSet<EventInst>();
		
		for (int i = 0; i < forms.length; i++) {
			
			for (String form: forms[i]) {
				
				tmpinst.clear();
				tmpinst.addAll(insts);
				for (EventInst ei: tmpinst) {
					int p = ArraysUtil.indexOf(ei.event.words, form);
					if (p < 0 || ei.wordTokens[p] >= 0) continue;
					if (checkpos && poss[i] != null && ei.event.posConstraints[p] != null && Arrays.binarySearch(ei.event.posConstraints[p], poss[i]) < 0) continue;

					EventInst ei1 = ei.clone(form, i);
					
					insts.add(ei1);
				}
				
				if (events.containsKey(form)) {
					for (Event e: events.get(form)) {
						int p = ArraysUtil.indexOf(e.words, form);
						if (p < 0) continue;
						if (checkpos && poss[i] != null && e.posConstraints[p] != null && Arrays.binarySearch(e.posConstraints[p], poss[i]) < 0) continue;
						
						insts.add(e.makeInstance(form, i));
					}
				}
				
			}
			
		}
		
		List<FS> ret = new ArrayList<FS>();
		StringBuilder toks = new StringBuilder();
		
inst_loop:
		for (EventInst ei: insts) {
			
			if (ei.toBeMarkedToken < 0) continue;
			
			toks.setLength(0);
			for (int i: ei.wordTokens) {
				if (i < 0) continue inst_loop;
				if (toks.length() == 0) toks.append(i); else toks.append(',').append(i);
			}
			
			if (! includeMatchingTokens) {
				for (FS ofs: ret) {
					if (ofs.getString("eventType") == ei.event.eventType
						&& ofs.getString("term").equals(StringUtil.join(" ", ei.event.words))
						&& ofs.getInt("start") == ei.toBeMarkedToken)
						continue inst_loop;
				}
			}
			
			ret.add(FSUtil.newFS(
					"start", ei.toBeMarkedToken,
					"end", ei.toBeMarkedToken + 1,
					"matchingTokens", toks.toString(),
					"term", StringUtil.join(" ", ei.event.words),
					"eventType", ei.event.eventType));
		}
	
    log.finest("found events: " + ret);
		return ret.toArray(new FS[ret.size()]);
		
	}
	
	
	
	private void loadFromTextFiles () {
		
		String filenames = options.get("textfiles", "");
		if (filenames.isEmpty()) return;
		
		for (String filename: filenames.split(";")) {
			
			try {
				
				BufferedReader r = new BufferedReader(new FileReader(filename));
				String line;
				while ((line = r.readLine()) != null) {
					int i = line.indexOf(':');
					if (i < 0) continue;
					
					String event = line.substring(0, i);
					
					for (String colloc: line.substring(i + 1).split(",")) {
						
						String toBeMarked = null;
						List<String> wordlist = new ArrayList<String>();
						List<String[]> posConstraints = new ArrayList<String[]>();
						
						String[] words = colloc.trim().split("\\s+");
						
						for (String word: words) {
							String[] tmp = StringUtil.split(word, "/", 0);
							wordlist.add(tmp[0].toLowerCase());
							if (tmp.length == 1) {
								posConstraints.add(null);
							} else {
								List<String> posc = new ArrayList<String>();
								for (int j = 1; j < tmp.length; j++) {
									if ("!".equals(tmp[j])) {
										toBeMarked = tmp[0];
									} else {
										if (expandPOS.containsKey(tmp[j])) {
											posc.addAll(expandPOS.get(tmp[j]));
										} else {
											posc.add(tmp[j]);
										}
									}
								}
								Collections.sort(posc);
								posConstraints.add(posc.isEmpty() ? null : posc.toArray(new String[posc.size()])); 
							}
						}
						
						if (toBeMarked == null) {
							toBeMarked = wordlist.get(0);
						}
						
						Event e = new Event(event, wordlist.toArray(new String[wordlist.size()]), posConstraints.toArray(new String[posConstraints.size()][]), toBeMarked);
						
						for (int k = 0; k < wordlist.size(); k++) {
							
							if (!events.containsKey(wordlist.get(k))) events.put(wordlist.get(k), new HashSet<Event>());
							events.get(wordlist.get(k)).add(e);
							
						}
						
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		}
		
		
	}
	
	private void loadFromXMLFiles () {
		
		String filenames = options.get("xmlfiles", "");
		if (filenames.isEmpty()) return;
		
		for (String filename: filenames.split(";")) {
			
			try {
				
				Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filename));
				
				for (Element event: DOMUtil.allElements(xml, "//event")) {
					
					String eventType = event.getAttribute("name");
					
					for (Element word: DOMUtil.allElements(event, "word")) {
						
						String pos = word.getAttribute("pos").toLowerCase();
						String lemma = word.getTextContent();
						String[] poss = (expandPOS.containsKey(pos)) ? expandPOS.get(pos).toArray(new String[0]) : new String[]{pos};
						
						Event e = new Event(eventType, new String[]{lemma}, new String[][]{poss}, lemma);
						if (!events.containsKey(lemma)) events.put(lemma, new HashSet<Event>());
						events.get(lemma).add(e);
						
					}

					for (Element phrase: DOMUtil.allElements(event, "phrase")) {
						
						Element[] words = DOMUtil.allElements(phrase, "word");
						String[] lemmata = new String[words.length];
						String[][] poss = new String[words.length][];
						String reftoken = null;
						
						for (int i = 0; i < words.length; i++) {
							
							String pos = words[i].getAttribute("pos").toLowerCase();
							lemmata[i] = words[i].getTextContent();
							poss[i] = (expandPOS.containsKey(pos)) ? expandPOS.get(pos).toArray(new String[0]) : new String[]{pos};
							String isRefToken = words[i].getAttribute("reftoken");
							if ("true".equalsIgnoreCase(isRefToken) && reftoken != null) throw new RuntimeException("more than one reftoken at phrase " + phrase.getTextContent());
							else if ("true".equalsIgnoreCase(isRefToken)) reftoken = lemmata[i];
							
						}
						
						if (reftoken == null) reftoken = lemmata[0];

						Event e = new Event(eventType, lemmata, poss, reftoken);
						for (String lemma: lemmata) {
							if (!events.containsKey(lemma)) events.put(lemma, new HashSet<Event>());
							events.get(lemma).add(e);
						}
						
					}

				}
				
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
	}

}
