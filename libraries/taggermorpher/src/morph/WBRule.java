package morph;

import java.util.Hashtable;

/**
 * WBRule codiert einen Uebergang zwischen zwei
 * Konstituenten. parseWord() sucht zu zwei gegebenen Konstituenten
 * (also Chartkanten) eine WBTransition, die einen gueltigen Uebergang
 * repraesentiert. In einer WBRule sind alle WBTransitions
 * gespeichert, deren Uebergang von leftSymbolName ausgeht.
 * @author BL
*/

public class WBRule {

	/**
	 * 
	 * @uml.property name="leftSymbolName"
	 * @uml.associationEnd multiplicity="(0 1)" qualifier="key:java.lang.String morph.WBRuleTransition"
	 */
	private String leftSymbolName;

	/**
	 * 
	 * @uml.property name="transitionTable"
	 * @uml.associationEnd multiplicity="(0 1)" qualifier="to:java.lang.String morph.WBRuleTransition"
	 */
	private Hashtable transitionTable;


  public WBRule() {
  }

  public WBRule( String left, Hashtable transitions ) {
    leftSymbolName = left;
    transitionTable = transitions;
  }

	/**
	 * 
	 * @uml.property name="leftSymbolName"
	 */
	public String getName() {
		return leftSymbolName;
	}

	/**
	 * 
	 * @uml.property name="transitionTable"
	 */
	public Hashtable getParams() {
		return transitionTable;
	}

  public WBRuleTransition transition( String to, WBRulesDictionary dict ) {
    WBRuleTransition t = ( WBRuleTransition )transitionTable.get( to );

    if( t != null ) t.setDict( dict );
    return t;
  }
}
