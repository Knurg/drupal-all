package morph;

import java.util.Vector;

/**
 * Kategorieinformation fuer Personalpronomina.
 * @author BL
*/

public class PossPronKategorieMerkmal extends KategorieMerkmal {
  //  private String grundform;
  //  private FlexMerkmal flex;

  public PossPronKategorieMerkmal() {
  }

  public PossPronKategorieMerkmal( String g, FlexMerkmal f ) {
    baseForm = g;
    flex = f;
  }

  public String toString() {
    return "Kategorie: POSSPRON\n" + super.toString();
  }

  public String category() {
    return "poss";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "PossPronKatMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
