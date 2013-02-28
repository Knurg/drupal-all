package morph;

/**
 * Die Klasse MorphAnalysis beschreibt ein Analyseergebnis. Es besteht aus
 * der analysierten Oberflaechenform und einer Instanz eines KategorieMerkmals,
 * das die Wortart-, Konstituenten- und Grundformeninformation enthaelt.
 * @author BL
*/

public class MorphAnalysis {

	/**
	 * 
	 * @uml.property name="surfaceForm" 
	 */
	private String surfaceForm;

	/**
	 * 
	 * @uml.property name="morphAnalysis"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private KategorieMerkmal morphAnalysis;


  public MorphAnalysis( String s, KategorieMerkmal m ) {
    surfaceForm = s;
    morphAnalysis = m;
  }

	/**
	 * 
	 * @uml.property name="surfaceForm"
	 */
	public String getSurfaceForm() {
		return surfaceForm;
	}

	/**
	 * 
	 * @uml.property name="morphAnalysis"
	 */
	public KategorieMerkmal getAnalysis() {
		return morphAnalysis;
	}

  public String toString() {
     return "morph analysis for: " + surfaceForm + "\n" + morphAnalysis.toString();
  }
}
