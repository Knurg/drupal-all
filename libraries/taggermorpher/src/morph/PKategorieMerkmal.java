package morph;

import java.util.Vector;

/**
 * Kategorie- und Rektionsinformation fuer Praepositionen.
 * @author BL
*/

public class PKategorieMerkmal extends KategorieMerkmal {
//    private FlexMerkmal flex;

    public PKategorieMerkmal() {
      flex = null;
    }

    public PKategorieMerkmal( FlexMerkmal feat ) {
	flex = feat;
    }

  public String toString() {
    return "Kategorie: P\n" + super.toString();
  }

  public String category() {
    return "P";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "PKategorieMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
