package lebada.fs;

import java.util.Arrays;
import java.util.List;

 abstract class AbstractFS implements FS {
	public static final long serialVersionUID = -5345732312426679842l;
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#get(java.lang.String)
	 */
	public abstract Object get(String key);
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#getString(java.lang.String)
	 */
	public String getString(String key) {
		Object o = get(key);
		return (o instanceof String) ? (String) o : null;
	}

	/* (non-Javadoc)
	 * @see lebada.fs.FS#getInt(java.lang.String)
	 */
	public Integer getInt(String key) {
		Object o = get(key);
		return (o instanceof Integer) ? (Integer) o : null;
	}
	

	/* (non-Javadoc)
	 * @see lebada.fs.FS#getInt(java.lang.String)
	 */
	public Boolean getBoolean(String key) {
		Object o = get(key);
		return (o instanceof Boolean) ? (Boolean) o : null;
	}
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#getLong(java.lang.String)
	 */
	public Long getLong(String key) {
		Object o = get(key);
		return (o instanceof Long) ? (Long) o : null;
	}
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#getDouble(java.lang.String)
	 */
	public Double getDouble(String key) {
		Object o = get(key);
		return (o instanceof Double) ? (Double) o : null;
	}
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#getFSArray(java.lang.String)
	 */
	public FS[] getFSArray(String key) {
		Object o = get(key);
		return (o instanceof FS[]) ? (AbstractFS[]) o : null;
	}
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#getFS(java.lang.String)
	 */
	public FS getFS(String key) {
		Object o = get(key);
		return (o instanceof AbstractFS) ? (AbstractFS) o : null;
	}
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#getFSList(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<? extends AbstractFS> getFSList(String key) {
		Object o = get(key);
		return (o instanceof List<?>) ? (List<? extends AbstractFS>) o : null;
	}
	
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#getFeatureNames()
	 */
	public abstract String[] getFeatureNames();
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#hasFeatureName(java.lang.String)
	 */
	public abstract boolean hasFeatureName(String key);
	
	/* (non-Javadoc)
	 * @see lebada.fs.FS#toString()
	 */
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (String f: getFeatureNames()) {
			Object o = get(f);
			b.append(", '");
			b.append(f);
			b.append("': ");
			if (o != null) {
				b.append("(");
				b.append(o.getClass().getCanonicalName());
				b.append(") ");
				if (o instanceof boolean[]) b.append(Arrays.toString((boolean[]) o)); 
				else if (o instanceof byte[]) b.append(Arrays.toString((byte[]) o)); 
				else if (o instanceof char[]) b.append(Arrays.toString((char[]) o)); 
				else if (o instanceof short[]) b.append(Arrays.toString((short[]) o)); 
				else if (o instanceof int[]) b.append(Arrays.toString((int[]) o)); 
				else if (o instanceof long[]) b.append(Arrays.toString((long[]) o)); 
				else if (o instanceof float[]) b.append(Arrays.toString((float[]) o)); 
				else if (o instanceof double[]) b.append(Arrays.toString((double[]) o)); 
				else if (o.getClass().isArray()) b.append(Arrays.toString((Object[]) o)); 
				else b.append(o);
			} else {
				b.append("null");
			}
		}
		
		return "< " + ((b.length() > 2) ? b.substring(2) : "") + " >";
		
	}
	
}
