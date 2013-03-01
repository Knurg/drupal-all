package hmmtagger.fixtree;

import util.ClassifiedItem;

/**
*abstract parent class of classes (<pre>PrefixItem</pre> and <pre>SuffixItem</pre>
 */
public abstract class WordItem implements ClassifiedItem 
{
    protected String word=null;
    protected int tag;
	
	public WordItem(String w,int t) 
	{
	    word=w;
	    tag=t;
	}

	public WordItem(hmmtagger.tagger.TaggedWord tw)
	{
		word=tw.getWord();
		tag=tw.getIntTag();
	}
	
	public WordItem(String w)
	{
		word=w;
		tag=-1;
	}
	
	public abstract char getChar(int index) throws Exception;
	
	
	public int getClassId() 
	{
		return tag;
	}

}
