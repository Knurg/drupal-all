package morph;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Kategorie- und Flexionsinformation fuer Verben.
 * @author BL
*/

public class VKategorieMerkmal extends KategorieMerkmal {
  public VKategorieMerkmal() {
  }
  
  public VKategorieMerkmal( FlexMerkmal feat ) {
    flex = feat;
  }
  
  public VKategorieMerkmal( FlexMerkmal feat, Vector consts, String base ) {
    flex = feat;
    constInfo = consts;
    baseForm = base;
  }
  
  public VKategorieMerkmal( FlexMerkmal feat, String base ) {
    flex = feat;
    baseForm = base;
  }
  
  public String toString() {
    return "Kategorie: V\n" + super.toString();
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
      res.addElement( new VKategorieMerkmal( f, constInfo, baseForm ) );
    }

    return res;
  }

  public String category() {
    return "V";
  }
}
