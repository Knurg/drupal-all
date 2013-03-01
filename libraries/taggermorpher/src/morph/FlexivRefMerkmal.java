package morph;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Speichert Verweise auf andere Lexeme, wie sie bei Eintraegen in lexeme.txt
 * vorkommen. Zum Beispiel: (ERFAHR (= FAHR (VPR ER))). flexivRef hat hier den 
 * Wert 'FAHR'.
 * @author BL
*/

public class FlexivRefMerkmal extends FlexivMerkmal {

	/**
	 * 
	 * @uml.property name="flexivRef" 
	 */
	private String flexivRef;


  public FlexivRefMerkmal() {
  }

  public FlexivRefMerkmal( String s ) {
    flexivRef = s;
  }

  public boolean VFMerkmal() {
    return false;
  }

  public boolean RefMerkmal() {
    return true;
  }

  public boolean isEQEntry() {
    return true;
  }
  public String toString() {
    return "= " + flexivRef;
  }

	/**
	 * 
	 * @uml.property name="flexivRef"
	 */
	public String getRef() {
		return flexivRef;
	}

  public void substRef( Vector v, int pos, Vector entries ) {
    LexemEintrag l;
    Enumeration e;

    // FlexivRefMerkmal loeschen
//    v.remove( pos ); // keine remove-Methode in J2ME
    v.removeElementAt( pos );

    // Eintraege des Verweises in die Liste aufnehmen
    e = entries.elements();
    while( e.hasMoreElements() ) {
      l = (LexemEintrag)e.nextElement();
      v.insertElementAt( l.holeDaten(), pos );
    }
  }
}
