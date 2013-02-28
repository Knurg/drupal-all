/**
 * 
 */
package eu.wiss_ki.aapi.editingSession;

import java.net.URL;
import java.net.URLConnection;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

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
import apus.tok.Tokenizer;
import eu.wiss_ki.aapi.Configuration;

class Annotator {
	
	private static Comparator<TermOccurence> TO_RANK = new Comparator<TermOccurence>() {
		
		@Override
		public int compare(TermOccurence o1, TermOccurence o2) {
			int r1 = o1.hasFeatureName("rank") ? o1.getInt("rank") : 0;
			int r2 = o2.hasFeatureName("rank") ? o2.getInt("rank") : 0;
			return r2 - r1;
		}
	}; 
	
	
	private Logger log;
	
	private Object lockAnno;
	
	public final Configuration config;
	public final String name;
	public final Set<Annotation> annos;
	public final Set<Annotation> toBeSent;
	public final Set<TermOccurence> termOccurences = new HashSet<TermOccurence>();
	
	Map<Token, Integer> token2Offsets = new HashMap<Token, Integer>();
	LinkedList<EToken> tokens = new LinkedList<EToken>();
	
	/**
	 * @param block
	 */
	Annotator(String name, Configuration config, Set<Annotation> annos, Set<Annotation> toBeSent, Object lockAnno) {
		
		this.log = Logger.getLogger(this.getClass().getCanonicalName() + ":" + name);
		
		this.name = name;
		this.config = config;
		this.annos = annos == null ? new HashSet<Annotation>() : annos;
		this.toBeSent = toBeSent == null ? new HashSet<Annotation>() : toBeSent;
    this.lockAnno = lockAnno;
		
	}
	
	public void reparse (String text) {
		
		if (config.disableAutomaticAnnotation) {
			log.info("automatic annotation disabled");
			return;
		}
		
		Set<String> whitelistLNA = null;
		if (config.possibleLNATermsURL != null) {
			try {
				whitelistLNA = new HashSet<String>();
				URL u = new URL(config.possibleLNATermsURL);
				URLConnection c = u.openConnection();
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(c.getInputStream());
				NodeList l = doc.getElementsByTagName("term");
				//Tokenizer ptok = Tokenizer.protoTokenizer();
				for (int i = l.getLength() - 1; i >= 0; i--) {
					String ts = ((Element) l.item(i)).getTextContent();
//					Tokenizer tok = ptok.tokenize(((Element) l.item(i)).getTextContent());
//					Token t;
//					while ((t = tok.read()) != null) {
//						if (t.surface.matches(".*(\\d|\\p))
//					}
					String[] terms = ts.toLowerCase().trim().split("[[^\\d]&&[^\\p{L}]]+");
					whitelistLNA.addAll(Arrays.asList(terms));
				}
				
			} catch (Exception e) {
				log.log(Level.WARNING, "reading whitelist for LNAs");
				whitelistLNA = null;
			}
			
		}
		
		
		log.info("start automatic text analysis");
		
		Tokenizer tok = config.protoTokenizer.tokenize(text);
		EToken t = null;
		Token t0 = null;
		
		LinkedList<EToken> wordlist = new LinkedList<EToken>();
		
		int length = 0;
		
		while ((t0 = tok.read()) != null) {
			
			t = new EToken(t0.surface, t0.type, FSUtil.newCFS());
			tokens.add(t);
			
			if (t.isType(Tokenizer.BLACK)) wordlist.add(t);
			
			token2Offsets.put(t, length);
			length += t.surface.length();
			
		}
		
		log.finest("analysing words: " + wordlist);
			
		EToken[] words = wordlist.toArray(new EToken[wordlist.size()]);
		config.taggerMorph.parsePOSLemmata(words);
			
		log.finest("tagged & lemmatized: " + wordlist);
			
		List<TermOccurence> tos = new ArrayList<TermOccurence>();
		for (Marker m: config.entityMarkers) {
			
			log.finest("running entity marker " + m.getName());
			TermOccurence[] mtos;
//			if (whitelistLNA != null && m instanceof VocabularyMarker) {
//				mtos = ((VocabularyMarker) m).markup(words, whitelistLNA);
//			} else {
				mtos = m.markup(words);
//			}
			log.finer("entity marker " + m.getName() + " found:\n" + Arrays.toString(mtos));
			for (TermOccurence to: mtos) {
				int rank = config.priorities.get(m);						// ranking base: vocab priority
        int rank_pr = rank;
				if (to.hasFeatureName("rank")) rank += to.getInt("rank"); 	// maybe marker extra rank
        int rank_to = rank;
				rank += to.end - to.start;									// token count gives extra points
				FS addinfo = FSUtil.newFS("rank", rank, "rank_pr", rank_pr, "rank_to", rank_to);
				to = token2charPos(words, to);
				tos.add(new TermOccurence(to.start, to.end, FSUtil.mergeFS(to, addinfo)));
			}
		}
		
		// hack for speedup: LNAs are queried by a single marker
		TermOccurence[] mtos2 = config.lnaMarker.markup(words, whitelistLNA);
		log.finer("LNA entity marker found:\n" + Arrays.toString(mtos2));
		for (TermOccurence to: mtos2) {
			int rank = to.getInt("rank"); 	// maybe marker extra rank
    int rank_to = rank;
			rank += to.end - to.start;		// token count gives extra points
			FS addinfo = FSUtil.newFS("rank", rank, "rank_to", rank_to);
			to = token2charPos(words, to);
			tos.add(new TermOccurence(to.start, to.end, FSUtil.mergeFS(to, addinfo)));
		}
		
		for (Marker m: config.eventMarkers) {
			log.finest("running event marker " + m.getName());
			TermOccurence[] mtos = m.markup(words);
			log.finer("entity marker " + m.getName() + " found:\n" + Arrays.toString(mtos));
			for (TermOccurence to: mtos) {
				int rank = 0;												// rank base is 0
				if (to.hasFeatureName("rank")) rank += to.getInt("rank"); 	// maybe marker extra rank
				rank += to.getString("matchingTokens").split(",").length;	// token count gives extra points
				FS addinfo = FSUtil.newFS("rank", rank, "type", config.eventType, "vocabulary", config.eventType, "new", true, "eventType", "ecrm:" + to.getString("eventType"));
				to = token2charPos(words, to);
				tos.add(new TermOccurence(to.start, to.end, FSUtil.mergeFS(to, addinfo)));
			}
		}
		
		Collections.sort(tos, TO_RANK);
		
    StringBuilder logbuf = new StringBuilder();
    for (TermOccurence to: tos)
      logbuf.append(to.getInt("rank_pr")).append(", ").append(to.getInt("rank_to")).append(", ").append(to.getInt("rank")).append(":\t").append(to.getString("term")).append("\n");
    log.warning("rankings:\n" + logbuf.toString());
    
		for (TermOccurence to: tos) {
				
			boolean contained = false;
			
			synchronized (lockAnno) {
				
				for (TermOccurence oldto: termOccurences) if (oldto.equals(to)) contained = true;
				if (!contained) {
					
					termOccurences.add(to);
					log.finer("registered term occurence " + to);
					
					boolean top = true;
					for (Annotation a: annos) {
						if (to.overlapsWith(a)) {
							top = false;
							break;
						}
					}
					
					if (top) {
						Annotation a = termOcc2Anno(to);
						if (a != null) {
							toBeSent.add(a);
							annos.add(a);
							log.finer("registered term occurence " + to + " as prefered annotation " + AnnoUtil.toString(a));
						}
					}
					
				} else {
					
					log.finer("already found term occurence " + to);
					
				}
				
			}
			
		}
		
	}
	
	// convert token position to character position (markers' TermOccurence has token position!)
	TermOccurence token2charPos (Token[] refTokens, TermOccurence to) {
		
		if (token2Offsets.get(refTokens[to.start]) == null) {
			System.err.println(token2Offsets);
			System.err.println();
		}
		
		int s = token2Offsets.get(refTokens[to.start]);
		Token et = refTokens[to.end - 1];
		int e = token2Offsets.get(et) + et.surface.length();
		log.finest("start="+to.start+" end="+to.end+"\nreftokens="+refTokens+"\ns="+s+" e="+e);
		
		return new TermOccurence(s, e, to.infos);
	}
	
	
	Annotation termOcc2Anno (TermOccurence to) {
		return termOcc2Anno(to, config.idgen.createID());
	}
		
	Annotation termOcc2Anno (TermOccurence to, String id) {
		
		CFS infos = FSUtil.newCFS(to, "aid", id, AnnotatorConstants.PROPOSED_ANNOTATION, true);
		
		if (infos.hasFeatureName("marker") && config.expandingEntityMarkers.contains(infos.getString("marker"))) {
			infos.set(AnnotatorConstants.NEW_VOCABULARY_ENTRY, true);
		} else {
			infos.set(AnnotatorConstants.EXISTING_VOCABULARY_ENTRY, true);
		}
		
		to = new TermOccurence(to.start, to.end, Util.enrich(config.vocabularies, to));
		
		return new Annotation(to, name, infos);
		
	}
	
	

	public boolean equals(TermOccurence to1, TermOccurence to2) {
		
		if (to1.start != to2.start || to1.end != to2.end) return false;
		
		String[] keys1 = to1.getFeatureNames();
		String[] keys2 = to2.getFeatureNames();
		if (keys1.length != keys2.length) return false;
		
		for (String key: keys1) {
			if (!to2.hasFeatureName(key)) return false;
			Object v1 = to1.get(key);
			Object v2 = to2.get(key);
			if (!v1.equals(v2)) return false;
		}
		
		return true;
		
	}

	
}
