package postprocessor.transformer.instances;

import postprocessor.transformer.RuleInstance;
import hmmtagger.api.TaggedResultWord;

/**
 *Class representing a transformation rule which fires if the word at a given offset 
 *(relative to the current one) is tagged with a given tag
 */
public class OnePositionConstraintInst extends RuleInstance 
{
	/**the offset*/
	protected int constraintPos;
	/**the condition tag*/
	protected int constraintTag;
	
	/**constructor
	 * 
	 * @param tag tag to be replaced
	 * @param refTag replacing tag
	 * @param cpos the offset
	 * @param ctag the condition tag
	 * @param p flag indicating if the rule condition is to be logically inverted
	 */
    public OnePositionConstraintInst(int tag,int refTag,int cpos, int ctag,boolean p)
    {
    	super(tag,refTag,p);
    	constraintPos=cpos;
    	constraintTag=ctag;
    }
    
    /**
     * sets the condition tag
     * @param t the new value
     */
	public void setTag(int t)
	  {constraintTag=t;}
    
	/**
	 * 
	 * @return the condition tag
	 */
	public int getTag()
	  {return constraintTag;} 
	
	/**@see postprocessor.transformer.RuleInstance#applyTo(TaggedResultWord[][], boolean)*/
	public int applyTo(TaggedResultWord[][] sentence,boolean lex)
	{

		int res=0;
		for (int l=0; l<sentence.length; l++)
		{	
		for (int k=0; k<sentence[l].length; k++)
		{
			if (sentence[l][k].getBestIntTag()==this.sourceTag)
			{
				try
				{
					if ( (sentence[l][k+constraintPos].getBestIntTag() == constraintTag) != positiv)
						{continue;}
					if (lex)
					  {if (lexCheck(sentence[l][k]) == false) continue;}
					sentence[l][k].setBestTag(targetTag);
					res++;
			
				} catch (Exception e) {continue;}
			}
		}
		}
		return res;
	}
	
	

}
