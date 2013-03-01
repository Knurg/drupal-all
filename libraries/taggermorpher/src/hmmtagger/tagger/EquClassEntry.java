package hmmtagger.tagger;

import java.io.Serializable;


/**
 * represents an equivalence class of words with the same subset of possible tags 
 */
public class EquClassEntry implements Serializable
{
   /**estimates of the lex probabilities in the class*/ 	
   public LexProb[] probs=null;
   /**the tag subset*/
   public int[] set=null;
   /**number of words in the class*/
   public int count;
   
  /**constructor
   * 
   * @param p prob. estimates
   * @param s tag subset
   * @param c number of words
   */ 
  public EquClassEntry(LexProb[] p, int[] s, int c)
  {
  	probs=p;
  	set=s;
  	count=c;
  }
   
}
