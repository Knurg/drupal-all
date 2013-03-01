package util;

import hmmtagger.api.TaggedResultWord;

/**Implementation of a result stream
 * @see hmmtagger.api.ResultStream*/
public class ResStream implements hmmtagger.api.ResultStream 
{
	/**underlying object stream*/
	private ObjStream oStream=null;
	
	/**scores of the tag sequences*/
	private float[] scores=null;
	
	/**
	 * constructor
	 * @param os underlying object stream
	 * @param sc scores
	 */
	public ResStream(ObjStream os,float[] sc)
	{
		oStream=os;
		scores=sc;
	}
	
	/**
	 * @see hmmtagger.api.ResultStream
	 */
	public final TaggedResultWord nextWord() throws java.io.IOException
	{
		return (TaggedResultWord) oStream.next();
	}

	/**
	 * @see hmmtagger.api.ResultStream
	 */
	public final TaggedResultWord[] toArray() throws java.io.IOException
	{
		Object o[] = oStream.toArray();
		TaggedResultWord[] res = new TaggedResultWord[o.length];
		for (int i=0; i<res.length; i++)
		{
			res[i] = (TaggedResultWord) o[i];
		}
		return res;
	}
	
	/**a score of a given tag sequence
	 * @param rank rank of the sequence
	 */
	public final float getScore(int rank) throws Exception
	{
		try
		{
			return scores[rank];
		} catch (Exception e) {throw new Exception("ResStream.getScore : invalid rank");}
	}
    
}
