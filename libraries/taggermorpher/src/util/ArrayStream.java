package util;


import hmmtagger.api.TokenStream;

import java.io.IOException;



/**
* TokenStream which can be wrapped around an array of tokens
* @see hmmtagger.api.TokenStream
 */
public final class ArrayStream implements TokenStream 
{
	/**the array*/
	private String[] tokens=null;
	/**pointer to current element*/
	private int currentToken=-1;
	
	/**
	 * constructor
	 * @param s the array
	 */
	public ArrayStream(String[] s)
	{
		tokens=s;
	}
	
	/**
	 * @see hmmtagger.api.TokenStream
	 */
	public final String nextToken() throws Exception, IOException 
	{
		currentToken++;
		if (currentToken>=tokens.length) {return null;}
		return tokens[currentToken];
	}

}
