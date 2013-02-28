package morph;

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;

//import me.FileInputStream;

/**
 * LexemeDictionary ist das Verzeichnis aller Eintraege in lexeme.txt.
 * @author BL
 */

public class LexemeDictionary extends MorphDictionary {

	/**
	 * 
	 * @uml.property name="mlexer"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private MorphLexer mlexer;

	/**
	 * 
	 * @uml.property name="mparser"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private MorphParser mparser;

  public LexemeDictionary() {
  }

  public LexemeDictionary( String filename ) {
      //System.out.print("Reading file " + filename + " ...");
    try {
//      mlexer = new MorphLexer( new FileInputStream( filename ) ); // no File class in J2ME
      mlexer = new MorphLexer(new InputStreamReader(new FileInputStream(filename) , "utf-8")); 
      mparser = new MorphParser( mlexer );

      lex = mparser.eintrag_liste();
if (lex == null) throw new NullPointerException();       
      
    }
    catch(Exception e) {
      System.err.println( "exception: " + e + "\n" );
      e.printStackTrace(); 
    }
    //System.out.println(" done!");
  }

  public Hashtable createTVZTable() {
    Enumeration keyEnum, flexFeatures;
    String key;
    LexemEintrag e;
    FlexivMerkmal feature;
    Hashtable tvzTable = new Hashtable();

if (lex == null) System.err.println("doolalala");
    keyEnum = lex.keys();

    while( keyEnum.hasMoreElements() ) {
      key = ( String )keyEnum.nextElement();
      e = ( LexemEintrag )lex.get( key );
      if( e != null ) {
	flexFeatures = e.holeDaten().elements();

	while( flexFeatures.hasMoreElements() ) {
	  feature = ( FlexivMerkmal ) flexFeatures.nextElement();
	  if( feature.isTVZ() ) tvzTable.put( e.holeLexem(), feature );
	}
      }
      else {
	System.err.println( "illegal entry for key: " + key );
      }
    }

    return tvzTable;
  }

  public Hashtable createVPRTable() {
     Enumeration keyEnum, flexFeatures;
     String key;
     LexemEintrag e;
     FlexivMerkmal feature;
     Hashtable vprTable = new Hashtable();

     keyEnum = lex.keys();

     while( keyEnum.hasMoreElements() ) {
          key = ( String )keyEnum.nextElement();
          e = ( LexemEintrag )lex.get( key );
          if( e != null ) {
    	flexFeatures = e.holeDaten().elements();

    	while( flexFeatures.hasMoreElements() ) {
    	  feature = ( FlexivMerkmal ) flexFeatures.nextElement();
    	  if( feature.isVPR() ) vprTable.put( e.holeLexem(), feature );
    	}
      }
          else {
    	System.err.println( "illegal entry for key: " + key );
          }
        }

        return vprTable;
      }

  public Eintrag getEntry( String key ) {
    LexemEintrag entry;
    if( key.equals( "" ) ) return (Eintrag)lex.get( "NIL" );
    else {
      entry = (LexemEintrag)lex.get( key.toUpperCase() );
      return entry;
    }
  }
}
