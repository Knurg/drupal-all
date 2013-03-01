package hmmtagger.tagger;



import hmmtagger.api.SpecificDictionaryManipulator;

import java.util.Iterator;




/**
 *  simple MLE-implementation of lexprob estimation 
 */
public class SimpleDictionary extends Dictionary 
{
    /**@see Dictionary*/
	public SimpleDictionary(TaggedCorpus corpus, int delimiter, int nTags, float[] tagP, SpecificDictionaryManipulator dm) throws Exception
	{
		super(corpus,delimiter,nTags,tagP,dm);
	}

	/**@see Dictionary*/
	public void calcLexicalProbs() 
	{
		Iterator it = lexicalProbs.values().iterator();
	    while (it.hasNext())
	    {   
	    	float total=(float)0.0;
	    	LexProb[] vr = (LexProb[]) it.next();
	    	for (int i=0; i<vr.length; i++)
	    	{
	    		total += vr[i].p;
	    	}
	    	for (int i=0; i<vr.length; i++)
	    	{
	    		vr[i].p = (float) Math.log((vr[i].p / total)) - tagProb[vr[i].s];          
	    	}
	    }
	}

	
}
