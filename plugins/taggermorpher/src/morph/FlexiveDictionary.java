package morph;

import java.io.*;
//import me.FileInputStream; // J2ME

/**
 * Im FlexiveDictionary wird die Flexivinformation aus der Datei
 * flexive.txt gespeichert. Die Klasse hat einen antlr-Parser
 * (flexive_parser.g) der die ASCII-Datei einliest und das
 * FlexiveDictionary aufbaut. Die geparsten Daten werden in einer
 * Hashtable gespeichert, die mit Instanzen der Klasse FlexivEintrag
 * belegt sind.
 * @author BL
 */

public class FlexiveDictionary extends MorphDictionary {

	/**
	 * 
	 * @uml.property name="flexer"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private FlexiveLexer flexer;

	/**
	 * 
	 * @uml.property name="fparser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private FlexiveParser fparser;

  public FlexiveDictionary() {
  }

  public FlexiveDictionary( String filename ) {
    try {
//      flexer = new FlexiveLexer( new FileInputStream( filename ) ); // no File in J2ME
      flexer = new FlexiveLexer( new FileInputStream( new File( filename ) ) ); // no File in J2ME
      fparser = new FlexiveParser(flexer);    
      lex = fparser.flexiv_liste();
    } 
    catch(Exception e) {
      System.err.println( "exception in FlexiveDictionary: " + e );
    }
  }

  public Eintrag getEntry( String key ) {
    FlexivEintrag entry;
    if( key.equals( "" ) ) entry = (FlexivEintrag)lex.get( "NIL" );
    else entry = (FlexivEintrag)lex.get( key.toUpperCase() );
    if( entry == null ) return null;
    else return (Eintrag)entry.clone();
  }
}
