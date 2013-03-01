package morph;

import java.util.Vector;

/**
 * Kategorieinformation fuer Pronomina.
 * @author BL
*/

public class PronKategorieMerkmal extends KategorieMerkmal {

	/**
	 * 
	 * @uml.property name="featList"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="de.fau.cs.jill.feature.FeatureStructure"
	 */
	private Vector featList;

  public PronKategorieMerkmal() {
  }

  public PronKategorieMerkmal( Vector v ) {
    featList = v;
  }

  public String toString() {
    String s = "Kategorie: PRON\n";
    if( featList != null ) s += "Merkmale: " + featList.toString() + "\n";
    s += super.toString();	
    return s;
  }

  public String category() {
    return "PRON";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "PronKatMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
