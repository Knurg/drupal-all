package hmmtagger.tagger;



import hmmtagger.api.TagSynonymSet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.ArrayList;

import morph.*;





import de.fau.cs.jill.feature.*;
/**
 * encapsulates the morph bib.
 */
public class Morphologizer implements java.io.Serializable
{
	/**morph. module*/
    protected transient MorphModule morphology=null;
    /**map between application tag set and morph. tag set*/
	protected TagSynonymSet tagSyn = null;
	/**tags for classes of capitalized words only*/
	protected int[] capitalizedTags=null;
	/**endings of german adjectives*/
    protected static final String[] adjEndings = {"e","en","er","es"};
    
	/**constructor
	 * 
	 * @param tss map between application tag set and morph. tag set
	 * @param capTags tags for classes of capitalized words only
	 * @param morphPath path to the data files of the morph. bib
	 * @throws Exception
	 */
    public Morphologizer(TagSynonymSet tss,int[] capTags,String morphPath) throws Exception
    {   
    	tagSyn=tss;
    	capitalizedTags=capTags;
    	morphology=new MorphModule(morphPath);
    	morphology.setFlag("*ALLE_WBSEG*");
		morphology.clearFlag("*TRACE*");
    }
    
    
    /**generates a new MorphModule instance, which will use the data files stored at the location
     * specified by the parameter
     * MUST BE CALLED AFTER DESERIALIZATION OF AN INSTANCE OF MORPHOLOGIZER
     * @param morphPath the location of the morphology data files
     */
    public void setMorphologyPath(String morphPath)
    {
    	morphology=new MorphModule(morphPath);
    	morphology.setFlag("*ALLE_WBSEG*");
		morphology.clearFlag("*TRACE*");
    }
    
    /**
     * passes a given word to the morph. bib. and returns an estimate of its lex prob. based on this analysis 
     * @param w the word
     * @return
     */
    public LexProb[] getDistribution(String w)
    {
    	try
		{
		    morphology.analyze(w);
		} catch (NullPointerException e) 
		{
			return null;
		}
		Hashtable cat=new Hashtable();
		while (morphology.moreAnalyses()) //see docu of morph-bib
		{   
			int [] current=null;
			String category=null;
			boolean subTypeInformation=false;
			try
			{
				MorphAnalysis analysis = morphology.nextAnalysis();
				category = analysis.getAnalysis().category();
			    if   (category.equals("V"))  
			    {
			    	Vector features=analysis.getAnalysis().getFlexInfo().getFeatures();
			    	for (int i=0; i<features.size(); i++)
			    	{
			    		FeatureStructure fs=(FeatureStructure) features.elementAt(i);
				  	 	FeatureValue value = fs.get(FeatureName.forName("typ"));
				  	 	if (value!=null) 
				  	 	{
				  	 		current=tagSyn.getIntForSynonym(category+value.toString());
				  	 		for (int j=0; j<current.length; j++)
					           {cat.put(new Integer(current[j]),"");}
				  	 		subTypeInformation=true;
				  	 	}
				  	 	else
				  	 	{
							   try
							   {
						         current=tagSyn.getIntForSynonym(category);
						         for (int j=0; j<current.length; j++)
						           {cat.put(new Integer(current[j]),"");}
							   }
							   catch (Exception e) {}
				  	 	}
			    	}
			    } 
			    if (category.equals("ADJ")) //additional analyse of endings of adjectives
			    {
			    	boolean attr=false;
			    	for (int i=0; i<adjEndings.length; i++)
			    	{  
			    		try
						{
			    		if ( w.substring( w.length()-adjEndings[i].length() , w.length() ).equals(adjEndings[i]) )
			    			{attr=true; break;}
						}
			    		catch (Exception e) {}	
			    	}
			    	if (attr) {category="ADJA";} else {category="ADJD";}
			    } 
			} catch (Exception e) {}   
			if (subTypeInformation==false)
			{  
			   try
			   {
		         current=tagSyn.getIntForSynonym(category);
		         for (int i=0; i<current.length; i++)
		           {cat.put(new Integer(current[i]),"");}
			   }
			   catch (Exception e) {}
			}      
		}
		
		if (cat.size() > 0)
		{	
		  //for uncapitalized words: only keep matching tags 
		   if (! (Character.isUpperCase(w.charAt(0))))
		   {
		   	  ArrayList toRemove=new ArrayList();
		   	  Enumeration en=cat.keys();
		   	  for (int i=0; i<cat.size(); i++)
			  {
				  int t=((Integer) en.nextElement()).intValue();
				  for (int j=0; j<capitalizedTags.length; j++)
				  {
				  	  if (t==capitalizedTags[j]) {toRemove.add(new Integer(t)); break;}
				  }
			  }
		   	  for (int i=0; i<toRemove.size(); i++)
		   	  {
		   	  	cat.remove(toRemove.get(i));
		   	  }
		   	  if (cat.size()==0) return null;
		   }
		   //calculate distribution
		   LexProb[] res=new LexProb[cat.size()];
		   float p= (float) Math.log(((float) 1) / ((float) cat.size()));
		   Enumeration en = cat.keys();
		   for (int i=0; i<cat.size(); i++)
		   {
			  int t=((Integer) en.nextElement()).intValue();
			  res[i] = new LexProb(t,p);
		   }
		   return res;
		}
	    return null;
		
    }
    
    /**uses the morph bib. to find out if a given tag subset for a given word should be extended to another
     * (including) subset
     * @param w a word
     * @param beamee the tagset to be extended
     * @param beamTarget the including tagset
     * @return
     */
    public boolean beamTagSet(String w,LexProb[] beamee,int[] beamTarget)
    {	 	
    	if (beamee.length >= beamTarget.length) return false;
    	//calculate set difference
    	ArrayList diff=new ArrayList();
    	int p=0;
    	for (int i=0; i<beamTarget.length; i++)
    	{
    		while (p<beamee.length)
    	    {
    			if (beamTarget[i]==beamee[p].s) break;
    			if (beamTarget[i] <beamee[p].s) 
    			{
    				diff.add(new Integer(beamTarget[i]));
    				break;
    			}
    			p++;
    		}
    		if (p==beamee.length)
    		{
    			for (int j=i; j<beamTarget.length; j++)
    			{
    				diff.add(new Integer(beamTarget[j]));
    			}
    		}
    	}
    	if (beamTarget.length!=diff.size()+beamee.length) return false;  //beamee is not a subset of beamtarget
        
    	//morpholgically analyse of w
    	LexProb[] morphSet=getDistribution(w);
    	if (morphSet==null) return false;
    	Arrays.sort(morphSet);
    	
    	//is diff a subset of morphSet?
    	int[] diffSet=new int[diff.size()];
    	for (int i=0; i<diffSet.length; i++)
    	  {diffSet[i]=((Integer) diff.get(i)).intValue();}
    	
    	boolean subset=true;
    	p=0;
    	for (int i=0; i<diffSet.length; i++)
    	{
    		
    		while (p<morphSet.length)
    		{
    			if (diffSet[i]==morphSet[p].s) 
    				{break;}
    			else
    		    {
    				if (diffSet[i]<morphSet[p].s) {p=morphSet.length; break;}
    		    }
    			p++;
    		}
    		if (p==morphSet.length) {subset=false; break;}
    	}
    	return subset;
    }
    
}
