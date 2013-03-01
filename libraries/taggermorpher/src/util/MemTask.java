package util;



import hmmtagger.api.*;
import hmmtagger.tagger.BeamViterbi;


/**
* A sub class of <pre>TaggingTask</pre>, for tagging an <pre>String</pre> array of tokens. 
* It stores the results in the memory.
*/
public class MemTask extends TaggingTask implements Runnable 
{
	/** the array of tokens*/
    private String[] tokens=null;
	
    /**
     * constructor
     * @param bv tagger object
     * @param t the token array
     * @param delim array of sentence delimiters
     * @param lexInfo flag indicating if lex. info ist to be returned
     */
	public MemTask(BeamViterbi bv,String[] t,String[] delim,boolean lexInfo)
	{
		super(bv,delim,lexInfo);
		tokens=t;
	}
	
	/** 
	 * @see java.lang.Runnable
	 */
	public void run() 
	{
		try
		{
		
			setResult(viterbi.run(new ArrayStream(tokens),delimiters,true,lexicalInfo));
			setReady(true);
		}
        catch (Exception e) {setError(e); setReady(true); return;}
	}

}
