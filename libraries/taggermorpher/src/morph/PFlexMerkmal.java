package morph;

import java.util.Vector;

import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureValue;

/**
 * Rektionsinformation bei Praepositionen.
 * @author BL
*/

public class PFlexMerkmal extends FlexMerkmal {

	/**
	 * 
	 * @uml.property name="featList"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="de.fau.cs.jill.feature.FeatureStructure"
	 */
	private Vector featList;

    public PFlexMerkmal( Vector v ) {
	featList = v;
    }

  public String toString() {
    return featList.toString();
  }
  
  
  

  @Override
	public FeatureValue asFeatureValue(FlexiveDictionary lex) {
		return new FeatureSequence(featList);
	}
	
	public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "PFlexMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
