package morph;

import java.util.Vector;

/**
 * Oberklasse aller Testaktionen fuer die Gueltigkeit einer WBTransition.
 * @author BL
 */

abstract public class WBRuleAction {

	/**
	 * 
	 * @uml.property name="actionName" 
	 */
	protected String actionName;

	/**
	 * 
	 * @uml.property name="actionParams"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	protected Vector actionParams;


  public WBRuleAction() {
  }

  public WBRuleAction( String action, Vector params ) {
    actionName = action;
    actionParams = params;
  }

	/**
	 * 
	 * @uml.property name="actionName"
	 */
	public String getName() {
		return actionName;
	}

	/**
	 * 
	 * @uml.property name="actionParams"
	 */
	public Vector getParams() {
		return actionParams;
	}

  abstract public boolean evalTest( Constituent currStart,
				    Constituent currEnd,
				    int wordEnd,
				    WBRulesDictionary d );

  public String toString() {
    return "ACTION: " + actionName + " PARAMS: " + actionParams;
  }
}
