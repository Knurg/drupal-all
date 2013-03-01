// added for intcer

package morph;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;


/**
 * Test-Wrapper f�r Morphologie.
 */
class TestWrapper {
	
	/** Die Morphologie. */
    private MorphModule mm;
    
    /** Konstruktor. */
    public TestWrapper() {
    	this.mm = new MorphModule("");
    	System.out.println( "TestWrapper started. Quit program with 'q'<enter>");
    }

    /** Eingabe einer Oberfl�che �ber Tastatur, Abbruch mit "q". */
    public void run() {
    	String surfaceform = null;
    	BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
    	boolean finished = false;
    	while (!finished) {
    		System.out.print( "Enter surface form: \n" );
    		try { surfaceform = b.readLine(); }
    		catch ( IOException ex) { System.out.println( ex ); }
    		if ("q".equals(surfaceform)) finished = true;
    		else
    		{
    			this.mm.analyze(surfaceform);
    			while (this.mm.moreAnalyses())
    				System.out.println(this.mm.nextAnalysis());
    		}
	    System.out.println( "Analysis finished!");
	    }
    }

    /**
     * Main-Methode.
     * @param args Die Argumente.
     */
    public static void main (String [] args) {
    	new TestWrapper().run();
    }
    
}
