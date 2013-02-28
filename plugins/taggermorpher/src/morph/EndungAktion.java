package morph;

/**
 * EndungAktion ist die Oberklasse der Oberflaechenoperationen bei der Bildung
 * von Wortbildungshypothesen.
 * @author BL
*/

public abstract class EndungAktion {
  public abstract String execute( String s );

  public abstract String toString();
}
