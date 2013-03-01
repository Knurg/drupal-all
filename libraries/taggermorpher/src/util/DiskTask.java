
package util;

import hmmtagger.api.*;
import hmmtagger.tagger.*;


/**
* A sub class of <pre>TaggingTask</pre>, for tagging a stream of tokens. It writes the results to the disk.
*/
public class DiskTask extends TaggingTask implements Runnable
{
	/**the stream of tokens*/
	private TokenStream tokens=null;
	
	/**
	 * constructor
	 * @param bv tagger object
	 * @param ts token stream
	 * @param delim sentence delimiters
	 * @param lexInfo flag indicating if lex. info is to be returned
	 */
    public DiskTask(BeamViterbi bv,TokenStream ts,String[] delim,boolean lexInfo)
    {
    	super(bv,delim,lexInfo);
    	tokens=ts;
    }
	/** 
	 * @see java.lang.Runnable
	 */
	public void run() 
	{   
		try
		{	   
	       setResult(viterbi.run(tokens,delimiters,false,lexicalInfo));
	       setReady(true);
		}
        catch (Exception e) {setError(e); setReady(true); return;}
	}

}
