package hmmtagger.fixtree;

import hmmtagger.tagger.TaggedWord;

/**
 * Container for a word to be searched (inserted) in(to) a prefix tree
 */
public class PrefixItem extends WordItem 
{

	/**
	 * constructor
	 * @param w word
	 * @param t tag
	 */
	public PrefixItem(String w, int t) {
		super(w, t);
	}

	/**
	 * constructor
	 * @param tw tagged word
	 */
	public PrefixItem(TaggedWord tw) {
		super(tw);
	}

	/**constructor
	 * @param w word
	 */
	public PrefixItem(String w) {
		super(w);
	}


	 /**
	  * returns a prefix character of this word
	  * @param index index of the character
	  */
	public final char getChar(int index) throws Exception 
	{
		if (word.length() <=  index) throw new Exception("PrefixItem.getChar: index out of range");
		return word.charAt(index);
	}

}
