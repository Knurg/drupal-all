package morph;

import java.util.Vector;

/**
 * Ein Eintrag fuer eine Endung.
 * @author BL
 */

public class EndungEintrag extends Eintrag {
  public EndungEintrag() {
  }
  
  public EndungEintrag( String endung, Vector daten ) {
    this.key = endung;
    // daten ist ein Vector mit KlassenMerkmale-Eintraegen.
    this.daten = daten;
  }
    
  public String holeEndung() {
    return holeEintrag();
  }
  
  public String toString() {
    return "Endung Eintrag fuer: " + key + "\n" + daten.toString();
  }
}
