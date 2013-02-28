package lebada.fs;

import java.util.Arrays;

class FSArrayImpl extends AbstractFS {
	public static final long serialVersionUID = 54357l;
	
	private static class Entry implements Comparable<Object> {
		
		final String key;
		final Object value;
		
		public Entry(String key, Object value) {
			super();
			this.key = key;
			this.value = value;
		}
		
		@Override
		public int compareTo(Object o) {
			if (o instanceof Entry) return key.compareTo(((Entry) o).key);
			if (o instanceof String) return key.compareTo((String) o);
			throw new IllegalArgumentException();
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Entry) return key.equals(((Entry) o).key);
			throw new IllegalArgumentException();
		}
		
		@Override
		public int hashCode () {
			return key.hashCode();
		}
		
	}
	
	private Entry[] entries;
	
	@Override
	public Object get(String key) {
		int i = Arrays.binarySearch(entries, key);
		if (i < 0) return null;
		return entries[i].value;
	}

	public boolean hasFeatureName(String key) {
		return Arrays.binarySearch(entries, key) >= 0;
	}
	
	public String[] getFeatureNames () {
		String[] ret = new String[entries.length];
		for (int i = 0; i < ret.length; i++) ret[i] = entries[i].key;
		return ret;
	}
	
	public FSArrayImpl (Object... fs) {
		if (fs.length % 2 == 1) throw new IllegalArgumentException("fs has odd number of elements");
		
		entries = new Entry[fs.length / 2];
		
		for (int i = 0; i < entries.length; i++) {
			String k = (String) fs[2 * i];
			Object v = fs[2 * i + 1];
			for (int j = 0; j < i; j++)
				if (entries[j].key.equals(k)) throw new IllegalArgumentException("duplicate feature name: " + k);
			entries[i] = new Entry(k, v);
		}
		
		Arrays.sort(entries);
		
	}
	
	public FSArrayImpl (FS copy, Object... fs) {
		if (fs.length % 2 == 1) throw new IllegalArgumentException("fs has odd number of elements");
		
		String[] keys = copy.getFeatureNames();
		int keyslen = keys.length;
		
		entries = new Entry[keys.length + fs.length / 2]; 
		
		for (int i = 0; i < keyslen; i++) {
			entries[i] = new Entry(keys[i], copy.get(keys[i]));
		}
		
		for (int i = 0; i < entries.length - keyslen; i++) {
			String k = (String) fs[2 * i];
			Object v = fs[2 * i + 1];
			for (int j = 0; j < keyslen + i; j++)
				if (entries[j].key.equals(k)) throw new IllegalArgumentException("duplicate feature name: " + k);
			entries[keyslen + i] = new Entry(k, v);
		}
		
		Arrays.sort(entries);
		
	}
	
}
