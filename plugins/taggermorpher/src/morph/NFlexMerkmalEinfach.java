package morph;

import java.util.Vector;
import java.util.Enumeration;

import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureValue;

/**
 * Flexionsinformation fuer Nomina, die genau einem Deklinationsschema folgen.
 * @author BL
 */

public class NFlexMerkmalEinfach extends FlexMerkmal {

	/**
	 * 
	 * @uml.property name="flex"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="de.fau.cs.jill.feature.FeatureStructure"
	 */
	private Vector flex;

    public NFlexMerkmalEinfach() {
    }

    public NFlexMerkmalEinfach( Vector v ) {
	flex = v;
    }

  public String toString() {
    return flex.toString();
  }

  
  
  
    @Override
public FeatureValue asFeatureValue(FlexiveDictionary lex) {
	return new FeatureSequence(flex);
}

// Vector mit FlexMerkmal - Eintr�gen
  public Vector readingList( FlexiveDictionary lex ) {

    Enumeration enum1;
    Vector res = new Vector(), f;

    enum1 = flex.elements();
    while( enum1.hasMoreElements() ) {
      f = new Vector();
      f.addElement( enum1.nextElement() );
      res.addElement( new NFlexMerkmalEinfach( f ) );
    }

    return res;

  }
}
