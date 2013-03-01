header {
package morph;
	import java.util.Vector;
	import java.util.Hashtable;
	import de.fau.cs.jill.feature.*;
}

class MorphParser extends Parser;
options {
	k = 3;
}

eintrag_liste returns [Hashtable h]
{
	LexemEintrag l;

	h = new Hashtable();
}
	: (l=eintrag
			{
				h.put( l.holeLexem(), l );
				// System.out.println( h.size() + ": " + l.holeLexem() );
			}
		)+
	;

eintrag returns [LexemEintrag l]
{
	String key;
	Vector lesarten;

	l = null;
}
	: LPAREN key=lemmaname lesarten=lesart_liste RPAREN
	{
	 l = new LexemEintrag( key, lesarten );
	};

lemmaname returns [String key]
{
	key = "";
}
	: ( sym:SYM_SYMBOL { key = sym.getText(); }
	// jetzt kommen lauter Symbole, die definierte tokens sind,
	// aber auch als Lexeme vorkommen.
	  | symsing:SYM_SING { key = symsing.getText(); }
	  | symdem:SYM_DEMON { key = symdem.getText(); }
	  | symfl:SYM_FLEXION { key = symfl.getText(); }
	  | symfu:SYM_FUGE { key = symfu.getText(); }
	  | symgen:SYM_GEN { key = symgen.getText(); }
	  | symgr:SYM_GRAD { key = symgr.getText(); }
	  | symimp:SYM_IMP { key = symimp.getText(); }
	  | symind:SYM_INDEF { key = symind.getText(); }
	  | symint:SYM_INTERROGPRON { key = symint.getText(); }
	  | symn:SYM_N { key = symn.getText(); }
	  | syminf:SYM_INF { key = syminf.getText(); }
	  | sympers:SYM_PERS { key = sympers.getText(); }
	  | symref:SYM_REFL { key = symref.getText(); }
	  | symrel:SYM_REL { key = symrel.getText(); }
	  | symtyp:SYM_TYP { key = symtyp.getText(); }
	  )
        ;

lesart_liste returns [Vector v]
{
	Merkmal f;
	v = new Vector();
}
	: (f=lesart { v.add( f ); })+;

lesart returns [FlexivMerkmal f]
{
	KategorieMerkmal m;
	String s;
	boolean hatTvz = false, hatVpr = false;
	Vector v;

	f = null;
}
	: LPAREN
		( s1:SYM_SYMBOL
			{
				f = new FlexivklasseMerkmal( s1.getText() );
			}
		| SYM_VF
			{
				f = new FlexivVFMerkmal();
			}
		| SYM_ORTNUMSUF { f = new FlexivORTNUMSUFMerkmal(); }
		| SYM_EQ lex:SYM_SYMBOL
			{ f = new FlexivRefMerkmal( lex.getText() ); }
		)
		( s=abk { f.abkSetzen( s ); }
	    	| s=gfstamm { f.grundformSetzen( s ); }
	    	| v=allo { f.allomorphSetzen( v ); }
	    	| LPAREN SYM_MWTRIGG RPAREN
	    	| LPAREN SYM_INFPART RPAREN
			{
				f = new FlexivVFMerkmal( new InfPartKategorieMerkmal() );
			}
		| LPAREN SYM_TVZ (t:SYM_SYMBOL { hatTvz = true; })? RPAREN
			{
				if( hatTvz ) f.hatTvzSetzen( t.getText() );
				else { f.tvzSetzen();
				       m = new TVZKategorieMerkmal(); 
                                       f.kategorieSetzen (m);}
			}
		| LPAREN SYM_VPR ( vpr:SYM_SYMBOL { hatVpr = true; })? RPAREN
			{
				if( hatVpr ) f.hatVprSetzen( vpr.getText() );
				else f.vprSetzen();
			}
		| LPAREN SYM_SEM s=sem RPAREN { f.semSetzen( s ); }
		| LPAREN SYM_SYN s=sem RPAREN
	    | LPAREN SYM_MK ( SYM_FUGE { f.fugeSetzen(); }
			| s2:SYM_KONFIXFUGE { f.mkSetzen( s2.getText() ); }
			| s3:SYM_LEHNSUF { f.mkSetzen( s3.getText() ); }
			| s8:SYM_PRAEFIX { f.mkSetzen( s8.getText() ); }
			| s9:SYM_SUFFIX { f.mkSetzen( s9.getText() ); }
			| s4:SYM_KONFIX { f.mkSetzen( s4.getText() ); }
			| s5:SYM_NOUNSUF { f.mkSetzen( s5.getText() ); }
			| p:SYM_PARTPRAEF { f.mkSetzen( p.getText() ); }
			| n:SYM_N { f.mkSetzen( n.getText() ); }
		        | s6:SYM_NUMSUF { f.mkSetzen( s6.getText() ); }
		        | s7:SYM_ORTNUMSUF { f.mkSetzen( s7.getText() ); }
				 // manchmal auch ohne "MK"
			| SYM_VPR { f.vprSetzen(); } // (tvz) aber: (mk vpr)
			| adj:SYM_ADJ { f.mkSetzen( adj.getText() ); }
				// kommt z.B. bei DISZIPLINAR vor; seltsam!!
				// warum nicht (ADJ)
				// Erklaerung: siehe Kommentar zu addKat
			) RPAREN
		| LPAREN SYM_BOUND RPAREN { f.boundSetzen(); }
	    | v=wbsubcat { f.wbsubcatSetzen( v ); }
		| m=kategorie[f]
			{
				f.kategorieSetzen( m );
			}
		)*
		RPAREN
	;

wbsubcat returns [Vector v]
{
	String s;
	
	v = new Vector();
}
	: LPAREN SYM_WBSUBCAT (catn:SYM_N {s=catn.getText(); v.add(s);}|
			       catv:SYM_V {s=catv.getText(); v.add(s);}|
			       catadj:SYM_ADJ {s=catadj.getText(); v.add(s);}|
	                       cats:SYM_SYMBOL {s=cats.getText(); v.add(s);})+ RPAREN
	;

sem returns [String s]
{
	s = "";
}
	: SYM_SYMBOL
	| LPAREN SYM_SYMBOL sym:SYM_SYMBOL RPAREN
	  {
		s = sym.getText();
	  }
	;

abk returns [String s]
{
	s = "";
}
	: LPAREN
	  SYM_ABK
	  (s1:SYM_SYMBOL
	   { if( !s.equals( "" ) ) s += " ";
	     s += s1.getText();
	   })+
	  RPAREN;

allo returns [Vector v]
{
	v = new Vector();
}
	: LPAREN SYM_ALLO (a:SYM_SYMBOL { v.add( a.getText() ); })+ RPAREN;

gfstamm returns [String s]
{
	s = "";
}
	: LPAREN SYM_GF (a:SYM_SYMBOL{s = a.getText();}|b:SYM_SING {s = b.getText();}) RPAREN;

kategorie [FlexivMerkmal stamm] returns [KategorieMerkmal m]
{
	boolean interrog = false, kasrek = false, 
		posspron = false, pronGf = false;
	String kas, s;
	UnpackedFeatureStructure feat;
	FeatureStructure grad;
	FlexMerkmal f;
	Vector v = new Vector();
	boolean nFlex = false;

	m = null;
}
	: LPAREN 
		( SYM_N ( RPAREN (LPAREN SYM_G ngf:SYM_SYMBOL RPAREN { stamm.grundformSetzen( ngf.getText() ); })? LPAREN
			( feat=genus
				( grad=kasusnumerus[feat] { v.add( grad ); } )+
//				RPAREN
				{
					m = new NKategorieMerkmal( new NFlexMerkmalEinfach( v ) );
				}
			| SYM_FLEXION LPAREN feat=genus
				// N Vollformen mal mit 'FLEXION' gespeichert, mal ohne ...
				( grad=kasusnumerus[feat] { v.add( grad ); } )+
//				RPAREN
				RPAREN
			) { nFlex = true; }
			)?
			{
				if( nFlex ) m = new NKategorieMerkmal( new NFlexMerkmalEinfach( v ) );
				else m = new NKategorieMerkmal();
			}
		| SYM_V RPAREN
			LPAREN SYM_G vgf:SYM_SYMBOL RPAREN
            ( LPAREN (SYM_HILFSVERB | SYM_MODALVERB) SYM_SYMBOL RPAREN )? f=vflex
			{
				stamm.grundformSetzen( vgf.getText() );
				m = new VKategorieMerkmal( f );
            }
		| SYM_NUM { m = new NumKategorieMerkmal(); }
		| SYM_ADV ( RPAREN
				LPAREN ( SYM_INTERROGADV { interrog = true; } 
				)
			)?
			{
				if( interrog ) m = new InterrogKategorieMerkmal();
				else m = new AdvKategorieMerkmal();
			}
		| SYM_ADJ ( RPAREN LPAREN SYM_G adjgf:SYM_SYMBOL RPAREN grad=gr RPAREN f=detflex
				{
					stamm.grundformSetzen( adjgf.getText() );
					m = new AdjGradKategorieMerkmal( grad, f );
				}
            )? { if( m == null ) m = new AdjGradKategorieMerkmal(); }
		| SYM_UNFLADJ RPAREN LPAREN SYM_G unfladjgf:SYM_SYMBOL RPAREN grad=gr
			{
				stamm.grundformSetzen( unfladjgf.getText() );
				m = new UnflAdjGradKategorieMerkmal( grad );
            }
		| SYM_P ( RPAREN LPAREN SYM_KASREK
			      LPAREN (kas=kasus
		            { feat = new UnpackedFeatureStructure();
						feat.add( FeatureName.forName( "kasrek" ),
							new StringValue( kas ) );
						v.add( feat.pack() );
					}
				)+
				RPAREN
				( RPAREN s=kontr
// gegenwaertig wird kontr (Kontraktion) nicht weiter verwendet...
//                                      {
//						feat.add( FeatureName.forName( "kontr" ),
//							new StringValue( s ) );
//					        v.add( feat.pack() );
					 { })?
	            { kasrek = true;
	            }
			)?
			{
				if( kasrek ) {
					m = new PKategorieMerkmal( new PFlexMerkmal( v ) );
				}
				else m = new PKategorieMerkmal();
			}
		| SYM_KONJ { m = new KonjKategorieMerkmal(); }
		| SYM_INTERJ { m = new InterjKategorieMerkmal(); }
		| SYM_DET RPAREN LPAREN SYM_G gf:SYM_SYMBOL RPAREN
            ( LPAREN SYM_POSS RPAREN { posspron = true; } )? f=detflex
            {
				if( posspron )
					m = new PossPronKategorieMerkmal( gf.getText(), f );
				else m = new DetKategorieMerkmal( gf.getText(), f );
			}
	  	| SYM_PRON ( RPAREN (LPAREN SYM_G prongf:SYM_SYMBOL RPAREN { pronGf = true; })? LPAREN SYM_TYP
			( s1:SYM_PERS
				{
					if( pronGf ) {
						stamm.grundformSetzen( prongf.getText() );
					}	
					s = s1.getText();
				}
			| s2:SYM_INTERROGPRON { s = s2.getText(); }
			| s3:SYM_INDEF { s = s3.getText(); }
            		| s4:SYM_REL { s = s4.getText(); }
            		| s5:SYM_DEMON { s = s5.getText(); }
            		| s6:SYM_REFL { s = s6.getText(); }
			)
			RPAREN
			v=pronflex[s]
		)?	// jedermann hat keine features
		        {
				m = new PronKategorieMerkmal( v );
		        }
		) RPAREN
	;

gr returns [FeatureStructure res]
{
	UnpackedFeatureStructure r = new UnpackedFeatureStructure();

	res = null;

	// RPAREN muessen vom Regelaufrufer getestet werden
	// (anders in flexive_parser.g)
}
	: LPAREN SYM_GRAD SYM_POS
		{ r.add( FeatureName.forName( "grad" ), new StringValue( "pos" ) );
		  res = r.pack();
		}
	| LPAREN SYM_GRAD SYM_KOMP
		{ r.add( FeatureName.forName( "grad" ), new StringValue( "komp" ) );
		  res = r.pack();
		}
	| LPAREN SYM_GRAD SYM_SUP
		{ r.add( FeatureName.forName( "grad" ), new StringValue( "sup" ) );
		  res = r.pack();
		};


// kontr repraesentiert Kontratktionen aus Praeposition und Artikel
// sinnvollerweise sollten die Werte beider SYM_SYMBOLE zurueckgegeben werden
kontr returns [String s]
{
	s = "";
}
	: LPAREN SYM_KONTR k:SYM_SYMBOL SYM_SYMBOL { s = k.getText(); };

pronflex [String s] returns [Vector featList]
{
	UnpackedFeatureStructure feat = new UnpackedFeatureStructure();
	FeatureStructure f;
	String person;

	featList = new Vector();
	f = null;
	feat.add( FeatureName.forName( "prontyp" ), new StringValue( s ) );

}
	: LPAREN SYM_FLEXION
		( (f=kasusnumerus[feat] { featList.add( f ); } )+
            {
            }
		| ( LPAREN feat=genus
				{  feat.add( FeatureName.forName( "prontyp" ),
						new StringValue( s ) );
				}
				(f=kasusnumerus[feat] { featList.add( f ); })+
				RPAREN
                  )+
		| ( LPAREN
                    person=ziff
                    ( ( LPAREN
                        feat=genus
                        {
			  feat.add( FeatureName.forName( "prontyp" ),
				    new StringValue( s ) );
                          feat.add( FeatureName.forName( "person" ),
                                    new StringValue( person ) );
                        }
                        (f=kasusnumerus[feat] { featList.add( f ); })+
                        RPAREN
                      )+
		    | ( {
		    	  feat.add(FeatureName.forName( "person" ),
		    	  	   new StringValue( person ) );
                        }
		        f=kasusnumerus[feat] { featList.add( f ); }
		      )+
                    )
                    RPAREN
                  )+
		)
	;

kasus returns [String s]
{
	s = "";
}
	: ( s1:SYM_NOM { s = s1.getText(); }
		| s2:SYM_GEN { s = s2.getText(); }
		| s3:SYM_DAT { s = s3.getText(); }
		| s4:SYM_AKK { s = s4.getText(); }
		)
	;

vflex returns [FlexMerkmal m]
{
	Vector v = new Vector();

	m = null;
}
	: LPAREN
		SYM_FLEXION
		(konjug[v])+		
		{
			m = new VFlexMerkmal( v );
		};

konjug [Vector v]
{
	FeatureStructure m, n, f;
	UnpackedFeatureStructure res;
	Vector p;
	int i, len, start = v.size();
}
	: LPAREN SYM_INF RPAREN
	 { res = new UnpackedFeatureStructure();
	   res.add( FeatureName.forName( "typ" ),new StringValue("infinitiv"));
           v.add( res.pack() );
	 }
	| LPAREN SYM_PPP RPAREN
	 { res = new UnpackedFeatureStructure();
	   res.add( FeatureName.forName( "typ" ),new StringValue("ppp"));
           v.add( res.pack() );
	 }
	| LPAREN SYM_IMP n=num RPAREN
	 { res = new UnpackedFeatureStructure();
	   res.add( FeatureName.forName( "typ" ),new StringValue("imperativ"));
	   f = res.pack();
           v.add( f.unification( n ) );
	 }
	| LPAREN
		m=modustempus
		(
			LPAREN
			n=num
			p=person
			RPAREN
			{
				len = p.size();
				for( i = 0; i < len; i++ ) {
					f = (FeatureStructure)p.elementAt( i );
					v.add( f.unification( n ) );
				}
			}
		)+
		RPAREN
		{
			// modustempus zu jedem Listenelement hinzufuegen
			len = v.size();
			for( i = start; i < len; i++ ) {
				f = (FeatureStructure)v.elementAt( i );
				v.setElementAt( f.unification( m ), i );
			}
		};

modustempus returns [FeatureStructure res]
{
	UnpackedFeatureStructure r = new UnpackedFeatureStructure();

	res = null;
}
	: SYM_IND_PRAES
		{ r.add( FeatureName.forName( "modus" ), new StringValue("indikativ"));
		  r.add( FeatureName.forName( "tempus" ), new StringValue("praesens"));
		  res = r.pack();
		}
	| SYM_KONJ_PRAES
		{ r.add( FeatureName.forName( "modus" ),
				 new StringValue( "konjunktiv" ) );
		  r.add( FeatureName.forName( "tempus" ),
				 new StringValue( "praesens" ) );
		  res = r.pack();
		}
	| SYM_IND_PRAET
		{ r.add( FeatureName.forName( "modus" ),new StringValue( "indikativ" ) );
	      r.add( FeatureName.forName( "tempus" ),
				   new StringValue( "praeteritum" ) );
		  res = r.pack();
		}
	| SYM_KONJ_PRAET
		{ r.add( FeatureName.forName( "modus" ),
				   new StringValue( "konjunktiv" ) );
	      r.add( FeatureName.forName( "tempus" ),
				   new StringValue( "praeteritum" ) );
		  res = r.pack();
		};

num returns [FeatureStructure res]
{
	UnpackedFeatureStructure r = new UnpackedFeatureStructure();

	res = null;
}
	: SYM_SG
		{ r.add( FeatureName.forName( "numerus" ),
				 new StringValue( "sg" ) );
		  res = r.pack();
		}
	| SYM_PL
		{ r.add( FeatureName.forName( "numerus" ),
				 new StringValue( "pl" ) );
		  res = r.pack();
		};

person returns [Vector v]
{
	String s;
	UnpackedFeatureStructure res;

	v = new Vector();
}
	: s=ziff
		{ res = new UnpackedFeatureStructure();
	      res.add( FeatureName.forName( "person" ),
				   new StringValue( s ) );
		  v.add( res.pack() );
		}
	| LPAREN
		(
			s=ziff
			{ res = new UnpackedFeatureStructure();
			  res.add( FeatureName.forName( "person" ),
				        new StringValue( s ) );
			  v.add( res.pack() );
			}
		)+
		RPAREN;

ziff returns [String s]
{
	s = "";
}
	: e:SYM_EINS { s = e.getText(); }
	| z:SYM_ZWEI { s = z.getText(); }
	| d:SYM_DREI { s = d.getText(); };

detflex returns [FlexMerkmal flex]
{
	UnpackedFeatureStructure gen;
	FeatureStructure res;
	String d;
	Vector featureList = new Vector();
	Hashtable dektypTab = new Hashtable();
	
	flex = null;
}
	: LPAREN
	  SYM_FLEXION
	  ( LPAREN
	    d=dektyp
            ( LPAREN
	      gen=genus
	      (res=kasusnumerus[gen]
		 { featureList.add( res ); }
	      )+
	      RPAREN
	    )+
	    RPAREN
	    { dektypTab.put( d, featureList );
	      featureList = new Vector(); }
	  )+
	   { flex = new NFlexMerkmalDektyp( dektypTab ); };

genus returns [UnpackedFeatureStructure res] {
	res = new UnpackedFeatureStructure();
}
	: SYM_MASK { res.add( FeatureName.forName( "genus" ),
			      new StringValue( "mask" ) );
	           }
	| SYM_FEM { res.add( FeatureName.forName( "genus" ),
		             new StringValue( "fem" ) ); }
	| SYM_NEUT { res.add( FeatureName.forName( "genus" ),
		              new StringValue( "neut" ) ); }
	| SYM_NIL { res.add( FeatureName.forName( "genus" ),
		             new StringValue( "nil" ) ); };

kasusnumerus [UnpackedFeatureStructure gen] returns [FeatureStructure res]
{
	UnpackedFeatureStructure r = (UnpackedFeatureStructure)gen.clone();

	res = null;
}
	: LPAREN SYM_NOM SYM_SING RPAREN
		{ r.add( FeatureName.forName( "kasus" ), new StringValue("nom") );
	      r.add( FeatureName.forName( "numerus" ), new StringValue("sg") );
		  res = r.pack();
		}
	| LPAREN SYM_GEN SYM_SING RPAREN
		{ r.add( FeatureName.forName( "kasus" ), new StringValue( "gen" ) );
		  r.add( FeatureName.forName( "numerus" ), new StringValue( "sg" ) );
		  res = r.pack();
		}
	| LPAREN SYM_DAT SYM_SING RPAREN
		{ r.add( FeatureName.forName( "kasus" ), new StringValue( "dat" ) );
	      r.add( FeatureName.forName( "numerus" ), new StringValue("sg") );
		  res = r.pack();
		}
	| LPAREN SYM_AKK SYM_SING RPAREN
		{ r.add( FeatureName.forName( "kasus" ), new StringValue( "akk") );
          r.add( FeatureName.forName( "numerus" ), new StringValue("sg") );
		  res = r.pack();
		}
	| LPAREN SYM_NOM SYM_PLUR RPAREN
		{ r.add( FeatureName.forName( "kasus" ), new StringValue( "nom") );
	      r.add( FeatureName.forName( "numerus" ), new StringValue("pl") );
		  res = r.pack();
		}
	| LPAREN SYM_GEN SYM_PLUR RPAREN
		{ r.add( FeatureName.forName( "kasus" ), new StringValue( "gen") );
	      r.add( FeatureName.forName( "numerus" ), new StringValue( "pl"));
		  res = r.pack();
		}
	| LPAREN SYM_DAT SYM_PLUR RPAREN
		{ r.add( FeatureName.forName( "kasus" ), new StringValue( "dat") );
	      r.add( FeatureName.forName( "numerus" ), new StringValue( "pl"));
		  res = r.pack();
		}
	| LPAREN SYM_AKK SYM_PLUR RPAREN
		{ r.add( FeatureName.forName( "kasus" ), new StringValue( "akk") );
	      r.add( FeatureName.forName( "numerus" ), new StringValue("pl"));
		  res = r.pack();
		};

dektyp returns [String s]
{
	s = "";
}
	: SYM_DEKTYPI { s = "dektypi"; }
	| SYM_DEKTYPII { s = "dektypii"; }
	| SYM_DEKTYPIII { s = "dektypiii"; };

class MorphLexer extends Lexer;
options {
  k = 1;
}

tokens {
  SYM_ORTNUMSUF = "ORTNUMSUF";
  SYM_NUMSUF = "NUMSUF";
  SYM_IND_PRAES = "IND_PRAESENS";
  SYM_KONJ_PRAES = "KONJ_PRAESENS";
  SYM_IND_PRAET = "IND_PRAETERITUM";
  SYM_KONJ_PRAET = "KONJ_PRAETERITUM";
  SYM_INF = "INFINITIV";
  SYM_IMP = "IMPERATIV";
  SYM_PPP = "PARTIZIP-PERFEKT";
  SYM_SG = "SG";
  SYM_PL = "PL";
  SYM_EINS = "1";
  SYM_ZWEI = "2";
  SYM_DREI = "3";
  SYM_HILFSVERB = "HILFSVERB";
  SYM_MODALVERB = "MODALVERB";
  SYM_N = "N";
  SYM_V = "V";
  SYM_NUM = "NUM";
  SYM_SEM = "SEM";
  SYM_SYN = "SYN";
  SYM_VPR = "VPR";
  SYM_FEM = "FEM";
  SYM_MASK = "MASK";
  SYM_NEUT = "NEUT";
  SYM_FLEXION = "FLEXION";
  SYM_DET = "DET";
  SYM_PRON = "PRON"; 
  SYM_DEKTYPI = "DEKTYPI";
  SYM_DEKTYPII = "DEKTYPII";
  SYM_DEKTYPIII = "DEKTYPIII";
  SYM_LEHNSUF = "LEHNSUF";
  SYM_KONJ = "KONJ";
  SYM_INTERJ = "INTERJ";
  SYM_P = "P";
  SYM_KASREK = "KASREK";
  SYM_NOM = "NOM";
  SYM_GEN = "GEN";
  SYM_DAT = "DAT";
  SYM_AKK = "AKK";
  SYM_SING = "SING";
  SYM_PLUR = "PLUR";
  SYM_TVZ = "TVZ";
  SYM_GRAD = "GRAD";
  SYM_POS = "POS";
  SYM_KOMP = "KOMP";
  SYM_SUP = "SUP";
  SYM_EQ = "=";
  SYM_MK = "MK";
  SYM_VF = "VF";
  SYM_KONFIXFUGE = "KONFIXFUGE";
  SYM_FUGE = "FUGE";
  SYM_PRAEFIX = "PRAEF";
  SYM_SUFFIX = "SUF";
  SYM_KONFIX = "KONFIX";
  SYM_PARTPRAEF = "PARTPRAEF";
  SYM_INFPART = "INFINITIVPARTIKEL";
  SYM_NOUNSUF = "NOUNSUF";
  SYM_BOUND = "BOUND";
  SYM_ABK = "ABK";
  SYM_ADV = "ADV";
  SYM_UNFLADJ = "UNFLADJ";
  SYM_ADJ = "ADJ";
  SYM_INTERROGADV = "INTERROGATIVADVERB";
  SYM_ALLO = "ALLO";
  SYM_GF = "GF_STAMM";
  SYM_G = "GF";
  SYM_MWTRIGG = "MWTRIGG";
  SYM_PERS = "PERSONAL";
  SYM_INTERROGPRON = "INTERROGATIV";
  SYM_INDEF = "INDEFINIT";
  SYM_REL = "RELATIV";
  SYM_REFL = "REFLEXIV";
  SYM_DEMON = "DEMONSTRATIV";
  SYM_POSS = "POSSESIVPRONOMEN";
  SYM_TYP = "TYP";
  SYM_KONTR = "KONTR";
  SYM_WBSUBCAT = "WB-SUBCAT";
}

WS: ( ' ' |
	'\r' |
      '\n' |
      '\t' ) { _ttype = Token.SKIP; };

LPAREN: '(';

RPAREN: ')';

SYM_EQ: "=";

SYM_SYMBOL: ( '_' | ':' | '-' | 'A'..'Z' | '0'..'9' | 'Ü' | 'Ä' | 'Ö')+;
