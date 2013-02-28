package morph;

import java.util.Vector;

import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureValue;

/**
 * Diese Klasse speichert eine Liste von FeatureStructures, die
 * Merkmale eines Flexivs oder einer Vollform sein koennen.
 * @author BL
*/

public class FlexMerkmalEinfach extends FlexMerkmal {

	/**
	 * 
	 * @uml.property name="feature" 
	 */
	protected Vector feature;


  public FlexMerkmalEinfach() {
  }

  public FlexMerkmalEinfach( Vector genkasnum ) {
    feature = genkasnum;
  }

  public String toString() {
    return feature.toString();
  }

  
  
	@Override
	public FeatureValue asFeatureValue(FlexiveDictionary lex) {
		return new FeatureSequence(feature);
	}

	/**
	 * 
	 * @uml.property name="feature"
	 */
	public Vector getFeatures() {
		return feature;
	}

  public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "FlexMerkmalEinfach reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
