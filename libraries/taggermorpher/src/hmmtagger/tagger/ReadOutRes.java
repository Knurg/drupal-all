package hmmtagger.tagger;

/**
 *  container class for the results of Viterbi path-readout
 */
public class ReadOutRes 
{
	/**the n most probable tag sequences*/
	int[][] tags=null;
	/**the scores of these sequences*/
	float[] scores=null;
}
