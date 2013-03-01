package postprocessor.transformer.instances;

import postprocessor.transformer.RuleInstance;
import hmmtagger.api.TaggedResultWord;

/**
 *Class representing a transformation rule which fires if the word at a given offset 
 *(relative to the current one) is (not) capitalized
 */
public class CapConstraintInst extends RuleInstance 
{
	/**the offset*/
	protected int offset=0;

	/**constructor
	 * 
	 * @param st tag to be replaced
	 * @param tt replacing tag
	 * @param off offset
	 * @param p flag indicating if the rule condition is to be logically inverted
	 */
	public CapConstraintInst(int st, int tt,int off,boolean p)
    {
    	super(st,tt,p);
    	offset=off;
    }
	
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
				if ( (k+offset>=sentence[l].length) || (k+offset<0) ) continue;
				if ( Character.isUpperCase( sentence[l][k+offset].getWord().charAt(0) ) != positiv) continue;
				if (lex)
					{if (lexCheck(sentence[l][k]) == false) continue;}
				sentence[l][k].setBestTag(targetTag);
				res++;
			}
		}
		}
		return res;
	}

	
}
