package morph;

import java.util.Vector;

/**
 * FlexivMerkmal ist die Oberklasse aller Klassen, die
 * Flexivinformationen fuer Lexeme enthalten. Der antlr-Parser
 * morph_parser.g konstruiert beim Analysieren von lexeme.txt jeweils
 * geeignete Instanzen von Unterklassen von FlexivMerkmal, um die
 * Information (Flexivklassenverweis, Vollformeninformation,
 * Grundformeninformation, Allomorphe etc.) zu speichern.
 * @author BL
*/

public abstract class FlexivMerkmal extends Merkmal {

    private static final String _HINT = "Hint from FlexivMerkmal.";

	/**
	 * 
	 * @uml.property name="grundform" 
	 */
	private String grundform = "";

	/**
	 * 
	 * @uml.property name="abk" 
	 */
	private String abk = "";

	/**
	 * 
	 * @uml.property name="tvzLexem" 
	 */
	private String tvzLexem = "";

	/**
	 * 
	 * @uml.property name="vprLexem" 
	 */
	private String vprLexem = "";

	/**
	 * 
	 * @uml.property name="mkStr" 
	 */
	private String mkStr = "";

	/**
	 * 
	 * @uml.property name="semStr" 
	 */
	private String semStr = "";

	/**
	 * 
	 * @uml.property name="bound" 
	 */
	private boolean bound = false;

	/**
	 * 
	 * @uml.property name="fuge" 
	 */
	private boolean fuge = false;

	/**
	 * 
	 * @uml.property name="tvz" 
	 */
	private boolean tvz = false;

	/**
	 * 
	 * @uml.property name="vpr" 
	 */
	private boolean vpr = false;

	/**
	 * 
	 * @uml.property name="praefix" 
	 */
	private boolean praefix = false;

	/**
	 * 
	 * @uml.property name="suffix"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private boolean suffix = false;

	/**
	 * 
	 * @uml.property name="allomorph" 
	 */
	private Vector allomorph = null;

	/**
	 * 
	 * @uml.property name="wbsubcat"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private Vector wbsubcat = null;


  public abstract boolean isEQEntry();
  public abstract boolean VFMerkmal();
  public abstract boolean RefMerkmal();

	/**
	 * 
	 * @uml.property name="allomorph"
	 */
	// MK:
	public Vector getAllomorph() {
		if (this.allomorph == null)
			return new Vector();
		else
			return this.allomorph;
	}


  public String getFlexive() {
    return "";
  }

  public Vector kategorie( WBRulesDictionary d ) {
//       System.out.println( "FlexivMerkmal-Kategorie!" );
    return new Vector();
  }

  public boolean flexiveClass( String s ) {
    return false;
  }

  public boolean isClass() {
    return false;
  }

  public boolean isAllomorph() {
    return false;
  }

	/**
	 * 
	 * @uml.property name="wbsubcat"
	 */
	public void wbsubcatSetzen(Vector v) {
		wbsubcat = v;
	}


  public Vector wbsubcat() {
    return wbsubcat;
  }

	/**
	 * 
	 * @uml.property name="mkStr"
	 */
	public void mkSetzen(String s) {
		mkStr = s;
	}

  
  public String mk() {
    return mkStr;
  }

	/**
	 * 
	 * @uml.property name="semStr"
	 */
	public void semSetzen(String s) {
		semStr = s;
	}

  
  public String sem() {
    return semStr;
  }

  public void boundSetzen() {
    bound = true;
  }

	/**
	 * 
	 * @uml.property name="bound"
	 */
	public boolean isBound() {
		return bound;
	}


  public void fugeSetzen() {
    fuge = true;
  }

	/**
	 * 
	 * @uml.property name="fuge"
	 */
	public boolean istFuge() {
		return fuge;
	}


  public void praefixSetzen() {
    praefix = true;
  }

	/**
	 * 
	 * @uml.property name="praefix"
	 */
	public boolean istPraefix() {
		return praefix;
	}


  public void suffixSetzen() {
    suffix = true;
  }

	/**
	 * 
	 * @uml.property name="suffix"
	 */
	public boolean istSuffix() {
		return suffix;
	}

	/**
	 * 
	 * @uml.property name="allomorph"
	 */
	public void allomorphSetzen(Vector s) {
		allomorph = s;
	}

	/**
	 * 
	 * @uml.property name="grundform"
	 */
	public void grundformSetzen(String s) {
		grundform = s;
	}

	/**
	 * 
	 * @uml.property name="abk"
	 */
	public void abkSetzen(String s) {
		abk = s;
	}


  public void kategorieSetzen( KategorieMerkmal k ) {
  }

  public void tvzSetzen() {
    tvz = true;
  }

  public void vprSetzen() {
    vpr = true;
  }

	/**
	 * 
	 * @uml.property name="tvzLexem"
	 */
	public void hatTvzSetzen(String s) {
		tvzLexem = s;
	}

	/**
	 * 
	 * @uml.property name="vprLexem"
	 */
	public void hatVprSetzen(String s) {
		vprLexem = s;
	}

	/**
	 * 
	 * @uml.property name="tvz"
	 */
	public boolean isTVZ() {
		return tvz;
	}

	/**
	 * 
	 * @uml.property name="vpr"
	 */
	public boolean isVPR() {
		return vpr;
	}

  public String grundform() {
    return grundform;
  }

  // ADD_KAT: Hilfsfunktion von GET_NEXT_KONSTS
  // Fuegt einer Konstituente die Kategorie zu
  // Hat der Lexikoneintrag der Konstituente das Merkmal MK (Morphemklasse)
  // wird dieser Wert als Kategorie eingesetzt, andernfalls wird abgefragt,
  // welcher Kategorie die Flexionsklasse angehoert

  public Vector getKat( WBRulesDictionary wbrLex ) {
    String msg = _HINT + "getKat: ";
    Vector res = new Vector();

    if( tvz ) res.addElement( "TVZ" );
    else if( vpr ) res.addElement( "VPR" );
    else if( !mkStr.equals( "" ) ) {
//       System.out.println(msg + "MK set" );
      res.addElement( mkStr );
    }
    else {
      if( this.VFMerkmal() ) {
// 	System.out.println(msg + "VF set" );
	if( fuge ) res.addElement( "FUGE" );
	else if( praefix ) res.addElement( "PRAEF" );
	else if( suffix ) res.addElement( "SUF" );
	else if( ((FlexivVFMerkmal)this).kategorieMerkmal() != null ) {
	  res.addElement( ((FlexivVFMerkmal)this).kategorieMerkmal().category() );
	}
	else {
// 	  System.err.println( "Don't know what to do with this: '" + toString()  + "'");
// 	  System.exit( 0 );
	  return null;
	}
      }
      else {
// 	System.out.println(msg + "default" );
	res = kategorie( wbrLex );
      }
    }

    return res;
  }
       
  public String toString() {
    String res = "";

    if( allomorph != null ) res += " Allomorph: " + allomorph.toString();
    if( wbsubcat != null ) res += " WB SubCat: " + wbsubcat.toString();
    if( !grundform.equals( "" ) ) res += " Grundform: " + grundform;
    if( !abk.equals( "" ) ) res += " Abkuerzung: " + abk;
    if( bound ) res += " bound";
    if( fuge ) res += " MK: FUGE";
    if( tvz ) res += " MK: TVZ";
    if( vpr ) res += " MK: VPR";
    if( !tvzLexem.equals( "" ) ) res += " TvzLexem: " + tvzLexem;
    if( !vprLexem.equals( "" ) ) res += " VprLexem: " + vprLexem;
    return res;
  }
}
