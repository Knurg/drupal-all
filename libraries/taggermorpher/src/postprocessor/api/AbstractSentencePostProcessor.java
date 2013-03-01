package postprocessor.api;


import hmmtagger.api.ResultStream;
import hmmtagger.api.TaggedResultWord;

import java.util.ArrayList;



/**abstract "sentence by sentence" post-processor. Reads the tokens from a ResultStream in a buffer
 * (sentence by sentence) and processes them, before providing them token by token*/
public abstract class AbstractSentencePostProcessor extends AbstractPostProcessor 
{
	/**sentence delimiter characters*/
	protected String[] delimiters=null;
	/**sentence buffer*/
	protected ArrayList buffer=new ArrayList();
	/**buffer pointer*/
	protected int bufPointer=0;
	/**flag indicating that the end of the underlying stream has been reached*/
	protected boolean endOfStream=false;
	
	/**constructor
	 * 
	 * @param rs the result stream to be processed
	 * @param delim sentence delimiters
	 */
    protected AbstractSentencePostProcessor(ResultStream rs,String[] delim)
    {
    	super(rs);
    	delimiters=delim;
    }
	
    /**
     * @see hmmtagger.api.ResultStream#nextWord()
     */
	public TaggedResultWord nextWord() throws Exception 
	{   
		if (stream==null) throw new Exception("AbstractSentencePostProcessor.nextWord: no underlying stream specified");
		if (endOfStream) return null;
		if (buffer.size()==0)
		{
			TaggedResultWord tw=null;
			boolean delimiter=false;
			do
			{   
				tw=stream.nextWord();
				if (tw != null) 
				{
					buffer.add(tw);
					delimiter=false;
					String w=tw.getWord();
					for (int i=0; i<delimiters.length; i++)
					{
						if (w.equals(delimiters[i]))
						{
							delimiter=true;
							break;
						}
					}
				}   
			}   while ((tw != null) && (delimiter==false));
			if (buffer.size() == 0)
			{
				endOfStream=true;
				return null;
			}
			processBuffer();
			bufPointer=0;
		}
		TaggedResultWord res=(TaggedResultWord) buffer.get(bufPointer);
		bufPointer++;
		if (bufPointer >= buffer.size())  buffer.clear();
		
		return res;
	}
    
	/**
	 * method doing the actual post-processing of the current sentence in the buffer
	 * @throws Exception
	 */
	protected abstract void processBuffer() throws Exception;
}
