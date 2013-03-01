package morph;

import java.util.Vector;

/**
 * Kategorieinformation fuer Interrogativa.
 * @author BL
 */

public class InterrogKategorieMerkmal extends KategorieMerkmal {
    public InterrogKategorieMerkmal() {
    }

  public String toString() {
    return "Kategorie: INTERROG\n" + super.toString();
  }

  public String category() {
    return "INTERROG";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "InterrogKatMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
