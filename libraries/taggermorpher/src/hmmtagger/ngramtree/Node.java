package hmmtagger.ngramtree;


import hmmtagger.api.TagTable;

import java.util.*;
import java.io.*;

import util.Utility;

/**Node of an ngram decision tree (transition probabilities*/
public class Node 
{
	/**yes-successor*/
    protected Node yesLink=null;
    /**no-successor*/
    protected Node noLink=null;
    /**entropy at this node*/
    protected float entropy;
    /**counts/probability estimates*/
    protected float[] probabilityVector=null;
    /**decision criterium of this node*/
    protected SplitCriterium split=null;
    
    /**
     * constructor
     * @param items current trainings set of ngram instances
     * @param criteria current list of decision criteria
     * @param nClasses number of tags
     * @param threasHold minimal number of elements at a node
     * @throws Exception
     */
    public Node(NgramItem[] items,ArrayList criteria,int nClasses,int threasHold) throws Exception
    {
    	entropy = ((float) -1) * Utility.calcEntropy(items,nClasses);
    	int nItems=items.length;
    	int nCriteria = criteria.size();
    	NgramItem[] minYes=null;
    	NgramItem[] minNo=null;
    	SplitCriterium minSplit=null;
    	float minEntropy = Float.POSITIVE_INFINITY;
    	for (int i=0; i<nCriteria; i++) //find optimal decision criterium by looping over all of them
    	{
    		//splitting
    		ArrayList yesPartition=new ArrayList();
        	ArrayList noPartition=new ArrayList();
    		SplitCriterium sc = (SplitCriterium) criteria.get(i);
    		for (int j=0; j<nItems; j++)
    		{
    			if (items[j].evalCriterium(sc))
    			   {yesPartition.add(items[j]);}
    			else
    			   {noPartition.add(items[j]);}	
    		}
    		NgramItem[] yes = new NgramItem[yesPartition.size()];
    		NgramItem[] no = new NgramItem[noPartition.size()];
    		yesPartition.toArray(yes);
    		noPartition.toArray(no);
    		float newEntropy = ((float) -1) *  ( (float) yes.length) / ((float) nItems) * Utility.calcEntropy(yes,nClasses) 
			                 + ((float) -1) *  ( (float) no.length) / ((float) nItems) * Utility.calcEntropy(no,nClasses) ;  
    		if (newEntropy < minEntropy)
    		{
    			minYes = yes;
    			minNo = no;
    			minEntropy=newEntropy;
    			minSplit = sc;
    		}
    	}
    	if ( (minYes != null) && (minEntropy < entropy) && (minYes.length >= threasHold) && (minNo.length >= threasHold) )
    	{
    		//recursive splitting, if there are enough elements at both successor nodes
    		split = minSplit;
    		ArrayList yes = (ArrayList) criteria.clone();
    		ArrayList no = (ArrayList) criteria.clone();
    		for (int i=0; i<nClasses; i++)
    		{
    			yes.remove( new SplitCriterium(split.pos,i) );
    		}
    		no.remove(split);
    		yesLink = new Node(minYes,yes,nClasses,threasHold);
    		noLink = new Node(minNo,no,nClasses,threasHold);
    	}
    	else
    	{
    		//otherwise, make current node a leaf
    	    probabilityVector = new float[nClasses];
    	    for (int i=0; i<nItems; i++)
    	      {probabilityVector[items[i].getClassId()]++;}
    	}	
    }
    
    /**
     * prune this tree
     * @param threasHold information gain threashold
     */
    public final void prune(float threasHold)
    {
    	if (isLeaf()) return;
    	if ( (yesLink.isLeaf()) && (noLink.isLeaf()) )
    	{
    		float nElementsTotal = 0;
    		float nElementsYes = 0;
    		float nElementsNo = 0;
    		for (int i=0; i<yesLink.probabilityVector.length; i++)
    		{
    			nElementsYes += yesLink.probabilityVector[i];
    			nElementsNo  += noLink.probabilityVector[i];
    		}
    		nElementsTotal = nElementsYes + nElementsNo;
    		float leafEntropy = (nElementsYes/nElementsTotal) * yesLink.entropy 
			                  +  (nElementsNo/nElementsTotal) * noLink.entropy; 
    		float weightedGain = (entropy-leafEntropy) * nElementsTotal;
    		if (weightedGain < threasHold)
    		{
    			float[] sum = new float[yesLink.probabilityVector.length];
    			for (int i=0; i<sum.length; i++)
        		{
        			sum[i] = yesLink.probabilityVector[i] + noLink.probabilityVector[i];
        		}
    			yesLink = null;
    			noLink = null;
    			probabilityVector=sum;
    			split = null;
    		}
    		
    	}
    	else
    	{
    		yesLink.prune(threasHold);
    		noLink.prune(threasHold);
    		if (yesLink.isLeaf() && noLink.isLeaf()) {prune(threasHold);}
    	}
    }
    
    /**calculate probability estimates at the leaf nodes of this tree
     * 
     * @param gamma estimation parameter
     */
    public final void calculateLeafProbabilityDistributions(float gamma)
    {
    	if (isLeaf())
    	{
           Utility.addOneEstimation(probabilityVector,gamma);
    	}
    	else
    	{
    	   yesLink.calculateLeafProbabilityDistributions(gamma);
    	   noLink.calculateLeafProbabilityDistributions(gamma);
    	}
    }
    
    /**get transition prob. for a given ngram
     * 
     * @param ngr the ngram
     * @return vector of transition prob.
     * @throws Exception
     */
    public final float[] classify(NgramItem ngr) throws Exception
    {
    	if (isLeaf())
    	{
    		return probabilityVector;
    	}
    	else
    	{
    		if (ngr.evalCriterium(split))
    		  {return yesLink.classify(ngr);}
    		else
    		  {return noLink.classify(ngr);}	
    	}
    }
    
    
    protected final boolean isLeaf()
      {return (probabilityVector != null);}
    
    /**dump this tree to a file
     * 
     * @param pw target stream
     * @param t tag table
     * @param recursion depth (should be called with 0) 
     * @throws Exception
     */
    public void dump(PrintWriter pw,TagTable t,int depth) throws Exception
    {
    	String s=new String();
    	for (int i=0; i<depth; i++)
	      {s = s + new Character((char) 9).toString();}
    	if (isLeaf())
    	{
    	    s=s+"< ";
    		for (int i=0; i<probabilityVector.length; i++)
    		{
    			if (probabilityVector[i] != 0) s=s + "(" + i + ","  + probabilityVector[i] + ")" + ";";
    		}
    		s=s.substring(0,s.length()-1);
    		s=s+" >";
    		pw.println(s);
    	}
    	else
    	{   	
    		s= s + "< " + split.pos + "==" + split.val + " >";
    		pw.println(s);
    		yesLink.dump(pw,t,depth+1);
    		noLink.dump(pw,t,depth+1);
    	}
    	
    	
    	
    }
    
    
    
}
