package morph;

import java.util.Vector;

/**
 * AdjOrdKategorieMerkmal enthaelt Informationen ueber Ordinalzahlen.
 * @author BL
*/

public class AdjOrdKategorieMerkmal extends AdjKategorieMerkmal {
    public AdjOrdKategorieMerkmal() {
      flex = null;
    }

    // MK:
    public String category() { return "Ordinalzahl"; }

  public String toString() {
    return "Kategorie: Ordinalzahl\n" + super.toString();
  }

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "AdjOrdKatADMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
