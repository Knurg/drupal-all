package hmmtagger.tagger;


/**
 *argmax array 
 */
public class ArgmaxArrayImpl
{
	/**initial number of elements per line*/
	private static final int lineSize=50;

	/**number of lines which are added when resizing the array*/
	private static final int bufferPlus=10;
	
	/**HMM order*/
    private int order;
    /**number of states*/
    private int nStates;
    /**number of tag sequences to be calculated*/  
	private int nRanks;
	/**current size of the array*/
	private int bufferSize;
	/**pointer to current buffer line*/
	private int bufferPointer=-1;
	/**the array*/
	private ArgmaxItem[][] matrix=null;
	/**width of the current line*/
	private int currentLineWidth=0;
    /**explicit representations of the cross-product states of the HMM*/
	private int[][] sequRepr=null;

	
	
	/**
	 * constructor
	 * @param bs initial buffer size
	 * @throws IOException
	 */
	ArgmaxArrayImpl(int bs) 
	{
		bufferSize=bs;
	}
	
	/**gets data from associated <pre>BeamViterbiAlgorithm</pre>-Instance
	 * 
	 * @param bva the associated instance
	 */
	void setAlgInstance(BeamViterbiAlgorithm bva)
	{
		order=bva.order;
		nStates=bva.nStates;
		nRanks=bva.nRanks;
		matrix=new ArgmaxItem[bufferSize][lineSize];
		for (int i=0; i<bufferSize; i++)
		{
			for (int j=0; j<lineSize; j++)
			{
				matrix[i][j]=new ArgmaxItem(nRanks);
			}
		}
		sequRepr=bva.sequRepr;
	}
	
	/**constructs the current argmax line, only keeping the pointers for states contained in the beam set
	 * 
	 * @param ai array of argmax pointers
	 * @param bi current beam set
	 * @param bLength length of beam set
	 */
	void constructLine(ArgmaxItem[] ai, BeamViterbiAlgorithm.BeamSetElement[] bi,int bLength)
	{
		bufferPointer++;
		currentLineWidth=bLength;
		if (bufferPointer==bufferSize) //resize array?
		{	
			
			ArgmaxItem[][] oldMatrix=matrix;
			int newBufferSize=bufferSize+bufferPlus; 
			matrix=new ArgmaxItem[newBufferSize][];
			for (int i=0; i<bufferSize; i++)
			{
				matrix[i]=new ArgmaxItem[oldMatrix[i].length];
				for (int j=0; j<oldMatrix[i].length; j++)
				{
					matrix[i][j]=oldMatrix[i][j];
				}
			}
			for (int i=bufferSize; i<newBufferSize; i++)
			{
				matrix[i]=new ArgmaxItem[lineSize];
				for (int j=0; j<lineSize; j++)
				{
					matrix[i][j]=new ArgmaxItem(nRanks);
				}
			}
			bufferSize=newBufferSize;
		}
		if (matrix[bufferPointer].length < bLength) //resize current line?
		{
			ArgmaxItem[] oldLine=matrix[bufferPointer];
			int oldLength=oldLine.length;
			int newLength=bLength;
			ArgmaxItem[] newLine=new ArgmaxItem[newLength]; 
			matrix[bufferPointer]=newLine;
			for (int i=0; i<oldLength; i++)
			  {newLine[i]=oldLine[i];}
			for (int i=oldLength; i<newLength; i++)
			  {newLine[i]=new ArgmaxItem(nRanks);}										 
		}
		//construct current line
		ArgmaxItem[] currentLine=matrix[bufferPointer];
		int s;
		int r;
		for (int i=0; i<bLength; i++)
		{
			s=bi[i].s;
			r=bi[i].currentNRanks;
			currentLine[i].state=s;
			for (int j=0; j<r; j++)
			{
				currentLine[i].pointer[j].item=ai[s].pointer[j].item;
				currentLine[i].pointer[j].rank=ai[s].pointer[j].rank;
			}
		}
		
	}
	
	/** performs readout of an Viterbi-Path
	 * 
	 * @param startState starting state
	 * @param startRank starting rank
	 * @return the tag sequence
	 */
	private int[] readOut(int startState, int startRank) 
	{
        int resLength=bufferPointer+order;
		int[] res=new int[resLength];
        int pos=resLength-1;
       
        int currentItem=startState;
        int currentRank=startRank;
        
        int oldCurrentRank;
        //following the back pointers
        for (int currentLine=bufferPointer; currentLine>0; currentLine--)
        {
        	ArgmaxItem i=matrix[currentLine][currentItem];
        	
        	res[pos]=sequRepr[i.state][order-1];
        	pos--;
        	
        	currentItem=i.pointer[currentRank].item;
        	currentRank=i.pointer[currentRank].rank;
        }
		//last state encodes a tuple of singular states
		int[] sequ=sequRepr[matrix[0][currentItem].state];
		for (int i=0; i<order; i++)
			 {res[i]=sequ[i];}
			
		return res;
	}
	
   /**readout of the n best tag sequences
    * 
    * @param beamSet current beam set
    * @return the n best tag sequences
    */	
   ReadOutRes readOut(BeamViterbiAlgorithm.BeamSet beamSet) 
   {
   	  ReadOutRes res= new ReadOutRes();
   	  res.scores=new float[nRanks];
   	  int[][] argmaxi=new int[nRanks][2];
   	  float max[]=new float[nRanks];
   	  
   
   	  
   	  for (int i=0; i<nRanks; i++)
   	      {max[i] = Float.NEGATIVE_INFINITY;}
   	  
   	  //determin n best scores
   	  for (int i=0; i<beamSet.currentSize; i++)
   	  {
   	  	  BeamViterbiAlgorithm.BeamSetElement el=beamSet.elements[i];
   	      for (int j=0; j<el.currentNRanks; j++)
   	      {
   	      	 float p=el.p[j];
   	  	     if ( p > max[nRanks-1] )
   	  	     {
   	  	        for (int y=nRanks-1; y>=0; y--)
  	       	    {	
  	       	        if (max[y] >= p)
  	       	        {
  	       	        	max[y+1] = p;
  	       	        	argmaxi[y+1][0] = i;
  	       	        	argmaxi[y+1][1] = j;
  	       	        	break;
  	       	        }
  	       	        else
  	       	        {
  	       	        	if (y!=nRanks-1)
  	       	        	{	
  	       	        	   max[y+1] = max[y];
  	       	        	   argmaxi[y+1][0] = argmaxi[y][0];
  	       	               argmaxi[y+1][1] = argmaxi[y][1];
  	       	        	}
  	       	        	if (y==0)
  	       	        	{	
  	       	        		max[0] = p;
  	       	                argmaxi[0][0] = i;
	       	        	    argmaxi[0][1] = j;
  	       	        	}	
  	       	        }
  	       	      }
   	  	      }
   	  	      else
   	  	      {
   	  	         break;
   	  	      }   
   	  	  }
   	  }
   	  //read out n best pathes
      int[][] resarray=null;
   	  for (int i=0; i<nRanks; i++)
   	  {
   	  	  res.scores[i] = max[i];
   	  	  int[] tags=readOut(argmaxi[i][0],argmaxi[i][1]);
   	  	  if (resarray==null) resarray=new int[tags.length][nRanks];
   	  	  for (int j=0; j<tags.length; j++)
   	  	    {resarray[j][i]=tags[j];}	
   	  }	
   	  res.tags=resarray;
	
	  bufferPointer=-1;
	
	  
   	  return res;  
   }

}
