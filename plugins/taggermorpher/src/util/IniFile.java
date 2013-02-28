package util;
import java.io.*;

import java.util.*;
/**This class provides methods for accessing a .ini-File of name/value pairs (one per line)
 *  Format: <name>"="<value>. The file location is to be specified via system property TAGGER_PATH
 */
public class IniFile 
{
	
	/**the instance*/
	private static IniFile theInstance=null;
	/**maps parameter names to values*/
    private Hashtable table=null;
    
    /**constructor
     * 
     * @param f the ini-File
     * @throws IOException
     * @throws Exception
     */
    private IniFile(File f) throws IOException,Exception
    {
    	table=new Hashtable();
    	BufferedReader b=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
    	String s;
    	String[] splitted;
    	while ( (s=b.readLine())!= null )
    	{
    		splitted = s.split("=");
    		if (splitted.length != 2) throw new Exception("IniFile.Constructor: Invalid ini-File");
    		if ((splitted[0].length() < 1) || (splitted[1].length() < 1))
			   {throw new Exception("IniFile.Constructor: Invalid ini-File");}
    		table.put(splitted[0],splitted[1]);	
    	}
    }
    
    /**returns the value of a given parameter as a string 
     * @param param parameter name
     * @return string representation of the parameter's value (or null, if the name is invalid)
     */
    public final String getStringVal(String param) throws Exception
    {
    	Object o= table.get(param);
    	if (o==null) 
    	   {throw new Exception("IniFile.getStringVal: invalid name");} 
    	else 
    	   {return (String) o;}
    }
    
    
    /**returns the value of a given parameter as an integer
     * @param param parameter name
     * @return Integer representation of the parameter's value (or null, if the name is invalid)
     */
    public final Integer getIntVal(String param)throws Exception
    {
    	Object o= table.get(param);
    	if (o==null) {throw new Exception("IniFile.getIntVal: invalid name");}
  
    	return new Integer((String) o);
    }
    
    /**
     * @return an <pre>IniFile</pre> instance
     * @throws IOException
     * @throws Exception
     */
    public final static IniFile getIniFile() throws IOException,Exception
	{   
    	if (theInstance == null)
    	{   
    		String location = System.getProperty("TAGGER_PATH");
    		if (location==null) {throw new Exception("IniFile.getIniFile: environnement var TAGGER_PATH missing");}
    		theInstance=new IniFile(new File(location + File.separator + "settings.ini"));	
    	}
    	return theInstance;
	}
	
	
}
