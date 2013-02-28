package morph;

import java.util.Hashtable;

/** 
 * MorphDictionary ist die Oberklasse aller benutzten Lexika.
 * @author BL
*/

abstract public class MorphDictionary {

	/**
	 * 
	 * @uml.property name="lex"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="ordered" elementType="morph.FlexivMerkmal"
	 * qualifier="toUpperCase:java.lang.String morph.LexemEintrag"
	 */
	protected Hashtable lex;

  abstract public Eintrag getEntry( String key );
}
