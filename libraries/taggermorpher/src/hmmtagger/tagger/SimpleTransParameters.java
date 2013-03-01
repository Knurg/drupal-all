package hmmtagger.tagger;

import java.io.Serializable;

import util.ArrayDistribution;
import util.CompressedDistribution;
import util.Distribution;
import util.SentenceSplitter;

/**
 * stores transition/starting probabilities of an HMM
 */
public class SimpleTransParameters implements Serializable 
{
	/**starting probabilities for the HMM*/
	protected Distribution[] initialProbs=null;
	/**transition probabilities*/
	protected  Distribution[] transProbs=null; 
	/**explicit matrix of transition prob.*/
	protected float[][] transMatrix = null;
	/**explicit matrix of starting prob.*/
	protected float[][] initialMatrix=null;
	/**order of the HMM*/
	protected int order;                    
	/**number of states (tags) of the HMM*/
	protected int nStates;	
	/**number of state sequences of length "order"*/
    protected int nSequ; 
    /**number of state sequences of length 1 to "order"*/
	protected int[] nSequAr;
	/**explicit representations of cross-product states*/
	protected int[][] sequRepr=null;
	/**integer codes of the last <pre>order</pre>-1 components of cross-product states*/
	protected int[] suffix=null;
	
	/**constructor
	 * 
	 * @param corpus training corpus
	 * @param nStates number of states
	 * @param order HMM-order
	 * @param delimiter sentence delimiter tag
	 * @param iniGamma estimation parameter (starting prob.)
	 * @param transGamma estimation parameter (transition prob.)
	 * @param quantity of memory to be used (0,1,2), for higher execution speed  
	 * @throws Exception
	 */
	public SimpleTransParameters(TaggedCorpus corpus,int nStates,int order,int delimiter,float iniGamma,float transGamma,int memory) throws Exception
	{
		
		this(corpus,nStates,order,delimiter,iniGamma,(memory==0));
		
		transProbs=new Distribution[nSequ];
		for (int i=0; i<nSequ; i++)
		{
			if (memory==0)
			  {transProbs[i]=new CompressedDistribution();}
			else
			  {transProbs[i]=new ArrayDistribution(nStates);}	
		}
		
		corpus.initialize();
		SentenceSplitter sp = new SentenceSplitter(corpus,delimiter);
		TaggedWord[] tw=null;
		//counting transitions
		while ( (tw=sp.nextSentence()) != null)
	    {      
			calcTransitionCounts(tw,order,nStates,transProbs);
	    }
        //estimation of transition prob.
   	    for (int i=0; i<nSequ; i++)
   	    {
   	    	transProbs[i].addOneEstimation(transGamma,nStates);
   	    }	
		//construct matrix for faster access
   	    if (memory==2)
		   {initializeTransProb(); this.initializeInitialProb();}   
	}
	
	
	/**constructor*/
	protected SimpleTransParameters(TaggedCorpus corpus,int nStates,int order,int delimiter,float iniGamma,boolean memSave) throws Exception
	{
		this.order = order;
		this.nStates = nStates;
		nSequ=(int)Math.pow(nStates,order);
		nSequAr=new int[order];
		for (int i=1; i<=order; i++)
		  {nSequAr[i-1]= (int) Math.pow(nStates,i);}
		
		initialProbs = new Distribution[order];
		for (int i=0; i<order; i++)
		{
		   if (memSave) 	
		      {initialProbs[i] = new CompressedDistribution();}
		   else
		      {initialProbs[i] = new ArrayDistribution(nSequAr[i]);}
		}
		
		SentenceSplitter sp = new SentenceSplitter(corpus,delimiter);
		TaggedWord[] tw=null;
		//counting starting states
		while ( (tw=sp.nextSentence()) != null)
	    {   
			for (int i=1; i<=order; i++)
			{	
			   calcInitialProbCounts(tw,i,nStates,initialProbs[i-1]);
			}   
	    }
		//estimation of starting prob.
		for (int i=0; i<order; i++)
 	      {initialProbs[i].addOneEstimation(iniGamma,nSequAr[i]);}	
		
	    //initialize "helpers"
	    suffix=new int[nSequ];
	    sequRepr=new int[nSequ][order];
	    int x=(int) Math.pow(nStates,order-1);
	    for (int s=0; s<nSequ; s++)
	    {
	       	  sequRepr[s]=BeamViterbiAlgorithm.indexToSequence(s,order,nStates);
	       	  if (order>1)
	       	  {	
	       	    suffix[s]=s % x;
	       	  }
	       	  else
	       	  {
	       	    suffix[s]=s;
	       	  }
	     }
         	    
	}
	
	/**
	 * 
	 * @return transiton prob matrix (if "memory"==2, null otherwise)
	 */
	public final float[][] getTransitionProbMatrix() 
	{
       return transMatrix;
	}
	
	/**
	 * 
	 * @return starting prob matrix (if "memory"==2, null otherwise)
	 */
	public final float[][] getInitialProbMatrix() 
	{
       return initialMatrix;
	}

	/**
	 * returns probability of a given transition
	 * @param state target state
	 * @param prevStates sequence of previous states (context)
	 * @return the prob of the transition
	 */
	public final float getTransitionProb(int state, int prevStates) 
	{
		try
		{
		return transProbs[prevStates].getProb(state);
		} catch (Exception e) {return -1;}
	}

	/**
	 * returns probability of a given sequence of starting states
	 * @param states the state sequence
	 * @param length number of relevant single states (in cases where the number of input words is smaller than the HMM-order)
	 * @return the probability of the starting sequence
	 */
	public final float getInitialProb(int states,int length) 
	{
		try
		{
		return initialProbs[length-1].getProb(states);
		} catch (Exception e) {return -1;}
	}

	/**
	 * 
	 * @return HMM-order
	 */
	public final int getOrder() 
	{
		return order;
	}

	/**
	 * 
	 * @return number of states
	 */
	public final int getNumberOfStates() 
	{
	   return nStates;	
	}

	/**
	* populates array <pre>transMatrix</pre>
	*/
	protected final void initializeTransProb()
	{
		
		transMatrix = new float[nSequ][];
		for (int s=0; s<nSequ; s++)
		{
			ArrayDistribution d=(ArrayDistribution) transProbs[s];
			transMatrix[s]=d.getArray();
		}
		
	}
	/**
	* populates array <pre>initialMatrix</pre>
	*/
	protected final void initializeInitialProb()
	{
		initialMatrix = new float[order][];
		for (int s=0; s<order; s++)
		{
			ArrayDistribution d=(ArrayDistribution) initialProbs[s];
			initialMatrix[s]=d.getArray();
		}
	}
	

	/**
	 * 
	 * @return integer codes of the last <pre>order</pre>-1 components of cross-product states*/
	public final int[] getSuffix()
	 {return suffix;}
	
	/**
	 * 
	 * @return explicit representations of cross-product states*/
	public final int[][] getSequRepr()
	  {return sequRepr;}
	
	   /**
	    * static function for reuse by other estimation methods; counts transitions of a given sentence
	    * @param w the sentence
	    * @param order the order of the HMM
	    * @param nStat number of states
	    * @param res the array in which the counts are stored
	    * @throws Exception
	    */
	   public final static void calcTransitionCounts(TaggedWord[] w,int order,int nStat,Distribution[] res) throws Exception
	   {
	   	  int nWords=w.length;
	   	  int nSequ = (int)Math.pow(nStat,order);
	      if (res.length != nSequ) throw new Exception("SimpleTransitionParameters.calcTransitionCounts : invalid parameters");
	     
	      for (int i=order; i<nWords; i++)
	   	  {
	   	  	 int context[]=new int[order];
	   	  	 for (int j=0; j<order; j++)
	   	     {
	   	  		context[j] = w[i-order+j].getIntTag();
	   	     }	
	   	  	 int c=BeamViterbiAlgorithm.sequenceToIndex(context,order,nStat);
	   	  	 res[c].incr(w[i].getIntTag()); 	
	   	  }
	   }
	   
	   /**
	    * static function for reuse by other estimation methods; counts starting sequences
	    * @param training corpus
	    * @param order the order of the HMM
	    * @param nStat number of states
	    * @param res the array in which the counts are stored
	    * @param sentenceDelimiter the sentence delimiter tag
	    * @throws Exception if the array length doesn't fit
	    */
	   public final static void calcInitialProbCounts(TaggedWord[] w, int order, int nStat, Distribution res) throws Exception
	   {   
	   	   if (w.length>=order)
	   	   {
	   	   	  int[] sequ=new int[order];
	   	   	  for (int i=0; i<order; i++)
	   	   	    {sequ[i] = w[i].getIntTag();}	
	   	   	  res.incr(BeamViterbiAlgorithm.sequenceToIndex(sequ,order,nStat));
	   	   }
	   }
}
