package morph;

import java.util.Vector;

/**
 * Kategoriemerkmale fuer Trennbare Verbzus�tze
 * @author BL
*/

public class TVZKategorieMerkmal extends KategorieMerkmal {
    public TVZKategorieMerkmal() {
    }

  public String toString() {
    return "Kategorie: TVZ\n" + super.toString();
  }

  public String category() {
    return "TVZ";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "TVZKategorieMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
