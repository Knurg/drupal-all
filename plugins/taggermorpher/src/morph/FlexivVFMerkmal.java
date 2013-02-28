package morph;

/**
 * Speichert ein KategorieMerkmal, das die Vollformeninformation des Eintrags
 * in lexeme.txt enthaelt. (VF (PRON) (TYP PERSONAL) (FLEXION (3 (NEUT
 * (NOM SING) (AKK SING)))))
 * @author BL
*/

public class FlexivVFMerkmal extends FlexivMerkmal {

	/**
	 * 
	 * @uml.property name="kategorie"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private KategorieMerkmal kategorie;

  public FlexivVFMerkmal() {
    kategorie = null;
  }

  public FlexivVFMerkmal( KategorieMerkmal k ) {
    kategorie = k;
  }

  public void kategorieSetzen( KategorieMerkmal k ) {
    kategorie = k;
  }

  public KategorieMerkmal kategorieMerkmal() {
    return kategorie;
  }

  public boolean isEQEntry() {
    return false;
  }

  public boolean VFMerkmal() {
    return true;
  }

  public boolean RefMerkmal() {
    return false;
  }

  public String toString() {
      String res;

      res = "VF Merkmal\n" + super.toString() + "\n";
//      if (kategorie == null) res += "Keine Kategorie! \n";
      if( kategorie != null ) res += kategorie.toString();
      return res;
  }
}
