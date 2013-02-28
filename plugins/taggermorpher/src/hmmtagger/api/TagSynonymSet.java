package hmmtagger.api;


import java.io.Serializable;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
/**
 * this class maps a tagset of synonyms to a given master tagset 
 */
public class TagSynonymSet implements Serializable
{
    /**master tagset*/
	private TagTable master=null;
	/**the map*/
	private Hashtable mapping=null;
	
	private boolean mappingCompressed=false;
	
	/**
	 * constructor
	 * @param masterTable master set
	 */
	public TagSynonymSet(TagTable masterTable) 
	{
		master=masterTable;
		mapping=new Hashtable();
	}
    
	/**
	 * adds a synonym
	 * @param masterTag the master tag
	 * @param synonym its synonym
	 * @throws Exception
	 */
	public void addSynonym(String masterTag,String synonym)throws Exception
	{   
		int masterCode;
		try
		{
		    masterCode=master.getTagCode(masterTag);
		} catch (Exception e) {throw new Exception("TagSynonymSet.addSynonym : invalid master tag (" + masterTag + ")");}
		Object o=mapping.get(synonym);
		if (o!=null)
		{
			ArrayList al= (ArrayList) o;
			al.add(new Integer(masterCode));
		}
		else
		{
			ArrayList al=new ArrayList();
			al.add(new Integer(masterCode));
			mapping.put(synonym,al);
		}
	}
	
	/**
	 * 
	 * @return the master set
	 */
	public TagTable getMaster()
	{
		return master;
	}
	
	/**
	 * returns master tags for a given synonym
	 * @param synonym synonym tag
	 * @return array of master tags for the synonym
	 * @throws Exception
	 */
	public int[] getIntForSynonym(String synonym)throws Exception
	{
		if (!(mappingCompressed)) {compressMapping(); mappingCompressed=true;}
		Object o = mapping.get(synonym);
		if (o==null) throw new Exception("TagSynonymSet.getIntForSynonym : invalid synonym (" + synonym +")");
		return (int[]) o; 
	}
	
	protected void compressMapping()
	{
		Hashtable newMap=new Hashtable();
		Enumeration keys = mapping.keys();
		while (keys.hasMoreElements())
		{
			Object o=keys.nextElement();
			ArrayList al = (ArrayList) mapping.get(o);
			int[] newMasterTags = new int[al.size()];
			for (int i=0; i<al.size(); i++)
			  {newMasterTags[i] = ((Integer) al.get(i)).intValue();}
			newMap.put(o,newMasterTags);
		}
		mapping=newMap;
	}
	
}
