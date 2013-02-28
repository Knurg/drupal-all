package morph;

import java.util.Vector;

/** 
 * Gueltigkeitstest fuer eine WBTransition (siehe parseWord()).
 * @author BL
*/

public class WBRuleActionNichtAllo extends WBRuleAction {
  public WBRuleActionNichtAllo() {
    super();
  }

  public WBRuleActionNichtAllo( String action, Vector params ) {
    super( action, params );
  }

  public boolean evalTest( Constituent start, Constituent end, int wordEnd, WBRulesDictionary d ) {
    Constituent c;
    FlexivMerkmal f;
    String p;

    p = ( String )actionParams.elementAt( 0 );
    if( p.equals( "1" ) ) c = start;
    else if( p.equals( "2" ) ) c = end;
    else {
      c = null;
      System.err.println( "WBRuleActionNichtAllo: illegal position: " + p );
      System.exit( 0 );
    }


    f = c.getFlexive();
    return f.grundform().equals( "" );
  }
}
