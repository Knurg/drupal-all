package postprocessor.transformer.instances;

import hmmtagger.api.TaggedResultWord;

/**
 **Class representing a transformation rule which fires if two word at two given offsets 
 *(relative to the current one) are tagged with two given tags 
 */
public class TwoPositionConstraintInst extends OnePositionConstraintInst 
{
    /**second offet*/
	protected int constraintPos2;
	/**second condition tag*/
	protected int constraintTag2;
	
    /**
     * 
     * @param tag tag to be replaced
     * @param refTag replacing tag
     * @param cpos first offset
     * @param ctag first condition tag
     * @param cpos2 second offset
     * @param ctag2 second condition tag
     * @param p flag indicating if the rule condition is to be logically inverted
     */
	public TwoPositionConstraintInst(int tag, int refTag,int cpos, int ctag,int cpos2,int ctag2,boolean p) 
	{
		super(tag, refTag, cpos, ctag,p);
	    constraintTag2=ctag2;
	    constraintPos2=cpos2;	
	}
	
	/**
	 * sets the second condition tag
	 * @param t the new value
	 */
	public void setTag2(int t)
	  {constraintTag2=t;}
   
	/**
	 * 
	 * @return the second condition tag
	 */
	public int getTag2()
	  {return constraintTag2;}
	
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
					if ( ( (sentence[l][k+constraintPos].getBestIntTag() == constraintTag) && (sentence[l][k+constraintPos2].getBestIntTag() == constraintTag2) ) != positiv)
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
