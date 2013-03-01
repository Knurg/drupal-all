package morph;

/**
 * Die Klasse Pair dient zur Speicherung zweier unabhaengiger Objekte.
 * @author BL
 */

public class Pair {

	/**
	 * 
	 * @uml.property name="left"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="morph.FlexivMerkmal"
	 */
	private Object left;

	/**
	 * 
	 * @uml.property name="right" 
	 */
	private Object right;


  public Pair() {
  }

  public Pair( Object l, Object r ) {
    left = l;
    right = r;
  }

	/**
	 * 
	 * @uml.property name="left"
	 */
	public Object getLeft() {
		return left;
	}

	/**
	 * 
	 * @uml.property name="right"
	 */
	public Object getRight() {
		return right;
	}

  public String toString() {
    return "(" + left.toString() + "," + right.toString() + ")";
  }
}
