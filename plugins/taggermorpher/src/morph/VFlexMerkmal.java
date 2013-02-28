package morph;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Flexionsinformation fuer Verben, wie sie in flexive.txt codiert ist.
 * @author BL
*/

public class VFlexMerkmal extends FlexMerkmalEinfach {

  public VFlexMerkmal( Vector v ) {
    super( v );
  }

  public String toString() {
    return super.toString();
  }

    // Vector mit FlexMerkmal - Eintr�gen
  public Vector readingList( FlexiveDictionary lex ) {
    Enumeration enum1;
    Vector res = new Vector(), f;

    enum1 = feature.elements();
    while( enum1.hasMoreElements() ) {
      f = new Vector();
      f.addElement( enum1.nextElement() );
      res.addElement( new VFlexMerkmal( f ) );
    }

    return res;
  }

  public Vector getFeatures() {
    return feature;
  }
}
