package morph;

import java.util.Vector;

/** 
 * Gueltigkeitstest fuer eine WBTransition (siehe parseWord()).
 * @author BL
*/

public class WBRuleActionKonj extends WBRuleAction {
  public WBRuleActionKonj() {
    super();
  }

  public WBRuleActionKonj( String action, Vector params ) {
    super( action, params );
  }

  public boolean evalTest( Constituent start, Constituent end, int wordEnd, WBRulesDictionary d ) {
    Constituent c;
    String lexem, p;

    p = ( String )actionParams.elementAt( 0 );
    if( p.equals( "1" ) ) c = start;
    else c = end;

    lexem = c.getWord();

    if( lexem.equals( "UND" ) || lexem.equals( "und" ) ) return true;
    else return false;
  }
}
