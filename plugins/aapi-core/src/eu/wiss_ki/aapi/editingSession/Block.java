package eu.wiss_ki.aapi.editingSession;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;
import lebada.marker.TermOccurence;
import lebada.voc.Vocabulary;
import de.fau.cs8.mnscholz.util.ArraysUtil;
import de.fau.cs8.mnscholz.util.Span;
import de.fau.cs8.mnscholz.util.collection.CollectionUtil;
import eu.wiss_ki.aapi.Configuration;

public class Block implements EditorConstants {
	
	final Logger log;
	
	public final String blockID;
	private int revision;
	
	private Object lockText = new Object();
	private Object lockAnno = new Object();
	
	private StringBuilder text = new StringBuilder();
	
	public Set<Annotation> toBeSent = new HashSet<Annotation>();
	public Set<Annotation> annos = new HashSet<Annotation>();
	
	public Set<Annotation> annosContext;
	
	Configuration config;
	
	private Annotator annotator;
	
	private boolean setup = false;
	
	
	public Block (String blockID, Configuration config, Set<Annotation> annosContext) {
		this.blockID = blockID;
		log = Logger.getLogger(this.getClass().getCanonicalName() + "#" + blockID);
		this.config = config;
		this.annosContext = annosContext;
	}
	
	
	public void reset (int revision) {
		
		synchronized (lockText) {
			
			this.revision = revision;
			
			// reset
			text = new StringBuilder();
			annos = new HashSet<Annotation>();
			toBeSent = new HashSet<Annotation>();
			
			// start anew
			Annotator a = new Annotator(blockID, config, annos, toBeSent, lockAnno);
			annotator = a;
			
			setup = true;
			
		}
			
	}
	
	public int getRevision() { return revision; }
	
	
	public void append (CharSequence cs) {
		
		synchronized (lockText) {
			
			text.append(cs);
			annotator.reparse(text.toString());

		}
		
	}
	
	
	public void setAnnotation (Annotation a) {
		
//		if (a.hasFeatureName(AnnotatorConstants.PROPOSED_ANNOTATION) && true == a.getBoolean(AnnotatorConstants.PROPOSED_ANNOTATION)) {
//
//			// proposed annotations will only come on new text revision.
//			// Thus the annotator annos are newer and override this annotation
//			// if they overlap (ie. a is discarded)
//			log.finer("want to set proposed annotation: " + AnnoUtil.toString(a));
//			if (! CollectionUtil.filterCopy(annos, AnnoUtil.overlapsWithFilter(a)).isEmpty()) return;
//			if (! CollectionUtil.filterCopy(toBeSent, AnnoUtil.overlapsWithFilter(a)).isEmpty()) return;
//			log.finer("annotation set!");
//			
//		} else {
		
		synchronized (lockAnno) {
			
			// a user annotation will always override other annotations
			log.finer("want to set annotation: " + AnnoUtil.toString(a));
			for (Annotation overlapping: CollectionUtil.filterCopy(annos, AnnoUtil.overlapsWithFilter(a))) {
				annos.remove(overlapping);
				log.finest("causes remove of annotation: " + AnnoUtil.toString(overlapping));
			}
			for (Annotation overlapping: CollectionUtil.filterCopy(toBeSent, AnnoUtil.overlapsWithFilter(a))) {
				toBeSent.remove(overlapping);
				log.finest("causes remove of new annotation: " + AnnoUtil.toString(overlapping));
			}
			
			annos.add(a);
			
		}
		
	}
	
	
	public void resetAnnotation (String id) {
		
		synchronized (lockAnno) {
			
			Iterator<Annotation> it = annos.iterator();
			while (it.hasNext()) {
				if (it.next().id.equals(id)) {
					it.remove();
					break;
				}
			}
		
		}
		
	}
	
	
	public void removeAnnotation (Span span) {
		
		synchronized (lockAnno) {
			
			for (Annotation a: annos) {
				if (a.start == span.start && a.end == span.end) {
					annos.remove(a);				
					annos.add(new Annotation(span, blockID, FSUtil.newFS("deleted", true)));
				}
			}
		
		}
		
	}
	
	
	public List<Annotation> getAnnotations (Span span, String[] types, String id) {
		
		
			
		log.finest("span: " + span + ", types: " + Arrays.toString(types) + ", id: " + id);
		List<Annotation> ret = new LinkedList<Annotation>();
		
		log.finest("setup: " + isSetup() + ", annotator: " + annotator + (annotator != null ? (", to: "+annotator.termOccurences) : ""));
		
		synchronized (lockAnno) {
			
			for (TermOccurence to: annotator.termOccurences) {
				if (span.overlapsWith(to) && ArraysUtil.indexOf(types, to.getString("type")) >= 0) {
					Annotation a = annotator.termOcc2Anno(to, id);
					CFS cfs = FSUtil.newCFS(a);
					cfs.unset(AnnotatorConstants.PROPOSED_ANNOTATION);
					cfs.set(AnnotatorConstants.APPROVED_ANNOTATION, true);
					ret.add(new Annotation(a, cfs));
				}
			}
		
		}
		
		log.finest("collectd annos 1/3: " + ret);
		
		String txt = text.substring(span.start, span.end);
		
		appendNewItemAnnotation(ret, span, types, id);
		log.finest("collected annos 2/3: " + ret);
		
		if (!config.disableVocabRequestOnListEvent) {
			// guess hard!
			for (Vocabulary v: config.vocabularies) {
				for (String type: v.getTypes()) {
	
					if (ArraysUtil.indexOf(types, type) >= 0) {
						
						int count = 0;
						
						for (FS fs: v.lookupTermsExactMatch(txt)) {
							
	//						if (count > 20) break;
							if (!fs.hasFeatureName("entryID") || !fs.hasFeatureName("term")) continue;
							
							String entryID = fs.getString("entryID");
							String term = fs.getString("term");
							boolean add = true;
							for (Annotation a: ret) {
								if (a.hasFeatureName("entryID") && a.getString("entryID").equals(entryID) &&
									a.hasFeatureName("term") && a.getString("term").equals(term)) {
	//System.err.println(":::::::::"+term+","+a.getString("term")+";"+entryID+";"+a.getString("entryID"));								
									add = false;
									log.finer("entryID " + entryID + "already there");
									break;
								} else {
	//System.err.println("........."+term+","+a.getString("term")+";"+entryID+";"+a.getString("entryID"));
								}
							}
							if (!add) continue;
							
							CFS cfs = FSUtil.newCFS(v.lookupTermInfo(fs.getString("entryID")));
							
							if (ArraysUtil.indexOf(types, cfs.getString("type")) < 0) continue;
							cfs.set("matchingTerm", term);
							cfs.set("term", txt);
							cfs.set("exists", true);
							cfs.set(AnnotatorConstants.APPROVED_ANNOTATION, true);
							ret.add(new Annotation(span, blockID, id, type, cfs));
							
							count++;
							
						}
						
						break;
						
					}
				}
			}
		}
		log.finest("collected annos 3/3: " + ret);

		return ret;
		
	}
	
	
	public List<Annotation> getForcedAnnotations (Span span, String[] types) {
		
		List<Annotation> ret = new LinkedList<Annotation>();
		
		String id = config.idgen.createID();
		
		appendNewItemAnnotation(ret, span, types, id);
		
//		String txt = getTokenRange(text, span.start, span.end).getText();
		String txt = text.substring(span.start, span.end).trim();
long tsa = System.currentTimeMillis();

		if (!config.disableVocabRequestOnListEvent) {
			// guess hard!
			for (Vocabulary v: config.vocabularies) {
long ts = System.currentTimeMillis();
				for (String type: v.getTypes()) {
					if (ArraysUtil.indexOf(types, type) >= 0) {
						
						int count = 0;
						
						for (FS fs: v.lookupTermsExactMatch(txt)) {
							
	//						if (count > 20) break;
							String term = fs.getString("term");
							
							CFS cfs = FSUtil.newCFS(v.lookupTermInfo(fs.getString("entryID")));
							
							if (ArraysUtil.indexOf(types, fs.getString("type")) < 0) continue;
							cfs.set("matchingTerm", term);
							cfs.set("term", txt);
							cfs.set("exists", true);
							cfs.set(AnnotatorConstants.APPROVED_ANNOTATION, true);
							ret.add(new Annotation(span, blockID, id, type, cfs));
								
							count++;
							
						}
						
						break;
						
					}
				}

System.err.println("--- " + v.getName() + ": " + (System.currentTimeMillis() - ts));

			}
System.err.println("---- all: " + (System.currentTimeMillis() - tsa));
		}
		
		return ret;
		
	}
	
	
	private void appendNewItemAnnotation(Collection<Annotation> pool, Span span, String[] types, String id) {
		
		for (Vocabulary v: config.vocabularies) {
			if (config.expandableVocabularies.contains(v.getName())) {
				for (String type: v.getTypes()) {
					if (ArraysUtil.indexOf(types, type) < 0) continue;

					CFS fs = FSUtil.newCFS();
					fs.set(AnnotatorConstants.APPROVED_ANNOTATION, true);
					fs.set(AnnotatorConstants.NEW_VOCABULARY_ENTRY, true);
					fs.set("aid", id);
					fs.set("term", text.substring(span.start, span.end));
					fs.set("type", type);
					fs.set("vocabulary", v.getName());
					pool.add(new Annotation(span, blockID, fs));
				}
			}
		}
		
		//TODO provisorium!
		if (ArraysUtil.indexOf(types, config.eventType) >= 0) {
			CFS fs = FSUtil.newCFS();
			fs.set(AnnotatorConstants.APPROVED_ANNOTATION, true);
			fs.set(AnnotatorConstants.NEW_VOCABULARY_ENTRY, true);
			fs.set("aid", id);
			fs.set("term", text.substring(span.start, span.end));
			fs.set("type", config.eventType);
			fs.set("vocabulary", config.eventType);
			pool.add(new Annotation(span, blockID, fs));
		}

		
	}
	
	
//	private TokenRange getTokenRange (Iterable<Token> toks, int start, int end) {
//		
//		int pos = 0;
//		List<Token> t = new LinkedList<Token>();
//		Iterator<Token> it = text.iterator();
//		Token tok;
//		
//		do {
//			if (!it.hasNext()) throw new IndexOutOfBoundsException("start " + start);
//			tok = it.next();
//			pos += tok.frontingWS.length() + tok.surface.length();
//		} while (pos <= start);
//		
//		int s = pos - start - tok.surface.length();
//		t.add(tok);
//		
//		while (it.hasNext() && pos < end) {
//			tok = it.next();
//			pos += tok.frontingWS.length() + tok.surface.length();
//			t.add(tok);
//		}
//		
//		if (end > pos) pos += tok.trailingWS.length();
//		if (end > pos) throw new IndexOutOfBoundsException("end " + end);
//		
//		return new TokenRange(t.toArray(new Token[t.size()]), s, pos - end - tok.surface.length());
//		
//	}
	
	
	public boolean isSetup () {
		return setup;
	}
	
}
