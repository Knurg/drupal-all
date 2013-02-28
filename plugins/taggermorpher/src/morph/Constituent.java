package morph;

/**
 * Die Klasse Constituent ist die Oberklasse fuer die Klassen, welche
 * Wortbildungsbestandteile repraesentieren, wie sie in der Methode
 * parseWord() in MorphModule erzeugt werden. Jeder Chartkante ist
 * dort ein Constituent zugeordnet, der das von der Kante abgedeckte
 * Wortsegment beschreibt. Die Klasse Constituent enthaelt die
 * Segmentinformation, die allen Unterklassen gemeinsam ist: Start-
 * und Endposition sowie Kategorie des Wortbestandteils nach Auskunft
 * von lexeme.txt.
 * @author BL
*/

public class Constituent {

	/**
	 * 
	 * @uml.property name="startPos" 
	 */
	protected int startPos;

	/**
	 * 
	 * @uml.property name="endPos" 
	 */
	protected int endPos;

	/**
	 * 
	 * @uml.property name="category" 
	 */
	protected String category;


  public Constituent() {
  }

  public Constituent( int start, String cat, int end ) {
    startPos = start;
    endPos = end;
    category = cat;
  }

  public String getFlexklasse() {
    System.out.println( "getFlexklasse" + this.toString() );
    return "";
  }

  public String getVerbFlexklasse() {
    System.out.println( "getVerbFlexklasse" + this.toString() );
    return "";
  }

	/**
	 * 
	 * @uml.property name="category"
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * 
	 * @uml.property name="startPos"
	 */
	public int getStart() {
		return startPos;
	}


  public void setStart( int i ) {
    startPos = i;
  }

	/**
	 * 
	 * @uml.property name="endPos"
	 */
	public int getEnd() {
		return endPos;
	}

  public String getWord() {
    return "";
  }

  public LexemEintrag getLexEntry() {
    return null;
  }

  public FlexivEintrag getFlexiveInfo() {
    return null;
  }

  public FlexivMerkmal getFlexive() {
    return null;
  }

  public String toString() {
    return "[start: " + startPos + ", end: " + endPos + ", cat: " + category + "]";
  }
}
