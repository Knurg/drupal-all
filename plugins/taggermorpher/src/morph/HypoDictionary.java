package morph;

import java.io.*;
//import me.FileInputStream;

/**
 * HypoDictionary ist das Verzeichnis der Endungshypothesen, die in der Datei
 * filename aufgelistet sind.
 * @author BL
*/

public class HypoDictionary extends MorphDictionary {

	/**
	 * 
	 * @uml.property name="flexer"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private EndungLexer flexer;

	/**
	 * 
	 * @uml.property name="fparser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private EndungParser fparser;

  
  public HypoDictionary() {
  }

  public HypoDictionary( String filename ) {
    try {
//      flexer = new EndungLexer( new FileInputStream( filename ) ); // no File in J2ME
      flexer = new EndungLexer( new FileInputStream( new File(filename) ) ); 
      fparser = new EndungParser(flexer);    
      lex = fparser.endung_liste();
    } 
    catch(Exception e) {
      System.err.println( "exception: " + e );
    }
  }

  public Eintrag getEntry( String key ) {
    if( key.equals( "" ) ) return (Eintrag)lex.get( "NIL" );
    else return (Eintrag)lex.get( key.toUpperCase() );
  }
}
