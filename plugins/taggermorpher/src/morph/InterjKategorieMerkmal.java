package morph;

import java.util.Vector;

/**
 * Kategorieinformation fuer Interjektionen.
 * @author BL
 */

public class InterjKategorieMerkmal extends KategorieMerkmal {
    public InterjKategorieMerkmal() {
    }

  public String toString() {
    return "Kategorie: INTERJ\n" + super.toString();
  }

  public String category() {
    return "INTERJ";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "InterjKatMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
