package hmmtagger.tagger;


import hmmtagger.api.SpecificDictionaryManipulator;
import hmmtagger.api.TagSynonymSet;

import java.io.Serializable;

import util.SentenceSplitter;
import util.Utility;


/**
 *  Lexicon class, integrating several sources of lex. information
 */
public class SimpleLexicon implements  Serializable 
{
	/**dictionary*/
	protected Dictionary dic=null;
	/**morph. bib. wrapper*/
	protected Morphologizer morph=null;
	/**prefix/suffix tree wrapper*/
	protected FixTreeAnalyzer fix=null;
	/**default entry for unknwon words*/
	protected LexProb[] unknownDistribution=null;
	/**default entry for capitalized unknown words*/
	protected LexProb[] unknownCapitalizedDistribution=null;
	/**default entry for card. numbers*/
	protected LexProb[] numberDistribution;
	/**default entry for ordinal numbers*/
	protected LexProb[] ordNumberDistribution;
	/**tag prob. estimates*/
	protected float tagProb[]=null;
	/**number of tags*/
	protected int nTags;
	/**a flag, indicating, if the morph. bib. is to be used first for unknown word analysis*/
	protected boolean morph1st;
	
	/**constructor
	 * 
	 * @param corpus training corpus
	 * @param delimiter sentence delimiter tag
	 * @param numberTag tag for cardinal numbers
	 * @param ordNumberTag tag for ordinal numbers
	 * @param openTags tags for open word classes
	 * @param capitalizedTags tags for classes of captialized words only
	 * @param nStates number of states
	 * @param ts map from morph. tagset to application tagset
	 * @param dm dictionary manipulator object
	 * @param morphPath path to the data files of the morph. bib.
	 * @param lPrefix prefix length (tree)
	 * @param lSuffix suffix length (tree)
	 * @param pThreashold information gain threashold for the prefix tree 
	 * @param sThreashold information gain threashold for the suffix tree
	 * @param pGamma estimation parameter for the prefix tree
	 * @param sGamma estimation parameter for the suffix tree
	 * @param morphFirst flag, indicating if morph. bib. is to be used first for unknown word analysis
	 * @throws Exception
	 */
	public SimpleLexicon(TaggedCorpus corpus, int delimiter, int numberTag, int ordNumberTag, int[] openTags, int[] capitalizedTags, int nStates,TagSynonymSet ts,SpecificDictionaryManipulator dm,String morphPath,int lPrefix,int lSuffix,float pThreashold,float sThreashold,float pGamma,float sGamma,boolean morphFirst) throws Exception
	{   
		corpus.initialize();
		nTags = nStates;
		morph1st=morphFirst;
		int nWords=0;
		SentenceSplitter sp = new SentenceSplitter(corpus,delimiter);
		TaggedWord[] tw=null;
		float[] capCounts = new float[nStates];
		tagProb = new float[nStates];
		float totalCap=0;
		for (int i=0; i<nStates; i++)
		  {tagProb[i]=0; capCounts[i]=0;}
		//counting tags
		while ( (tw=sp.nextSentence()) != null)
	    {
			for (int i=1; i<tw.length; i++)
			{
				if (Character.isUpperCase(tw[i].getWord().charAt(0)))
				   {capCounts[tw[i].getIntTag()]++; totalCap++;}
			}			
		    calcTagCounts(tw,nStates,tagProb);
		    nWords += tw.length;
	    }
	    //estimation of tag probabilities
		Utility.addOneEstimation(tagProb,(float)0.00000001);
		//construct default entries
   	    float p= (float)Math.log(((float) 1) / ((float) openTags.length));
   	    unknownDistribution = new LexProb[openTags.length];
   	    for (int i=0; i<openTags.length; i++)
   	    {
   	    	unknownDistribution[i] = new LexProb(openTags[i],p-tagProb[openTags[i]]);
   	    }    
	    unknownCapitalizedDistribution = new LexProb[capitalizedTags.length];
	    for (int i=0; i<capitalizedTags.length; i++)
	    {
	    	unknownCapitalizedDistribution[i] = new LexProb(
	    			                            capitalizedTags[i],
												(float) Math.log(capCounts[capitalizedTags[i]]/totalCap) - tagProb[capitalizedTags[i]]);
	    }
	    numberDistribution = new LexProb[1];
	    numberDistribution[0]=new LexProb(numberTag,1-tagProb[numberTag]);
	    ordNumberDistribution = new LexProb[1];
	    ordNumberDistribution[0]=new LexProb(ordNumberTag,1-tagProb[ordNumberTag]);
	    corpus.initialize();
	    dic=new SimpleDictionary(corpus,delimiter,nStates,tagProb,dm);
		morph=new Morphologizer(ts,capitalizedTags,morphPath);
		corpus.initialize();
		fix=new FixTreeAnalyzer(corpus,nTags,lPrefix,lSuffix,pThreashold,sThreashold,pGamma,sGamma);
	}
	
	/**constructor*/
	protected SimpleLexicon()
	 {}
	
	/**returns a lexical entry for a given word
	 * 
	 * @param w  the word
	 * @param bOS flag indicating if the word is at the beginning of a sentence
	 * @return
	 */
	public LexicalEntry getLexicalEntry(String w,boolean bOS) 
	{	
		Object o=null;
		boolean unknown=false;
		boolean cap=false;
		if (w==null) {return new LexicalEntry(w,unknownDistribution,'u');}	 
		if (isNumber(w))
		{	
			return new LexicalEntry(w,numberDistribution,'s');
		}
		if (isOrdNumber(w))
		{
			return new LexicalEntry(w,ordNumberDistribution,'s');
		}
	    LexProb[] resArray = dic.getDistribution(w);
	    
	    if (resArray != null) return new LexicalEntry(w,resArray,'f');
	    if (bOS) 
	    {
	    	resArray=dic.getDistribution(w.toLowerCase());
	    	if (resArray != null) return new LexicalEntry(w,resArray,'c');
	    }

	    if (!(morph1st))
	    {	
	      resArray=fix.getDistribution(w);
	      if (resArray != null)
	         { return new LexicalEntry(w,resArray,'t');}
	    } 
	    
	    resArray=morph.getDistribution(w);
	    if (resArray != null)
	    {   
	    	boolean ok=true;
	    	if ( (Character.isUpperCase(w.charAt(0))) && (bOS==false) )
	    	{
	    		if (resArray.length <= unknownCapitalizedDistribution.length)
	    		{
	    			for (int i=0; i<resArray.length; i++)
	    			{
	    				boolean contained=false;
	    				for (int j=0; j<unknownCapitalizedDistribution.length; j++)
	    				{
	    					if (resArray[i].s == this.unknownCapitalizedDistribution[j].s)
	    					  {contained = true; break;}
	    				}
	    				if (contained==false) {ok=false; break;}
	    			}
	    		}
	    		else
	    		{
	    			ok=false;
	    		}
	    	}
	    	if (ok) return new LexicalEntry(w,resArray,'m');
	    }
	    
	    if (morph1st)
	    {	
	      resArray=fix.getDistribution(w);
	      if (resArray != null)
	         { return new LexicalEntry(w,resArray,'t');}
	    } 
	    
	    if (Character.isUpperCase(w.charAt(0)))
	       {return new LexicalEntry(w,unknownCapitalizedDistribution,'u');}
	    return new LexicalEntry(w,unknownDistribution,'u'); 	    
	}

	/**
	 * 
	 * @return number of tags
	 */
	public int getNTags()
	{
		return nTags;
	}
	
	/**
	 * 
	 * @return the dictionary object
	 */
	public Dictionary getDic()
	{
		return dic;	
	}
	
	public Morphologizer getMorphologizer()
	{
        return morph;		
	}
	
	
	/**
	 * number predicate (german number format)
	 * @param w the word to be analysed
	 * @return true if the word is a number
	 */
	protected boolean isNumber(String w)
	{
		if (w.equals("")) return false;
		boolean number=false;
		if ( ( Character.isDigit(w.charAt(0)) ) && ( Character.isDigit(w.charAt(w.length()-1))  ) ) 
		{
		  number=true; 	
	      for (int i=0; i<w.length()-1; i++)
	      {   
	    	char c=w.charAt(i);
			if  (!( (Character.isDigit(c)) || (c == ',') ||  (c=='.') ))
			  {number=false; break;}	
	      }
		}  
		return number;
	}
	
	/**
	 * ordinal number predicate
	 * @param w the word to be analysed
	 * @return true if the word is an ordinal number
	 */
	protected boolean isOrdNumber(String w)
	{
		if (w.equals("")) return false;
		if (w.charAt(w.length()-1) == '.')
		{
			return isNumber(w.substring(0,w.length()-1));
		}
		return false;
	}
	
	/**
	    * static function for reuse by other estimation methods; counts tag frequencies
	    * @param w training corpus
	    * @param nStat number of states
	    * @param res the array in which the counts are stored
	    * @throws Exception
	    */
	   public static void calcTagCounts(TaggedWord[] w,int nStat,float[] res) throws Exception
	   {
	   	  if (res.length != nStat) throw new Exception("SimpleLexicon.calcTagCounts : invalid parameters");
	   	  for (int i=0; i<w.length; i++)
	   	  {
	   	  	res[w[i].getIntTag()]++;
	   	  }
	   }
	
}
