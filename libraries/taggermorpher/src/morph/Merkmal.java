package morph;

//import me.Cloneable;
/**
 * Ein Merkmal.
 * @author BL
 */

abstract class Merkmal implements Cloneable { 

    public Object clone() {
	try{
	    return super.clone();
	} catch(Exception ex) {
	    return null;
	}
    }

}
