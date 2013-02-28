/**
 * 
 */
package nerimd.annotation;

public interface POSSet {
	public boolean isVerb (String pos);
	public boolean isNoun (String pos);
	public boolean isNamedEntity (String pos);
	public boolean isAdjective (String pos);
	public boolean isAdverb (String pos);
	public boolean isPreposition (String pos);
	public boolean isPronoun (String pos);
	public boolean isPossPronoun (String pos);
	public boolean isDetArticle (String pos);
	public boolean isUndetArticle (String pos);
	public boolean isNumber (String pos);
}