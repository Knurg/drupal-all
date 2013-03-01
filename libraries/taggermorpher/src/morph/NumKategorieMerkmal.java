package morph;

import java.util.Vector;

/**
 * Kategorieinformation fuer Zahlen.
 * @author BL
*/

public class NumKategorieMerkmal extends KategorieMerkmal {
    public NumKategorieMerkmal() {
    }

  public String toString() {
    return "Kategorie: NUM\n" + super.toString();
  }

  public String category() {
    return "NUM";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "NUmKategorieMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
