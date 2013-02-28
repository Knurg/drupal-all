package eu.wiss_ki.aapi;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import lebada.marker.Marker;
import lebada.marker.SimpleEventMarker;
import lebada.marker.VocabularyMarker;
import lebada.voc.MultiplexVocabulary;
import lebada.voc.Vocabulary;
import lebada.voc.VocabularyFactory;
import apus.TaggerMorpher;
import apus.tok.Tokenizer;
import de.fau.cs8.mnscholz.util.options.Options;
import eu.wiss_ki.IDGenerator;

public class Configuration {
	
	public static class ConfigurationException extends RuntimeException {
		private static final long serialVersionUID = -5673206277317943664L;
		ConfigurationException() { super(); }
		ConfigurationException(String message, Throwable cause) { super(message, cause); }
		ConfigurationException(String message) { super(message); }
		ConfigurationException(Throwable cause) { super(cause); }
	}
	
	
	private static Map<String, Configuration> instances = new HashMap<String, Configuration>();
	private static Logger log = Logger.getLogger(Configuration.class.getCanonicalName());
	
	public static Configuration getConfiguration(String id) {
		log.finest("available configuration ids: " + instances.keySet());
		return instances.get(id);
	}
	
	
	public static void updateConfiguration(Options conf) {
		
		StringBuilder b = new StringBuilder("=== Config Options ===\n");
		for (String k: conf.getKeys()) b.append(k).append(" = ").append(conf.get(k)).append("\n");
		b.append("======================");
		log.info(b.toString());
		
		if (conf.get("id", null) == null) throw new ConfigurationException("no id");
		
		Configuration inst;
		if (! instances.containsKey(conf.get("id"))) { 
			inst = new Configuration(conf.get("id"));
		} else {
			inst = instances.get(conf.get("id"));
		}
		
		inst.update(conf);
		
		instances.put(inst.id, inst);
		log.finest("configuration set for id " + inst.id + ", now available ids: " + instances.keySet());


	}
	
	
	public Configuration (String id) {
		this.id = id;
	}
	
	public final String id;
	
	public Options options;
	
	public String language;
	
	public String keepAliveURL = null;
	
	public IDGenerator idgen = null;
	
	public Marker[] entityMarkers = null;
	
	public Marker[] eventMarkers = null;
	
	public Map<Marker, Integer> priorities = null;
	
	public Set<String> expandingEntityMarkers = null;
	
	public Set<String> expandableVocabularies = null;
	
	public Vocabulary[] vocabularies = null;
	
	public Vocabulary multiplexVoc = null;
	
	public String[] termTypes = null;
	
	public Tokenizer protoTokenizer = null;
	
	public TaggerMorpher taggerMorph = null;
	
	public boolean disableAutomaticAnnotation = false;
	
	public boolean disableVocabRequestOnListEvent = false;
	
	public String possibleLNATermsURL = null;
	
	public String eventType = null;
	
	public LNAMarker lnaMarker = null;
	
	
	private synchronized void update (Options o) {

		String basePackage = Configuration.class.getPackage().getName() + ".";
		
		// check if incremental update
		if ("true".equalsIgnoreCase(o.get("incrementalUpdate", "false"))) {
			for (String k: this.options.getKeys()) if (!o.exists(k)) o.set(k, this.options.get(k));
		}
		
		this.language = o.get("language", "de").toLowerCase();
		
		Options so;
		
		this.disableAutomaticAnnotation = "true".equalsIgnoreCase(o.get("annotator.disableAutomaticAnnotation", "false"));
		this.disableVocabRequestOnListEvent = "true".equalsIgnoreCase(o.get("annotator.disableVocabRequestOnListEvent", "false"));
		this.possibleLNATermsURL = o.get("possibleLNATerms", null);
		
		// set id generator
		try {
			so = o.subset("IDGenerator.");
			Class<?> cls = Class.forName(IDGenerator.class.getPackage().getName() + "." + so.get("class"));
			if (! IDGenerator.class.isAssignableFrom(cls)) throw new ClassCastException(so.get("class") + " is no IDGenerator");
			Constructor<?> c = cls.getConstructor(Options.class);
			idgen = (IDGenerator) c.newInstance(so);
		} catch (Exception e) {
			log.log(Level.WARNING, "exception installing id generator", e);
		}
		
		// set event type
		this.eventType = o.get("eventType", "Event");
		
    // preset termtypes
		Set<String> termtypes = new HashSet<String>();
		termtypes.add(this.eventType);
		
		// set vocabs and marker for vocabs
		
		List<Vocabulary> vocabs = new ArrayList<Vocabulary>();
		TreeMap<Integer, List<Marker>> markermap = new TreeMap<Integer, List<Marker>>();
		Set<String> expandableVocabularies = new HashSet<String>();
		
		int i = 0;
		while ((so = o.subset("vocabularies." + i++ + ".")).exists("class")) {
			
			try {
				
//				Class cls = Class.forName(so.get("class"));
//				if (! Vocabulary.class.isAssignableFrom(cls)) {
//					log.warning("no valid vocabulary class: " + so.get("class"));
//					continue;
//				}
				boolean makeMarker = (so.get("class").equals("AuthorityDBVocabulary"));
				so.set("class", basePackage + so.get("class"));
				
				Vocabulary v = VocabularyFactory.create(so);
//				Vocabulary v = (Vocabulary) cls.getConstructor(Options.class).newInstance(so); 
				vocabs.add(v);

        log.finest(v.getName() + " has term types " + Arrays.toString(v.getTypes()));
				termtypes.addAll(Arrays.asList(v.getTypes()));
				
				if ("true".equalsIgnoreCase(so.get("expandable", "false"))) expandableVocabularies.add(v.getName());
				
				Options mo = so.subset("marker.");
				
				String ps = mo.get("priority", null);
				if (makeMarker && ps != null && ps.matches("-?\\d+")) {
					int p = Integer.parseInt(ps);
					if (!markermap.containsKey(p)) markermap.put(p, new ArrayList<Marker>());
					markermap.get(p).add(new VocabularyMarker(mo, new Vocabulary[]{v}));
				}
				
			} catch (Exception e) {
				log.log(Level.WARNING, "exception loading vocabulary", e);
			}	
			
		}
		vocabularies = vocabs.toArray(new Vocabulary[vocabs.size()]);
		multiplexVoc = new MultiplexVocabulary(vocabularies);
		this.expandableVocabularies = expandableVocabularies;
		
		Options lnao = Options.create();
		lnao.set("lookupURL", o.get("lookupLNA"));
		lnaMarker = new LNAMarker(lnao);
		
		
		// set entity markers
		i = 0;
		while ((so = o.subset("markers." + i++ + ".")).exists("class")) {
			
			try {
				
				Class<?> cls = Class.forName(basePackage + so.get("class"));
				if (! Marker.class.isAssignableFrom(cls)) {
					log.warning("no valid marker class: " + so.get("class"));
					continue;
				}
				
				String ps = so.get("priority", "");
				int p = ps.matches("-?\\d+") ? Integer.parseInt(ps) : 0;
				
				if (!markermap.containsKey(p)) markermap.put(p, new ArrayList<Marker>());
				log.finer("installing marker " + so.get("name") + " (" + so.get("class") + ")");
				Marker m = (Marker) cls.getConstructor(Options.class).newInstance(so);
				log.finer("installed marker " + m.getName());
				markermap.get(p).add(m);
				
			} catch (Exception e) {
				log.log(Level.WARNING, "exception loading marker", e);
			}	
		
		}
		List<Marker> markers = new ArrayList<Marker>();
		for (List<Marker> ml: markermap.values()) markers.addAll(ml);
		Collections.reverse(markers);

		this.entityMarkers = markers.toArray(new Marker[markers.size()]);
		HashMap<Marker, Integer> priorities = new HashMap<Marker, Integer>();
		for (Entry<Integer, List<Marker>> e: markermap.entrySet())
			for (Marker m: e.getValue()) priorities.put(m, e.getKey());
		this.priorities = priorities;
		
		// all markers that are not VocabularyMarker's are expanding markers
		HashSet<String> expandingMarkers = new HashSet<String>();
		for (Marker m: this.entityMarkers) if (! (m instanceof VocabularyMarker)) expandingMarkers.add(m.getName());
		this.expandingEntityMarkers = expandingMarkers;
		
		
		// set event markers
    try {
  		if (this.eventMarkers == null) {
  			Options evo = Options.create();
  			evo.set("xmlfiles", "data/event/event-lex-compiled_" + language + ".xml");
  			evo.set("considerMatchingTokens", "false");
  			evo.set("checkPOS", "false");
  			this.eventMarkers = new Marker[]{
  				new SimpleEventMarker(evo)
  			};
  		}
    } catch (Exception e) {
      log.log(Level.WARNING, "exception loading event marker", e);
    }
    
		termTypes = termtypes.toArray(new String[termtypes.size()]);
		
		if (protoTokenizer == null) {
			
//			Options tokopt = Options.create();
//			tokopt.set("tokenizers", "base abbrevlist hyphen dot_in_word sent etoken");
//			//tokopt.set("tokenizers", "base abbrevlist hyphen lcabbrev sent etoken");
//			tokopt.set("tokenizers.base.class", BaseTokenizer.class.getCanonicalName());
//			tokopt.set("tokenizers.lcabbrev.class", LCAbbrev.class.getCanonicalName());
//			tokopt.set("tokenizers.dot_in_word.class", HyphenGlue.class.getCanonicalName());
//			tokopt.set("tokenizers.dot_in_word.hyphens", ".");
//			tokopt.set("tokenizers.dot_in_word.truncs", "");
//			tokopt.set("tokenizers.hyphen.class", HyphenGlue.class.getCanonicalName());
//			tokopt.set("tokenizers.sent.class", SentenceSplitter.class.getCanonicalName());
//			tokopt.set("tokenizers.abbrevlist.class", ListGlue.class.getCanonicalName());
//			tokopt.set("tokenizers.abbrevlist.combinedTokensFiles", "fixed_tokens.txt");
//			tokopt.set("tokenizers.etoken.class", ETokenConverter.class.getCanonicalName());
//			protoTokenizer = Tokenizer.protoTokenizer(tokopt);
			
			protoTokenizer = Tokenizer.protoTokenizer();
			
		}
		
		// tagger and morphology
		if (taggerMorph == null) {
			
			taggerMorph = TaggerMorpher.getStandard(language);
			
		}
		
		// TODO keepalive handling

		// set new options
		this.options = o;
		
		log.info("configuration " + id + " updated successfully");
		
	}
	
}
