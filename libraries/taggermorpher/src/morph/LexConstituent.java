package morph;

/**
 * In LexConstituent wird ein Wortbildungselement beschrieben, das
 * einen Eintrag in lexeme.txt hat, also ein bekanntes Lexem
 * ist. LexConstituent speichert den LexemEintrag aus LexemeDictionary
 * und die dafuer gefundene Flexivinformation, die fuer die Analyse
 * der gueltigen Wortbildungsuebergaenge nach den in wbrules.txt
 * gegebenen Regeln wichtig ist.
 * @author BL
*/

public class LexConstituent extends Constituent {

	/**
	 * 
	 * @uml.property name="konstInfo"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	Pair konstInfo;

  public LexConstituent() {
  }

  public LexConstituent( int start, String cat, Pair p, int end ) {
    super( start, cat, end );
    konstInfo = p;
  }

  public String getWord() {
    LexemEintrag e;

    e = (LexemEintrag)konstInfo.getLeft();
    return (String)e.holeLexem();
  }

  public LexemEintrag getLexEntry() {
    return (LexemEintrag)konstInfo.getLeft();
  }

  public FlexivMerkmal getFlexive() {
    return (FlexivMerkmal)konstInfo.getRight();
  }

  public String getFlexklasse() {
    String stamm;

    stamm = getLexEntry().holeLexem().toLowerCase();

    if( stamm.charAt( stamm.length()-1 ) == 'd' ||
	stamm.charAt( stamm.length()-1 ) == 't' ) return "SWZ2";
    else if(  stamm.charAt( stamm.length()-1 ) == 's' ||
	      stamm.charAt( stamm.length()-1 ) == 'x' ||
	      stamm.charAt( stamm.length()-1 ) == 'z' ) return "SWZ3";
    else if( stamm.substring( stamm.length()-2 ).equals( "el" ) )
      return "SWZ4";
    else if( stamm.substring( stamm.length()-2 ).equals( "er" ) )
      return "SWZ5";
    else return "SWZ1";
  }

  public String getVerbFlexklasse() {
    String stamm;

    stamm = getLexEntry().holeLexem().toLowerCase();

    if( stamm.charAt( stamm.length()-1 ) == 'd' ||
	stamm.charAt( stamm.length()-1 ) == 't' ) return "SWZ2";
    else if(  stamm.charAt( stamm.length()-1 ) == 's' ||
	      stamm.charAt( stamm.length()-1 ) == 'x' ||
	      stamm.charAt( stamm.length()-1 ) == 'z' ) return "SWZ3";
    else if( stamm.substring( stamm.length()-2 ).equals( "el" ) )
      return "SWZ4";
    else if( stamm.substring( stamm.length()-2 ).equals( "er" ) ||
	     stamm.charAt( stamm.length()-1 ) == 'e' )
      return "SWZ5";
    else return "SWZ1";
  }

  public String toString() {
    return "[start: " + startPos + ", end: " + endPos + ", cat: " + category + ", word: " + getWord() + "]";
  }
}
