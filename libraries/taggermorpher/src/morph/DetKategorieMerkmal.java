package morph;

import java.util.Vector;

/** 
 * Kategorieinformation fuer Artikel
 * @author BL
*/

public class DetKategorieMerkmal extends KategorieMerkmal {
  public DetKategorieMerkmal() {
  }

  public DetKategorieMerkmal( String g, FlexMerkmal f ) {
    baseForm = g;
    flex = f;
  }

  public String toString(){
    return "Kategorie: DET\n" + super.toString();
  }

  public String category() {
    return "DET";
  }
  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "DetKatMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
