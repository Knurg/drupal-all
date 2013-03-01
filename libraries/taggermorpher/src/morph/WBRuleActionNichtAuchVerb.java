package morph;

import java.util.Vector;
import java.util.Enumeration;

/** 
 * Gueltigkeitstest fuer eine WBTransition (siehe parseWord()).
 * @author BL
*/

public class WBRuleActionNichtAuchVerb extends WBRuleAction {
  public WBRuleActionNichtAuchVerb() {
    super();
  }

  public WBRuleActionNichtAuchVerb( String action, Vector params ) {
    super( action, params );
  }

  public boolean evalTest( Constituent start,
			   Constituent end,
			   int wordEnd,
			   WBRulesDictionary d ) {
    Constituent c;
    LexemEintrag l;
    FlexivMerkmal f;
    Enumeration enum1, catEnum;
    String className, p;
    boolean notAVerb = true;
    Vector cats;

    p = ( String )actionParams.elementAt( 0 );
    if( p.equals( "1" ) ) c = start;
    else if( p.equals( "2" ) ) c = end;
    else {
      c = null;
      System.err.println( "WBRuleActionNichtAuchVerb: illegal position: " + p );
      System.exit( 0 );
    }


    l = c.getLexEntry();
    enum1 = l.holeDaten().elements();

    while( enum1.hasMoreElements() ) {
      f = ( FlexivMerkmal )enum1.nextElement();
//      cats = ((FlexivklasseMerkmal)f).kategorie( d );
      cats = f.kategorie( d );

      catEnum = cats.elements();
      while( catEnum.hasMoreElements() ) {
	className = (String)catEnum.nextElement();
	if( className.equals( "V" ) ||
	    className.equals( "ALLOV" ) ) {
	  notAVerb = false;
	}
      }
    }

    return notAVerb;
  }
}
