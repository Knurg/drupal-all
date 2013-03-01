package util;


import hmmtagger.api.TokenStream;

import java.io.IOException;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;


/**token stream with an underlying file in one word per line format*/
public class OneWperLTS implements TokenStream 
{
	/**stream for accessing the file*/
    private BufferedReader r=null;
	
    /**constructor
     * 
     * @param f corpus file
     * @throws Exception
     */
	public OneWperLTS(File f) throws Exception
	{
		r=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
	}
	
	/**
	 * @see hmmtagger.api.TokenStream#nextToken()
	 */
	public final String nextToken() throws Exception, IOException 
	{
		return r.readLine();
	}

}
