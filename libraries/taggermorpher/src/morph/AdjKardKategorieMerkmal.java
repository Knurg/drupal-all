package morph;

import java.util.Vector;


/**
 * AdjKardKategorieMerkmal enthaelt Informationen ueber Kardinalzahlen.
 * @author BL
 */

public class AdjKardKategorieMerkmal extends AdjKategorieMerkmal {
    public AdjKardKategorieMerkmal() {
      flex = null;
    }

    //MK:
    public String category() { return "Kardinalzahl"; }

  public String toString() {
    return "Kategorie: Kardinalzahl\n" + super.toString();
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "AdjKardKatMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
