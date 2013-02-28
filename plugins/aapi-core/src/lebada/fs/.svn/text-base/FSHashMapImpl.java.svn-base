package lebada.fs;

import java.util.HashMap;

class FSHashMapImpl extends AbstractCFS {
	public static final long serialVersionUID = 54357l;
	
	private HashMap<String, Object> f;
	
	@Override
	public Object get(String key) {
		return f.get(key);
	}
	
	public String[] getFeatureNames () {
		return f.keySet().toArray(new String[0]);
	}
	
	@Override
	public boolean hasFeatureName(String key) {
		return f.containsKey(key);
	}

	public FSHashMapImpl (Object... fs) {
		if (fs.length % 2 == 1) throw new IllegalArgumentException("fs has odd number of elements");
		
		f = new HashMap<String, Object>((int) (fs.length * 0.7));
		
		for (int i = 0; i < fs.length; i+=2) {
			if (fs[i] == null) throw new NullPointerException("feature name may not be null");
			f.put((String) fs[i], fs[i+1]);
		}
		
	}

	public FSHashMapImpl (FS copy, Object... fs) {
		if (fs.length % 2 == 1) throw new IllegalArgumentException("fs has odd number of elements");
		
		String[] keys = copy.getFeatureNames();
		f = new HashMap<String, Object>((int) (fs.length * 0.7 + keys.length * 1.4));
		
		for (String k: keys)
			f.put(k, copy.get(k));
		
		for (int i = 0; i < fs.length; i+=2) {
			if (fs[i] == null) throw new NullPointerException("feature name may not be null");
			f.put((String) fs[i], fs[i+1]);
		}
		
	}
	
	@Override
	public void set(String name, Object value) {
		f.put(name, value);
	}

	@Override
	public void unset(String name) {
		f.remove(name);
	}
	
}
