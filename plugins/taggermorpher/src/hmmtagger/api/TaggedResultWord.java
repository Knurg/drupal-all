package hmmtagger.api;

import hmmtagger.tagger.LexicalEntry;

/**encapsulates a word and its tags*/
public class TaggedResultWord implements java.io.Serializable, Cloneable
{
	/**the word*/
	protected String word=null;
	/**the tags*/
	protected int[] tags=null;
	/**tag table*/
    protected TagTable table=null;
    /** lex. info*/
    protected LexicalEntry info=null;
    
    
    protected TaggedResultWord()
    {}
    
    /**constructor
     * 
     * @param w word
     * @param t tags
     * @param tab tag table
     */
    public TaggedResultWord(String w, int t[], TagTable tab)
    {
    	word=w;
    	tags=t;
    	table = tab;
    }
    /**constructor
     * 
     * @param w word
     * @param t tags
     * @param tab tag table
     * @param le lex. info
     */
    public TaggedResultWord(String w, int t[], TagTable tab,LexicalEntry le)
    {
    	this(w,t,tab);
    	info=le;
    }
    
    /**
     * 
     * @return the word
     */
    public String getWord()
    {
    	return word;
    }
    
    /**
     * 
     * @return the most probable tag (String)
     */
    public String getBestStringTag()
    {   
    	try
		{
    	   return table.getTagName(tags[0]);
		}
    	catch (Exception e) {/*internal error...logging?*/}
    	return null;
    }
    
    /**
     * sets the most probable tag
     * @param val new value
     */
    public void setBestTag(int val)
    {
    	tags[0] = val;
    }
    
    /**
     * 
     * @return the most probable tag (Integer)
     */
    public int getBestIntTag()
    {
    	return tags[0];
    }
    
    
    /**
     * sets a tag
     * @param r rank of the tag
     * @param val new value
     * @throws Exception
     */
    public void setTag(int r,int val) throws Exception
    {
    	try
		{
    	   tags[r] = val;
		}
    	catch (ArrayIndexOutOfBoundsException e) 
		   {throw new Exception("TaggedResultWord.setTag: rank out of bounds");}
    	catch (Exception e) {/*internal error...logging?*/}
    }
    
    /**
     * returns a tag (String)
     * @param r rank of the tag
     * @return the tag 
     * @throws Exception
     */
    public String getStringTag(int r) throws Exception
    {   
    	try
		{
    	   return table.getTagName(tags[r]);
		}
    	catch (ArrayIndexOutOfBoundsException e) 
		   {throw new Exception("TaggedResultWord.getStringTag: rank out of bounds");}
    	catch (Exception e) {/*internal error...logging?*/}
    	return null;
    }
    
    /**
     * returns a tag (Integer)
     * @param r rank of the tag
     * @return the tag
     * @throws Exception
     */
    public int getIntTag(int r) throws Exception
    {
    	try
		{
    	   return tags[r];
		}
    	catch (ArrayIndexOutOfBoundsException e) 
		   {throw new Exception("TaggedResultWord.getStringTag: rank out of bounds");}
    }
	
    /**
     * 
     * @return the tag table
     */
    public TagTable getTagTable()
    {
    	return table;
    }
    
    /**
     * 
     * @return lex. info
     */
    public LexicalEntry getLexicalInfo()
    {
    	return info;
    }
    
    
    /**
     * clones this instance; everything but the tag array is shared
     */
    public Object clone()
    {
        TaggedResultWord res=new TaggedResultWord();
        res.word=this.word;
        res.table=this.table;
        res.info=this.info;
        res.tags=new int[this.tags.length];
        for (int i=0; i<res.tags.length; i++)
          {res.tags[i] = this.tags[i];}
        return res;
    }
}
