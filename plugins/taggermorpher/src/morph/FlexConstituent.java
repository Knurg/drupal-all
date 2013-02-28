package morph;

/**
 * Eine Flexions-Konstituente.
 * @author BL
 */

public class FlexConstituent extends Constituent {

	/**
	 * 
	 * @uml.property name="flexiveInfo"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	FlexivEintrag flexiveInfo;


  public FlexConstituent(){
  }

  public FlexConstituent( int el, String c, FlexivEintrag fi, int wl ) {
    super( el, c, wl );
    flexiveInfo = fi;
  }

  public String getFlexklasse() {
    String stamm;

    stamm = flexiveInfo.holeFlexiv().toLowerCase();

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

    stamm = flexiveInfo.holeFlexiv().toLowerCase();

    if( stamm.charAt( stamm.length()-1 ) == 'd' ||
	stamm.charAt( stamm.length()-1 ) == 't' ) return "SWV2";
    else if(  stamm.charAt( stamm.length()-1 ) == 's' ||
	      stamm.charAt( stamm.length()-1 ) == 'x' ||
	      stamm.charAt( stamm.length()-1 ) == 'z' ) return "SWV3";
    else if( stamm.substring( stamm.length()-2 ).equals( "el" ) )
      return "SWV4";
    else if( stamm.substring( stamm.length()-2 ).equals( "er" ) ||
	     stamm.charAt( stamm.length()-1 ) == 'e' )
      return "SWV5";
    else return "SWV1";
  }

	/**
	 * 
	 * @uml.property name="flexiveInfo"
	 */
	public FlexivEintrag getFlexiveInfo() {
		return flexiveInfo;
	}

  public String toString() {
    return "[start: " + startPos + ", end: " + endPos + ", cat: " + category + ", flex: " + flexiveInfo.holeFlexiv() + "]";
  }
}
