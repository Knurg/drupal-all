package morph;

import java.util.Vector;

import de.fau.cs.jill.feature.FeatureStructure;
import de.fau.cs.jill.feature.FeatureValue;

/** 
 * FlexMerkmale enthalten die Information, die in der Datei flexive.txt
 * unter (FLEXION ...) gespeichert ist. Es gibt fuer unterschiedliche
 * Flexionstypen unterschiedliche Unterklassen von
 * FlexMerkmal. Syntaktische features werden mit Hilfe der Klasse
 * de.fau.cs.jill.feature.FeatureStructure repraesentiert.
 * @author BL
*/

public abstract class FlexMerkmal extends Merkmal {
  public boolean RefMerkmal() {
    return false;
  }

  public String getRef() {
    return "";
  }

  public void substRef( Vector v, int pos, Vector entries ) {
  }

  public Vector getFeatures() {
    return null;
  }

  abstract public Vector readingList( FlexiveDictionary lex );
  
  
  public abstract FeatureValue asFeatureValue (FlexiveDictionary lex);
  
}
