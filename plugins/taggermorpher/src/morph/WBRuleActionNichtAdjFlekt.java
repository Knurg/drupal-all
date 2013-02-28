package morph;

import java.util.Vector;
import java.util.Enumeration;

/** 
 * Gueltigkeitstest fuer eine WBTransition (siehe parseWord()).
 * @author BL
 */

public class WBRuleActionNichtAdjFlekt extends WBRuleAction {
  public WBRuleActionNichtAdjFlekt() {
    super();
  }

  public WBRuleActionNichtAdjFlekt( String action, Vector params ) {
    super( action, params );
  }

  public boolean evalTest( Constituent start,
			   Constituent end,
			   int wordEnd,
			   WBRulesDictionary d ) {
    Constituent c;
    LexemEintrag l;
    FlexivMerkmal f;
    Enumeration enum1;
    String className, p;
    boolean notAVerb = true;

    p = ( String )actionParams.elementAt( 0 );
    if( p.equals( "1" ) ) c = start;
    else if( p.equals( "2" ) ) c = end;
    else {
      c = null;
      System.err.println( "WBRuleActionNichtAdjFlekt: illegal position: " + p );
      System.exit( 0 );
    }


    l = c.getLexEntry();
    enum1 = l.holeDaten().elements();

    while( enum1.hasMoreElements() ) {
      f = ( FlexivMerkmal )enum1.nextElement();
      if( f.getFlexive().equals( "ADJFLEK" ) ) return false;
    }

    return true;
  }
}
