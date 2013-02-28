package lebada.voc;

import lebada.fs.FS;


public interface Vocabulary {
	
	public String getName();
	
	/**@return an array of all types of terms this vocabulary comprises or null if no such information is available. 
	 * 
	 */
	public String[] getTypes();
	
	//public String[] getTermsStartingWith(String word);
	
	//public String[] getTermsContaining(String word);
	
	/**
	 * Each feature structure MUST contain these nun-null features:
	 * <ul>
	 * <li>"id" (String): ID of term entry
	 * <li>"canonical" (String): canonical/preferred name
	 * <li>"vocabulary" (Vocabulary): the vocabulary (usually this)
	 * </ul>
	 * 
	 * The following are recommended: 
	 * <ul>
	 * <li>"type"  (String): the term type
	 * <li>"alternates" (String[]): alternate names, if any
	 * <li>"scheme" (String): a SKOS scheme, if available
	 * </ul>
	 * 
	 * @param term
	 * @return
	 */
	//public FS[] getTermInfo(String term);
	
	
	public FS[] lookupTermsContaining(String termPart);
	
	public FS[] lookupTermsStartingWith(String termStart);
	
	public FS[] lookupTermsExactMatch(String termStart);
	
	public FS lookupTermInfo(String entryID);
	
}
