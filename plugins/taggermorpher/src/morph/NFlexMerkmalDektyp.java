package morph;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.Hashtable;

import de.fau.cs.jill.feature.FeatureName;
import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureValue;
import de.fau.cs.jill.feature.UnpackedFeatureStructure;

/**
 * Flexionsinformation fuer Nomina, die mehreren Deklinationsschemata folgen.
 * @author BL
 */

public class NFlexMerkmalDektyp extends FlexMerkmal {

	/**
	 * 
	 * @uml.property name="flex"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="de.fau.cs.jill.feature.FeatureStructure"
	 * qualifier="key:java.lang.String java.util.Vector"
	 */
	private Hashtable flex;

    public NFlexMerkmalDektyp() {
    }

    public NFlexMerkmalDektyp( Hashtable v ) {
	flex = v;
    }

  public String toString() {
    return flex.toString();
  }

  
  
  
  @Override
public FeatureValue asFeatureValue(FlexiveDictionary lex) {
	  UnpackedFeatureStructure ufs = new UnpackedFeatureStructure();
	  Enumeration e = flex.keys();
	  while (e.hasMoreElements()) {
		  String k = (String) e.nextElement();
		  ufs.add(FeatureName.forName(k), new FeatureSequence((Vector) flex.get(k)));
	  }
	return ufs.pack();
}

public Vector readingList( FlexiveDictionary lex ) {
    System.err.println( "NFlexMerkmalDektyp reading list not implemented" );
    System.exit( 0 );
    return null;
  }
}
