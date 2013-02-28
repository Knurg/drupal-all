package morph;

import java.util.Vector;

//import me.Cloneable; // J2ME

/**
 * Eintrag ist die Oberklasse aller benutzten Lexikoneintraege und stellt die
 * in allen Unterklassen notwendigen Zugriffsmethoden zur Verfuegung.
 * @author BL
*/

abstract public class Eintrag implements Cloneable { 
  protected String key;

	/**
	 * 
	 * @uml.property name="daten"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="morph.KlassenMerkmale"
	 */
	protected Vector daten;

  
  public String holeEintrag() {
    return key;
  }

  public Vector holeDaten() {
    return daten;
  }
}
