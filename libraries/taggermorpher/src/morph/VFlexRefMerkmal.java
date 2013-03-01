package morph;

import java.util.Vector;
import java.util.Enumeration;

import de.fau.cs.jill.feature.FeatureName;
import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureStructure;
import de.fau.cs.jill.feature.FeatureValue;
import de.fau.cs.jill.feature.UnpackedFeatureStructure;

/**
 * Verweis auf einen anderen Eintrag, der Flexionsinformation fuer Verben
 * codiert.
 * @author BL
*/

public class VFlexRefMerkmal extends FlexMerkmal {

    private String flexiv;

	/**
	 * 
	 * @uml.property name="klasse" 
	 */
	private String klasse;


    public VFlexRefMerkmal( String f, String k ) {
	flexiv = f;
	klasse = k;
    }

  public String toString() {
    return "FLEXION = (" + flexiv + "," + klasse + ")";
  }

  public boolean RefMerkmal() {
    return true;
  }

	/**
	 * 
	 * @uml.property name="klasse"
	 */
	public String getRef() {
		return klasse;
	}

  public void substRef( Vector v, int pos, Vector entries ) {
    LexemEintrag l;
    Enumeration e;

    // FlexivRefMerkmal loeschen
    v.remove( pos );

    // Eintraege des Verweises in die Liste aufnehmen
    e = entries.elements();
    while( e.hasMoreElements() ) {
      l = (LexemEintrag)e.nextElement();
      v.insertElementAt( l.holeDaten(), pos );
    }
  }
  
  
  
  
	  
	@Override
	public FeatureValue asFeatureValue (FlexiveDictionary lex) {
		
		return new FeatureSequence(readingList(lex));
		
	}
	
	public Vector readingList( FlexiveDictionary lex ) {
      FlexivEintrag entry;
      Enumeration enum1;
      Vector flexMerkmale;
      KategorieMerkmal k;
      
      // hole Eintrag f�r Flexivklasse klasse
      // d.h. l�se die Referenz auf.
      
      if( flexiv.equals( "" ) ) entry = (FlexivEintrag)lex.getEntry( "NIL" );
      else entry = (FlexivEintrag)lex.getEntry( flexiv );
      
      // Suche die Klassenmerkmale zur Klasse klasse dieses FlexRefMerkmals

      enum1 = entry.categoriesFor( klasse ).elements();
      flexMerkmale = new Vector();
      
      while( enum1.hasMoreElements() ) {
	  k = (KategorieMerkmal)enum1.nextElement();
	  flexMerkmale.add( k.getFlexInfo() );
      }
      
      return flexMerkmale;
  }


    // public Vector readingList( FlexiveDictionary lex ) {
    // System.err.println( "VFlexRefMerkmal reading list not implemented" );
    // System.exit( 0 );
    // return null;
    // }
}
