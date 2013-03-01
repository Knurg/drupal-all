package hmmtagger.api;

import hmmtagger.tagger.Dictionary;


/**
*classes implementing this interface can be passed to the constructor of class <pre>SimpleLexikon</pre>
*in order to perform manipulations of the dictionary of this class
 */
public interface SpecificDictionaryManipulator 
{
	/**manipulates the counts of the dictionary, before probabilities are estimated
	 * 
	 * @param dic the dictionary to manipulate
	 */
	public void manipCounts(Dictionary dic);
	
	/**manipulates the probabilities of the dictionary
	 * 
	 * @param dic the dictionary to manipulate
	 */
	public void manipProbs(Dictionary dic);

}
