package morph;

import java.util.Vector;

/**
 * Kategorieinformation fuer Infinitivpartikel 'zu'.
 * @author BL
 */

public class InfPartKategorieMerkmal extends KategorieMerkmal {
    public InfPartKategorieMerkmal() {
    }

  public String toString() {
    return "Kategorie: INFINITIVPARTIKEL\n" + super.toString();
  }

  public String category() {
    return "INFINITIVPARTIKEL";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "InfPartKatMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
