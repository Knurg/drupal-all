package morph;

// Java-Importe
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import de.fau.cs.jill.feature.FeatureName;
import de.fau.cs.jill.feature.FeatureStructure;
import de.fau.cs.jill.feature.FeatureValue;
import de.fau.cs.jill.feature.StringValue;
import de.fau.cs.jill.feature.UnpackedFeatureStructure;

/**
 * Das Morphologie-Modul zur Analyse.
 * @author BL
 */

public class MorphModule {
	
	/** Die Ergebnisse der Analyse. */
    private Vector morphResult;
    
    /** Die Enumeration der Ergebnisse. */
    private Enumeration resultEnum;

	// MK: NLGFlexiveDictionary mit zus�tzlicher invertierter Flexionstabelle
//	protected NLGFlexiveDictionary flexivLex;
    // heike
    /** Das Flexivlexikon. */
	public FlexiveDictionary flexivLex;

	// MK: NLGLexemeDictionary mit zus�tzlicher invertierter Lexemtabelle
//	protected NLGLexemeDictionary lexemLex;
	// heike
	/** Das Lexemlexikon. */
	public LexemeDictionary lexemLex;

	/** Dictionary der trennbaren Verbzus�tze. */
	public TVZDictionary tvzLex;

	/** Dictionary mit Wortbildungsregeln. */
	public  WBRulesDictionary wbrLex;

	/** Dictionary mit Endungshypothesen. */
	public  HypoDictionary hypoLex;
	
	/** Flexionsmorphologische Analyse einschalten? */
    private boolean flexOn;
    
    /** Wortbildungsanalyse einschalten? */
    private boolean wbOn;
    
    /** Endungshypothese einschalten? */
    private boolean hypoOn;
    
    /** Minimalhypothese liefern? */
    private boolean minHypo;
    
    /** Alle Wortbildungszerlegungen ausgeben? */
    private boolean alleWbSeg;
    
    /** Tracing einschalten? */
    private boolean trace;
    
    private String path;
    
    /** Konstruktor. */
    public MorphModule(String path) {
    	/*String path = "./";
    	if ( System.getProperty( "os.name" ).toLowerCase().indexOf("windows") != -1 )
    		path = "morph/";*/
		//System.out.println( "reading rules" );
		this.wbrLex = new WBRulesDictionary( path + "wbrules.txt", path + "flexclasses.txt" );
		//System.out.println( "reading flexives" );
//  	this.flexivLex = new NLGFlexiveDictionary( "morph/flexive.txt" ); 
	  	// heike:
		this.flexivLex = new FlexiveDictionary( path + "flexive.txt" ); 
	  	//System.out.println( "reading lexemes" );
//  	this.lexemLex = new NLGLexemeDictionary( "morph/lexeme.txt");
	  	// heike:
	  	this.lexemLex = new LexemeDictionary( path + "lexeme.txt");
	  	//System.out.println( "creating tvz" );
	  	this.tvzLex = new TVZDictionary( this.lexemLex.createTVZTable() );
	    //System.out.println( "reading hypos" );
	    this.hypoLex = new HypoDictionary( path + "endung.txt" ); 
	    this.setFlagsDefault();
	    // this.flagStatus();
    }

//    /** FLAG_STATUS: Zeigt alle FLags an */
//    private void flagStatus() {
//    	System.out.println( "MORPH-MODUL FLAG-STATUS:" );
//    	System.out.println( "*FLEX_ON*    : " + this.flexOn );
//    	System.out.println( "*WB_ON*      : " + this.wbOn );
//    	System.out.println( "*HYPO_ON*    : " + this.hypoOn );
//    	System.out.println( "*MIN_HYPO*   : " + this.minHypo );
//    	System.out.println( "*ALLE_WBSEG* : " + this.alleWbSeg );
//    	System.out.println( "*TRACE*      : " + this.trace );
//    }
       
    /** SET_FLAGS_DEFAULT : Globale Variablen auf Default-Werte setzen */
    private void setFlagsDefault() {
    	this.flexOn = true; // Flexionsmorphologische Analyse eingeschaltet
    	this.wbOn = true; // Wortbildungsanalyse eingeschaltet
    	this.hypoOn = true; // Endungshypothese ausgeschaltet
    	this.minHypo = false; // keine Minimalhypothese
    	this.alleWbSeg = true; // nur Wortbildungszerlegungen mit geringster Konstituentenanzahl ausgeben
    	this.trace = false; // keine TRACE-Ausgabe
    }

    /**
     * SET_FLAG: Setzen eines einzelnen Flags
     * @param flag Das Flag.
     */
    public void setFlag( String flag ) {
    	if ( flag.equals( "*FLEX_ON*" ) ) this.flexOn = true;
    	else if ( flag.equals( "*WB_ON*" ) ) this.wbOn = true;
    	else if ( flag.equals( "*HYPO_ON*" ) ) this.hypoOn = true;
    	else if ( flag.equals( "*MIN_HYPO*" ) ) this.minHypo = true;
    	else if ( flag.equals( "*ALLE_WBSEG*" ) ) this.alleWbSeg = true;
    	else if ( flag.equals( "*TRACE*" ) ) this.trace = true;
    	else System.err.println( "MorphModule: setFlag: Unbekanntes flag:" + flag );
    }

    /**
     * CLEAR_FLAG: Setzen eines einzelnen Flags
     * @param flag Das Flag.
     */
    public void clearFlag( String flag ) {
    	if ( flag.equals( "*FLEX_ON*" ) ) this.flexOn = false;
    	else if ( flag.equals( "*WB_ON*" ) ) this.wbOn = false;
    	else if ( flag.equals( "*HYPO_ON*" ) ) this.hypoOn = false;
    	else if ( flag.equals( "*MIN_HYPO*" ) ) this.minHypo = false;
    	else if ( flag.equals( "*ALLE_WBSEG*" ) ) this.alleWbSeg = false;
    	else if ( flag.equals( "*TRACE*" ) ) this.trace = false;
    	else System.err.println( "MorphModule: setFlag: Unbekanntes flag:" + flag );
    }

    /**
     * �berpr�ft, ob mehr Analysen vorhanden sind.
     * @return True, falls mehr Analysen vorhanden sind, sonst false.
     */
    public boolean moreAnalyses() {
    	if (this.resultEnum != null) return this.resultEnum.hasMoreElements();
		return false;
    }

    /**
     * Liefert die n�chste Analyse.
     * @return Die n�chste Analyse.
     */
    public MorphAnalysis nextAnalysis() {
    	return (MorphAnalysis)this.resultEnum.nextElement();
    }
    

  // ; *********************************************************
  // ; * TOPLEVEL-ANALYSE-FUNKTION ANALYZE                     *
  // ; *********************************************************

  // ;ANALYZE:
  // ; Zentrale Analyse-Funktion: Uebergibt jeweils ein Wort an die 
  // ; morphologische Analyse
  // ; Liefert das Ergebnis der Analyse 

  public void analyze( String wortform ) {
    String lowercase = wortform;
    Vector flexForm = null,
      wortbildung = null,
      hypothese = null;

    if( wortform.length() == 1 ) return;

    if( flexOn ) {
	flexForm = flexAnalyze( lowercase );
	if( ( flexForm != null ) && ( flexForm.size() > 0 ) ) {
	    morphResult = flexForm;
	}
	else {
	    if( wbOn ) {
		wortbildung = wbAnalyse( lowercase );
		if( ( wortbildung != null ) &&
		    ( wortbildung.size() > 0 ) ) {
		    morphResult = wortbildung;
		}
		else {
		    if( hypoOn ) {
			hypothese = endHypothese( lowercase );
			if( ( hypothese != null ) &&
			    ( hypothese.size() > 0 ) ) {
			    morphResult = hypothese;
			}
			else morphResult = null;
		    }
		    else morphResult = null;
		}
	    }
	}
    }
    
    if (morphResult != null) resultEnum = morphResult.elements();
    else resultEnum = new Vector().elements();
  }


    // -------------------------------------------------
    // Analyze a word and return the result as string.
    // -------------------------------------------------

   public StringBuffer analyzeToString ( String wortform ) {
     String lowercase = wortform;
     Vector flexForm = null,
       wortbildung = null,
     hypothese = null;
     int i = 0;
     StringBuffer analyze_string = new StringBuffer();

//     System.err.println("----------- analyzeToString --------------");

     if( wortform.length() == 1 ) { 
//	 System.err.println("------- end of analyzeToString -------");
	 return analyze_string;
     } else if( wortform.indexOf( "-" ) == wortform.length() - 1 ) {
       analyze_string.append("new Pair( ");
       analyze_string.append(wortform);
       analyze_string.append(" MorphModule.KOMPOSITUMTEIL );");
     }


     if( flexOn ) {
//	 System.err.println("-- Calling flexAnalyze  --------------");
	 flexForm = flexAnalyze( lowercase );
//	 System.err.println("-- Calling flexAnalyze done ----------");
	
 	if( ( flexForm != null ) && 
 	    ( flexForm.size() > 0 ) ) {

 	    analyze_string.append("RESULTAT: ");
	    analyze_string.append(flexForm);
	    System.err.println(analyze_string);
 	}
 	else {
 	    if( wbOn ) {
//		System.err.println("-- Calling wbAnalyze  --------------");
 		wortbildung = wbAnalyse( lowercase );
//		System.err.println("-- Calling wbAnalyze  done ---------");
 		if( ( wortbildung != null ) &&
 		    ( wortbildung.size() > 0 ) ) {
 		    analyze_string.append("RESULTAT: ");
		    analyze_string.append(wortbildung);
		    System.err.println(analyze_string);
 		}
 		else {
 		    if( hypoOn ) {
//			System.err.println("-- Calling endHypothese --------");
 			hypothese = endHypothese( lowercase );
//			System.err.println("-- Calling endHypothese done ---");
 			if( ( hypothese != null ) &&
 			    ( hypothese.size() > 0 ) ) {
 			    analyze_string.append("RESULTAT: ");
			    analyze_string.append(hypothese);
//     System.err.println(analyze_string);
 			}
 			// else return minimalHypo( wortform );
 		    }
 		    // else return minimalHypo( wortform );
 		}
 	    }
 	}
     }
//     System.err.println("------- end of analyzeToString -------");
     return (analyze_string);
   }


  // ; *********************************************************
  // ; * HAUPTFUNKTIONEN DER FLEXIONSMORPHOLOGISCHEN ANALYSE:  *
  // ; *********************************************************
  // FLEX_ANALYSE:
  // Erzeugt eine Liste moeglicher Stamm-Endungszerlegungen
  // einer Wortform durch Stamm- und Flexivlexikon-Abfrage, und gibt 
  // diese Liste weiter an die Funktion pruefe_zerlegungen.
  // Scheitert die Stamm-Endungszerlegung, wird #f zurueckgegeben.

    private Vector flexAnalyze( String wort ) {
	
	Vector v;
	
	int zaehler;
	String stamm, endung, temp;
	FlexivEintrag endT, partEndT;
	LexemEintrag stammT, partStammT;
	boolean praefixGE;
	Vector zerlegungen;
	    
	zerlegungen = new Vector();
	zaehler = 0;
	praefixGE = wort.startsWith( "ge" ) && ( wort.length() > 5 );

	for( zaehler = 0; zaehler < wort.length(); zaehler++ ){

	    stamm = wort.substring( 0, wort.length() - zaehler );
	    endung = wort.substring( wort.length() - zaehler );
	    // Anweisungsteil fuer Schleifendurchgaenge
	    
	    endT = ( FlexivEintrag )flexivLex.getEntry( endung );
	    if( endT != null ) {
		stammT = ( LexemEintrag )lexemLex.getEntry( stamm );
	    }
	    else stammT = null;
	    
	    if( praefixGE ) {
		partEndT = 
		    ( FlexivEintrag )flexivLex.getEntry( "ge-" + endung );
		if( partEndT != null ) {
		    partStammT = 
			( LexemEintrag )lexemLex.getEntry(stamm.substring( 2 ) );
		}
		else partStammT = null;
	    }
	    else {
		partEndT = null;
		partStammT = null;
	    }
	    
	    if( trace ) {
		System.out.println( "ANALYSE stamm: " + stamm );
		System.out.println( "ANALYSE endung: " + endung );
		System.out.println( "ANALYSE praefix_ge: " + praefixGE );
	    }
	    
	    if( ( endT != null ) && 
		( stammT != null ) ){
		zerlegungen.addElement( new Pair( stammT, endT ) );
		//System.out.println( "added Zerlegung" );
	    }
	    
	    if( praefixGE && 
		( partEndT != null ) &&
		( partStammT != null ) ) {
		zerlegungen.addElement( new Pair( partStammT, partEndT ) );
	    }
		
	}
	    
	// ;Anweisung nach Schleifenende
	if( zerlegungen.size()  == 0 ) {
	    return null;
	} else {
	    return pruefeZerlegungen( zerlegungen.elements(), wort );
	}
    }
    
    
  // PRUEFE_ZERLEGUNGEN: Die Liste der Stamm-Endungszerlegungen wird ab-
  // gearbeitet. Jede Zerlegung wird an die Funktion BESTIMME uebergeben, die
  // den Eintrag generiert, wenn Stamm und Endung kompatibel sind.
  // Enthaelt ein Stammeintrag mehrere Lesarten, wird jede Flexionsklasse
  // mit Stamm und Endung an BESTIMME uebergeben. (=do innerhalb von cond)
  // Liefert BESTIMME fuer alle moeglichen Zerlegungen #f zurueck, ist die
  // Bestimmung der Wortform gescheitert.

  private Vector pruefeZerlegungen ( Enumeration zerlegungen,
				     String wortform ) {
    boolean wortformBestimmt = false;
    Pair zerlegung; // Paar bestehend aus Stamm- und Endungsinfo
    LexemEintrag lexentry; // Eintrag im Lexemlexikon
    LexemEintrag reflexentry;
    FlexivMerkmal lesart; // Element von lexentry
    FlexivMerkmal reflesart;
    String stamm; // Zeichenkette des Stamms
    String refstamm;
    FlexivEintrag endinfo; // Eintrag im Flexivlexion
    Vector resultat = new Vector(), res;
    Enumeration lesarten, reflesarten, tmp;

    if( zerlegungen != null ) {
      while( zerlegungen.hasMoreElements() ) {
	if( trace ) {
	  System.out.println( "pruefeZerlegungen: " + zerlegungen );
	}
	zerlegung = ( Pair )zerlegungen.nextElement();
	lexentry = ( LexemEintrag )zerlegung.getLeft();
	stamm = lexentry.holeLexem();
	endinfo = ( FlexivEintrag )zerlegung.getRight();
	
	if( lexentry != null ) {
	  // Schleifeninitialisierung fuer Analyse aller gefundenen
	  // Lesarten
	  
	  lesarten = lexentry.holeDaten().elements();
	  
	  while( lesarten.hasMoreElements() ) {
	    lesart = ( FlexivMerkmal )lesarten.nextElement();

	    if( trace ) {
	      System.out.println( "pruefeZerlegungen " + lesart );
	    }

	    // Lesart ist Vollform
	    if( lesart.VFMerkmal() &&
		!lesart.isBound() && 
		stamm.equals( wortform.toUpperCase() ) ) {
	      resultat.addElement( new MorphAnalysis( wortform,
		            ((FlexivVFMerkmal)lesart).kategorieMerkmal() ) );
	    }
	    // Lesart ist weder Vollform noch Referenz (Normalfall)
	    else if( !lesart.VFMerkmal() &&
		     !lesart.RefMerkmal() &&
		     !lesart.isBound() ) {
	      // res soll ein Vector von Kategoriemerkmalen sein.

	      res = bestimme( stamm, endinfo, lesart, wortform );
	      tmp = res.elements();

	      while( tmp.hasMoreElements() ) {
		resultat.addElement( new MorphAnalysis( wortform, 
						 (KategorieMerkmal)tmp.nextElement() ) );
	      }
	    }
	    // Lesart ist Referenz (eines pr�figierten Verbs auf das Verb)
	    else if( lesart.RefMerkmal() ) {

		// L�se Referenz auf
	        reflexentry = ( LexemEintrag )lexemLex.getEntry( (( FlexivRefMerkmal )lesart).getRef() );
		refstamm = reflexentry.holeLexem();

		// reflexentry wird normalerweise ein Verb
		// ohne weitere Referenzen sein.
		reflesarten = reflexentry.holeDaten().elements();
		while( reflesarten.hasMoreElements() ) {
		    reflesart = ( FlexivMerkmal ) reflesarten.nextElement();

		    if( trace ) {
			System.out.println( "pruefeRefZerlegungen " + reflesart );
		    }
		    
		    if( !reflesart.VFMerkmal() &&
			!reflesart.RefMerkmal() &&
			!reflesart.isBound() ) {
			// res soll ein Vector von Kategoriemerkmalen sein.

			res = bestimme( stamm, endinfo, reflesart, wortform );
			tmp = res.elements();

			while( tmp.hasMoreElements() ) {
			    resultat.addElement( new MorphAnalysis( wortform, 
				       (KategorieMerkmal)tmp.nextElement() ) );
			}
		    }
		}

	    }
	    else  {
	      // do nothing
	      if ( trace ) {  
	          System.err.println( "not processed: " + lesart );
	          System.err.println( "pruefeZerlegungen: processing not implemented." );
	      }
	    }
	  }
	}
      }
      return resultat;
    }
    else return null;
  }

  // BESTIMME
  // Falls Stamm und Flexiv kompatibel sind, wird die Outputliste generiert,
  // wobei Verweise im Flexivmerkmalsblock aufgeloest werden. Ausserdem wird
  // die Grundform generiert und alle im Lexikoneintrag kodierten Merkmale
  // mit ausgegeben. (Bei Allomorphstaemmen werden die lexikalischen Merkmale
  // des Grundformstammes ausgegeben)

  private Vector bestimme( String stamm,
			   FlexivEintrag flexiv,
			   FlexivMerkmal flexklasse,
			   String wortform ) {
    Vector features;
    String gfstamm, grundform;

    if( trace ) {
      System.out.println( "BESTIMME Stamm: " + stamm );
      System.out.println( "BESTIMME Wortform: " + wortform );
      System.out.println( "BESTIMME Flexklasse: " + flexklasse );
    }

    features = flexklassSuche( flexklasse.getFlexive(), flexiv.holeDaten() );

    //System.out.println( "BESTIMME tornato" );

    if( features.size() > 0 ) { // wenn Wortstamm und Flexiv kombinierbar
      // Output generieren:

      // zuerst aufloesen der FlexRefMerkmal_e
      int i;
      KategorieMerkmal f;

      if( trace ) {
          System.out.println( "BESTIMME: features abarbeiten" );
      }

      i = 0;
      while( i < features.size() ) {
	if( trace ) {
	  System.out.println( "feature[i]: " + features.elementAt( i ) );
	}
	f = (KategorieMerkmal)features.elementAt( i );

	if( f.RefMerkmal() ) {
	  // Referenz aufloesen
	  if( trace ) {
	    System.out.println( "BESTIMME RefMerkmal" );
	  }

	  f.substRef( features,
		      i,
		      flexivLex.getEntry( f.getRef() ).holeDaten() );
	}
	else {
	  if( trace ) {
	    System.out.println( "BESTIMME kein RefMerkmal" );
	  }
	}

	i++;
      }

      // Stamm der Grundform bestimmen
      if( !flexklasse.grundform().equals( "" ) ) {
	gfstamm = flexklasse.grundform();
      }
      else {
	gfstamm = stamm;
      }

      // Grundform komplettieren
      if( flexklasse.flexiveClass( "SWV4" ) ||
	  flexklasse.flexiveClass( "SWV4_1SG" ) ||
	  flexklasse.flexiveClass( "SWV5" ) ||
	  flexklasse.flexiveClass( "SWZ4" ) ||
	  flexklasse.flexiveClass( "SWZ4_1SG" ) ||
	  flexklasse.flexiveClass( "SWZ5" ) ) {
	grundform = gfstamm + "N";
      }
      else if( ((FlexivklasseMerkmal)flexklasse).verb() ) {
	grundform = gfstamm + "EN";
      }
      else grundform = gfstamm;

      if( trace ) {
          System.out.println( "BESTIMME grundform: " + grundform );
      }

      i = 0;
      while( i < features.size() ) {
	if( trace ) {
	  System.out.println( "i: " + i );
	}
	f = (KategorieMerkmal)features.elementAt( i );
	f.setBaseForm( grundform );

	i++;
      }

      if( trace ) {
	System.out.println( "ende" );
      }
      return features;
    }
    else return new Vector();
  }


  // ********************************************************
  // * HILFSFUNKTIONEN DER FLEXIONSMORPHOLOGISCHEN ANALYSE: *
  // ********************************************************
  
  // FLEXKLASS_SUCHE: Hilfsfunktion von BESTIMME
  // Prueft, ob eine Kategorie und ein Flexiv kompatibel sind.
  // Wenn ja, wird die entsprechende Flexionsinformation zurueckgegeben,
  // ansonsten null.
      
  // private Vector flexklassSuche( FlexivMerkmal kat, Vector flexinfo ) {
  private Vector flexklassSuche( String kat, Vector flexinfo ) {
    // flexinfo enthaelt KlassenMerkmale aus dem FlexivEintrag
    Enumeration enum1, klassen;
    KlassenMerkmale feature;
    Vector treffer = new Vector(), k;

    //System.out.println( "FLEXKLASS_SUCHE  kat: " + kat );
    
    enum1 = flexinfo.elements();

    while( enum1.hasMoreElements() ) {
      feature = (KlassenMerkmale)enum1.nextElement();
      k = feature.klassenVon( kat );
      klassen = k.elements();

      // klassen enthaelt die FlexivMerkmale fuer jede Klasse

      while( klassen.hasMoreElements() ) {
	treffer.addElement( klassen.nextElement() );
      }
    }

    return treffer;
  }

  // ; *****************************************
  // ; * WORTBILDUNGSFUNKTIONEN:               *
  // ; *****************************************
  // ; Die Funktionen zur Zerlegung komplexer Woerter
  // ; ----------------------------------------------

  // WB-ANALYSE
  // Wenn globale Variable *ALLE_WBSEG* im Eingangsmenue auf #t gesetzt wurde,
  // werden alle gefundenen Segmentierungen ausgegeben; andernfalls gibt die
  // heuristische Filterfunktion FILTWB nur die Segmentierung(en) mit der
  // geringsten Wortkonstituentenanzahl zurueck.

  private Vector wbAnalyse( String wortform ) {

    if (wortform == null) {
	System.err.println("ERROR: wbAnalyse : wortform == null");
	return(null);
    }


    if( alleWbSeg ) {
      return wbAnal( wortform );
    }
    else {
      System.err.println( "filtwb not impl" );
      return new Vector(); // filtwb( wbAnal( wortform ) );
    }
  }

  // WB-ANAL
  // Top-Level-Wortbildungsfunktion
  // Uebergibt das zu analysierende Wort an die Funktion PARSE_WORD
  // Ist PARSE_WORD erfolgreich, werden die Ausgaben generiert (Uebergabe
  // des Parse-Ergebnisses an die Funktion MAKE_WB_OUT)

  private Vector wbAnal( String wortform ) {
    Vector wb, res = new Vector(), wbRes;
    KategorieMerkmal wbOut;
    Enumeration enum1, readings;
    Vector lesart;

    if (wortform == null) {
	System.err.println("ERROR: wbAnal : wortform == null");
	return(null);
    }


    if( trace ) {
      System.out.println( "wbAnal: " + wortform );
    }

    wb = parseWord( wortform );
    if( wb != null ) {
      // Analyse erfolgreich
      // wb ist Vector von Pfaden (repraesentiert als Vectoren), die 
      // Zerlegungshypothesen enthalten.

      enum1 = wb.elements();
      while( enum1.hasMoreElements() ) {
	lesart = (Vector)enum1.nextElement();
	if( trace ) {
	  System.out.println( "wbAnal result: " + lesart );
	}
	wbRes = makeWbOut( lesart, wortform );
	if ( wbRes != null ){
	  readings = wbRes.elements();
	  while( readings.hasMoreElements() ) {
	    wbOut = (KategorieMerkmal)readings.nextElement();
	    //System.err.println( "wbOut:" + wbOut );
	    res.addElement( wbOut );
	  }
	}
      }

      if( res.size() > 0 ) return insertWortform( res, wortform );
      else return new Vector();
    }
    else {
      return new Vector();
    }
  }

  // INSERT_WORTFORM : Hilfsfunktion von WB-ANAL

  private Vector insertWortform( Vector res, String wf ) {
    Vector lOut = new Vector();
    Enumeration enum1;
    KategorieMerkmal lesart;


    if ((res == null) ||
	(wf  == null)) {
	System.err.println("ERROR: insertWortform : one argument is null");
	return(null);
    }

    if( trace ) {
      System.out.println( "INSERT_WORTFORM  res: "  + res );
      System.out.println( "INSERT_WORTFORM  wf: " + wf );
    }

    enum1 = res.elements();

    while( enum1.hasMoreElements() ) {
      lesart = (KategorieMerkmal)enum1.nextElement();

      lOut.addElement( new MorphAnalysis( wf, lesart ) );
    }

    return lOut;
  }

  // ***********************************************************
  // * Hilfsfunktionen
  // ***********************************************************
  // ZIFFER_STRINGP:
  // Hilfsfunktion von Analyze_sentence_words
  // Liefert #t, wenn ein String ausschliesslich aus Integern besteht, sonst #f

  private boolean zifferStringp( String str ) {
    int pos;

    if (str == null) {
	System.err.println("ERROR: zifferStringp : argument is null");
	return (false);
    }

    if( trace ) {
      System.out.println( "zifferStringp: " + str + " " + str.length() );
    }
    pos = 0;
    while( pos < str.length() ) {
      if( !Character.isDigit( str.charAt( pos ) ) ) return false;
      pos ++;
    }

    return true;
  }

  // START_OR_END_FUGE: Hilfsfunktion von GET_NEXT_KONSTS
  // Fugenmorpheme am Wortanfang oder -ende werden nicht zugelassen
     
  private boolean startOrEndFuge( String lexem,
				  FlexivMerkmal konst,
				  int start,
				  int ende,
				  int wortlaenge ) {
    if( !lexem.equals( "-" ) ) {
      return konst.istFuge() &&
	( ( start == 0 ) || ( wortlaenge == ende ) ||
	  ( ende == wortlaenge - 1 ) || ( ende == wortlaenge - 2 ) );
    }
    else return false;
  }
       
  // GET_NEXT_KONSTS: Hilfsfunktion von PARSE_WORD
  // Expandiert eine Wortform von startindex aus und gibt eine Liste moeglicher
  // Fortsetzungskonstituenten bzw. #f zurueck
  // z.B. (get_next_konsts "GASTANK" 0)
  //  ==> ((0 (N) ("GAS" ..) 3) (0 (N) ("GAST" ..) 4))

  private Vector getNextKonsts( String wortform, int startindex ) {
    int start = startindex,
      ende = start,
      laenge = wortform.length();
    boolean numBegin;
    String konstString;
    Vector lesarten = new Vector(), konstList, path;
    LexemEintrag konst;
    FlexivMerkmal lesart;
    Enumeration enum1;

    //konstString = wortform.substring( start, ende );
    // numBegin = zifferStringp( konstString );

//     if( trace ) {
//       System.out.println( "get next konsts: " + wortform + " " + startindex + " " + laenge );
//       System.out.println( "GET_NEXT_KONSTS ende: " + ende );
//       System.out.println( "GET_NEXT_KONSTS numbegin: " + numBegin );
//       System.out.println( "GET_NEXT_KONSTS konst?: " + konstString );
//     }
    
    konstString = "";
    while( ( laenge > ende ) &&
	   !Character.isDigit( wortform.charAt( ende ) ) ) {
      konstString += wortform.charAt( ende );
      konst = (LexemEintrag)lexemLex.getEntry( konstString );

      if( trace ) {
	System.out.println( "GET_NEXT_KONSTS ende: " + ende );
	System.out.println( "GET_NEXT_KONSTS konst?: " + konstString );
	System.out.println( "GET_NEXT_KONSTS konst: " + ( konst == null ) );
      }
      
      if( konst != null ) {
	enum1 = konst.holeDaten().elements();
	while( enum1.hasMoreElements() ) {
	  lesart = (FlexivMerkmal)enum1.nextElement();
	  if( trace ) {
	    System.out.println( "LESART: " + lesart );
	  }
	  if( !startOrEndFuge( konst.holeLexem(),
			       lesart,
			       start,
			       ende,
			       laenge ) ) {
	    Vector categories = lesart.getKat( wbrLex );
	    String category;
	    Enumeration catEnum;

	    catEnum = categories.elements();
	    while( catEnum.hasMoreElements() ) {
	      category = ( String )catEnum.nextElement();
	      if( trace ) {
		System.out.println( "GET_NEXT_KONSTS cat: " + category );
	      }
	      path = new Vector();
	      path.addElement( new LexConstituent( start,
					    category,
					    new Pair( konst, lesart ),
					    ende + 1 ) );
	      
	      if( trace ) {
		System.out.println( "new LC: " + path );
	      }
	      lesarten.addElement( path );
	    }
	  }
	}
      }
      ende ++;
    }

    if( trace ) {
      System.out.println( "getNextKonsts returns with " + lesarten );
    }
    if( laenge == ende ) return lesarten;
    else return expandNum( wortform, startindex, konstString, laenge, lesarten );
  }

  // EXPAND_NUM: Hilfsfunktion von GET_NEXT_KONSTS
  // ermittelt Integer-Konstituenten in Wortbildungen

  private Vector expandNum( String wortform,
			    int startindex,
			    String numstring,
			    int laenge, 
			    Vector lesarten) {
    Vector res = new Vector();
    int endindex = startindex + 2;
    String str;
    boolean weiter;

//    if( laenge >= endindex ) str = wortform.substring( startindex, endindex );
//    else str = "";

//    weiter = zifferStringp( str );

//    while( weiter ) {
//      numstring = str;
//      endindex ++;
//      if( laenge >= endindex )
//	str = wortform.substring( startindex, endindex );
//      weiter = zifferStringp( str );
//    }

//    res.addElement( new LexConstituent( startindex,
//			      "NUM",
//			      new Pair( new LexemEintrag( numstring,
//							  null ),
//					"NUM" ),
//			      endindex - 1 ) );
    
//    lesarten.addElement (res);
    return lesarten;
  }

  // MOEGL_FLEX: Hilfsfunktion von PARSE_WORD
  // Generiert eine Liste potentiell moeglicher Flexionsendungen eines Wortes
    
  private Vector moeglFlex( String wort ) {
    int zaehler = 0;
    String endung;
    FlexivEintrag endT;
    Vector flexList = new Vector();

    while( zaehler < ( wort.length() - 2 ) ) {
      endung = wort.substring( wort.length() - zaehler );
      endT = ( FlexivEintrag )flexivLex.getEntry( endung );
      if( endT != null ) {
	flexList.addElement( new FlexConstituent( wort.length() - zaehler,
					   "FL",
					   endT,
					   wort.length() ) );
	if( trace ) {
	  System.out.println( "MOEGL_FLEX zaehler: " + zaehler );
	  System.out.println( "MOEGL_FLEX endung: " + endung );
	}
      }

      zaehler ++;
    }

    if( trace ) System.out.println( "MOEGL_FLEX flexlist:" + flexList);
    return flexList;
  }  

  // PARSE_WORD
  // Chartbasiertes, depth-first Wortparsing
  // unter Verwendung eines Uebergangsnetzwerkes (*wb-rules*)
  // Gueltige Pfade durch die Wortform werden auf der Variablen lesarten
  // gesammelt. z.B.: (parse_word "GASTANK")
  //   ==> (((0 (N) ("GAS" (N3)) 3)
  //         (3 (N) ("TANK" (M4)) 7)
  //         (7 (FL) ("" (..INFO..)) 7)
  //       ))

  private Vector parseWord( String wortform ) {
    Vector lesarten = new Vector(), flexive, expand, agenda, currPath, v;
    Hashtable chart = new Hashtable();
    int currPos, endPos, agendaPos, pathEnd;
    Constituent agendaTop, nextKonst, curr;
    FlexConstituent seg;
    String aktuelleKat;
    boolean uebergangMoegl, ende;
    WBRuleTransition uebergang, trans;
    Enumeration enum1;

    if( trace ) {
      System.out.println( "parse word: " + wortform );
    }

    agenda = getNextKonsts( wortform, 0 );
    if( agenda.size() > 0 ) flexive = moeglFlex( wortform );
    else flexive = new Vector();

    agendaPos = 0;

    endPos = wortform.length();

    if( trace ) {
      System.out.println( "process agenda: " + agenda.size() );
    }

    while( agenda.size() > 0 ) {
      currPath = (Vector)agenda.elementAt( agendaPos );
      agenda.removeElementAt( agendaPos );
      agendaTop = (Constituent)currPath.lastElement();
	  
      if( trace ) {
	System.out.println( "PARSE_WORD processing new agenda element: " + agendaTop );
      }
	  
      pathEnd = agendaTop.getEnd();
      aktuelleKat = agendaTop.getCategory();
      uebergangMoegl = wbrLex.moeglUebergaenge( aktuelleKat );
      if( uebergangMoegl ) {
	if( trace ) {
	  System.out.println( "uebergang moeglich" );
	}

	if( chart.containsKey( new Integer( pathEnd ) ) ) {
	  if( trace ) {
	    System.out.println( "loop up chart" );
	  }
	  expand = ( Vector )chart.get( new Integer( pathEnd ) );
	}
	else expand = getNextKonsts( wortform, pathEnd );

	enum1 = flexive.elements();
	while( enum1.hasMoreElements() ) {
	  seg = (FlexConstituent)enum1.nextElement();
	  if( trace ) {
	    System.out.println( "PARSE WORD internal loop: " + seg.getStart() + " " + pathEnd );
	  }
	  if( seg.getStart() == pathEnd && segtest(expand, seg)) {
	    v = new Vector();
	    v.addElement( seg );
	    expand.addElement( v );

	    if( trace ) {
	      System.out.println( "PARSE WORD add flexive info: " + seg );
	    }
	  }
	}
	
	if( expand.size() > 0 ) {
	  if( trace ) {
	    System.out.println( "PARSE WORD: " + expand.size() );
	  }
	  v = ( Vector )expand.elementAt( 0 );
	  nextKonst = (Constituent)v.elementAt( 0 );
	  uebergang = wbrLex.uebergang( aktuelleKat, nextKonst.getCategory() );
	}
	else {
	  uebergang = null;
	  nextKonst = null;
	}
      }
      else {
	expand = new Vector();
	uebergang = null;
	nextKonst = null;
	if( trace ) {
	  System.out.println( "kein Uebergang moeglich: " + aktuelleKat );
	}
      }
	
      if( trace ) {
	System.out.println( "oben computing ende: " );
	System.out.println( "oben PARSE_WORD path-end-index: " + pathEnd );
	System.out.println( "oben PARSE_WORD uebergang: " + uebergang );
	System.out.println( "endPos: " + endPos + " pathEnd: "  + pathEnd + " aktuelleKat: " + aktuelleKat );
      }
      ende = ( ( aktuelleKat.equals( "FL" ) && ( endPos == pathEnd ) ) ||
	       ( endPos == pathEnd ) &&
	       wortform.equals( agendaTop.getWord() ) &&
	       agendaTop.getFlexive().VFMerkmal() );
  
      while( !ende &&
	     ( uebergangMoegl || aktuelleKat.equals( "FL" ) ) ) {
	// Wenn an einem Punkt mehrere Fortsetzungsmoeglichkeiten bestehen,
	// werden ausser dem ersten Uebergang alle weiteren (sofern sie
	// gueltige Fortsetzungen des bisherigen Pfades sind) als Teilpfade
	// der Agenda zugefuegt (do)
	    
	if( trace ) {
	  System.out.println( "PARSE WORD oben path-end-index: " + pathEnd );
	  System.out.println( "PARSE WORD oben aktuelle_kat: " + aktuelleKat );
	  System.out.println( "PARSE WORD oben uebergang: " + uebergang );
	  System.out.println( "PARSE WORD oben expand: " + expand.size() );
	}
	    
	if( expand.size() > 1 ) {
	  expand.removeElementAt( 0 );
	  enum1 = expand.elements();
	  while( enum1.hasMoreElements() ) {
	    v = ( Vector )enum1.nextElement();
	    curr = ( Constituent )v.elementAt( 0 );
	    trans = wbrLex.uebergang( aktuelleKat, curr.getCategory() );
	    //System.out.println( "PARSE WORd expansion test" );
	    if( ( trans != null ) &&
		trans.evalTests( agendaTop, curr, endPos ) ) {
	      Vector clone = (Vector)currPath.clone();
	      clone.addElement( curr );
	      if( trace ) {
		System.out.println( "updating agenda: " + curr );
		System.out.println( "updating agenda: " + clone );
	      }
	      agenda.insertElementAt( clone, 0 );
	    }
	  }

	  if( trace ) {
	    System.out.println( "NEW AGENDA: " + agenda );
	  }
	}
	    
	// Ist die erste Fortsetzungsmoeglichkeit gueltig, wird der Pfad
	// weiter expandiert, ansonsten wird er terminiert
	    
	//System.out.println( "PARSE WORd hier: " + uebergang );
	if( ( uebergang != null ) &&
	    uebergang.evalTests( agendaTop, nextKonst, endPos ) ) {
	  // Pfad weiter expandieren
	      
	  currPath.addElement( nextKonst );
	  agendaTop = nextKonst;
	  pathEnd = nextKonst.getEnd();
	  aktuelleKat = nextKonst.getCategory();
	  uebergangMoegl = wbrLex.moeglUebergaenge( aktuelleKat );
	  if( chart.containsKey( new Integer( pathEnd ) ) == false ) {
	    chart.put( new Integer( pathEnd ),
		       getNextKonsts( wortform, pathEnd ) );
	  }
	  if( trace ) {
	    System.out.println( "PARSE WORD chart: " + chart.get( new Integer( pathEnd ) ) );
	  }
	  expand = ( Vector )chart.get( new Integer( pathEnd ) );
	  if( expand == null ) expand = new Vector();
	   
	  enum1 = flexive.elements();
	  while( enum1.hasMoreElements() ) {
	    seg = (FlexConstituent)enum1.nextElement();
	    if( trace ) {
	      System.out.println( "PARSE WORD inner internal loop: " + seg.getStart() + " " + pathEnd );
	    }
	    if( seg.getStart() == pathEnd && segtest(expand, seg)) {
	      v = new Vector();
	      v.addElement( seg );
	      expand.addElement( v );

	      if( trace ) {
		System.out.println( "PARSE WORD add flexive info: " + seg );
	      }
	    }
	  }
	      
	  if( expand.size() > 0 ) {
	    v = ( Vector )expand.elementAt( 0 );
	    nextKonst = (Constituent)v.elementAt( 0 );
	    if( trace ) {
	      System.out.println( "PARSE WORD nextKonst: " + nextKonst );
	      System.out.println( "PARSE WORD aktuelleKat: " + aktuelleKat );
	      System.out.println( "PARSE WORD Kategorie: " + nextKonst.getCategory() );
	    }
	    uebergang = wbrLex.uebergang( aktuelleKat, nextKonst.getCategory() );
	  }
	  else {
	    nextKonst = null;
	    uebergang = null;
	  }

	  if( trace ) {
	  }
	}
	else {
	  // Pfad terminieren
	      
	  uebergangMoegl = false;
	  aktuelleKat = "";
	}
	    
	if( trace ) {
	  System.out.println( "computing ende: " );
	  System.out.println( "PARSE_WORD path-end-index: " + pathEnd );
	  System.out.println( "PARSE_WORD uebergang: " + uebergang );
	  System.out.println( "endPos: " + endPos + " pathEnd: "  + pathEnd + " aktuelleKat: " + aktuelleKat );
	}
	ende = ( ( aktuelleKat.equals( "FL" ) && ( endPos == pathEnd ) ) ||
		 ( ( endPos == pathEnd ) &&
		   wortform.equals( agendaTop.getWord() ) &&
		   agendaTop.getFlexive().VFMerkmal() ) );

      }
      if( trace ) {
	System.out.println( "PARSE WORD: items left on agenda: " + agenda.size() );
      }
      if( ende ) {
	if( trace ) {
	  System.out.println( "adding new reading: " + currPath );
	}
	lesarten.addElement( currPath );
      }
    } // Ende der while-Schleife
    
    //System.out.println( "PARSE WORD result: " + lesarten );
    return lesarten;
  }

  private boolean segtest ( Vector expand, Constituent seg ){

      Enumeration enum1 = expand.elements();
      Vector v;

      while(enum1.hasMoreElements()){
	  v = (Vector) enum1.nextElement();
	  if (v.contains( seg )) return false;
      }   
      
      return true;

  }


  
  // MAKE_WB_OUT: Hilfsfunktion von WB-ANAL
  // Generiert fuer jeweils ein Segmentierungs-Ergebnis von PARSE_WORD
  // sofern es gueltig ist, eine
  // Ausgabe der Form ((KATEGORIE) [(GRUNDFORM)] (MERKMALE) [(KONSTITUENTEN)])
       
  private Vector makeWbOut( Vector lesart, String wortform ) {
    String katEinsVorFlex = "", katZweiVorFlex = "", grundform = "";
    FlexivEintrag lastLexEnt;
    FlexivMerkmal lastFeat;
    KategorieMerkmal lastCatFeat;
    Constituent konstNull, konstEins, konstZwei, c;
    Enumeration enum1;
    Vector lesartenres, flexFeats;
    int i;

    // Check arguments
    if ((lesart == null) ||
	(wortform == null)) {
	System.err.println("ERROR: makeWbOut, at least one argument is null");
	return(null);
    }
			    
    if( trace ) {
      System.out.println( "MAKE_WB_OUT  lesart: " + lesart );
      System.out.println( "MAKE_WB_OUT  wortform: " + wortform );
    }

    i = lesart.size() - 1;

    //System.out.println( "# readings: " + i );

    konstNull = ( Constituent )lesart.elementAt( i );
    if( i >= 1 ) {
      konstEins = ( Constituent )lesart.elementAt( i - 1 );
      katEinsVorFlex = konstEins.getCategory(); // Kategorie vom vorletzten
    }
    else {
      konstEins = null;
      katEinsVorFlex = "";
    }

    if( i >= 2 ) {
      konstZwei = ( Constituent )lesart.elementAt( i - 2 );
      katZweiVorFlex = konstZwei.getCategory(); // Kategorie vom vorvorletzten
    }
    else {
      konstZwei = null;
      katZweiVorFlex = "";
    }

    if( lesart.size() == 1 ) {
      lastFeat = konstNull.getFlexive();
      if( lastFeat.VFMerkmal() && // Vollform
	  lastFeat.isBound() ) { // gebunden
	return null; // scheitert
      }
      else if( ( lesart.size() == 1 ) &&
	       lastFeat.VFMerkmal() ) { // Vollform
	lesartenres = new Vector();
	lesartenres.addElement( ((FlexivVFMerkmal)lastFeat).kategorieMerkmal() );
	return lesartenres;
      }
    }
    else if( katEinsVorFlex.equals( "V" ) || // Verbwortbildung
	     katEinsVorFlex.equals( "ALLOV" ) ||
	     ( katEinsVorFlex.equals( "SUF" ) &&
	       konstEins.getFlexive().getFlexive().equals( "SWZ1" ) ) || // Verbsuffix
	     katZweiVorFlex.equals( "VPR" ) ) { // Headpraefix
      return makeVerbWbOut( lesart, wortform );
    }
    else { // sonstige Wortbildung
	//System.out.println( "sonstige Wortbildung" );

	flexFeats = flexklassSuche( konstEins.getFlexive().getFlexive(),
				    konstNull.getFlexiveInfo().holeDaten() );
      // flexFeats enthaelt FlexivMerkmale

      if( flexFeats != null ) {
	enum1 = flexFeats.elements();
	lesartenres = new Vector();
       
	while( enum1.hasMoreElements() ) {

 	  lastCatFeat = (KategorieMerkmal)
	      ((KategorieMerkmal)enum1.nextElement()).clone();
	  if( lastCatFeat.RefMerkmal() ) {
	    System.err.println( "make WB Out: RefMerkmal not implemented" );
	    // System.exit( 0 );
	    return( null );
	  }

 	  lastCatFeat.setConstInfo( lesart );
	  for( i = 0 ; i < lesart.size() - 1; i++ ) {
	      c = (Constituent)lesart.elementAt(i);
	      grundform += c.getWord();
	  }
	  lastCatFeat.setBaseForm( grundform );
 	  lesartenres.addElement( lastCatFeat );
	}

	return lesartenres;
      }
      else return null;
    }

    if (trace) {
        System.out.println( "makeWbOut" );
    }
    // System.exit( 0 );
    return null;
  }

  // MAKE_VERBWB_OUT: Unterfunktion von MAKE_WB_OUT
  // Outputgenerierung bei Verbwortbildung

  private Vector makeVerbWbOut( Vector zerlegung, String wortform ) {
    Constituent c, flexConst, verbLexEnt, konstVorVerb, konstZweiVorVerb;
    FlexivMerkmal flex;
    String flexklasse, katVorVerb, katZweiVorVerb, flexiv;
    int i;

    if( trace ) {
      System.out.println( "MAKE_VERBWB_OUT zerlegung: " + zerlegung );
      System.out.println( "MAKE_VERBWB_OUT wortform: " + wortform );
    }

    i = zerlegung.size();

    flexConst = ( Constituent )zerlegung.elementAt( i - 1 );
    verbLexEnt = ( Constituent )zerlegung.elementAt( i - 2 );
    if( zerlegung.size() >= 3 )
      konstVorVerb = ( Constituent )zerlegung.elementAt( i - 3 );
    else konstVorVerb = null;
    if( zerlegung.size() >= 4 )
      konstZweiVorVerb = ( Constituent )zerlegung.elementAt( i - 4 );
    else konstZweiVorVerb = null;
    flex = verbLexEnt.getFlexive();
    if( ( konstVorVerb != null ) && flex.RefMerkmal() ) {
      flexklasse = verbLexEnt.getFlexklasse();
    }
    else if( !verbLexEnt.getCategory().equals( "V" ) &&
	     !verbLexEnt.getCategory().equals( "ALLOV" ) &&
	     !verbLexEnt.getCategory().equals( "SUF" ) ) {
      flexklasse = verbLexEnt.getVerbFlexklasse();
    }
    else flexklasse = flex.getFlexive();
    if( konstVorVerb != null ) katVorVerb = konstVorVerb.getCategory();
    else katVorVerb = "";
    if( konstZweiVorVerb != null )
      katZweiVorVerb = konstZweiVorVerb.getCategory();
    else katZweiVorVerb = "";
    flexiv = flexConst.getFlexiveInfo().holeFlexiv();

    c = (Constituent)zerlegung.elementAt( 0 );
    if( ( konstVorVerb == null ) || 
	( ( zerlegung.size() == 3 ) && flex.mk().equals( "SUF" ) ) ) {
      // Verbsimplex
         return makeSimpVOut( flexklasse, verbLexEnt, flexConst, zerlegung );
    }
    else if( c.equals( "PRAEF" ) && c.getWord().equals( "UN" ) ||
	     ( ( flexklasse.equals( "SWZ1" ) ||
		 flexklasse.equals( "SWZ2" ) ||
		 flexklasse.equals( "SWZ3" ) ||
		 flexklasse.equals( "SWZ4" ) ||
		 flexklasse.equals( "SWZ5" ) ) &&
	       ( katVorVerb.equals( "N" ) ||
		 katVorVerb.equals( "FUGE" ) ||
		 katVorVerb.equals( "ADJ" ) ) ) ||
	     ( katVorVerb.equals( "VPR" ) &&
	       ( katZweiVorVerb.equals( "N" ) ||
		 katZweiVorVerb.equals( "ADJ" ) ||
		 katZweiVorVerb.equals( "FUGE" ) ||
		 katZweiVorVerb.equals( "PRAEF" ) ) ) ||
	     ( katVorVerb.equals( "PARTPRAEF" ) &&
	       ( katZweiVorVerb.equals( "N" ) ||
		 katZweiVorVerb.equals( "FUGE" ) ||
		 katZweiVorVerb.equals( "PRAEF" ) ) ) ) {
      return makeVerbAdjOut( flexiv, flexklasse, wortform, zerlegung, verbLexEnt );
    }
    else if( katVorVerb.equals( "PARTPRAEF" ) ) {
      // Partizip
      return makePartOut( flexiv, flexklasse, verbLexEnt, zerlegung );
    }
    else if( katVorVerb.equals( "VPR" ) ) {
      return makePraefOut( flexiv, flexklasse, verbLexEnt, flexConst, zerlegung );
    }
    else if( katVorVerb.equals( "TVZ" ) ||
	     katVorVerb.equals( "ADJ" ) ) {
      // trennbarer Verbzusatz
      return makeTrennzusOut( flexklasse, zerlegung, verbLexEnt, flexConst );
    }
    else if( katVorVerb.equals( "INFINITIVPARTIKEL" ) ) {
      // infigierter Zu-Inf.
      Vector features;
      FlexMerkmal fm;
      FeatureStructure fs;
      FeatureValue fv;
      Enumeration enum1, readings;
      KategorieMerkmal k;

      features = flexklassSuche( flexklasse, //verbLexEnt.getFlexive().getFlexive(),
				 flexConst.getFlexiveInfo().holeDaten() );
      if( features != null ) {
	enum1 = features.elements();
	while( enum1.hasMoreElements() ) {
	  k = ( KategorieMerkmal )enum1.nextElement();
	  fm = k.getFlexInfo();
	  if( fm != null ) {
	    readings = fm.getFeatures().elements();
	    while( readings.hasMoreElements() ) {
	      fs = (FeatureStructure)readings.nextElement();
	      //System.out.println( "MAKE_VERBWB_OUT: " + fs );
	      fv = fs.get( FeatureName.forName( "typ" ) );
	      //System.out.println( "MAKE_VERBWB_OUT: " + fv );
	      if( fv != null ) {
		if( fv.equals( new StringValue( "infinitiv" ) ) ) {
		  if( trace ) { 
		    //System.out.println( "infinitiv found" );
		  }
		  k.setConstInfo( zerlegung );
		  return makeVerbListOut( k, flexklasse, "TVZ" );
		}
	      }
	    }
	  }
	}
	return null;
      }
      else return null;
    }
    else if( katVorVerb.equals( "N" ) ||
	     ( katVorVerb.equals( "FUGE" ) &&
	       katZweiVorVerb.equals( "N" ) ) ) {
      // Substantivierter Infinitiv
      return makeSubstInfOut( flexklasse, zerlegung, verbLexEnt, flexConst );
    }
    else {
      // System.out.println( "unknown situation in makeVerbOut: " + flexklasse + " " + flexConst + " " + verbLexEnt + " " + konstVorVerb + " " + konstZweiVorVerb);
      // System.exit( 0 );
      return new Vector();
    }
  }

  // MAKE_SIMPV_OUT : Hilfsfunktion von MAKE_VERBWB_OUT
  // Outputgenerierung fuer Simplexverbformen
  private Vector makeSimpVOut( String flexklasse,
			       Constituent verbConst,
			       Constituent flex, 
			       Vector zerlegung) {
    Vector features, res, verbList;
    Enumeration enum1, readings;
    KategorieMerkmal m;

    if( trace ) {
       System.out.println( "MAKE_SIMPV_OUT flexklasse: " + flexklasse );
       System.out.println( "MAKE_SIMPV_OUT konstVorVerb: " + verbConst );
       System.out.println( "MAKE_SIMPV_OUT flex: " + flex );
    }
	
    features = flexklassSuche( flexklasse,
			       // verbConst.getFlexive().getFlexive(),
			       flex.getFlexiveInfo().holeDaten() );

    if( features != null ) {
      enum1 = features.elements();
      res = new Vector();

      while( enum1.hasMoreElements() ) {
	m = ( KategorieMerkmal )enum1.nextElement();
	m.setConstInfo( zerlegung );
	verbList = makeVerbListOut( m, flexklasse, "VPR" );
	readings = verbList.elements();
	while( readings.hasMoreElements() ) {
	  res.addElement( readings.nextElement() );
        }
      }
 
      return res;

    }

    return null;

//  gueltfeat
// 		(if (myassoc 'L gueltfeat)
// 		  (let ((lesarten '()))
// 		   ;(dolist (les gueltfeat lesarten))
// 		   (let* ((liste gueltfeat)
// 			   (les (car* gueltfeat)))
// 			(do ()
// 			  ((null? liste) lesarten)
// 			 (set! lesarten (append lesarten (make_verb_listout
// 							  (remove (car* revzerl) zerlegung) (append (cdr* les)
// 												   (check_lexfeat (cdadr* (caddr* (cadr* revzerl))))) 
// 							  flexklasse #f)))
// 			 (set! liste (cdr* liste))
// 			 (set! les (car* liste))))
// 		   ); end let
// 		  (make_verb_listout (remove (car* revzerl) zerlegung) 
// 				    (append gueltfeat (check_lexfeat (cdadr* (caddr* 
// 										 (cadr* revzerl))))) 
// 				    flexklasse #f)
// 		  )  
// 		#f)
// 	  )))     
      

  }

  // MAKE_VERB_LISTOUT : Hilfsfunktion von MAKE_VERBWB_OUT

  private Vector makeVerbListOut( KategorieMerkmal m,
				  String flexklasse,
				  String klasse ) {
    KategorieMerkmal k;
    FlexivMerkmal headFlex;
    Vector konsts = m.getConstInfo();
    int konstLength = konsts.size(), i;
    String alloHead, infin, anfStr = "", grundform;
    boolean simplex;
    Constituent c;
    Vector readings;

    if( trace ) {
      System.out.println( "MAKE_VERB_LISTOUT  konsts: " + konsts );
      System.out.println( "MAKE_VERB_LISTOUT  flexklasse: " + flexklasse );
      System.out.println( "MAKE_VERB_LISTOUT  klasse: " + klasse );
    }

    headFlex = ((Constituent)konsts.elementAt( konstLength - 2 )).getFlexive();
    alloHead = headFlex.grundform();

    if( flexklasse.equals( "SWV4" ) ||
	flexklasse.equals( "SWV4_1SG" ) ||
	flexklasse.equals( "SWV5" ) ||
	flexklasse.equals( "SWZ4" ) ||
	flexklasse.equals( "SWZ4_1SG" ) ||
	flexklasse.equals( "SWZ5" ) ) infin = "N";
    else infin = "EN";
    simplex = ( konstLength == 1 );

    i = 0;
    while( i < konstLength-2 ) {
      c = (Constituent)konsts.elementAt(i);
      if( !c.getCategory().equals( "PARTPRAEF" ) )
	anfStr += c.getWord();

      i++;
    }

    if( !alloHead.equals( "" ) ) anfStr += alloHead;
    else anfStr += ((Constituent)konsts.elementAt( konstLength - 2 )).getWord();

    grundform = anfStr + infin;
    m.setBaseForm( grundform );
    readings = m.readingList( flexivLex );

    if( klasse.equals( "TVZ" ) ) {
      FeatureStructure fs;
      FeatureValue fv;
      FlexMerkmal fm;
      Vector fmv;

      // Imperative rausfiltern

      for( i = 0; i < readings.size(); i++ ){
	k = ( KategorieMerkmal )readings.elementAt( i );
	fm = k.getFlexInfo();
	if( fm != null ) {
	  fmv = fm.getFeatures();
	  if( fmv != null ) {
	    fs = (FeatureStructure)fmv.elementAt( 0 );
	    fv = fs.get( FeatureName.forName( "typ" ) );
	  
	    if( fv != null ){
	      if( k.category().equals( "V" ) &&
		  fv.equals( new StringValue( "imperativ" ) ) ) {
		readings.removeElementAt( i );
		i--;
	      }
	    }
	  }
	}
      }
    }
    
    return readings;
    // This part is not implemented yet.

    // 		 (kat (car* feats))
    // 		 (lexikalisiert (if (not simplex) (compound_lookup stamm klasse) #f))
    // 		 (konst (if (not simplex) (if lexikalisiert
    // 					      (check_lexfeat (cadr* lexikalisiert))
    // 					      (get_konst_feat konsts)) 
    // 			    '()))
    // 		 )
    // 	    (if kat
    // 		(list (append (list kat) (list grundform) (cdr* feats) konst)) #f)
    // 	    )))
  }

  // MAKE_PART_OUT : Hilfsfunktion von MAKE_VERBWB_OUT
  // Outputgenerierung bei Verbpartizipformen
  private Vector makePartOut( String flexiv,
			      String flexklasse,
			      Constituent verbKonst,
			      Vector zerlegung ) {
    FlexivEintrag flexGuelt;
    Vector features, res, verbList;
    Enumeration enum1, readings;
    KategorieMerkmal m;

    if( trace ) {
      System.out.println( "MAKE_PART_OUT  flexiv: " + flexiv );
      System.out.println( "MAKE_PART_OUT  flexklasse: " + flexklasse );
      System.out.println( "MAKE_PART_OUT  konst_vor_verb: " + verbKonst );
      System.out.println( "MAKE_PART_OUT  zerlegung: " + zerlegung );
    }

    flexGuelt = (FlexivEintrag)flexivLex.getEntry( "ge-" + flexiv );
    if ( flexGuelt == null ) features = null;
    else features = flexklassSuche( flexklasse,
				    flexGuelt.holeDaten() );
    if( features != null ) {
      enum1 = features.elements();
      res = new Vector();

      while( enum1.hasMoreElements() ) {
	m = ( KategorieMerkmal )enum1.nextElement();
	m.setConstInfo( zerlegung );
	verbList = makeVerbListOut( m, flexklasse, "TVZ" );
	readings = verbList.elements();
	while( readings.hasMoreElements() ) {
	  res.addElement( readings.nextElement() );
	}
      }

      return res;
    }
    else return null;
  }

  // MAKE_PRAEF_OUT : Hilfsfunktion von MAKE_VERBWB_OUT
  // Outputgenerierung bei Verbpraefigierung mit nicht trennbarem Praefix
  // Hier muessen zusaetzlich diskontinuierliche Partizipflexive geprueft
  // werden.
  private Vector makePraefOut( String flexiv,
			       String flexklasse,
			       Constituent verbKonst,
			       Constituent flex,
			       Vector zerlegung ) {
    Vector gueltFeat, partGuelt;
    FlexivEintrag f;
    Vector features, res, verbList;
    Enumeration enum1, readings;
    KategorieMerkmal m;

    if( trace ) {
      System.out.println( "MAKE_PRAEF_OUT flexiv: " + flexiv );
      System.out.println( "MAKE_PRAEF_OUT flexklasse: " + flexklasse );
      System.out.println( "MAKE_PRAEF_OUT zerlegung: " + zerlegung );
    }

    gueltFeat = flexklassSuche( flexklasse, //verbKonst.getFlexive().getFlexive(),
				flex.getFlexiveInfo().holeDaten() );
    f = ( FlexivEintrag )flexivLex.getEntry( "ge-" + flexiv );
    if (f != null){
      partGuelt = flexklassSuche( flexklasse, // verbKonst.getFlexive().getFlexive(),
				f.holeDaten() );
    } else partGuelt = null;

    if( ( gueltFeat == null ) && ( partGuelt == null ) ) return null;
    res = new Vector();

    if( gueltFeat != null ) {
      enum1 = gueltFeat.elements();

      while( enum1.hasMoreElements() ) {
	m = ( KategorieMerkmal )enum1.nextElement();
	m.setConstInfo( zerlegung );
	verbList = makeVerbListOut( m, flexklasse, "VPR" );
	readings = verbList.elements();
	while( readings.hasMoreElements() ) {
	  res.addElement( readings.nextElement() );
	}
      }
    }

    if( partGuelt != null ) {
      enum1 = partGuelt.elements();

      while( enum1.hasMoreElements() ) {
	m = ( KategorieMerkmal )enum1.nextElement();
	m.setConstInfo( zerlegung );
	verbList = makeVerbListOut( m, flexklasse, "VPR" );
	readings = verbList.elements();
	while( readings.hasMoreElements() ) {
	  res.addElement( readings.nextElement() );
	}
      }
    }

    return res;
  }

  // MAKE_TRENNZUS_OUT: Hilfsfunktion von MAKE_VERBWB_OUT
  // Outputgenerierung bei Verbpraefigierung mit Adjektiv oder trennbarem
  // Zusatz

  private Vector makeTrennzusOut( String flexklasse,
				  Vector zerlegung,
				  Constituent verbKonst,
				  Constituent flex ) {
    Vector features, res, verbList;
    Enumeration enum1, readings;
    KategorieMerkmal m;

    if( trace ) {
      System.out.println( "MAKE_TRENNZUS_OUT flexklasse: " + flexklasse );
      System.out.println( "MAKE_TRENNZUS_OUT verbKonst: " + verbKonst );
      System.out.println( "MAKE_TRENNZUS_OUT flex: " + flex );
    }

    features = flexklassSuche( flexklasse, // verbKonst.getFlexive().getFlexive(),
			       flex.getFlexiveInfo().holeDaten() );

    if( features != null ) {
      enum1 = features.elements();
      res = new Vector();

      while( enum1.hasMoreElements() ) {
	m = ( KategorieMerkmal )enum1.nextElement();
	m.setConstInfo( zerlegung );
	verbList = makeVerbListOut( m, flexklasse, /*((FINIT-STELLUNG)) '())) flexklasse */ "TVZ" );
	readings = verbList.elements();
	while( readings.hasMoreElements() ) {
	  res.addElement( readings.nextElement() );
	}
      }

      return res;
    }
    else return null;
  }

  // MAKE_VERBADJ_OUT: Hilfsfunktion von MAKE_VERBWB_OUT
  // Outputgenerierung fuer Verbpseudokomposita (handgeschnitzt) als Adjektiv
  
  private Vector makeVerbAdjOut( String flexiv,
				 String flexklasse,
				 String wortform,
				 Vector zerlegung,
				 Constituent verbConst ) {
    boolean compoundVerb;
    FlexivEintrag f;
    Vector features, verbList, res;
    int i;
    Enumeration enum1, readings;
    KategorieMerkmal m;

    if( trace ) {
      System.out.println( "MAKE_VERBADJ_OUT  flexiv: " + flexiv );
      System.out.println( "MAKE_VERBADJ_OUT  flexklasse: " + flexklasse );
      System.out.println( "MAKE_VERBADJ_OUT  wortform: " + wortform );
      System.out.println( "MAKE_VERBADJ_OUT  zerlegung: " + zerlegung );
    }

    if( flexklasse.equals( "SWZ1" ) ||
	flexklasse.equals( "SWZ2" ) ||
	flexklasse.equals( "SWZ3" ) ||
	flexklasse.equals( "SWZ4" ) ||
	flexklasse.equals( "SWZ5" ) ) {
      compoundVerb = true;
    }
    else {
      compoundVerb = false;
    }

    if( compoundVerb ) {
      f = (FlexivEintrag)flexivLex.getEntry( flexiv );
      features = flexklassSuche( flexklasse,
				 f.holeDaten() );
      i = 0;
      if( features != null ) {
	while( i < features.size() ) {
	  m = (KategorieMerkmal)features.elementAt( i );
	  if( m.category().equals( "ADJ" ) ) i++;
	  else features.removeElementAt( i );
	}
      }
    }
    else {
      f = (FlexivEintrag)flexivLex.getEntry( "ge-" + flexiv );
      if ( f == null ) features = null;
      else features = flexklassSuche( flexklasse,
				      f.holeDaten() );
    }
    
    if( features != null ) {
      enum1 = features.elements();
      res = new Vector();

      while( enum1.hasMoreElements() ) {
	m = ( KategorieMerkmal )enum1.nextElement();
	m.setConstInfo( zerlegung );
	verbList = makeVerbAdjListOut( m,
				       wortform,
				       flexiv,
				       zerlegung,
				       flexklasse );
	readings = verbList.elements();
	while( readings.hasMoreElements() ) {
	  res.addElement( readings.nextElement() );
	}
      }

      return res;
    }
    else return null;
  }

  // MAKE_SUBSTINF_OUT: Hilfsfunktion von MAKE_VERBWB_OUT
  // Outputgenerierung fuer Nomen-Verb-Komposita (Speerwerfen)

  private Vector makeSubstInfOut( String flexklasse,
				  Vector zerlegung,
				  Constituent verbKonst,
				  Constituent flex ) {
    Vector features, verbList, res;
    int i;
    Enumeration enum1, readings;
    KategorieMerkmal m;

    if( trace ) {
      System.out.println( "MAKE_SUBSTINF_OUT flexklasse: " + flexklasse );
      System.out.println( "MAKE_SUBSTINF_OUT zerlegung: " + zerlegung );
    }

    features = flexklassSuche( flexklasse, //verbKonst.getFlexive().getFlexive(),
			       flex.getFlexiveInfo().holeDaten()  );
    if( features != null ) {
      enum1 = features.elements();
      res = new Vector();
      
      while( enum1.hasMoreElements() ) {
	m = ( KategorieMerkmal )enum1.nextElement();
	m.setConstInfo( zerlegung );
	verbList = makeSubstInfAux( flexklasse, m, zerlegung );
	if( verbList != null ) {
	  readings = verbList.elements();
	  while( readings.hasMoreElements() ) {
	    res.addElement( readings.nextElement() );
	  }
	}
      }
      return res;
    }
    else return null;
  }

  // MAKE_VERBADJ_LISTOUT: Hilfsfunktion von MAKE_VERBADJ_OUT
  private Vector makeVerbAdjListOut( KategorieMerkmal m,
				     String wortform,
				     String flexiv,
				     Vector zerlegung,
				     String flekla ) {
    boolean tFlexiv, participle;
    String gf;
    Vector readings, res;
    KategorieMerkmal k;
    int i;
    FlexMerkmal fm;
    Vector fmv;
    FeatureStructure fs;
    FeatureValue fv;

    if( trace ) {
      System.out.println( "MAKE_VERBADJ_LISTOUT  wortform: " + wortform );
      System.out.println( "MAKE_VERBADJ_LISTOUT  flexiv: " + flexiv );
      System.out.println( "MAKE_VERBADJ_LISTOUT  zerlegung: " + zerlegung );
      System.out.println( "MAKE_VERBADJ_LISTOUT  flekla: " + flekla );
    }

    tFlexiv  = ( flexiv.length() > 0 ) && ( flexiv.charAt( 0 ) == 't' );
    if( tFlexiv ) {
      gf = wortform.substring( 0, wortform.length() - flexiv.length() ) + "t";
    }
    else {
      gf = wortform.substring( 0, wortform.length() - flexiv.length() ) + "en";
    }

    m.setBaseForm( gf );
    readings = m.readingList( flexivLex );

    // Partzipform?

    participle = false;
    res = new Vector();

    for( i = 0; i < readings.size(); i++ ) {
      k = ( KategorieMerkmal )readings.elementAt( i );
      fm = k.getFlexInfo();

      if( fm != null ) {
	fmv = fm.getFeatures();
	if ( fmv != null ) {
  	  fs = (FeatureStructure)(fmv.elementAt( 0 ));
	  fv = fs.get( FeatureName.forName( "typ" ) );
	  
  	  if( fv == null ) res.addElement( k );
	  else if( !fv.equals( new StringValue( "ppp" ) ) &&
		   !k.category().equals( "ADJ" ) ) {
	    Enumeration enum1;
	    Vector tmp;
	    KategorieMerkmal km;

	    tmp = makeSubstInfAux( flekla, m, zerlegung );
	    enum1 = tmp.elements();
	    while( enum1.hasMoreElements() ) {
	      km = (KategorieMerkmal)enum1.nextElement();
	      res.addElement( km );
	    }
	  }
	  else res.addElement( k );
	}
	else res.addElement( k );
      }
      else res.addElement( k );

    }

    return res;
  }

  private Vector makeSubstInfAux( String flexklasse,
				  KategorieMerkmal m,
				  Vector zerlegung ) {
    FeatureStructure fs;
    FeatureValue fv;
    FlexMerkmal fm;
    Enumeration readings;

    if( trace ) {
      System.out.println( "MAKE_SUBSTINF_AUX  flexklasse: " + flexklasse );
      System.out.println( "MAKE_SUBSTINF_AUX  merkmal: " + m );
      System.out.println( "MAKE_SUBSTINF_AUX  zerlegung: " + zerlegung);
    }

    fm = m.getFlexInfo();
    
    if( fm != null ) {
      Vector fmv = fm.getFeatures();
      if ( fmv != null ){
        readings = fmv.elements();
        while( readings.hasMoreElements() ) {
	  fs = (FeatureStructure)readings.nextElement();
	  fv = fs.get( FeatureName.forName( "typ" ) );
      
	  if( fv != null ) {
	    if( fv.equals( new StringValue( "infinitiv" ) ) ) {
	      return makeVerbListOut( m, flexklasse, "" );
	    }
	    else if( fv.equals( new StringValue( "partpraes" ) ) ) {
	      return makeVerbListOut( m, flexklasse, "" );
	    }
	    else if( m.category().equals( "N" ) ) {
	      FeatureValue gen, kas, num;
	    
	      gen = fs.get( FeatureName.forName( "genus" ) );
	      kas = fs.get( FeatureName.forName( "kasus" ) );
	      num = fs.get( FeatureName.forName( "numerus" ) );
	    
	      if( ( gen != null ) && ( kas != null ) && ( num != null ) ) {
		if( gen.equals( new StringValue( "neut" ) ) &&
		    kas.equals( new StringValue( "gen" ) ) &&
		    num.equals( new StringValue( "sing" ) ) )
		    return makeVerbListOut( m, flexklasse, "" );
	      }
	    }
	  }
	}
      }
    }
    return null;
  }

  // *************************************************************************
  // *  Die Funktionen zur                                                   *
  // *  Hypothesen-Bildung aufgrund der Endung                               *
  // *************************************************************************

  // INFIG_ZU_HYPO:
  // Fuer den Fall, dass Infinitiv trennbarer Praefixverben mit
  // infigiertem ZU vorliegt, wird entsprechend lemmatisiert:
  //  z.B.: (infig_zu "AUFZUHOEREN")
  //        ==> (AUFZUHOEREN (V) (GF AUFHOEREN) (FLEXION (ZU-INFINITIV)))

  // kommt in EMBASSI-Wortliste nicht vor, von Wortbildung abgefangen?

  private Vector infigZuHypo( String wort ) {
    Pair vz;
    String stamm;
    MorphAnalysis m;
    Vector v, featVect;
    UnpackedFeatureStructure res;

    vz = vzAnfang( wort );
    if( vz != null ) {
	stamm = ( String )vz.getRight();
	if( ( wort.length() > 8 ) &&
	    stamm.substring( 0, 2 ).equals( "zu" ) &&
	
	    ( wort.substring( wort.length() - 2 ).equals( "en" ) ||
	      wort.substring( wort.length() - 3 ).equals( "ern" ) ||
	      wort.substring( wort.length() - 3 ).equals( "eln" ) ) ) {
	    // ZU-INFINITIV trennbarer Praefixverben
	    // dann gib ZU-INFINITIV-HYPOTHESE aus:
	    featVect = new Vector();
	    res = new UnpackedFeatureStructure();
	    res.add( FeatureName.forName( "typ" ),new StringValue("infinitiv"));
	    featVect.addElement( res.pack() );
	    m = new MorphAnalysis( wort,
				   new VKategorieMerkmal(new VFlexMerkmal(featVect),
							 wort ) );
	    v = new Vector();
	    v.addElement( m );
	    return v;
	}
	else return null;
    }
    else return null;
  }

    // VZ_ANFANG: Hilfsfunktion von INFIG_ZU_HYPO
    // Prueft, ob eine Wortform mit einem potentiell abtrennbaren Verbzusatz
    // beginnt. (longest matching)
    // (vz_anfang "ABFAHREN") --> ("AB" "FAHREN")
    
    private Pair vzAnfang( String wortform ) {
	int index;
	String zusatzHypo, zusatz = "";
	
	if( wortform.length() >= 5 ) {
	    index = 2;
	    
	    while( index < wortform.length() - 2 ) {
		zusatzHypo = wortform.substring( 0, index );
		if( tvzLex.zusatz( zusatzHypo ) ) {
		    zusatz = zusatzHypo;
		}
		
		index ++;
	    }
	    
	    if( trace ) {
		System.out.println( "vzAnfang: " + zusatz + " " + wortform.substring( zusatz.length() ) );
	    }
	    if( zusatz.length() > 0 )  { 
		return new Pair( zusatz, wortform.substring( zusatz.length() ) );
	    }
	    else { 
		return null;
	    }
	}
	else {
	    return null;
	}
    }

  // END_HYPOTHESE: Longest-matching des Wortendes mit den in *HYPO*
  // gespeicherten Endungen.

  private Vector endHypothese(String wort ) {
    Vector infZu;
    int wortLaenge;

    if( trace ) {
      System.out.println( "END_HYPOTHESE  wort: " + wort );
    }

    infZu = infigZuHypo( wort );
    wortLaenge = wort.length();

    if( infZu != null ) {
      // Hypothese, dass infigierter Zu-Infinitiv trennb. Praefixverb
      return infZu;
    }
    else {
      // else matche Wortende gegen Endungsbaum
      if( wortLaenge > 3 ) {
	int zaehler = 1;
	String endung;
	EndungEintrag treffer;

	while( zaehler < wortLaenge ) {
	  endung = wort.substring( wortLaenge - zaehler );
	  treffer = (EndungEintrag)hypoLex.getEntry( endung );
	  if( treffer != null ) {
	    return getHypoFeat( wort, treffer );
	  }

	  zaehler ++;
	}

	return null;
      }
      else return null;
    }
  }

  // MAKE_HYPO_OUT
  // Generiert die Ausgabeeinheit
//   private Vector makeHypoOut( String wort, Vector hypothesen ) {
//     return new Vector();
// 	  (if *TRACE*
// 	      (begin
// 		(writeln "MAKE_HYPO_OUT wort: "wort)
// 		(writeln "MAKE_HYPO_OUT hypothesen: "hypothesen)
// 		))
// 	  (cond ((> (length hypothesen) 1)
// 		 (let ((liste  hypothesen)
// 		       (lesart (car* hypothesen))
// 		       (res '())
// 		       )
// 		   (do ()
// 		       ((null? liste)
// 			(cons (string->symbol wort) res))
// 		     (set! res (append res (list (cons 'L lesart))))
// 		     (set! liste (cdr* liste))
// 		     (set! lesart (car* liste))
// 		     )
// 		   ))
// 		(else (cons (string->symbol wort) (car* hypothesen)))
// 		)))
  //  }
  // GET_HYPO_FEAT
  // Stellt die Merkmale der Hypothesen zusammen
  
  private Vector getHypoFeat( String wort, EndungEintrag endfeat ) {
    Vector lesarten, res = new Vector(), readings;
    AktionMerkmal lesart;
    KategorieMerkmal k;
    Enumeration enum1, r;
    String grundform;

    if( trace ) {
      System.out.println( "GET_HYPO_FEAT  wort: " + wort );
      System.out.println( "GET_HYPO_FEAT  endfeat: " + endfeat );
    }

    enum1 = endfeat.holeDaten().elements();
    while( enum1.hasMoreElements() ) {
      lesart = (AktionMerkmal)enum1.nextElement();
      if (trace) {
          System.out.println( lesart );
      }
      
      grundform = lesart.getAction().execute( wort );
      
      k = lesart.getKategorieMerkmal();
      readings = k.readingList( flexivLex );
      r = readings.elements();
      
      while( r.hasMoreElements() ) {
	k = ( KategorieMerkmal )r.nextElement();
	k.setBaseForm( grundform );
	
	res.addElement( new MorphAnalysis( wort, k ) );
      }
    } 

    return res;
  }
  
  
  /**
   * Main method.
   * @param args The arguments.
   */
  public static void main(String[] args) {
  	MorphModule test = new MorphModule(args[0]);
  	
  	long startTime, stopTime;
  	
  	String n = "Film", v = "beginn", adj = "schön", adv = "auch", det = "der";
  	
  	// Tests f�r morphologische Analyse
  	startTime = System.currentTimeMillis();
  	
  	System.out.println(test.analyzeToString(n + "e"));
  	System.out.println(test.analyzeToString(v + "t"));
  	System.out.println(test.analyzeToString(adj + "er"));
  	System.out.println(test.analyzeToString(adv));
  	System.out.println(test.analyzeToString(det));
  	
  	stopTime = System.currentTimeMillis() - startTime;
  	System.out.println("\nMorph analysis took " + stopTime + " msecs.\n\n");
  }
}

