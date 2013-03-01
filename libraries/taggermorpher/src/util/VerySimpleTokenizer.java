package util;



import hmmtagger.api.TokenStream;

import java.io.*;


/**
*Simple Tokenizer
 */
public class VerySimpleTokenizer implements TokenStream 
{
    protected static int lookAheadBufferSize=50;
	protected PushbackReader instr=null;
	protected String [] abbrev=null;
	protected char[] oneSignTokens=null;
	
	/**
	 * constructor
	 * @param f input file
	 * @param abbr list of abbreviations
	 * @param ost list of tokens of length one
	 * @throws IOException
	 */
	public VerySimpleTokenizer(File f,String[] abbr,char[] ost) throws IOException
	{
		this(abbr,ost);
		instr=new PushbackReader(new InputStreamReader(new FileInputStream(f)),lookAheadBufferSize);
	}
	
	/**
	 * constructor
	 * @param s input string
	 * @param abbr list of abbreviations
	 * @param ost list of tokens of length one
	 */
    public VerySimpleTokenizer(String s, String[] abbr, char[] ost) 
    {
    	this(abbr,ost);
    	instr=new PushbackReader(new StringReader(s),lookAheadBufferSize);	
    }

    protected VerySimpleTokenizer(String[] abbr,char[] ost)
    {
    	abbrev=abbr;
    	oneSignTokens=ost;
    }
    
    protected boolean isOneSignToken(char c)
    {
    	for (int i=0; i<oneSignTokens.length; i++)
    	{
    		if (oneSignTokens[i] == c) return true; 
    	}
    	return false;
    }
    protected boolean isAbbreviation(String s)
    {
    	for (int i=0; i<abbrev.length; i++)
    	{
    		if (s.equals(abbrev[i])) return true;
    	}
    	return false;
    }
	/**
	 * @see hmmtagger.api.TokenStream#nextToken()
	 */
	public String nextToken() throws Exception, IOException 
	{
		if (instr==null) return null;
		String token="";
		int ch;
		while ( (ch=instr.read()) != -1 )
		{
			char c=(char) ch;
			switch (c) {
			  case ' ' :  if   (!(token.equals(""))) return token; continue;
			  case 9   :  instr.unread(' '); continue; //tab
			  case 10  :  if (token.equals("")) continue;
			  	          if (token.charAt(token.length()-1) == '-')
			  	             {token=token.substring(0,token.length()-1);}
			  	          else
			  	            {instr.unread(' ');}
			              continue;
			              	 
			  case 13  :  if (token.equals("")) continue;
			  	          if (token.charAt(token.length()-1) == '-')
	                        {token=token.substring(0,token.length()-1);}
			  	          else
			  	            {instr.unread(' ');}
                          continue; 
			} 
			if (isOneSignToken(c))
			{   
				if (token.equals("")) return String.valueOf(c);
				if ( (c=='.') || (c==',') )
				{   
					boolean integer=isGermanInt(token);
					if (c=='.')
					{	
					  String t=token+".";
					  if ( (isAbbreviation(t)) || (integer) ) return t;
					}  
					else
				    {
						if (integer)
						{	
						    char lookAhead= (char) instr.read();
						    if (Character.isDigit(lookAhead))
						    {
							    token = token + String.valueOf(c) + String.valueOf(lookAhead);
							    continue;
						    }
						    else
						    {
							    instr.unread(lookAhead);
						    }
						}   
				     }
					
				}
				if (c!='-')
				{
				  instr.unread(c);
				  return token;
				}  			
			}
	
			token=token+String.valueOf(c);
			
		}
		instr.close();
		instr=null;
		return null;
	}

	/**
	 * checks if a given String represents an integer number
	 * @param s the string
	 * @return true if it does
	 */
	public static boolean isGermanInt(String s)
	{
		char first=s.charAt(0);
		if  (!( (Character.isDigit(first)) || (first=='+') || (first=='-') ) ) return false;
		for (int i=1;i<s.length(); i++)
		{
			if ( !( Character.isDigit(s.charAt(i)) ) ) return false;
		}
		return true;
	}
	
}
