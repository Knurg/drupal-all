header {
package morph;
	import java.util.Vector;
	import java.util.Hashtable;
	import de.fau.cs.jill.feature.*;
}

class FlexiveParser extends Parser;
options {
	k = 3;
}


flexiv_liste returns [Hashtable d]
{
	FlexivEintrag f;

	d = new Hashtable();
}
	: (f=flexiv_eintrag { d.put( f.holeFlexiv(), f ); })+;

flexiv_eintrag returns [FlexivEintrag f]
{
	Vector v = new Vector(), k, m;
	String s;

	f = null;
	k = null;
	m = null;
}
	: LPAREN
		s=flexiv
		(
			LPAREN
			k=klassen
			m=merkmale
			RPAREN
			{ v.add( new KlassenMerkmale( k, m ) ); }
		)+
		RPAREN
		{ f = new FlexivEintrag( s, v ); };

flexiv returns [String s]
{
	s = "";
}
	: ( s1:SYM_SYMBOL { s = s1.getText(); }
		| s2:SYM_EN { s = s2.getText(); }
		| s3:SYM_N { s = s3.getText(); }
		| s4:SYM_NIL { s = s4.getText(); } );

klassen returns [Vector v]
{
	String s;

	v = new Vector();
}
	: LPAREN (s=flexionsklasse { v.add( s ); })+ RPAREN;

flexionsklasse returns [String s]
{
	s = "";
}
	: sym:SYM_SYMBOL { s = sym.getText(); };


merkmale returns [Vector v]
{
	KategorieMerkmal k;

	v = null;
}
	: v=merkmal_liste
	| k=merkmal { v = new Vector(); v.add( k ); };

merkmal returns [KategorieMerkmal res]
{
	String category, fl, flk;
	FlexMerkmal f;
	FeatureStructure grad = null, p;
	
	res = null;
}
	: LPAREN
		( ( nCat:SYM_N { category = nCat.getText(); }
			| enCat:SYM_EN { category = enCat.getText(); } )
			RPAREN
			f=nflex { if( category.equals( "N" ) ) res = new NKategorieMerkmal( f ); else res = new ENKategorieMerkmal( f ); }
		| SYM_V RPAREN f=vflex { res = new VKategorieMerkmal( f ); }
		| SYM_UNFLADJ RPAREN
			( ( LPAREN SYM_PARTPRAES RPAREN )
				{
					res = new UnflAdjPartKategorieMerkmal();
				}
			| grad=gr { res = new UnflAdjGradKategorieMerkmal( grad ); } )
		| SYM_ADJ RPAREN
			( grad=gr f=aflex
				{ res = new AdjGradKategorieMerkmal( grad, f ); }
			| p=partyp f=aflex { res = new AdjPartKategorieMerkmal( p, f ); }
			| ( LPAREN
					( SYM_KARD { res = new AdjKardKategorieMerkmal(); }
					| SYM_ORD { res = new AdjOrdKategorieMerkmal(); } )
					RPAREN
					( LPAREN
						SYM_EQ
						SYM_FLEXION
						fl=flexiv
						flk=flexionsklasse
						RPAREN
						{
							((AdjKategorieMerkmal)res).setFlexRef( new AdjFlexRefMerkmal( fl, flk ) );
						}
					)?
				)?
			)
		);

merkmal_liste returns [Vector v]
{
	KategorieMerkmal k;
	
	v = new Vector();
}
	: ( LPAREN SYM_L k=merkmal RPAREN { v.add( k ); } )+;

nflex returns [FlexMerkmal flex]
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
			gen=genus
			( res=kasusnumerus[gen] { featureList.add( res ); } )+
			RPAREN
			RPAREN
			{ flex = new NFlexMerkmalEinfach( featureList );
			}
		| 
			( LPAREN
				d=dektyp
				( LPAREN
					gen=genus
					(res=kasusnumerus[gen] { featureList.add( res ); } )+
					RPAREN
				)+
				RPAREN
				{ dektypTab.put( d, featureList );
	                          featureList = new Vector();
		 	        }
			)+
			RPAREN { flex = new NFlexMerkmalDektyp( dektypTab ); }
		);

genus returns [UnpackedFeatureStructure res] {
	res = new UnpackedFeatureStructure();
}
	: SYM_MASK { res.add( FeatureName.forName( "genus" ),
			       	    new StringValue( "mask" ) ); }
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

gr returns [FeatureStructure res]
{
	UnpackedFeatureStructure r = new UnpackedFeatureStructure();

	res = null;
}
	: LPAREN SYM_GRAD SYM_POS RPAREN
		{ r.add( FeatureName.forName( "grad" ), new StringValue( "pos" ) );
		  res = r.pack();
		}
	| LPAREN SYM_GRAD SYM_KOMP RPAREN
		{ r.add( FeatureName.forName( "grad" ), new StringValue( "komp" ) );
		  res = r.pack();
		}
	| LPAREN SYM_GRAD SYM_SUP RPAREN
		{ r.add( FeatureName.forName( "grad" ), new StringValue( "sup" ) );
		  res = r.pack();
		};

aflex returns [FlexMerkmal m]
{
	UnpackedFeatureStructure gen;
	FeatureStructure res;
	Vector featureList = new Vector();
	Hashtable dektypTab = new Hashtable();
	String d, f, fk;

	m = null;
}
	: LPAREN
		(
			( SYM_FLEXION
				( LPAREN
					d=dektyp
					( LPAREN
						gen=genus
						(res=kasusnumerus[gen] { featureList.add( res ); } )+
						RPAREN
					)+
					RPAREN
	                                { dektypTab.put( d, featureList );
					  featureList = new Vector();
					} )+
				RPAREN
			) { m = new AdjFlexMerkmal( dektypTab ); }
		| ( SYM_EQ SYM_FLEXION f=flexiv fk=flexionsklasse RPAREN )
			{ m = new AdjFlexRefMerkmal( f, fk ); }
		);

dektyp returns [String s]
{
	s = "";
}
	: SYM_DEKTYPI { s = "dektypi"; }
	| SYM_DEKTYPII { s = "dektypii"; }
	| SYM_DEKTYPIII { s = "dektypiii"; };

partyp returns [FeatureStructure res]
{
	UnpackedFeatureStructure r = new UnpackedFeatureStructure();
	res = null;
}
	: LPAREN SYM_PART_ADJ_PRAES RPAREN
		{ r.add( FeatureName.forName( "typ" ),
				 new StringValue( "partpraes" ) );
		  res = r.pack();
		}
	| LPAREN SYM_PART_ADJ_PERF RPAREN
		{ r.add( FeatureName.forName( "typ" ),
			     new StringValue( "partperf" ) ); 
		  res = r.pack();
		};

vflex returns [FlexMerkmal m]
{
	Vector v = new Vector();

	m = null;
}
	: LPAREN
		SYM_FLEXION
		(konjug[v])+
		RPAREN
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
		{ r.add( FeatureName.forName( "modus" ), new StringValue( "indikativ" ) );
	      r.add( FeatureName.forName( "tempus" ), new StringValue( "praeteritum" ) );
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

class FlexiveLexer extends Lexer;

tokens {
  SYM_EINS = "1";
  SYM_ZWEI = "2";
  SYM_DREI = "3";
  SYM_V = "V";
  SYM_N = "N";
  SYM_EN = "EN";
  SYM_L = "L";
  SYM_FEM = "FEM";
  SYM_IMP = "IMPERATIV";
  SYM_INF = "INFINITIV";
  SYM_PPP = "PARTIZIP-PERFEKT";
  SYM_IND_PRAES = "IND_PRAESENS";
  SYM_KONJ_PRAES = "KONJ_PRAESENS";
  SYM_IND_PRAET = "IND_PRAETERITUM";
  SYM_KONJ_PRAET = "KONJ_PRAETERITUM";
  SYM_SG = "SG";
  SYM_PL = "PL";
  SYM_PART_ADJ_PRAES = "PART-ADJ-PRAESENS";
  SYM_PART_ADJ_PERF = "PART-ADJ-PERFEKT";
  SYM_FLEXION = "FLEXION";
  SYM_PARTPRAES = "PARTIZIP-PRAESENS";
  SYM_NOM = "NOM";
  SYM_GEN = "GEN";
  SYM_DAT = "DAT";
  SYM_AKK = "AKK";
  SYM_SING = "SING";
  SYM_PLUR = "PLUR";
  SYM_UNFLADJ = "UNFLADJ";
  SYM_ADJ = "ADJ";
  SYM_GRAD = "GRAD";
  SYM_POS = "POS";
  SYM_KOMP = "KOMP";
  SYM_SUP = "SUP";
  SYM_DEKTYPI = "DEKTYPI";
  SYM_DEKTYPII = "DEKTYPII";
  SYM_DEKTYPIII = "DEKTYPIII";
  SYM_MASK = "MASK";
  SYM_NEUT = "NEUT";
  SYM_NIL = "NIL";
  SYM_KARD = "KARDINALZAHL";
  SYM_ORD = "ORDINALZAHL";
}

WS: ( ' '
		| '\n'
		| '\r'
		| '\t' )
		{ _ttype = Token.SKIP; };

LPAREN: '(';

RPAREN: ')';

SYM_EQ: "=";

SYM_SYMBOL: ( '_' | ':' | '-' | 'A'..'Z' | '0'..'9' )+;
