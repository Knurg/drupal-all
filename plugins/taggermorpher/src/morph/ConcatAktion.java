package morph;

/**
 * ConcatAktion implementiert das Zusammenfuegen zweier Wortbestandteile, wie 
 * es in endung.txt angegeben ist. Z.B. (CONCAT (CUT 2) EL): zunaechst werden
 * von der Wortform die letzten beiden Buchstaben abgeschnitten, und dann wird
 * die Buchstabenfolge "EL" angehaengt.
 * @author BL
*/

public class ConcatAktion extends EndungAktion {

	/**
	 * 
	 * @uml.property name="arg1"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private EndungAktion arg1;

  private String arg2;

  public ConcatAktion() {
    arg1 = null;
    arg2 = "";
  }

  public ConcatAktion( EndungAktion a, String s ) {
    arg1 = a;
    arg2 = s.toLowerCase();
  }

  public String execute( String s ) {
    String res = "";

    if( arg1 != null ) res = arg1.execute( s ).toLowerCase();
    return res + arg2;
  }

  public String toString() {
    return "(CONCAT " + arg1.toString() + " " + arg2 + ")";
  }
}
