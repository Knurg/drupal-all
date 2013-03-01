package morph;

import java.util.Hashtable;

/**
 * Verzeichnis aller trennbaren Verbzusaetze
 * @author BL
*/

public class TVZDictionary extends MorphDictionary {
  public TVZDictionary() {
    lex = null;
  }

  public TVZDictionary( Hashtable d ) {
    lex = d;
  }

  public boolean zusatz( String s ) {
    return lex.containsKey( s.toUpperCase() );
  }

  public Eintrag getEntry( String k ) {
    return null;
  }
}
