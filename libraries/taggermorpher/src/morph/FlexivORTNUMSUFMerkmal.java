package morph;

/**
 * Speichert die Information, die bei Eintraegen in lexeme.txt unter dem
 * Schluesselwort 'ORTNUMSUF' zu finden ist.
 * @author BL
*/

public class FlexivORTNUMSUFMerkmal extends FlexivMerkmal {

	/**
	 * 
	 * @uml.property name="kategorie"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private KategorieMerkmal kategorie;

  public FlexivORTNUMSUFMerkmal() {
    kategorie = null;
  }

  public FlexivORTNUMSUFMerkmal( KategorieMerkmal k ) {
    kategorie = k;
  }

  public void kategorieSetzen( KategorieMerkmal k ) {
    kategorie = k;
  }

  public boolean isEQEntry() {
    return false;
  }

  public boolean VFMerkmal() {
    return false;
  }

  public boolean RefMerkmal() {
    return false;
  }

  public String toString() {
    if( kategorie != null ) {
      return super.toString() + "\n" + kategorie.toString();
    }
    else return super.toString();
  }
}
