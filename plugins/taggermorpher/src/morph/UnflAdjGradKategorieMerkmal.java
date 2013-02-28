package morph;

import java.util.Vector;
import de.fau.cs.jill.feature.*;

/**
 * Kategorieinformation und Features fuer Adjektive, die nicht flektieren.
 * @author BL
*/

public class UnflAdjGradKategorieMerkmal extends AdjKategorieMerkmal {

	/**
	 * 
	 * @uml.property name="grad"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	public FeatureStructure grad;

    public UnflAdjGradKategorieMerkmal() {
    }

    public UnflAdjGradKategorieMerkmal( FeatureStructure feat ) {
	grad = feat;
    }

  public String toString() {
    return super.toString() + "\nGrad: " + grad.toString() + "\n";
  }

  public Vector readingList( FlexiveDictionary lex ) {
    Vector res = new Vector();

    res.addElement( this );
    return res;
  }
}
