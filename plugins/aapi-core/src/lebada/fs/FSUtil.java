package lebada.fs;

public final class FSUtil {
	
	public static final FS EMPTY_FS = new AbstractFS() {
		private static final long serialVersionUID = 7666242316885449505L;
		@Override
		public boolean hasFeatureName(String key) { return false; }
		@Override
		public String[] getFeatureNames() { return new String[0]; }
		@Override
		public Object get(String key) { return null; }
	};

	private FSUtil () {}
	
	public static FS mergeFS (FS... fss) {
		return newFS(mergeCFS(fss));
	}
	
	public static CFS mergeCFS (FS... fss) {
		CFS cfs = FSUtil.newCFS();
		for (FS fs: fss) {
			for (String key: fs.getFeatureNames()) {
				cfs.set(key, fs.get(key));
			}
		}
		return cfs;
	}

	public static FS newFS (FS copy, Object ... keysValues) {
		return new FSArrayImpl(copy, keysValues);
	}

	public static FS newFS (Object ... keysValues) {
		return new FSArrayImpl(keysValues);
	}

	public static CFS newCFS (Object ... keysValues) {
		return new FSHashMapImpl(keysValues);
	}

	public static CFS newCFS (FS copy, Object ... keysValues) {
		return new FSHashMapImpl(copy, keysValues);
	}
	
	
}
