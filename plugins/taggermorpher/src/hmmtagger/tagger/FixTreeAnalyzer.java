package hmmtagger.tagger;


import hmmtagger.fixtree.*;
import hmmtagger.tagger.TaggedWord;

import java.io.IOException;
import java.util.ArrayList;


/**
 * class for unknown word analysis using prefix/suffix trees
 */
public class FixTreeAnalyzer implements java.io.Serializable
{
	/**prefix tree*/
	protected FixNode prefix=null;
	/**suffix tree*/
	protected FixNode suffix=null;
	
	/**constructor
	 * 
	 * @param corpus trainig corpus
	 * @param nTags number of tags
	 * @param lPrefix prefix length
	 * @param lSuffix suffix length
	 * @param pThreashold information gain threasHold (prefix tree)
	 * @param sThreashold information gain threasHold (suffix tree)
	 * @param pGamma prob. estimation parameter (prefix tree)
	 * @param sGamma prob. estimation parameter (suffix tree)
	 * @throws IOException
	 * @throws Exception
	 */
	public FixTreeAnalyzer(TaggedCorpus corpus,int nTags,int lPrefix,int lSuffix,float pThreashold,float sThreashold,float pGamma,float sGamma) throws IOException, Exception
	{	
		FixNode.setNClasses(nTags);
		prefix=new FixNode();
		suffix=new FixNode();
		TaggedWord tw=null;
		int nWords=0;
		while ( (tw=corpus.nextToken()) != null )
		{
			String w=tw.getWord();
			if (w.length() >= lPrefix) prefix.addWord(new PrefixItem(tw),lPrefix-1);
			if (w.length() >= lSuffix) suffix.addWord(new SuffixItem(tw),lSuffix-1);
			nWords++;
		}
		
		FixNode.pruneTree(prefix,pThreashold,pGamma);
		FixNode.pruneTree(suffix,sThreashold,sGamma);
		
			
	}

	/**returns lex. prob estimate for a given word
	 * 
	 * @param w the word
	 * @return
	 */
	public LexProb[] getDistribution(String w)
	{   
		float[] d1=suffix.getProbabilityVector(new SuffixItem(w));
		float[] d2=prefix.getProbabilityVector(new PrefixItem(w));
		float[] res=null;
		if (d1 != null) 
		{
			res=new float[d1.length];
			for (int i=0; i<d1.length; i++)
			{
				res[i] = d1[i];
			}
            if (d2 != null)
            {
            	for (int i=0; i<d1.length; i++)
            	{
            		res[i] += d2[i];
            	}
            }
		}
		else
		{
			if (d2 != null)
			{   
				res=new float[d2.length];
				for (int i=0; i<d2.length; i++)
				{
					res[i] = d2[i];
				}
			}
		}
		if (res==null) return null;
		ArrayList lres=new ArrayList();
		for (int i=0; i<res.length; i++)
		{
            if (res[i] != Float.NEGATIVE_INFINITY)
            {
            	lres.add(new LexProb(i,res[i]));
            }
		}
		if (lres.size()==0) return null;
		LexProb[] resArray=new LexProb[lres.size()];
		lres.toArray(resArray);
		return resArray;
	}
}
