package hmmtagger.tagger;
import java.io.*;


/**Represents a pretagged corpus for training/test purposes*/
public interface TaggedCorpus 
{
	/**reset (must be called at first)*/
	public void initialize() throws IOException;
	
	/**
	 * returns next token
	 * @return the next token
	 * @throws IOException
	 * @throws Exception
	 */
	public TaggedWord nextToken() throws IOException,Exception;
	
	
}
