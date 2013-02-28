package lebada.voc;

import java.lang.reflect.Constructor;

import org.shiftone.cache.Cache;
import org.shiftone.cache.policy.lru.LruCacheFactory;
import org.shiftone.cache.CacheConfiguration;
import org.shiftone.cache.ConfigurationException;

import de.fau.cs8.mnscholz.util.options.Options;

import java.util.logging.Logger;

public class VocabularyFactory {
	
	private static Cache cache;

  private static Logger log = Logger.getLogger(VocabularyFactory.class.getCanonicalName());
	
	private static Cache getCache () {
		if (cache == null) {
//			try {
        LruCacheFactory cacheFactory = new LruCacheFactory();
        cache = cacheFactory.newInstance("vocabularies", 5 * 60 * 1000, 100);
//			} catch (ConfigurationException e) {
//log.info("blubb outsch! " + java.util.Arrays.toString(e.getStackTrace()));
//				throw new RuntimeException("cannot create cache for a cached vocabulary", e);
//			}
		}
		return cache;
	}
	
	public static Vocabulary create(Options o) {

		try {
			Class<?> cl = Class.forName(o.get("class"));
			if (! Vocabulary.class.isAssignableFrom(cl))
				throw new RuntimeException("class " + o.get("class") + " does not implement " + Vocabulary.class.getCanonicalName());
			Constructor<?> cn = cl.getConstructor(Options.class);
			Vocabulary v = (Vocabulary) cn.newInstance(o.subset("data."));
			if ("true".equalsIgnoreCase(o.get("caching")))
				v = new CachingVocabularyProxy(o.subset("cacheData."), v, getCache());
			return v;
		} catch (Exception e) {
			throw new RuntimeException("creating vocabulary from options " + o, e);
		}
		
	}
	
}
