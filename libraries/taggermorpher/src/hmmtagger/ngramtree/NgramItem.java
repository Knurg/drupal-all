package hmmtagger.ngramtree;

/**
* Represents an ngram of tags
 */
public class NgramItem 
{
	/**the order of the ngrams*/
    protected static int order;
    /**the ngram*/
    protected int[] ngr=null;
    
     /**
     * 
     * @param ng the ngram
     * @throws Exception
     */
    public NgramItem(int[] ng) throws Exception
    {
    	if (ng.length != order) throw new Exception("NgramItem.constructor: invalid argument");
    	ngr=ng;
    }
	
    /**
     * returns an element of the ngram
     * @param k index of the element
     * @return k-th element of the ngram
     * @throws Exception
     */
	public final int getVal(int k) throws Exception
	{
		try
		{
			return ngr[k];
		} catch (Exception e) {throw new Exception("NgramItem.getVal: invalid argument");}
		

	}

	/**evaluates a given criterium
	 * 
	 * @param c the criterium
	 * @return true if this ngram matches the criterium, false otherwise
	 * @throws Exception
	 */
	public final boolean evalCriterium(SplitCriterium c) throws Exception
	{
		try
		{
			return (ngr[c.pos] == c.val);	
		} catch (Exception e) {throw new Exception("NgramItem.evalCriterium: invalid argument");}
		
	}

	/**
	 * 
	 * @return last element of the ngram
	 */
	public final int getClassId() 
	{
		return ngr[order-1];
	}
    
	/**
	 * sets the order of all ngrams...has to be called before instantiating ngrams
	 * @param o the new value
	 */
	public static void setOrder(int o)
	{
		order = o;
	}
	
	/**
	 * 
	 * @return current order
	 */
	public static int getOrder()
	{
		return order;
	}
}
