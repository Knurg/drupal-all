package morph;

import java.util.Vector;

/** 
 * Gueltigkeitstest fuer eine WBTransition (siehe parseWord()).
 * @author BL
*/

public class WBRuleActionNichtWortAnf extends WBRuleAction {
  public WBRuleActionNichtWortAnf() {
    super();
  }

  public WBRuleActionNichtWortAnf( String action, Vector params ) {
    super( action, params );
  }

  public boolean evalTest( Constituent start,
			   Constituent end,
			   int wordEnd,
			   WBRulesDictionary d ) {
    Constituent c;
    FlexivMerkmal f;
    String p;

    //System.out.println( "WBRuleActionNichtWortAnf.evalTest: " + start + " " + end );

    p = ( String )actionParams.elementAt( 0 );
    if( p.equals( "1" ) ) c = start;
    else if( p.equals( "2" ) ) c = end;
    else {
      c = null;
      System.err.println( "WBRuleActionNichtWortAnf: illegal position: " + p );
      System.exit( 0 );
    }

    return ( c.getStart() != 0 );
  }
}
