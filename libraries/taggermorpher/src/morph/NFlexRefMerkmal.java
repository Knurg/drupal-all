package morph;

import java.util.Vector;
import java.util.Enumeration;

import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureValue;

/**
 * NFlexRefMerkmal codiert einen Verweis fuer das Deklinationschema
 * eines Nomens.
 * @author BL
*/

public class NFlexRefMerkmal extends FlexMerkmal {
    private String flexiv;

	/**
	 * 
	 * @uml.property name="klasse" 
	 */
	private String klasse;


    public NFlexRefMerkmal( String f, String k ) {
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
    v.removeElementAt( pos );

    // Eintraege des Verweises in die Liste aufnehmen
    e = entries.elements();
    while( e.hasMoreElements() ) {
      l = (LexemEintrag)e.nextElement();
      v.insertElementAt( l.holeDaten(), pos );
    }
  }

  
  @Override
  public FeatureValue asFeatureValue(FlexiveDictionary lex) {
  	FeatureSequence fs = new FeatureSequence();
  	Enumeration e = readingList(lex).elements();
  	while (e.hasMoreElements()) {
  		fs = fs.concat(((KategorieMerkmal) e.nextElement()).flex.asFeatureValue(lex));
  	}
  	return null;
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
}
