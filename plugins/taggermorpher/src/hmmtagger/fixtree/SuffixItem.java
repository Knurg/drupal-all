package hmmtagger.fixtree;

import hmmtagger.tagger.TaggedWord;

/**
 * Container for a word to be searched (inserted) in(to) a suffix tree
 */
public class SuffixItem extends WordItem 
{

	/**
	 * constructor
	 * @param w word
	 * @param t tag
	 */
	public SuffixItem(String w, int t) {
		super(w, t);
		
	}

	/**
	 * constructor
	 * @param tw tagged word
	 */
	public SuffixItem(TaggedWord tw) {
		super(tw);
	}

	/**
	 * constructor
	 * @param w word
	 */
	public SuffixItem(String w) {
		super(w);
	}

	/**
	 * returns a suffix character of this word
	 * @param index index of the character
	 */
	public final char getChar(int index) throws Exception
	{
		if (word.length() <= index) throw new Exception("SuffixItem.getChar: index out of range");
		return word.charAt(word.length()-1-index);
	}

}
