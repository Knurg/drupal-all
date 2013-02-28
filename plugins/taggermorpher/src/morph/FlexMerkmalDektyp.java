package morph;

import java.util.Vector;

import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureValue;

/**
 * In FlexMerkmalDektyp werden verschiedene Deklinationsklassen
 * gespeichert. Jeder Eintrag in dektypFeature ist wiederum ein
 * Vector, diesmal von FeatureStructures wie bei
 * FlexMerkmalEinfach.
 * @author BL
*/

public class FlexMerkmalDektyp extends FlexMerkmal {
  private Vector dektypFeature;

  public FlexMerkmalDektyp() {
  }

  public FlexMerkmalDektyp( Vector genkasnum ) {
    dektypFeature = genkasnum;
  }

  public String toString() {
    return dektypFeature.toString();
  }
  
  public Vector getFeatures () {
	  return dektypFeature;
  }
  
  
  

  	@Override
	public FeatureValue asFeatureValue(FlexiveDictionary lex) {
		return new FeatureSequence(dektypFeature);
	}
	
	public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "FlexMerkmalDektyp reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
