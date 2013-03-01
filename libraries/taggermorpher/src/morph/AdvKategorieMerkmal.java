package morph;

import java.util.Vector;

/**
 * AdvKategorieMerkmal repraesentiert die Kategorie Adverb.
 * @author BL
 */

public class AdvKategorieMerkmal extends KategorieMerkmal {

    public AdvKategorieMerkmal() {
    }

  public String toString() {
    return "Kategorie: ADV\n" + super.toString();
  }

  public String category() {
    return "ADV";
  }

  public Vector readingList( FlexiveDictionary lex ) {
      Vector res =  new Vector();

      res.addElement( this );
      return res;
  }

}
