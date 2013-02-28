package hmmtagger.tagger;
import java.io.Serializable;

/** This class stores a word / tag number pair*/
public class TaggedWord implements Serializable
{
	
   /**the word*/
   protected String word;
   /**the tag*/
   protected int tag;
	
  /**constructor
   * 
   * @param w the word
   * @param t the tag number
   */	
  public TaggedWord(String w,int t)
  {
  	word=w;
  	tag=t;
  }
	
  /**
   * 
   * @return the word
   */
  public String getWord()
  {
  	return word;
  }
  /**
   * 
   * @return the tag number
   */
  public int getIntTag()
  {
  	return tag;
  }
  

}
