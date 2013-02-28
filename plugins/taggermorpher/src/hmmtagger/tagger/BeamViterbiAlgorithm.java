package hmmtagger.tagger;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The core tagging algorithm (N-List Beam Viterbi for k-order HMM). For k>1,
 * the HMM is reduced to an equivalent first order HMM by building the
 * cross-product S^k of the state set. These cross-product states are also
 * referred to as "sequences" and encoded as one integer. The encoding is
 * defined by the methods sequenceToIndex and indexToSequence. Multiplication of
 * probabilities is replaced by addition of their logarithms.
 */
public class BeamViterbiAlgorithm {
	/** container for a tag number/prob. pair in the beam set */
	class BeamSetElement {
		int currentNRanks;
		int s;
		float[] p;

		BeamSetElement(int nRanks) {
			p = new float[nRanks];
		}
	};

	/** beam set */
	class BeamSet {
		/** maximal size of the set */
		int maxSize;
		/** current size of the set */
		int currentSize = -1;
		/** the array of the elements */
		BeamSetElement[] elements = null;

		/**
		 * constructor
		 * 
		 */
		BeamSet(int maxS, int nRanks) {
			maxSize = maxS;
			elements = new BeamSetElement[maxSize];
			for (int i = 0; i < maxSize; i++) {
				elements[i] = new BeamSetElement(nRanks);
			}
		}

		/**
		 * constructs the beam set
		 * 
		 * @param max
		 *            maximal scores for all states
		 * @param maxi
		 *            maximal score
		 * @param d
		 *            logarithmic beam "factor"
		 */
		void construct(float[][] max, float maxi, float d) {
			int l = max.length;
			float th = maxi - d;
			currentSize = 0;
			for (int i = 0; i < l; i++) {
				if (max[i][0] > th) {
					for (int j = 0; j < nRanks; j++) {
						elements[currentSize].p[j] = max[i][j];
						elements[currentSize].currentNRanks = nRanks;
						elements[currentSize].s = i;
					}
					currentSize++;
				}
				for (int j = 0; j < nRanks; j++) {
					max[i][j] = negInfinity;
				}
			}
		}
	};

	protected final static float negInfinity = (float) Float.NEGATIVE_INFINITY;
	/** the order of the HMM */
	protected int order;
	/** number of elemenary states of the HMM */
	protected int nStates;
	/**
	 * number of cross-product states (state sequences of length
	 * 
	 * <pre>
	 * order
	 * </pre>
	 * 
	 * )
	 */
	protected int nSequences;
	/** number of tag sequences to be calculated */
	protected int nRanks;
	/** paramter object */
	protected HMMParams params = null;
	/** current max array line */
	protected float[][] max = null;
	/** current argmax array line */
	protected ArgmaxItem[] argmaxLine;
	/** beam set */
	protected BeamSet beamSet = null;
	/** argmax array */
	protected ArgmaxArrayImpl argmax = null;
	/** beam factor */
	protected float beamWindow;
	/** table of transition probabilities */
	protected float[][] transProb = null;
	/** table of starting probabilities */
	protected float[][] initialProb = null;
	/** explicit representations of cross-product states */
	protected int[][] sequRepr = null;
	/**
	 * integer codes of the last
	 * 
	 * <pre>
	 * order
	 * </pre>
	 * 
	 * -1 components of cross-product states
	 */
	protected int[] suffix = null;

	/**
	 * constructor
	 * 
	 * @param nRanks
	 *            number of state sequences to be returned
	 * @param pm
	 *            parameter object
	 * @param ar
	 *            argmax array
	 * @param beamWin
	 *            beam factor
	 */
	BeamViterbiAlgorithm(int nRanks, HMMParams pm, ArgmaxArrayImpl ar,
			float beamWin) {
		params = pm;
		order = pm.getOrder();
		nStates = pm.getNumberOfStates();
		this.nRanks = nRanks;
		nSequences = (int) Math.pow(nStates, order);

		suffix = pm.getTransitionParameters().getSuffix();
		sequRepr = pm.getTransitionParameters().getSequRepr();

		transProb = params.getTransitionProbMatrix();
		initialProb = params.getInitialProbMatrix();
		max = new float[nSequences][nRanks];
		argmaxLine = new ArgmaxItem[nSequences];
		for (int i = 0; i < nSequences; i++) {
			argmaxLine[i] = new ArgmaxItem(nRanks);
		}
		beamSet = new BeamSet(nSequences, nRanks);
		argmax = ar;

		beamWindow = (float) Math.log(beamWin);
	}

	/**
	 * N-List Viterbi Beam Algorithm
	 * 
	 * @param input
	 *            sentence to be tagged
	 * @return array of lexicon entries
	 * @throws IOException
	 */
	final synchronized LexicalEntry[] run(String[] input) throws IOException {

		if (input.length == 0)
			return null;

		float maxProb = negInfinity;
		ArrayList lexEntries = new ArrayList();
		int nWords = input.length;

		// precalculate lexical probs for the first input words

		int length = Math.min(order, nWords);
		float[][] lexicalProbsIni = new float[length][nStates];
		for (int j = 0; j < length; j++) {
			for (int s = 0; s < nStates; s++) {
				lexicalProbsIni[j][s] = negInfinity;
			}
			LexicalEntry lp = null;
			lp = params.getLexicalEntry(input[j], (j == 0));
			lexEntries.add(lp);
			for (int i = 0; i < lp.p.length; i++) {
				lexicalProbsIni[j][lp.p[i].s] = lp.p[i].p;
			}
		}

		// calculate initial probabilities
		int diff = order - length;
		int shift = (int) Math.pow(nStates, diff);
		int oldDivValue = -1;
		boolean check;
		float[] maxi = null;
		ArgmaxItem.PointerPair[] amaxi = null;
		for (int s = 0; s < nSequences; s++) {
			check = true;
			int index = s;
			if (diff > 0) {
				int newDivValue = (s - (s % shift)) / shift;
				if (newDivValue != oldDivValue) {
					oldDivValue = newDivValue;
					index = oldDivValue;
				} else {
					check = false;
				}
			}
			maxi = max[s];
			if (check) {
				float product = 0;
				int[] sequ = sequRepr[s];
				for (int i = 0; i < length; i++) {
					product += lexicalProbsIni[i][sequ[i]];
					if (product == negInfinity)
						break;
				}
				if (product != negInfinity) {
					maxi[0] = product + getInitialProb(index, length);
					if (maxi[0] > maxProb) {
						maxProb = maxi[0];
					}
				} else {
					maxi[0] = negInfinity;
				}
			} else {
				maxi[0] = negInfinity;
			}
			for (int i = 1; i < nRanks; i++) {
				maxi[i] = negInfinity;
			}
		}
		beamSet.construct(max, maxProb, beamWindow);
		argmax.constructLine(argmaxLine, beamSet.elements, beamSet.currentSize);
		lexicalProbsIni = null;

		float[] lexicalProbs = new float[nStates];

		// Viterbi bottom up calculation
		for (int t = order; t < nWords; t++) {
			maxProb = negInfinity;
			// precalculate lexical probs for current input word
			for (int s = 0; s < nStates; s++) {
				lexicalProbs[s] = negInfinity;
			}
			LexicalEntry lp = params.getLexicalEntry(input[t], false);
			lexEntries.add(lp);
			for (int i = 0; i < lp.p.length; i++) {
				lexicalProbs[lp.p[i].s] = lp.p[i].p;
			}

			for (int j = 0; j < beamSet.currentSize; j++) // for all beam set
															// elements (source
															// states)
			{
				BeamSetElement prev = beamSet.elements[j];
				int superStar = 0;
				if (order > 1)
					superStar = suffix[prev.s] * nStates;
				for (int s2 = 0; s2 < nStates; s2++) // for all possible target
														// states
				{
					float product = lexicalProbs[s2];
					if (product == negInfinity) {
						continue;
					}

					int s = superStar + s2;

					maxi = max[s];
					amaxi = argmaxLine[s].pointer;

					float pTrans;

					pTrans = getSequTransitionProb(prev.s, s2);

					for (int i = 0; i < prev.currentNRanks; i++) // for all
																	// ranks of
																	// current
																	// source
																	// state
					{
						// calculate probability for target state and possibly
						// insert into list of "nRank" highest probabilities
						float p = prev.p[i] + pTrans + product;
						if (p > maxi[nRanks - 1]) {
							if (p > maxProb) {
								maxProb = p;
							}
							for (int y = 0; y < nRanks; y++) {
								if (maxi[y] < p) {
									for (int u = nRanks - 1; u > y; u--) {
										maxi[u] = maxi[u - 1];
										amaxi[u].item = amaxi[u - 1].item;
										amaxi[u].rank = amaxi[u - 1].rank;
									}
									maxi[y] = p;
									amaxi[y].item = j;
									amaxi[y].rank = i;
									break;
								}
							}
						} else {
							break;
						}
					}
				}
			}
			beamSet.construct(max, maxProb, beamWindow); // construct new beam
															// set
			argmax.constructLine(argmaxLine, beamSet.elements,
					beamSet.currentSize); // construct new argmax line
		}
		LexicalEntry[] resArray = new LexicalEntry[lexEntries.size()];
		lexEntries.toArray(resArray);
		return resArray;
	}

	/**
	 * performs readout of Viterbi pathes...must be called AFTER calling Method
	 * 
	 * <pre>
	 * run
	 * </pre>
	 */
	final ReadOutRes readOut() throws IOException {
		ReadOutRes res = argmax.readOut(beamSet);
		beamSet.currentSize = -1;
		return res;
	}

	private final float getSequTransitionProb(int s1, int s2) {
		if (transProb != null) {
			return transProb[s1][s2];
		}
		return params.getTransitionProb(s2, s1);
	}

	private final float getInitialProb(int s, int length) {
		if (initialProb == null) {
			return params.getInitialProb(s, length);
		} else {
			return initialProb[length - 1][s];
		}
	}

	/**
	 * mapping of int representation of a state sequence to its explicit
	 * representation
	 * 
	 * @param index
	 *            the int representation
	 * @param k
	 *            length of state sequence
	 * @param nStat
	 *            number of elementary states
	 * @return
	 */
	public static final int[] indexToSequence(int index, int k, int nStat) {
		int ind = index;
		int[] res = new int[k];
		for (int state = k - 1; state >= 0; state--) {
			int r = (int) (ind % nStat);
			res[state] = r;
			ind = (int) ((ind - r) / nStat);
		}
		return res;
	}

	/**
	 * inverse function to
	 * 
	 * <pre>
	 * indexToSequence
	 * </pre>
	 * 
	 * @param s
	 *            the sequence
	 * @param k
	 *            length of state sequence
	 * @param nStat
	 *            number of elementary states
	 * @return
	 */
	public static final int sequenceToIndex(int[] s, int k, int nStat) {
		// Horner
		int res = 0;
		for (int i = 0; i < k; i++) {
			res *= nStat;
			res += s[i];
		}
		return res;
	}

}
