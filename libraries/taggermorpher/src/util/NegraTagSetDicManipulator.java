package util;

import hmmtagger.api.SpecificDictionaryManipulator;
import hmmtagger.api.TagSynonymSet;
import hmmtagger.api.TagTable;
import hmmtagger.tagger.Dictionary;
import hmmtagger.tagger.LexProb;
import hmmtagger.tagger.Morphologizer;

import java.util.Arrays;
import java.util.Enumeration;


/**
 * Dictionary manipulator...performs minor morphological analysis of the dictionary using the morph bib.
 */
public class NegraTagSetDicManipulator implements SpecificDictionaryManipulator 
{
	private TagSynonymSet tss=null;
	private TagTable tab=null;
    private int[][] sets=null; 
	private LexProb[][] equDistr=null;
	private Morphologizer morph=null;
	
	public NegraTagSetDicManipulator(TagTable t,TagSynonymSet syn,int[] capTags,String morphPath) throws Exception
	{
		tss=syn;
		tab=t;
		morph=new Morphologizer(syn,capTags,morphPath);
		sets=new int[3][];
		int vvfin=0;
		int vvinf=0;
		int vvpp=0;
		try
		{
		  vvfin=tab.getTagCode("VVFIN");
		  vvinf=tab.getTagCode("VVINF");
		  vvpp=tab.getTagCode("VVPP");
		}
		catch (Exception e) {}  
		sets[1]=new int[2]; sets[1][0]=vvinf; sets[1][1]=vvfin;
		sets[0]=new int[3]; sets[0][0]=vvinf; sets[0][1]=vvfin; sets[0][2]=vvpp;
	    sets[2]=new int[2]; sets[2][0]=vvpp;  sets[2][1]=vvfin;
	    Arrays.sort(sets[0]); Arrays.sort(sets[1]); Arrays.sort(sets[2]);
	}
	/** 
	 * @see hmmtagger.api.SpecificDictionaryManipulator#manipCounts(java.util.Hashtable)
	 */
	public void manipCounts(Dictionary dic) 
	{
		equDistr=new LexProb[sets.length][];
		for (int i=0; i<sets.length; i++)
		  {equDistr[i]=null;}
		for (int i=0; i<sets.length; i++)
		{   
			try
			{
			equDistr[i]=dic.getTagSetClassDistribution(sets[i],true).probs;	
			} catch (Exception e) {}
		}
	}

	/** 
	 * @see hmmtagger.api.SpecificDictionaryManipulator#manipProbs(java.util.Hashtable)
	 */
	public void manipProbs(Dictionary dic) 
	{
		Enumeration en=dic.getEntries();
		while (en.hasMoreElements())
		{		
		   String w=(String)en.nextElement();
		   LexProb[] lp=dic.getDistribution(w);
		   for (int i=0; i<sets.length; i++)
		   {
		   	 if (equDistr[i]==null) continue;
			 if (morph.beamTagSet(w,lp,sets[i]))
			 {
			 	dic.setEntryDistribution(w,equDistr[i]);
			 	break;
			 }
		   }
		}
	}

}
