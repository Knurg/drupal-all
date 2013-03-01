package hmmtagger.tagger;



import hmmtagger.api.TagTable;

import java.io.Serializable;

/**HMM-Parameter class; composed of a Lexicon and a transition parameter object*/
public class HMMParams implements Serializable
{  
	/**the lexicon*/
	protected SimpleLexicon lexicon=null;
	/**transition parameter object*/
	protected SimpleTransParameters transParams=null;
	/**tag table*/
	protected TagTable table=null;
	
	/**constructor
	 * 
	 * @param l lexicon
	 * @param tp transition parameters
	 * @param tab tag table
	 * @throws Exception
	 */
	public HMMParams(SimpleLexicon l,SimpleTransParameters tp,TagTable tab) throws Exception
	{
		if (l.getNTags() != tp.getNumberOfStates()) 
			throw new Exception("HMMParams.constructor: arguments don't match each other");
		if (l.getNTags() != tab.getNTags()) 
			throw new Exception("HMMParams.constructor: arguments don't match each other");
		lexicon=l;
		transParams = tp;
		table=tab;
	}
	
	/**
	 * 
	 * @return table of transition probabilities
	 */
	public final float[][] getTransitionProbMatrix()
	{
		return transParams.getTransitionProbMatrix();
	}
	
	/**
	 * 
	 * @return table of starting probabilities
	 */
	public final float[][] getInitialProbMatrix()
	{
		return transParams.getInitialProbMatrix();
	}
	
	/** returns the prob. of a specified transition 
	 * @param state target state
	 * @param prevStates sequence of previous states (context)
	 * @return probability of this transition
	 */
	public final float  getTransitionProb(int state, int prevStates)
	{
		return transParams.getTransitionProb(state,prevStates);
	}
	/**
	 * returns the probability for a sequence of starting states
	 * @param states the sequence
	 * @param length number of relevant states (if number of input words is smaller than the HMM-order)
	 * @return probability of this sequence
	 */
    public final float  getInitialProb(int states,int length)
    {
    	return transParams.getInitialProb(states,length);
    }
    
    /**
     * return lex. entry for a given word
     * @param w the word
     * @return lex. emtry
     */
    public final LexicalEntry getLexicalEntry(String w,boolean bOS)
    {
    	return lexicon.getLexicalEntry(w,bOS);
    }
    
    
    /**
     * 
     * @return the order of the HMM
     */
    public final int getOrder()
    {
    	return transParams.getOrder();
    }
    
    /**
     * 
     * @return the number of states (tags)
     */
    public final int getNumberOfStates()
    {
    	return transParams.getNumberOfStates();
    }
    
    /**
     * 
     * @return parameter object
     */
    public final  SimpleTransParameters getTransitionParameters()
      {return transParams;}

    /**
     * 
     * @return lexicon object
     */
    public final SimpleLexicon getLexicon()
    {
    	return lexicon;
    }
    
    /**
     * 
     * @return tag table
     */
    public TagTable getTagTable()
    {
    	return table;
    }
    
}
