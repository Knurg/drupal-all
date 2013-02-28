package morph;

import java.util.Vector;
import java.util.Enumeration;

import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureValue;


/**
 * AdjFlexRefMerkmal speichert Flexionsinformation fuer Adjektive, die
 * vom Typ (= FLEXION E ADJ1) ist, d.h. einen Verweis auf eine
 * andernorts gespeicherte Flexionsinformation darstellt. In der
 * Methode readingList() wird die Referenz benutzt, um die
 * tatsaechliche Flexionsinformation zu erhalten.
 * @author BL
 */

public class AdjFlexRefMerkmal extends FlexMerkmal {

	/**
	 * 
	 * @uml.property name="flexiv" 
	 */
	private String flexiv;

    private String klasse;

    public AdjFlexRefMerkmal( String f, String k ) {
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
	 * @uml.property name="flexiv"
	 */
	public String getRef() {
		return flexiv;
	}

  public void substRef( Vector v, int pos, Vector entries ) {
    KlassenMerkmale k;
    FlexMerkmal fm;
    KategorieMerkmal km;
    Enumeration e;
    Vector kmv = new Vector(); // Vector mit KategorieMerkmal - Eintr�gen

    // Hole Kategorie Merkmal
    km = ( KategorieMerkmal ) v.get( pos );

    // Verweis Kategorie Merkmale suchen:
    e = entries.elements();
    while( e.hasMoreElements() ) {
      k = (KlassenMerkmale)e.nextElement();
      kmv.addAll( k.klassenVon( klasse ) );
    }

    // Flexionsinformationen ersetzen -- derzeit unvollst�ndig
    // (sollte kmv tats�chlich mehrere Elemente enthalten
    //  m�sste km entsprechend oft geclont werden
    //  und dann wieder in v eingef�gt werden ...)

    if ( kmv.size() > 0 ){
	fm = ((KategorieMerkmal) kmv.get( 0 )).getFlexInfo();
	if ( fm != null ) km.setFlexRef( fm );
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

// Vector mit KategorieMerkmal - Eintr�gen
  public Vector readingList( FlexiveDictionary lex ) {
    FlexivEintrag e;
    Vector katVector;

    e = ( FlexivEintrag )lex.getEntry( flexiv );

    return e.categoriesFor( klasse );
  }
}






