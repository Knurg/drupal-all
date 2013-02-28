package morph;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureName;
import de.fau.cs.jill.feature.FeatureValue;
import de.fau.cs.jill.feature.UnpackedFeatureStructure;

/**
 * AdjFlexMerkmal repraesentiert die Flexionsinformation fuer Adjektive.
 * @author BL
*/

public class AdjFlexMerkmal extends FlexMerkmal {

	/**
	 * 
	 * @uml.property name="dektyps"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="de.fau.cs.jill.feature.FeatureStructure"
	 * qualifier="key:java.lang.String java.util.Vector"
	 */
	private Hashtable dektyps;

  
  public AdjFlexMerkmal() {
  }
  
  public AdjFlexMerkmal( Hashtable v ) {
    dektyps = v;
  }
  
  public String toString() {
    return dektyps.toString();
  }

  
  
  @Override
public FeatureValue asFeatureValue(FlexiveDictionary lex) {
	  UnpackedFeatureStructure ufs = new UnpackedFeatureStructure();
	  Enumeration e = dektyps.keys();
	  while(e.hasMoreElements()) {
		  String k = (String) e.nextElement();
		  ufs.add(FeatureName.forName(k), new FeatureSequence((Vector) dektyps.get(k)));
	  }
	return ufs.pack();
}

public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "AdjFlexMerkmal reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
