package morph;

import java.util.Vector;

/**
 * Kategorieinformation fuer Eigennamen
 * @author BL
*/

public class ENKategorieMerkmal extends KategorieMerkmal {
  public ENKategorieMerkmal( FlexMerkmal feat ) {
    flex = feat;
  }
  
  public ENKategorieMerkmal() {
  }
  
  public String toString() {
    return "Kategorie: EN\n" + super.toString();
  }
  
  public String category() {
    return "EN";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "ENKAtMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
