package hmmtagger.api;
import java.io.IOException;

/**Interface representing a stream of string tokens*/
public interface TokenStream 
{  
	/**
	 * 
	 * @return the next token (null if there is no token left)
	 * @throws Exception
	 * @throws IOException
	 */ 
	public String nextToken() throws Exception,IOException;
}
