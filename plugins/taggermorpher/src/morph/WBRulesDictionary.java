package morph;

import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Verzeichnis aller WBRules 
 * @author BL
*/

public class WBRulesDictionary extends MorphDictionary {

	/**
	 * 
	 * @uml.property name="flexClasses"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="ordered" elementType="java.lang.String"
	 * qualifier="className:java.lang.String java.util.Vector"
	 */
	private Hashtable flexClasses;

	/**
	 * 
	 * @uml.property name="flexiveTable"
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="ordered" elementType="java.lang.String"
	 * qualifier="toLowerCase:java.lang.String java.util.Vector"
	 */
	private Hashtable flexiveTable;

  public WBRulesDictionary() {
  }

  public WBRulesDictionary( String rulesFilename, String classesFilename ) {
    Enumeration enum1, flexives;
    String className, flexive;
    Vector classes, v;

    try {
    	
      WBRulesLexer mlexer = new WBRulesLexer( new FileInputStream( new File( rulesFilename ) ) );
//      InputStream inRules = getClass().getResourceAsStream( rulesFilename ); 
//      WBRulesLexer mlexer = new WBRulesLexer( inRules );
      WBRulesParser mparser = new WBRulesParser( mlexer );
      FlexClassLexer clexer = new FlexClassLexer( new FileInputStream( new File( classesFilename ) ) );
//      InputStream inClasses = getClass().getResourceAsStream( classesFilename );
//      FlexClassLexer clexer = new FlexClassLexer( inClasses );
      FlexClassParser cparser = new FlexClassParser( clexer );

      lex = mparser.rules();
      flexClasses = cparser.flexclasslist();

      flexiveTable = new Hashtable();

      enum1 = flexClasses.keys();
      while( enum1.hasMoreElements() ) {
	className = (String)enum1.nextElement();
	classes = ( Vector )flexClasses.get( className );

	flexives = classes.elements();
	while( flexives.hasMoreElements() ) {
	  flexive = (String)flexives.nextElement();
	  if( flexiveTable.containsKey( flexive ) ) {
	    v = ( Vector )flexiveTable.get( flexive );
	    v.addElement( className );
	  }
	  else {
	    v = new Vector();
	    v.addElement( className );
	    flexiveTable.put( flexive, v );
	  }
	}
      }
    }
    catch( Exception e ) {
      System.err.println( "exception in WBRulesDictionary: " + e );
    }
  }

  public Vector category( String flexive ) {
    return (Vector)flexiveTable.get( flexive.toLowerCase() );
  }
  
  public boolean moeglUebergaenge( String cat ) {
    return ( lex.get( cat ) != null );
  }

  public Eintrag getEntry( String k ) {
    return null;
  }

  public WBRuleTransition uebergang( String from, String to ) {
    WBRule r;

    //System.out.println( "WBRULES uebergang: " + from + " " + to );
    r = ( WBRule )lex.get( from );
    if( r != null ) return r.transition( to, this );
    else return null;
  }
}
