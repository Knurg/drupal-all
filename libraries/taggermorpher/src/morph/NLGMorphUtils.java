package morph;

// Java-Importe
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Morphologie-Utilities (haupts�chlich f�r String-Manipulation).
 * @author IT & MK
 */

public abstract class NLGMorphUtils {

    /** Zum besseren Lokalisieren von Debug-Meldungen. */
    private static final String _HINT = "Hint from NLGMorphUtils." ;
    /** Zum besseren Lokalisieren von Debug-Meldungen. */
    private static final String _ERR = "Error in NLGMorphUtils." ;
    
    /**
     * Liefert die syntaktische Kategorie zu Eintragsinformationen.
     * @param info Die Eintragsinformationen als String.
     * @return Die syntaktische Kategorie zu Eintragsinformationen.
     */
    public static String getCatFromInfo(String info) {
	return getEntryFromInfo("Kategorie: ", info);
    }

    /**
     * Liefert den Stamm zu Eintragsinformationen.
     * @param info Die Eintragsinformationen als String.
     * @return Den Stamm zu Eintragsinformationen.
     */
    public static String getStemFromInfo(String info) {
	return getEntryFromInfo("Grundform: ", info);
    }

    /**
     * Liefert den Eintrag zu Eintragsinformationen.
     * @param info Die Eintragsinformationen als String.
     * @return Den Eintrag zu Eintragsinformationen.
     */
    private static String getEntryFromInfo(String tag, String info) {
	int start = info.indexOf(tag);
	if (start == -1) return null;
// 	System.out.println("'" + tag + "' an Pos. " + start + " gefunden!");
	int ende = info.indexOf("\n", start);
// 	System.out.println("Zeilenwechsel an Pos. " + ende + " gefunden!");
	return info.substring(start + tag.length(), ende);
    }

    /**
     * Teilt Suffixe wie GE-EN und  GE-T in ein Paar auf.
     * @param suffix Das Suffix.
     * @return Ein Paar aus den beiden Bestandteilen des Suffixes.
     */
    private static Pair splitSuffix(String suffix) {
 	String leftPart = suffix.substring(0, suffix.indexOf("-"));
 	String rightPart = suffix.substring(suffix.indexOf("-")+1);
 	return new Pair(leftPart, rightPart);
    }

    /**
     * Konkateniert einen Stamm und ein Suffix.
     * @param stem Der Stamm.
     * @param suffix Das Suffix.
     * @return Den String aus Stamm und Suffix.
     */
    public static String concat(String stem, String suffix) {
 	if (suffix.equals("NIL"))
 	    return stem;
 	if (suffix.indexOf("-") != -1) {
 	    Pair parts = splitSuffix(suffix);
 	    return parts.getLeft().toString() + stem +
 		parts.getRight().toString();
 	}
 	else return stem + suffix;
    }

    /**
     * Liefert die Features zu Eintragsinformationen.
     *<br>NB: Sch�ner als eine String-Analyse w�re hier ein Methodenaufruf !!!
     *        Aber daf�r w�re ein Einblick in Jans FeatureStructure n�tig! 
     * @param info Die Eintragsinformationen als String.
     * @return Einen Vektor mit Features.
     */
    public static Vector getFeaturesFromInfo(String info) {
	String msg = _HINT + "getFeaturesFromInfo:\n";
	Vector structs = new Vector(), features = null;
	String featTag = "FeatureStructure", struct = null;
	int featIndex = info.indexOf(featTag);
	int curPos = 0, startPos = 0, stopPos = 0;
	boolean done = false;
	if (featIndex > -1) {
	    // remove the brackets to get the string of features.
	    curPos = featIndex + featTag.length();
	    while (curPos < info.length() - 2 && !done) {
		startPos = info.indexOf("[[", curPos);
		stopPos = info.indexOf("]]", startPos);
		if (startPos > -1 && stopPos > -1) {
		    struct = info.substring(startPos + 2, stopPos);
		    features = getFeatures(struct);
		    structs.add(features);
//  		    System.out.println(msg + features + " hinzugef�gt");
		    curPos = stopPos + 2;
		}
		else done = true;
	    }
	}
	return structs;
    }

    /**
     * Segments the feature structure string into its parts and stores
     * the features and their values in a vector.
     * <brEach element in the vector (feature name and its value) has
     * the format: "name value", so that they can be compared with the
     * elements of an NLGAgreement.
     * <br>A feature structure has the following formats: 
     * <ol>
     *   <li>numerus: "sg", kasus: "gen", genus: "mask" OR
     *   <li>numerus: "sg", modus: "indikativ", tempus: "praesens", *
     *       person: "1"
     * </ol>
     * @param struct Die Featurestruktur als String.
     * @return Die Featurestruktur als Vektor.
     */  
    public static Vector getFeatures(String struct) {
	char c;
	String str = "", feat, name, value;
	Vector feats = new Vector(), structs = new Vector();
	// remove empty spaces
	for (int i = 0; i < struct.length(); i++) {
	    c = struct.charAt(i);
	    if (c != ' ') str += c;
	}
	StringTokenizer st = new StringTokenizer(str, ",");
	while (st.hasMoreTokens()) structs.add(st.nextToken());
	for (int i = 0; i < structs.size(); i++) {
	    feat = structs.elementAt(i).toString();
	    name = feat.substring(0, feat.indexOf(":"));
	    value = feat.substring(feat.indexOf('"') + 1,
				   feat.lastIndexOf('"'));
	    feats.add(name + " " + value);
	}
	return feats;
    }


    public static void main(String[] args) {

	String info = "Kat: DET\netc";
	System.out.println(NLGMorphUtils.getEntryFromInfo("Kat: ", info));
	
    }

}
