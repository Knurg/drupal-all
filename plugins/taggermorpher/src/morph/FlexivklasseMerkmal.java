package morph;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Eine Instanz von FlexivklasseMerkmal speichert zu einem Eintrag in
 * lexeme.txt einen Verweis (den Namen) auf die Flexivklasse des
 * Eintrags. Damit werden die Lexeme in lexeme.txt und die Flexive in
 * flexive.txt miteinander verbunden. Beispiel: (ABART (FEM1)). Das
 * Lexem 'ABART' hat also Flexivklasse 'FEM1'. Bei der Zerlegung von
 * Wortformen wird dann untersucht, ob ein potentielles Flexiv fuer
 * eine gegebene Wortform auch zur Klasse 'FEM1' gehoeren kann.
 * @author BL
*/

public class FlexivklasseMerkmal extends FlexivMerkmal {

	/**
	 * 
	 * @uml.property name="flexiv" 
	 */
	private String flexiv;


  public FlexivklasseMerkmal() {
  }

  public FlexivklasseMerkmal( String s ) {
    flexiv = s;
  }

  public boolean isEQEntry() {
    return false;
  }

  public boolean VFMerkmal() {
    return false;
  }

  public boolean RefMerkmal() {
    return false;
  }

  public boolean isClass() {
    return true;
  }

  public boolean flexiveClass( String classname ) {
    return flexiv.equals( classname );
  }

	/**
	 * 
	 * @uml.property name="flexiv"
	 */
	public String getFlexive() {
		return flexiv;
	}

  // KATEGORIE
  // Wird von ADD-KAT benoetigt, um die Flexionsklassen in Wortklassen zu
  // ueberfuehren. Greift auf die in der Datei wbrules.lsp initialisierten
  // globalen Variablen zu

  public Vector kategorie( WBRulesDictionary wbrLex ) {
    Vector classes, res = new Vector();
    String className;
    Enumeration enum1;

    classes = wbrLex.category( flexiv );

    if( classes != null ) {
      enum1 = classes.elements();
      while( enum1.hasMoreElements() ) {
	className = (String)enum1.nextElement();
	
	if( className == null ) res.add( flexiv );
	else if( className.equals( "N-FLEXKLASSEN" ) ) res.add( "N" );
	else if( className.equals( "V-FLEXKLASSEN" ) ) res.add( "V" );
	else if( className.equals( "ADJ-FLEXKLASSEN" ) ) res.add( "ADJ" );
	else if( className.equals( "ALLOV" ) ) res.add( "ALLOV" );
	else if( className.equals( "NUM1" ) ||
		 className.equals( "NUM2" ) ||
		 className.equals( "NUM3" ) ||
		 className.equals( "NUM4" ) ||
		 className.equals( "NUM5" ) ) res.add( "NUM" );
	else res.add( flexiv );
      }
    }
    else res.add( flexiv );

    return res;
  }

  public boolean verb() {
    return ( flexiv.equals( "SWV1" ) ||
      flexiv.equals( "SWV2" ) ||
      flexiv.equals( "SWV3" ) ||
      flexiv.equals( "SWZ1" ) ||
      flexiv.equals( "SWZ2" ) ||
      flexiv.equals( "SWZ3" ) ||
      flexiv.equals( "STV1A" ) ||
      flexiv.equals( "STV1B" ) ||
      flexiv.equals( "STV1C" ) ||
      flexiv.equals( "STV2A" ) ||
      flexiv.equals( "STV2B" ) ||
      flexiv.equals( "STV3AA" ) ||
      flexiv.equals( "STV3AB" ) ||
      flexiv.equals( "STV3B" ) ||
      flexiv.equals( "STV3C" ) ||
      flexiv.equals( "STV4A" ) ||
      flexiv.equals( "STV4B" ) ||
      flexiv.equals( "STV4C" ) ||
      flexiv.equals( "STV5A" ) ||
      flexiv.equals( "STV5B" ) ||
      flexiv.equals( "STV4AA" ) ||
      flexiv.equals( "STV4BB" ) ||
      flexiv.equals( "STV4CC" ) ||
      flexiv.equals( "STV5AA" ) ||
      flexiv.equals( "STV5BB" ) ||
      flexiv.equals( "STV5CC" ) ||
      flexiv.equals( "STV5C" ) ||
      flexiv.equals( "STV6" ) ||
      flexiv.equals( "STV7A" ) ||
      flexiv.equals( "STV7B" ) ||
      flexiv.equals( "STV7C" ) ||
      flexiv.equals( "STV8" ) ||
      flexiv.equals( "STV9" ) ||
      flexiv.equals( "STVKONJ" ) );
  }

  public String toString() {
    return "Flexivklasse: " + flexiv + "\n" + super.toString();
  }
}
