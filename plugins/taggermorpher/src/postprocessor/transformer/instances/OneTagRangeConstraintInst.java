package postprocessor.transformer.instances;

import postprocessor.transformer.RuleInstance;
import hmmtagger.api.TaggedResultWord;


/**
 *Class representing a transformation rule which fires if at least one of the words in a given frame 
 *(relative to the current word) is tagged with a given tag. The frame extends either to the right (positive
 *frame range) or to the left (negative frame range).
 */
public class OneTagRangeConstraintInst extends RuleInstance 
{
	/**the frame range*/
	protected int constraintRange;
	/**the condition tag*/
	protected int constraintTag;
	
	/**constructor
	 * 
	 * @param tag tag to be replaced
	 * @param refTag replacing tag
	 * @param crange frame range
	 * @param ctag condition tag
	 * @param p flag indicating if the rule condition is to be logically inverted
	 */
    public OneTagRangeConstraintInst(int tag,int refTag,int crange, int ctag,boolean p)
    {
    	super(tag,refTag,p);
    	constraintRange=crange;
    	constraintTag=ctag;
    }
    
    /**@see postprocessor.transformer.RuleInstance#applyTo(TaggedResultWord[][], boolean)*/
    public int applyTo(TaggedResultWord[][] sentence,boolean lex)
	{
		int res=0;
		for (int l=0; l<sentence.length; l++)
		{	
		boolean b=false;
		for (int k=0; k<sentence[l].length; k++)
		{
			if (sentence[l][k].getBestIntTag()==this.sourceTag)
			{
				int i1=k+constraintRange;
				if (i1<0) 
				  {i1=0;}
				else
				{
				   if (i1>=sentence[l].length) {i1=sentence[l].length-1;}
				}
				int start=i1;
				int end=k-1;
				if (end<start)
				{
					start=k+1;
					end=i1;
				}
				b=false;
				for (int i=start; i<=end; i++)
				{
					if (sentence[l][i].getBestIntTag() == constraintTag) {b=true; break;}
				}
				if (b!=positiv) continue;
				if (lex)
				  {if (lexCheck(sentence[l][k]) == false) continue;}
				sentence[l][k].setBestTag(targetTag);
				res++;
			}
		}
		}
		return res;
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
	
}
