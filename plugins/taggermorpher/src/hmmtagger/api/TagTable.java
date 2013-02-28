package hmmtagger.api;
import java.util.Hashtable;
import java.io.File;
import java.io.Serializable;
import java.io.PrintWriter;
import java.io.FileOutputStream;
/**This class maps tag names to corresponding int values and vice versa*/
public final class TagTable implements Serializable
{
	/**Number of tags*/
	private int nTags;
	/**Array of tag names (Mapping number->name)*/
	private String[] tagNames = null;
	/**Mapping name->number*/
	private Hashtable  tagCodes=null;
	
	/**
	 * constructor
	 * @param n Number of Tags
	 */
    public TagTable(int n)
    {
    	nTags=n;
    	tagCodes = new Hashtable();
    	tagNames=new String[n];
    	for (int i=0; i<n; i++)
    	  {tagNames[i]=null;}
    }
    
    /**
     * constructor
     * @param n Number of Tags
     * @param names Array of tag names
     * @throws Exception if array length not equal to the number of tags
     */
    public TagTable(int n,String[] names) throws Exception
    {
    	this(n);
    	if (n!=names.length) {throw new Exception("TagTable.constructor: invalid array");}
    	for (int i=0; i<n; i++)
    	{
    		setTagName(names[i],i);
    	}
    }
    
    /**
     * sets name of a tag 
     * @param s the tag name
     * @param c the tag number
     * @throws Exception if c is out of bounds
     */
    public final void setTagName(String s,int c) throws Exception
    {
    	if (c>=nTags) throw new Exception("TagTable.setTagName: invalid tag code");
    	tagNames[c]=s;
    	tagCodes.put(s,new Integer(c));
    }
    
    /**
     * returns tag name
     * @param c the tag number
     * @return the tag name
     * @throws Exception if c is out of bounds
     */
    public final String getTagName(int c)throws Exception
    {  
        if (c>=nTags) throw new Exception("TagTable.getTagName: invalid tag code");	
        return tagNames[c];     
    }
    
    /**
     * returns tag number
     * @param name the tag name
     * @return the tag number
     * @throws Exception
     */
    public final int getTagCode(String name) throws Exception
    {
        Object o = tagCodes.get(name);	
        if (o==null) {throw new Exception("TagTable.getTagCode: invalid tag name");}
        return ((Integer) o).intValue();
    }
    
    /**
     * 
     * @return number of tags
     */
    public final int getNTags()
    {
    	return nTags;
    }
    
    /**
     * dumps the table to a file
     * @param f the file
     * @throws Exception
     */
    public final void dump(File f)throws Exception
    {
    	PrintWriter pw=new PrintWriter(new FileOutputStream(f));
    	for (int i=0; i<nTags; i++)
    	{
    		pw.println(i + " : " +tagNames[i]);
    	}
    	pw.flush();
    	pw.close();
    }
}
