package morph;

import java.util.Vector;
import java.util.Enumeration;
import de.fau.cs.jill.feature.*;

/**
 * AdjPartKategorieMerkmal repraesentiert wie Adjektive deklinierte
 * Partizipien.
 * @author BL
*/

public class AdjPartKategorieMerkmal extends AdjKategorieMerkmal {

	/**
	 * 
	 * @uml.property name="typ"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private FeatureStructure typ;

  
  public AdjPartKategorieMerkmal() {
  }
  
  public AdjPartKategorieMerkmal( FeatureStructure feat ) {
    typ = feat;
  }
  
  public AdjPartKategorieMerkmal( FeatureStructure feat,
				  FlexMerkmal f ) {
    typ = feat;
    flex = f;
  }

  public String toString() {
    return super.toString() + "\nPartiziptyp: " + typ + "\n";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    Enumeration enum1;
    Vector features, res;
    FlexMerkmal f;

    //System.out.println( "ADJ: flex: " + flex );

    return flex.readingList( lex );

    //    System.err.println( "AdjPartKatMerkmal reading list not implemented" );
    //    System.exit( 0 );
    //    return null;
  }
}
