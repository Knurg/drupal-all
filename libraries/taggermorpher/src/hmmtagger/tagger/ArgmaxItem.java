package hmmtagger.tagger;

/**Argmax Array Element, stores an array of  back pointer pairs for one state.*/
public class ArgmaxItem 
{
	/**pair of pointers (index of pointed <pre>ArgmaxItem</pre>-Object in the previous line, rank)*/
	class PointerPair
	{
		int item=0;
		int rank=0;
	}

	/**the state represented by this element*/
	int state=0;
	/**the pointers for this state*/
	PointerPair[] pointer=null;
	
	/**constructor
	 * 
	 * @param nRanks number of ranks to be stored
	 */
	public ArgmaxItem(int nRanks) 
	{
		   pointer=new PointerPair[nRanks];
		   for (int i=0; i<nRanks; i++)
		     {pointer[i]=new PointerPair();}	
	}

}
