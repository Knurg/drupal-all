package hmmtagger.api;

import java.io.IOException;


/**
interface representing a stream of instances of class <pre>TaggedWord</pre>
 */
public interface ResultStream 
{  
	/**
	 * returns the next word in the stream
	 * @return the next word or null if any
	 * @throws IOException
	 */
	public TaggedResultWord nextWord() throws Exception;
	
	/**returns an array representation of the stream
	 * @return the array representation
	 * @throws IOException
	 */
	public TaggedResultWord[] toArray() throws Exception;
	
	/**returns the score of a tag sequence
	 * 
	 * @param rank the number of the tag sequence
	 * @return the score
	 * @throws Exception
	 */
	public float getScore(int rank) throws Exception;
}
