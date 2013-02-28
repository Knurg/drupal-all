package hmmtagger.tagger;


import hmmtagger.api.SpecificDictionaryManipulator;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.ArrayList;


import util.SentenceSplitter;
import util.Utility;
/**
Contains a table of words with their lexical probabilities. The data is stored in a<pre>Hashtable</pre>-Object,
the keys being the words represented as Strings, the values being arrays of instances of the container class <pre>LexProb</pre>
 */
public abstract class Dictionary implements java.io.Serializable
{
	/**the table*/
	protected Hashtable lexicalProbs=null;
	/**table of relative tag frequencies*/
	protected float[] tagProb=null;
	/**List of equivalence class entries already calculated*/
	protected ArrayList equClasses=null;
	/**number of tags*/
	protected int nTags;
	/**flag indicating if counts are available or have already been replaced by prob estimates*/
	protected boolean countsAvailable=true;
	
	/**constructor
	 * 
	 * @param corpus training corpus
	 * @param delimiter sentence delimiter tag
	 * @param nTags number of tags
	 * @param tagP table of tag probabilities
	 * @param dm manipulator object
	 * @throws Exception
	 */
	public Dictionary(TaggedCorpus corpus, int delimiter,int nTags,float[] tagP,SpecificDictionaryManipulator dm) throws Exception
	{   
		this.nTags=nTags;
		tagProb=tagP;
		equClasses=new ArrayList();
		lexicalProbs=new Hashtable();
		SentenceSplitter sp = new SentenceSplitter(corpus,delimiter);
		TaggedWord[] tw=null;
		float totalCap=0;
	  
		//counting
	    int nWords=0;
		while ( (tw=sp.nextSentence()) != null)
	    {			
			calcLexCounts(tw,lexicalProbs);
			nWords += tw.length;
	    }
		
		//for all entries: replace vector by an array
		Enumeration en=lexicalProbs.keys();
		while (en.hasMoreElements())
		{
			String w=(String) en.nextElement();
			Vector v=(Vector) lexicalProbs.get(w);
			LexProb[] lpa=new LexProb[v.size()];
			v.toArray(lpa);
			Arrays.sort(lpa);
			lexicalProbs.put(w,lpa);
			v=null;
		}
		//call manipulator
		if (dm!=null) dm.manipCounts(this);
   	    calcLexicalProbs();
		countsAvailable=false;
   	    if (dm!=null) dm.manipProbs(this);
	}
	
    /**
     * returns vector of lexical probabilities for a given word
     * @param w the word
     * @return vector of lex probabilities
     */
	public LexProb[] getDistribution(String w)
	{
		if (lexicalProbs.containsKey(w))
		{
			return (LexProb[]) lexicalProbs.get(w);
		}
		return null;
			
	}
	
	/**
	 * 
	 * @return table of tag prob.
	 */
	public float[] getTagProb()
	  {return tagProb;}
	
	/**
	 * 
	 * @return Enumeration of all dictionary entries
	 */
	public Enumeration getEntries()
	{
		return lexicalProbs.keys();
	}
	
	/**sets the vector of lexical prob. for a given word
	 * 
	 * @param w the word
	 * @param d the new prob. vector
	 */
	public void setEntryDistribution(String w,LexProb[] d)
	{
		lexicalProbs.put(w,d);
	}
	
	
	/** returns an equivalence class entry for the class of words with a given subset of possible tags
	 * 
	 * @param tagSet the subset of tags (must be sorted in ascending order!)
	 * @param save flag indicating if the entry is to be stored
	 * @return eqivalence class entry
	 * @throws Exception
	 */
	public EquClassEntry getTagSetClassDistribution(int[] tagSet,boolean save) throws Exception
	{
		boolean buffered=false;
		int[] ts=new int[tagSet.length];
		for (int i=0; i<ts.length; i++)
		  {ts[i]=tagSet[i];}
		//search for entry in buffer
		for (int i=0; i<equClasses.size(); i++)
		{
			EquClassEntry e=(EquClassEntry) equClasses.get(i);
			if (e.set.length==ts.length)
			{
				boolean equal=true;
				for (int j=0; j<ts.length; j++)
				{
					if (ts[j]!=e.set[j]) {equal=false; break;}
				}
				if (equal==true) return e;
			}
		}
		//if not found in buffer, calculate it
		if (countsAvailable==false) 
			throw new Exception("Dictionary.getTagSetClassDistribution: distribution cannot be calculated");
		EquClass ec=Dictionary.getTagSetClassCounts(ts,this,nTags);
		if (ec==null) return null;
		Utility.Mle(ec.lp);
		ArrayList al=new ArrayList();
		for (int i=0; i<ec.lp.length; i++)
		{
			if (ec.lp[i] != Float.NEGATIVE_INFINITY) 
			  {al.add(new LexProb(i,(float) Math.log(ec.lp[i] - tagProb[i] )));}
		}
		LexProb[] resAr=new LexProb[al.size()];
		al.toArray(resAr);
		EquClassEntry res=new EquClassEntry(resAr,ts,ec.count);
		if (save) equClasses.add(res);
		return res;
	}
	

	/**method calculating the probability estimates out of the counts*/
	public abstract void calcLexicalProbs();
	
	/**
	 * returns an object representing an equivalence class of words with a given subset of possible tags
	 * @param set the subset of tags (must be sorted in ascending order!)
	 * @param dic the dictionary to be used
	 * @param nTags number of tags
	 * @return
	 */
	public static EquClass getTagSetClassCounts(int[] set,Dictionary dic,int nTags)
	{   
		float[] c=new float[nTags];
		for (int i=0; i<nTags; i++)
		   {c[i]=0;}
		Enumeration en=dic.getEntries();
		int z=0;
		while (en.hasMoreElements()) //for all entries: check if their set of possible tags is equal to the given one
		{
			String w=(String) en.nextElement();
			LexProb[] lpa=dic.getDistribution(w); //sorted!
			if (set.length==lpa.length)
			{
				boolean equal=true;
			    for (int i=0; i<lpa.length; i++)
			    {
			    	if (lpa[i].s != set[i]) 
			    	  {equal=false; break;}
			    }
				if (equal)
				{
					z++;
					for (int i=0; i<lpa.length; i++)
					{
						c[lpa[i].s] += lpa[i].p;
					}
				}	
			}	
		}
		if (z==0) return null;
		return new EquClass(c,z);
	}
	
	
	
	
	
	/**counts the tag frequencies of the words of a given training corpus
	 * 
	 * @param w the training corpus
	 * @param t the table where the counts are to be stored in
	 */ 
	public static void calcLexCounts(TaggedWord[] w,Hashtable t)
	{
		int l=w.length; 
		for (int i=0; i<l; i++)
		{   
			String wd = w[i].getWord(); 
			int tag = w[i].getIntTag();
			if (t.containsKey(wd))
			{
				Vector v = (Vector) t.get(wd);
				boolean newTag=true;
				for (int j=0; j<v.size(); j++)
				{
					LexProb lp = (LexProb) v.elementAt(j);
					if (lp.s == tag)
					{
						newTag=false;
						lp.p++;
						break;
					}	
				}
				if (newTag) {v.addElement(new LexProb(tag,(float) 1.0));}	
			}
			else
			{
				Vector v=new Vector();
				v.addElement(new LexProb(tag,(float) 1.0));
				t.put(wd,v);
			}
		}
	}
	
}
