package morph;

import java.util.Vector;

/**
 * Kategoriemerkmale fuer Konjunktionen
 * @author BL
*/

public class KonjKategorieMerkmal extends KategorieMerkmal {
    public KonjKategorieMerkmal() {
    }

  public String toString() {
    return "Kategorie: KONJ\n" + super.toString();
  }

  public String category() {
    return "KONJ";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "KonjKategorieMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
