package hmmtagger.tagger;


/**
 * container for lexical info about a word
 */
public class LexicalEntry implements java.io.Serializable
{
	/**the word*/
	protected String word;
	/**vector of lexical probability estimates*/
	protected LexProb[] p;
	/**additional info: 
	 *  c: capitalized form of word has been found in dictionary; 
	 *  f: word has been found in dictionary
	 *  m: word has been analysed by the morph bib.
	 *  s: special word (number)
	 *  t: word has been analysed by means of the prefix/suffix trees
	 *  u: word is completely unknown */
	protected char info;
	
    /**constructor 
     * 
     * @param w word
     * @param p lex prob.
     * @param prop additional info
     */
	public LexicalEntry(String w, LexProb[] p, char prop)
	{
		word=w;
		this.p=p;
		info=prop;
	}
	
	/**
	 * 
	 * @return word
	 */
	public String getWord()
	  {return word;}
	/**
	 * 
	 * @return lex prob.
	 */
	public LexProb[] getProb()
	  {return p;}
	
	/**
	 * 
	 * @return additional info
	 */
	public char getInfo()
	  {return info;}
	
}
