package morph;

/**
 * Die CutAktion beschreibt den Vorgang des Endungabschneidens bei der
 * Hypothetisierung von Wortbildungen. @see ConcatAktion.
 * @author BL
 */

public class CutAktion extends EndungAktion {
  private Integer offset;

  public CutAktion() {
    offset = null;
  }

  public CutAktion( String s ) throws NumberFormatException {
//    offset = new Integer( s ); // no constructor with String as param in J2ME
	  offset = new Integer( Integer.parseInt( s ) );
  }

  public String execute( String s ) {
     return s.substring( 0, s.length() - offset.intValue() );
  }

  public String toString() {
    return "(CUT " + offset + ")";
  }
}
