
package hmmtagger.tagger;

/**container, stores a pair (integer tag code / probability) */
public class LexProb implements java.io.Serializable,Comparable
{
	/**the prob*/
    public float p=(float)0.0; 
    /**the tag number*/
    public int  s=0; 
	
	/**constructor*/
	public LexProb()
	{}
	
	/**constructor
	 * 
	 * @param t tag code
	 */
	public LexProb(int t)
	{s=t;}
	
	/**
	 * constructor
	 * @param t tag code
	 * @param x probability
	 */
	public LexProb(int t,float x)
	{p=x; s=t;}
	
	
	public int compareTo(Object o)
	{
		LexProb l=(LexProb) o;
		return this.s - l.s;
	}
	
 
}
