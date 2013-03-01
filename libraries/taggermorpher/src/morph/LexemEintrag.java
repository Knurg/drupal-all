package morph;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Ein Lexem-Eintrag.
 * @author BL
 */

public class LexemEintrag extends Eintrag {
  public LexemEintrag() {
  }
  
  public LexemEintrag( String lexem, Vector daten ) {
    this.key = lexem;
    // daten ist ein Vector mit FlexivMerkmal-Eintraegen.
    this.daten = daten;
  }
  
  public String holeLexem() {
    return holeEintrag();
  }

  public String toString() {
    return "Lexem Eintrag fuer: " + key + "\n" + daten.toString();
  }

  public boolean onlyEQEntries() {
    Enumeration entries;
    FlexivMerkmal feature;

    boolean res = true;

    entries = daten.elements();
    while( entries.hasMoreElements() ) {
      feature = ( FlexivMerkmal )entries.nextElement();
     
      res = res && feature.isEQEntry();
    }

    return res;
  }
}
