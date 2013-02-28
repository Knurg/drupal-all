package hmmtagger.api;

import hmmtagger.tagger.*;
/**
*This abstract class implements the interface <pre>Runnable</pre> and represents a tagging task that can be 
*executed in its own thread.
 */
public abstract class TaggingTask implements Runnable
{  
	/**instance of the viterbi algorithm usesd by this instance*/
	protected BeamViterbi viterbi=null;
	/**array of sentence delimiters*/
	protected String[] delimiters=null;
	/**the <pre>ResultStream</pre>  which stores the results of this run*/
	private ResultStream result = null;
	/**flag indicating the state of the run*/
	private boolean ready=false;
	/**stores an Exception if one occured during the run*/
	private Exception error=null;
	/**flag indicating if lex. info is to be returned*/
	protected boolean lexicalInfo;
	/**
	 * constructor
	 * @param bv instance of <pre>BeamViterbi</pre>
	 * @param delim array of sentence delimiters
	 * @param lexInfo flag indicating if lex. info is to be returned
	 */
   protected TaggingTask(BeamViterbi bv,String[] delim,boolean lexInfo)
   {
   	   viterbi=bv;
   	   delimiters=delim;   
  
   	   lexicalInfo=lexInfo;
   }
	
   /**
    * @see java.lang.Runnable
    */
   public abstract void run();
  
   /**
    * should only be called after termination of the run
    * @return the result stream, s
    */
   public synchronized ResultStream getResult()
   {
   	return result;
   }
   
   
   /**
    * should only be called after termination of the run
    * @return an Exception occured during the run or null if any
    */
   public synchronized Exception getError()
   {
   	return error;
   }
    
   /**
    * 
    * @return the state of the run
    */
   public synchronized boolean isReady()
   {
   	 return ready;
   }
   
   protected synchronized void setResult(ResultStream res)
   {
   	  result=res;
   }
   
   protected synchronized void setError(Exception e)
   {
   	  error=e;
   }
   protected synchronized void setReady(boolean r)
   {
   	  ready=r;
   }
   
   
}
