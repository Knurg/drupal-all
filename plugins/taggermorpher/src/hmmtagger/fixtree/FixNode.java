package hmmtagger.fixtree;

import java.util.ArrayList;



/**
Node of a prefix/suffix tree
 */
public class FixNode implements java.io.Serializable
{
	/**number of tags*/
	protected static int nClasses;
	/**entropy at this node*/
	protected float entropy;
	/**character of this node*/
	protected char character=(char) -1;
	/**list of successor nodes*/
	protected ArrayList successors=null;
	/**vector of counts/ estimates of lex. probabilities at this node*/
	protected float[] probabilityVector=null;
	
	/**constructor*/
	public FixNode()
	{}
	
	/**constructor
	 * 
	 * @param c the character
	 */
	protected FixNode(char c)
	{
		character=c;
	}
	
	
	/**
	 * add a new word to the tree
	 * @param it the word item
	 * @param maxIndex length of prefix/suffix
	 * @throws Exception
	 */
	public void addWord(WordItem it,int maxIndex) throws Exception
	{
		addWord(it,maxIndex,0);
	}
    
	/**
	 * returns lex. probabilities for a word
	 * @param it the word item
	 * @return
	 */
	public float[] getProbabilityVector(WordItem it) 
	{
		return getProbabilityVector(it,0);
	}
	
	/**
	 *  returns lex. probabilities for a word
	 * @param it word item
	 * @param index current recursion depth
	 * @return
	 */
	protected float[] getProbabilityVector(WordItem it,int index)
	{
		if (isLeaf()) return probabilityVector;
		char c;
		try
		{
		   c=it.getChar(index);
		}
		catch (Exception e) {return null;}
		FixNode succ=null;
		for (int i=0; i<successors.size(); i++)
		{
			succ = (FixNode) successors.get(i);
			if (succ.character == c)  return succ.getProbabilityVector(it,index+1);
		}
		if  (succ.character==(char) -2)  return succ.getProbabilityVector(it,index+1);
		return null;
	}
	
	/**
	 * prunes this tree
	 * @param threasHold information gain threashold
	 */
	protected void pruneTree(float threasHold)
	{   
		
		if (isLeaf()) return;
		FixNode defaultNode=null;
		for (int i=0; i<successors.size(); i++)
		{
			FixNode succ = (FixNode) successors.get(i);
		    if (!(succ.isLeaf()) )
		       {succ.pruneTree(threasHold);}
		    if (succ.isLeaf())
		    {
		    	float nItems=0;
		    	for (int j=0; j<nClasses; j++)
		    	{
		    		nItems += succ.probabilityVector[j];
		    	}
		    	float weightedGain =  nItems * (this.entropy - succ.entropy);
		    	if (weightedGain < threasHold)
		    	{
		    		successors.remove(i);
		    		i--;
		    		if (defaultNode==null) 
		    		{	
		    			defaultNode=new FixNode( (char) -2 );
		    			defaultNode.probabilityVector=new float[nClasses];
		    			for (int j=0; j<nClasses; j++)
		    			{
		    				defaultNode.probabilityVector[j] = 0;
		    			}
		    		}
		    		for (int j=0; j<nClasses; j++)
		    		{
		    			defaultNode.probabilityVector[j] += succ.probabilityVector[j];
		    		}
		    		
		    	}
		    }
		}
		if ( successors.size() == 0)
	    {
	    	this.probabilityVector=defaultNode.probabilityVector;
	    	successors=null;
	    }
	    else
	    {
	    	if (defaultNode != null) successors.add(defaultNode);
	    }
	}
	
	/**
	 * calculates prob. estimates for this tree
	 * @param gamma estimation parameter
	 */
	protected void calculateProbs(float gamma)
	{
		if (isLeaf()) 
		   {util.Utility.addOneEstimation(probabilityVector,gamma);}
		else
		{
			for (int i=0; i<successors.size(); i++)
			{
			  FixNode succ = (FixNode) successors.get(i);
			  succ.calculateProbs(gamma);
			}
		}	
	}
	
	/**
	 * calculates entropies for this tree
	 * @return
	 */
	protected float[] calculateEntropy()
	{
		if (isLeaf())
		{
			entropy=util.Utility.calcEntropy(probabilityVector);
			return probabilityVector;
		}
		else
		{
			float[] res=new float[nClasses];
			for (int i=0; i<successors.size(); i++)
			{
				float[] leafCount = ((FixNode) successors.get(i)).calculateEntropy();
				for (int j=0; j<nClasses; j++)
				{
					res[j] += leafCount[j];
				}
			}
			entropy=(-1) * util.Utility.calcEntropy(res);
			return res;
		}
	}
	/**
	 * adds a word to this tree
	 * @param it word item
	 * @param maxIndex prefix/suffix length
	 * @param currentIndex current recursion depth
	 * @throws Exception
	 */
	protected void addWord(WordItem it,int maxIndex,int currentIndex) throws Exception
	{
		if (currentIndex>maxIndex)
		{   
			if (probabilityVector==null)
			{
				probabilityVector = new float[nClasses];
				for (int i=0; i<nClasses; i++)
				   {probabilityVector[i] = 0;}
			}
			probabilityVector[it.getClassId()]++;
			return;
		}
		char c = it.getChar(currentIndex);
		if (successors == null) successors=new ArrayList();
		for (int i=0; i<successors.size(); i++)
		{
			FixNode succ = (FixNode) successors.get(i);
			if (succ.getCharacter() == c)
			{
				succ.addWord(it,maxIndex,currentIndex+1);
				return;
			}
		}
		FixNode newNode = new FixNode(c);
		successors.add(newNode);
		newNode.addWord(it,maxIndex,currentIndex+1);
		return;
	}
	
	protected char getCharacter()
	{
		return character;
	}
	protected boolean isRoot()
	{
		return (character == (char) -1);
	}
	
	protected boolean isLeaf()
	{
		return (successors==null);
	}
	
	/**
	 * sets the number of tags, must be called before construction of tree
	 * @param n
	 */
	public static void setNClasses(int n)
	{
		nClasses=n;
	}
	
	/**
	 * prunes a given tree
	 * @param tree the tree
	 * @param threasHold the information gain threashold
	 * @param gamma estimation parameter
	 */
	public static void pruneTree(FixNode tree,float threasHold,float gamma)
	{
		tree.calculateEntropy();
		tree.pruneTree(threasHold);
		tree.calculateProbs(gamma);
	}
}
