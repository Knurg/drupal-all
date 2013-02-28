package lebada.fs;

 abstract class AbstractCFS extends AbstractFS implements CFS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1575847750224389327L;

	/* (non-Javadoc)
	 * @see lebada.fs.CFS#set(java.lang.String, java.lang.Object)
	 */
	public abstract void set(String name, Object value);
	
	/* (non-Javadoc)
	 * @see lebada.fs.CFS#unset(java.lang.String)
	 */
	public abstract void unset(String name);
	
	/* (non-Javadoc)
	 * @see lebada.fs.CFS#set(java.lang.String, int)
	 */
	public void set (String name, int value) {
		set(name, new Integer(value));
	}

	/* (non-Javadoc)
	 * @see lebada.fs.CFS#set(java.lang.String, int)
	 */
	public void set (String name, boolean value) {
		set(name, new Boolean(value));
	}

	/* (non-Javadoc)
	 * @see lebada.fs.CFS#set(java.lang.String, long)
	 */
	public void set (String name, long value) {
		set(name, new Long(value));
	}

	/* (non-Javadoc)
	 * @see lebada.fs.CFS#set(java.lang.String, double)
	 */
	public void set (String name, double value) {
		set(name, new Double(value));
	}
	
	
}
