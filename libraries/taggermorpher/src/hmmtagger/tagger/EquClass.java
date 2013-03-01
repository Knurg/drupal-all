package hmmtagger.tagger;

/**
*   Contains data about a class of words with the same subset of possible tags
 */
public class EquClass 
{
	/**lex. prob. of the equivalence class*/
	public float[] lp=null;
	/**number of words in the class*/
	public int count;
	
	/**constructor*
	 * 
	 * @param p vector of lex. prob.
	 * @param c number of words
	 */
	public EquClass(float[] p, int c)
	{
		lp=p;
		count=c;
	}
    
}
