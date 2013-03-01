package morph;

import java.util.Vector;

/** 
 * Gueltigkeitstest fuer eine WBTransition (siehe parseWord()).
 * @author BL
*/

public class WBRuleActionPraef extends WBRuleAction {
  public WBRuleActionPraef() {
    super();
  }

  public WBRuleActionPraef( String action, Vector params ) {
    super( action, params );
  }

  public boolean evalTest( Constituent start,
			   Constituent end,
			   int wordEnd,
			   WBRulesDictionary d ) {
    Constituent c;
    FlexivMerkmal f;
    String p;

    p = ( String )actionParams.elementAt( 0 );
    if( p.equals( "1" ) ) c = start;
    else if( p.equals( "2" ) ) c = end;
    else {
      c = null;
      System.err.println( "WBRuleActionPraef: illegal position: " + p );
      System.exit( 0 );
    }

    return ( !c.getWord().equals( "UN" ) );
  }
}
