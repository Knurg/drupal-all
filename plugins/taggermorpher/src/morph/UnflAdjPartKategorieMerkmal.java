package morph;

import java.util.Vector;

import morph.AdjKategorieMerkmal;


/**
 * Kategorieinformation und Features fuer Partizipien, die nicht flektieren.
 * @author BL
*/

public class UnflAdjPartKategorieMerkmal extends AdjKategorieMerkmal {
    public UnflAdjPartKategorieMerkmal() {
    }

  public String toString() {
    return "Kategorie: UnflPart\n" + super.toString();
  }

  public Vector readingList( FlexiveDictionary lex ) {
    Vector res = new Vector();

    res.addElement( this );
    return res;
  }
}
