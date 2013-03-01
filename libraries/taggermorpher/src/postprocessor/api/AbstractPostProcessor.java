package postprocessor.api;



import hmmtagger.api.ResultStream;
import hmmtagger.api.TaggedResultWord;

import java.util.ArrayList;


/**abstract post-processor class*/
public abstract class AbstractPostProcessor implements ResultStream 
{
	/**the result stream to be post-processed*/
    protected ResultStream stream=null;
	
   /**constructor
    * 
    * @param rs the result stream to be post-processed
    */
	protected AbstractPostProcessor(ResultStream rs)
	{
		stream=rs;
	}
	
	/**
	 * @see hmmtagger.api.ResultStream#nextWord()
	 */
	public abstract TaggedResultWord nextWord() throws Exception ;

	/** 
	 * @see hmmtagger.api.ResultStream#toArray()
	 */
	public TaggedResultWord[] toArray() throws Exception 
	{
		ArrayList al=new ArrayList();
		TaggedResultWord tw=null;
		while ((tw=nextWord()) != null)
		  {al.add(tw);}
		TaggedResultWord[] res = new TaggedResultWord[al.size()];
		al.toArray(res);
		return res;
	}

	/** 
	 * @see hmmtagger.api.ResultStream#getScore(int)
	 */
	public float getScore(int rank) throws Exception 
	{
		return stream.getScore(rank);
	}

}
