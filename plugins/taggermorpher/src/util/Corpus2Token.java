package util;


import hmmtagger.api.TokenStream;
import hmmtagger.tagger.TaggedCorpus;
import hmmtagger.tagger.TaggedWord;

import java.io.IOException;

/**
* A bridge from a stream of instances of <pre>TaggedWord</pre> to a stream of <pre>String</pre> tokens
 */
public class Corpus2Token implements TokenStream 
{
	/**<pre>TaggedWord</pre> Stream*/
	private TaggedCorpus corpus =null;
	
	/**constructor
	 * 
	 * @param c <pre>TaggedWord</pre> Stream
	 */
	public Corpus2Token(TaggedCorpus c)
	{
	    corpus=c;	
	}
	
	/**@see hmmtagger.api.TokenStream#nextToken()*/
	public final String nextToken() throws Exception, IOException 
	{
        TaggedWord tw = corpus.nextToken(); 	
		if (tw==null) return null;
		return tw.getWord();
	}

}
