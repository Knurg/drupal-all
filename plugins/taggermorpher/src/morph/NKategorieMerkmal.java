package morph;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Kategorie- und Flexionsinformation fuer Nomina.
 * @author BL
*/

public class NKategorieMerkmal extends KategorieMerkmal {
  public NKategorieMerkmal( FlexMerkmal feat ) {
    flex = feat;
  }
  
  public NKategorieMerkmal() {
  }

  public String toString() {
    return "Kategorie: N\n" + super.toString();
  }
  
  public String category() {
    return "N";
  }

  public void setFlexRef( NFlexRefMerkmal f ) {
    flex = f;
  }

  public Vector readingList( FlexiveDictionary lex ) {
    Enumeration enum1;
    Vector features, res;
    FlexMerkmal f;

    features = flex.readingList( lex );
    res = new Vector();
    enum1 = features.elements();
    while( enum1.hasMoreElements() ) {
      f = (FlexMerkmal)enum1.nextElement();
      res.addElement( new NKategorieMerkmal( f ) );
    }

    return res;
  }
}
