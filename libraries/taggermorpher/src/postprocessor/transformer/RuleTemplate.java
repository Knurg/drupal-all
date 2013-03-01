package postprocessor.transformer;



/**abstract super class of all transformation rule templates*/
public abstract class RuleTemplate 
{   
	/**number of tags*/
	protected int nTags;
	/**flag indicating if the rule condition is to be logically inverted*/
	protected boolean positiv;
	/**the subset of tags rules instantiated from this template will work on*/
	protected int[] tagSubSet;
	
	/**constructor
	 * 
	 * @param nT number of tags
	 * @param p flag indicating if the rule condition is to be logically inverted
	 */
	protected RuleTemplate(int nT,boolean p)
	{
		nTags=nT;
		positiv=p;
		tagSubSet=null;
	}
	
    /**
     * 
     * @return number of tags
     */
	public int getNTags()
	   {return nTags;}

    /**
     * @return next portion of instances (to retrieve the complete set of instances of this template), this method must possibly be called
     * several times, until null is returned. Attention: Instances returned by this method must be cloned to be used!
     */
    public abstract RuleInstance[] getNextInstances();
    
    /**resets the template by setting new values for the transformation (after that, another set of rules can be retrieved by 
     * repeated calls of method<pre>getNextInstances</pre> 
     * 
     * @param tag tag to be replaced
     * @param refTag replacing tag
     */
    public abstract void reset(int tag, int refTag);
    
    /**
     * sets the subset of tags rules instantiated from this template will work on
     * @param tss the tag subset
     */
    public abstract void setTagSubSet(int[] tss);
    
}
