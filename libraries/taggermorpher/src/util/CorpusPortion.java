package util;

import hmmtagger.tagger.TaggedCorpus;
import hmmtagger.tagger.TaggedWord;

import java.io.IOException;




/**
*This class represents a portion of another <pre>TaggedCorpus</pre> instance
*It implements itself the interface <pre>TaggedCorpus</pre>
*@see hmmtagger.tagger.TaggedCorpus
 */
public class CorpusPortion implements TaggedCorpus 
{  
	/**the original corpus instance*/
	protected TaggedCorpus corpus=null;
	/**index of the first sentence of the represented portion (inclusive)*/
    protected int firstSentence=0;
    /**index of the last sentence of the represented portion (exclusive)*/
    protected int lastSentence=0; 
    /**sentence delimiter tag*/
	protected int delimiter;
	/**index of the current sentence*/
    protected int currentSentence;
	
    /**
     * constructor
     * @param c the original corpus
     * @param start index of the first sentence of the portion
     * @param end index of the last sentence of the portion (exclusive)
     * @param d  the sentence delimiter tag
     */
	public CorpusPortion(TaggedCorpus c,int start,int end,int d)
	{
		corpus=c;
		firstSentence=start;
		lastSentence=end;
		delimiter=d;
	}
	
	/**
	 * @see hmmtagger.tagger.TaggedCorpus
	 */
	public final void initialize() throws IOException 
	{
		corpus.initialize();
		TaggedWord tw=null;
		currentSentence=0;
		while (currentSentence != firstSentence)  //skip until firstSentence is reached
		{   
			try 
			   {tw=corpus.nextToken();} 
			catch (Exception e) {if (e instanceof IOException) throw (IOException) e;}
			if (tw==null) break;
			if (tw.getIntTag() == -1) {currentSentence++;}
		}
	}
	
	/**
	 * @see hmmtagger.tagger.TaggedCorpus
	 */
	public final TaggedWord nextToken() throws IOException, Exception 
	{
		if (currentSentence >= lastSentence) return null;
		TaggedWord tw=corpus.nextToken();
        if (tw==null) return null;
        if (tw.getIntTag()==-1)
        {	
        	currentSentence++;
        	tw=corpus.nextToken();
        }   	
        return tw;
	}

}
