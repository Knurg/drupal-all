package hmmtagger.tagger;


import hmmtagger.ngramtree.*;

import java.util.ArrayList;

import util.ArrayDistribution;
import util.CompressedDistribution;
import util.Distribution;
import util.SentenceSplitter;

/**
 *  transition parameters...estimation by means of a decision tree
 */
public class TreeTransParameters extends SimpleTransParameters 
{
	
     /**constructor
     * 
     * @param corpus training corpus
     * @param nStates number of states
     * @param order HMM-order
     * @param delimiter sentence delimiter tag
     * @param binSize minimal number of instances at a node (recursion stop criterium)
     * @param threasHold information gain threashold
     * @param iniGamma estimation parameter (starting prob.)
     * @param transGamma estimation parameter (transition prob.)
     * @param memory quantity of memory to be used (0,1,2)...the more the quicker
     * @throws Exception
     */
	public TreeTransParameters(TaggedCorpus corpus,int nStates,int order,int delimiter,int binSize,float threasHold,float iniGamma,float transGamma,int memory) throws Exception
	{
		super(corpus,nStates,order,delimiter,iniGamma,(memory==0));
		
		//build decision tree
		NgramItem.setOrder(order+1);
		corpus.initialize();
		SentenceSplitter spl = new SentenceSplitter(corpus,53);
		TaggedWord[] tw = null;
		ArrayList items = new ArrayList();	
		//build set of items
		while ( (tw=spl.nextSentence()) != null)
		{  
			for (int i=order; i<tw.length; i++)
			{
				int c[] = new int[order+1];
				for (int j=0; j<=order; j++)
				{
					c[j] = tw[i-order+j].tag; 
				}
				items.add(new NgramItem(c));
			}
		}
		//build set of decision criteria
		ArrayList expr=new ArrayList();
		for (int j=0; j<order; j++)
	    {	
		     for (int i=0; i<nStates; i++)
		     {
		   	    expr.add(new SplitCriterium(j,i));
		     }
		}
		NgramItem[] itemarray = new NgramItem[items.size()];
		items.toArray(itemarray);
		
		Node tree = new Node(itemarray,expr,nStates,binSize);
		tree.prune(threasHold);
		//determin all transition probabilities using the decision tree
		transProbs=new Distribution[nSequ];
		NgramItem.setOrder(order);
		for (int s=0; s<nSequ; s++)
		{
			if (memory==0)	
			  {transProbs[s] =  new CompressedDistribution(tree.classify(new NgramItem(BeamViterbiAlgorithm.indexToSequence(s,order,nStates))));}
			else
			  {transProbs[s] =  new ArrayDistribution(tree.classify(new NgramItem(BeamViterbiAlgorithm.indexToSequence(s,order,nStates))));}
			transProbs[s].addOneEstimation(transGamma,nStates);
		}
		
		//sequence transition probabilities
   	    if (memory==2)
		   {initializeTransProb(); this.initializeInitialProb();}   
	}
}
