package postprocessor.transformer;



import hmmtagger.api.TaggedResultWord;
import hmmtagger.tagger.LexProb;

import java.io.Serializable;




/**abstract super class of all transformation rule instances*/
public abstract class RuleInstance implements Serializable,Cloneable 
{
	/**the tag to be replaced*/
	protected int sourceTag;
	/**the replacing tag*/
	protected int targetTag;
	/**flag indicating if the rule condition is to be logically inverted*/
	protected boolean positiv;
	
	/**constructor
	 * 
	 * @param st tag to be replaced
	 * @param tt replacing tag
	 * @param p flag indicating if the rule condition is to be logically inverted
	 */
	protected RuleInstance (int st,int tt,boolean p)
	{
		sourceTag=st;
		targetTag=tt;
		positiv=p;
	}
	
	/**transforms the tags of a given sentence
	 * 
	 * @param sentence the sentence
	 * @param lex flag indicating if lex. prob. is to be respected
	 * @return
	 */
    public abstract int applyTo(TaggedResultWord[][] sentence,boolean lex);
    
    
    /**checks if the replacing tag is possible for a given word
     * 
     * @param w the word
     * @return
     */
    public final boolean lexCheck(TaggedResultWord w)
    {
    	if (w == null || w.getLexicalInfo() == null) return false;
    	LexProb[] lp=w.getLexicalInfo().getProb();
	    for (int i=0; i<lp.length; i++)
	    {
	    	if ( (lp[i].s==targetTag) && (lp[i].p != Float.NEGATIVE_INFINITY) )
	          return true; 	  	
	    }
		return false;
    }
	/**
	 * @return the tag to be replaced
	 */
	public final int getSourceTag() {
		return sourceTag;
	}
	/** sets the tag to be replaced
	 * @param sourceTag the new value
	 */
	public final void setSourceTag(int sourceTag) {
		this.sourceTag = sourceTag;
	}
	/**
	 * @return the replacing tag
	 */
	public final int getTargetTag() {
		return targetTag;
	}
	/**sets the replacing tag
	 * @param the new value
	 */
	public final void setTargetTag(int targetTag) 
	{
		this.targetTag = targetTag;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
