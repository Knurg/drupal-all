package morph;

import java.util.Vector;

/**
 * In einer WBRuleTransition ist ein Uebergang zwischen zwei
 * Kategorien gespeichert. D.h. die Kategorien von zwei zu
 * analysierenden Konstituenten auf der Chart von parseWord() muessen
 * uebereinstimmen und ebenso muessen alle der WBRuleTransition
 * attributierten WBRuleActions erfolgreich sein.
 * @author BL
*/

public class WBRuleTransition {

	/**
	 * 
	 * @uml.property name="transitionName"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="morph.WBRuleAction"
	 */
	private String transitionName;

	/**
	 * 
	 * @uml.property name="transitionParams"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="morph.WBRuleAction"
	 */
	private Vector transitionParams;

	/**
	 * 
	 * @uml.property name="myDict"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private WBRulesDictionary myDict;


  public WBRuleTransition() {
  }

  public WBRuleTransition( String transition, Vector params ) {
    transitionName = transition;
    transitionParams = params;
  }

	/**
	 * 
	 * @uml.property name="transitionName"
	 */
	public String getName() {
		return transitionName;
	}

	/**
	 * 
	 * @uml.property name="transitionParams"
	 */
	public Vector getParams() {
		return transitionParams;
	}

	/**
	 * 
	 * @uml.property name="myDict"
	 */
	public void setDict(WBRulesDictionary d) {
		myDict = d;
	}

  public boolean evalTests( Constituent start, Constituent end, int wordEnd ) {
    int i = 0;
    WBRuleAction a;
    boolean result;

    while( i < transitionParams.size() ) {
      a = ( WBRuleAction )transitionParams.elementAt( i );

      //System.out.println( "performing test: " + a );
      result = a.evalTest( start, end, wordEnd, myDict );
      //System.out.println( "result: " + result );
      if( result == false ) return false;

      i++;
    }

    // alle Tests waren erfolgreich.

    return true;
  }
}
