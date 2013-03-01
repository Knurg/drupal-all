package morph;

import java.util.Vector;
import java.util.Enumeration;

/** 
 * Gueltigkeitstest fuer eine WBTransition (siehe parseWord()).
 * @author BL
*/

public class WBRuleActionAgreeWbKat extends WBRuleAction {
  public WBRuleActionAgreeWbKat() {
    super();
  }

  public WBRuleActionAgreeWbKat( String action, Vector params ) {
    super( action, params );
  }

  public boolean evalTest( Constituent start, Constituent end, int wordEnd, WBRulesDictionary d ) {
    FlexivMerkmal f;
    Vector v;
    String cat, c;
    Enumeration enum1;

    f = start.getFlexive();
    if( f != null ) {
      v = f.wbsubcat();
      if( v == null ) return true;
      else {
	cat = end.getCategory();
	enum1 = v.elements();
	while( enum1.hasMoreElements() ) {
	  c = ( String )enum1.nextElement();
	  if( c.equals( cat ) ) return true;
	}

	// cat nicht in wbsubcat enthalten
	return false;
      }
    }
    else return false;
  }
}
