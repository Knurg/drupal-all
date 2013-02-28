package eu.wiss_ki.aapi.editingSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.fau.cs8.mnscholz.util.ArraysUtil;
import de.fau.cs8.mnscholz.util.Span;
import de.fau.cs8.mnscholz.util.collection.ComposedSet;
import eu.wiss_ki.aapi.Configuration;

public class EditingSession {
	
	protected Comparator<Object> BLOCKID_COMPARATOR = new Comparator<Object>() {
		@Override
		public int compare(Object o1, Object o2) {
			if (o1 == null && o2 == null) return 0;
			if (o1 == null) return -1;
			if (o2 == null) return 1;
			String s1, s2;
			s1 = (o1 instanceof Block) ? ((Block) o1).blockID : (String) o1;
			s2 = (o2 instanceof Block) ? ((Block) o2).blockID : (String) o2;
			return s1.compareTo(s2);
		}
	};
	
	private Configuration conf;
	
	private Logger log = Logger.getLogger(this.getClass().getCanonicalName());
	
	private List<Block> blocks = new ArrayList<Block>();
	private Object blocklock = new Object() {};
	
	private Map<String, List<Annotation>> cache = new HashMap<String, List<Annotation>>(); 
	
	protected ComposedSet<Annotation> annos = new ComposedSet<Annotation>();
	
	public EditingSession (Configuration conf) {
		this.conf = conf;
	}
	
	
	public String getIDForBlock (Block b) {
		return b.blockID;
	}
	
	public String[] getBlockIDs () {
		synchronized (blocklock) {
			String[] ret = new String[blocks.size()];
			for (int i = 0; i < ret.length; i++) ret[i] = blocks.get(i).blockID;
			return ret;
		}
	}
	
	public Block getBlockForID (String blockID) {
		synchronized (blocklock) {
			int i = Collections.binarySearch(blocks, blockID, BLOCKID_COMPARATOR);
			return (i < 0) ? null : blocks.get(i);
		}
	}
	
	public void createBlock(String blockID) {
		synchronized (blocklock) {
			int i = Collections.binarySearch(blocks, blockID, BLOCKID_COMPARATOR);
			if (i >= 0) throw new EditingSessionException("block id " + blockID + " already exists");
			Block b = new Block(blockID, conf, annos);
			blocks.add(-i - 1, b);
//			annos.addSet(b.annos);
		}
	}
	
	public boolean hasBlock (String blockID) {
		synchronized (blocklock) {
			return Collections.binarySearch(blocks, blockID, BLOCKID_COMPARATOR) >= 0;
		}
	}
	
	
	public void destroyBlock(String blockID) {
		synchronized (blocklock) {
			int i = Collections.binarySearch(blocks, blockID, BLOCKID_COMPARATOR);
			if (i < 0) throw new EditingSessionException("block id " + blockID + " does not exist");;
			Block b = blocks.remove(i);
//			annos.removeSet(b.annos);
			
		}
	}
	
	
	public void append (String blockID, int revision, CharSequence cs) {
		
		Block b = getBlockForID(blockID);
		if (! b.isSetup()) b.reset(revision);
		else {
			// check revisions
			int rev = b.getRevision();
			if (revision < rev) return;			// outdated
			if (revision > rev) {
				cache.clear();
				b.reset(revision);	// new revision, delete text
			}
		}
		b.append(cs);
		
	}
	
	public void setAnnotation (Annotation a) {
		
		Block b = getBlockForID(a.blockID);
		if (b != null) b.setAnnotation(a);
		else log.warning("block ID does not exist for annotation " + a);
		
	}
	
	
	public List<Annotation> proposeAnnotations (String blockID, Span span, String[] types, int offsetFrom, int offsetTo) {

		if (types == null || types.length == 0) {
			types = conf.termTypes; 
		}
		
		List<Annotation> annos = getCachedAlternatives("hardguess_" + blockID + span + Arrays.toString(types));
		if (annos == null) {
			
			log.finest("empty annotations cache '" + "hardguess_" + blockID + span + Arrays.toString(types) + "'; retrieving");
			
			Block b = getBlockForID(blockID);
			annos = b.getForcedAnnotations(span, types);
			setCachedAlternatives("hardguess_" + blockID + span + Arrays.toString(types), annos);
			
		}
		String lm = "alternate annotations in cache:";
		for (Annotation a: annos) lm += "\n" + AnnoUtil.toString(a);
		
		log.finest(lm);
		
		return annos.subList(offsetFrom, Math.min(annos.size(), offsetTo));
		
	}
	
	
	public List<Annotation> getAlternateAnnotations (String aid, String[] types, int offsetFrom, int offsetTo) {
		if (types == null || types.length == 0) types = conf.termTypes;
log.finest("term types: " + Arrays.toString(types) + " all: " + Arrays.toString(conf.termTypes));
		
		log.finest("searching for alternative annotations for " + aid + " with types " + ArraysUtil.toString(types, "[", "'", ", ", "]"));
		
		List<Annotation> annos = getCachedAlternatives("alternates_" + aid + Arrays.toString(types));
		if (annos == null) {
			
			log.finest("empty annotations cache '" + "alternates_" + aid + Arrays.toString(types) + "'; retrieving");
			
			Annotation a = null;
			for (Block b1: blocks) {
				for (Annotation a1: b1.annos) {
					if (a1.id.equals(aid)) {
						a = a1;
						break;
					}
				}
			}
			if (a == null) {
				log.info("no anno with ID " + aid);
				return Collections.emptyList();
			}
			
			Block b = getBlockForID(a.blockID);
			
			annos = b.getAnnotations(a, types, conf.idgen.createID());
			setCachedAlternatives("alternates_" + aid + Arrays.toString(types), annos);
		}
		String lm = "alternate annotations in cache:";
		for (Annotation a: annos) lm += "\n" + AnnoUtil.toString(a);
		
		log.finest(lm);
		
		return annos.subList(offsetFrom, Math.min(annos.size(), offsetTo));
		
	}
	
	
	public void resetAnnotation (String blockID, String id) {
		Block b = getBlockForID(blockID);
		b.resetAnnotation(id);
	}
	
	
	public synchronized List<Annotation> getNewAnnotations () {
		List<Annotation> l = new ArrayList<Annotation>();
		for (Block b: blocks) {
			l.addAll(b.toBeSent);
			b.toBeSent.clear();
		}
		return l;
	}
	
	public List<Annotation> getCachedAlternatives (String id) {
		return cache.get(id);		
	}
	
	public void setCachedAlternatives (String id, List<Annotation> annos) {
		// this is a cache with max 1 item!
		cache.clear();
		cache.put(id, annos);
	}
	
}
