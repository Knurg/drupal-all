package lebada.voc;

import lebada.fs.FS;
import lebada.fs.FSUtil;

import org.shiftone.cache.Cache;

import de.fau.cs8.mnscholz.util.AbstractOptionedClass;
import de.fau.cs8.mnscholz.util.options.Options;

public class CachingVocabularyProxy extends AbstractOptionedClass implements Vocabulary {
	
	protected Cache cache;
	protected Vocabulary v;
	protected int expt;
	
	
	public CachingVocabularyProxy (Options o, Vocabulary backing, Cache cache) {
		super(o);
		
		v = backing;
		this.cache = cache;
		expt = Integer.parseInt(options.get("timeToExpiration", "0"));
		
	}

	
	public String getName() {
		return v.getName();
	}


	@Override
	public String[] getTypes () {
		String cachekey = hashCode() + "getTypes";
		Object[] o = (Object[]) cache.getObject(cachekey);
		if (o != null) {
			if (expt <= 0 || ((Long) o[0]) > System.currentTimeMillis())
				// cache hit
				return ((String[]) o[1]).clone();
			// else cache hit, but expired
		}
		
		String[] t = v.getTypes();
		cache.addObject(cachekey, new Object[]{System.currentTimeMillis() + expt, t});
		return t;
		
	}

	@Override
	public FS lookupTermInfo (String entryID) {
		String cachekey = hashCode() + "lookupTermInfo:" + entryID;
		Object[] o = (Object[]) cache.getObject(cachekey);
		if (o != null) {
			if (expt <= 0 || ((Long) o[0]) > System.currentTimeMillis()) {
				// cache hit
				return FSUtil.newFS((FS) o[1]);
			} else {
      }
			// else cache hit, but expired
		} else {
    }
		
		FS t = v.lookupTermInfo(entryID);
		cache.addObject(cachekey, new Object[]{System.currentTimeMillis() + expt, t});
    return t;
		
	}

	@Override
	public FS[] lookupTermsContaining (String termPart) {
		String cachekey = hashCode() + "lookupTermsContaining:" + termPart;
		Object[] o = (Object[]) cache.getObject(cachekey);
		if (o != null) {
			if (expt <= 0 || ((Long) o[0]) > System.currentTimeMillis()) {
				// cache hit
				return ((FS[]) o[1]).clone();
			}
			// else cache hit, but expired
		}
		
		FS[] t = v.lookupTermsContaining(termPart);
		cache.addObject(cachekey, new Object[]{System.currentTimeMillis() + expt, t});
		return t;
		
	}

	@Override
	public FS[] lookupTermsStartingWith(String termStart) {
		String cachekey = hashCode() + "lookupTermsStartingWith:" + termStart;
		Object[] o = (Object[]) cache.getObject(cachekey);
		if (o != null) {
			if (expt <= 0 || ((Long) o[0]) > System.currentTimeMillis()) {
				// cache hit
				return ((FS[]) o[1]).clone();
			}
			// else cache hit, but expired
		}
		
		FS[] t = v.lookupTermsStartingWith(termStart);
		cache.addObject(cachekey, new Object[]{System.currentTimeMillis() + expt, t});
		return t;
		
	}
	
	@Override
	public FS[] lookupTermsExactMatch(String termExact) {
		String cachekey = hashCode() + "lookupTermsExactMatch:" + termExact;
		Object[] o = (Object[]) cache.getObject(cachekey);
		if (o != null) {
			if (expt <= 0 || ((Long) o[0]) > System.currentTimeMillis()) {
				// cache hit
				return ((FS[]) o[1]).clone();
			}
			// else cache hit, but expired
		}
		
		FS[] t = v.lookupTermsExactMatch(termExact);
		cache.addObject(cachekey, new Object[]{System.currentTimeMillis() + expt, t});
		return t;
		
	}
	
}
