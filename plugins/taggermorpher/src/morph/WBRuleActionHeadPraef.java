package morph;

import java.util.Vector;

/** 
 * Gueltigkeitstest fuer eine WBTransition (siehe parseWord()).
 * @author BL
 */

public class WBRuleActionHeadPraef extends WBRuleAction {
  public WBRuleActionHeadPraef() {
    super();
  }

  public WBRuleActionHeadPraef( String action, Vector params ) {
    super( action, params );
  }

  public boolean evalTest( Constituent start, Constituent end, int wordEnd, WBRulesDictionary d ) {
    Constituent c;
    String lexem, p;

    p = ( String )actionParams.elementAt( 0 );
    if( p.equals( "1" ) ) c = start;
    else if( p.equals( "2" ) ) c = end;
    else {
      c = null;
      System.err.println( "WBRuleActionHeadPraef: illegal position: " + p );
      System.exit( 0 );
    }

    lexem = c.getWord();

    if( lexem.equals( "BE" ) ||
	lexem.equals( "ER" ) ||
	lexem.equals( "VER" ) ||
	lexem.equals( "ENT" ) ||
	lexem.equals( "ZER" ) ) return true;
    else return false;
  }
}
