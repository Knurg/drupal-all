header {
package morph;
	import java.util.Vector;
	import java.util.Hashtable;
	import de.fau.cs.jill.feature.*;
}

class EndungParser extends Parser;
options {
	k = 3;
}


endung_liste returns [Hashtable d]
{
	EndungEintrag f;

	d = new Hashtable();
}
	: (f=endung_eintrag { d.put( f.holeEndung(), f ); })+;

endung_eintrag returns [EndungEintrag f]
{
	Vector v = new Vector();
	KategorieMerkmal m;
	EndungAktion a;
	String s;

	a = null;
	f = null;
	m = null;
}
	: LPAREN
		s=endung
		(
			LPAREN
			a=aktion
			m=merkmal
			RPAREN
			{ v.add( new AktionMerkmal( a, m ) ); }
		)+
		RPAREN
		{ f = new EndungEintrag( s, v ); };

flexionsklasse returns [String s]
{
	s = "";
}
	: sym:SYM_SYMBOL { s = sym.getText(); }
	;

endung returns [String s]
{
	s = "";
}
	: ( s1:SYM_SYMBOL { s = s1.getText(); }
		| s2:SYM_EN { s = s2.getText(); }
		| s3:SYM_N { s = s3.getText(); }
		| s4:SYM_NIL { s = s4.getText(); }
		| s5:SYM_EMPTYSTRING { s = ""; }
	 )
	;

aktion returns [EndungAktion a]
{
	EndungAktion s;
	String e, o;

	a = null;
}
	: LPAREN
	  ( SYM_CONCAT s=aktion e=endung { a = new ConcatAktion( s, e ); }
	  | SYM_CUT o=offset { a = new CutAktion( o ); }
	  )
	  RPAREN
	;

offset returns [String s]
{
	s = "";
}
	: ( arg:SYM_SYMBOL { s = arg.getText(); }
	  | e:SYM_EINS { s = e.getText(); }
	  | z:SYM_ZWEI { s = z.getText(); }
	  | d:SYM_DREI { s = d.getText(); }
	  )
	;

merkmal returns [KategorieMerkmal res]
{
	String category;
	FeatureStructure grad = null, p;
	FlexMerkmal f;

	res = null;
}
	: LPAREN
	  ( ( SYM_N RPAREN f=nflex { res = new NKategorieMerkmal( f ); }
	    | SYM_EN RPAREN f=nflex { res = new ENKategorieMerkmal( f ); }
	    )
		| SYM_ADV RPAREN { res = new AdvKategorieMerkmal(); }
		| ( SYM_V) RPAREN f=vflex  { res = new VKategorieMerkmal(); } 
		| SYM_UNFLADJ RPAREN
			( ( LPAREN SYM_PARTPRAES RPAREN )
				{
					res = new UnflAdjPartKategorieMerkmal();
				}
			| grad=gr { res = new UnflAdjGradKategorieMerkmal( grad ); } )
		| SYM_ADJ RPAREN
			( ( grad=gr { res = new AdjGradKategorieMerkmal( grad ); } ) f=aflex { res.setFlexRef( f ); }
				
			| ( p=partyp { res = new AdjPartKategorieMerkmal( p ); } ) f=aflex { res.setFlexRef( f ); }
			| ( LPAREN
					( SYM_KARD { res = new AdjKardKategorieMerkmal(); }
					| SYM_ORD { res = new AdjOrdKategorieMerkmal(); } )
					RPAREN
				)?
				f=aflex {res.setFlexRef(f);}
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
	String d, fl, flk;
	Vector featureList = new Vector();
	Hashtable dektypTab = new Hashtable();
	
	flex = null;
}
	: LPAREN
	  ( SYM_FLEXION
	   ( LPAREN
	     gen=genus
	     ( res=kasusnumerus[gen] { featureList.add( res ); } )+
	     RPAREN
	     RPAREN
	     { flex = new NFlexMerkmalEinfach( featureList ); }
	   | 
	     ( LPAREN
	       d=dektyp
	       ( LPAREN
	         gen=genus
	         ( res=kasusnumerus[gen] { featureList.add( res ); } )+
	         RPAREN
	       )+
	       RPAREN
	       { dektypTab.put( d, featureList );
                 featureList = new Vector();
               }
	     )+
             RPAREN { flex = new NFlexMerkmalDektyp( dektypTab ); }
           )
	| SYM_EQ SYM_FLEXION fl=endung flk=flexionsklasse RPAREN
	  { flex = new NFlexRefMerkmal( fl, flk ); }
        )
	;

dektyp returns [String s]
{
	s = "";
}
	: SYM_DEKTYPI { s = "dektypi"; }
	| SYM_DEKTYPII { s = "dektypii"; }
	| SYM_DEKTYPIII { s = "dektypiii"; };

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

vflex returns [FlexMerkmal res]
{
res = null;
	String fl, flk;
}
	: LPAREN SYM_EQ SYM_FLEXION fl=endung flk=flexionsklasse RPAREN
	{	
	  res = new VFlexRefMerkmal( fl, flk );
	}
	;

aflex returns [FlexMerkmal res]
{
res = null;
	String fl, flk;
}
	: LPAREN SYM_EQ SYM_FLEXION fl=endung flk=flexionsklasse RPAREN
	{	
	  res = new AdjFlexRefMerkmal( fl, flk );
	}
	;

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


ziff returns [String s]
{
	s = "";
}
	: e:SYM_EINS { s = e.getText(); }
	| z:SYM_ZWEI { s = z.getText(); }
	| d:SYM_DREI { s = d.getText(); };

class EndungLexer extends Lexer;

tokens {
  SYM_EINS = "1";
  SYM_ZWEI = "2";
  SYM_DREI = "3";
  SYM_V = "V";
  SYM_ADV = "ADV";
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
  SYM_CUT = "CUT";
  SYM_CONCAT = "CONCAT";
}

WS: ( ' ' |
		'\r'
		| '\n'
		| '\t' )
		{ _ttype = Token.SKIP; };

LPAREN: '(';

RPAREN: ')';

SYM_EQ: "=";

SYM_SYMBOL: ( '_' | ':' | '-' | 'A'..'Z' | '0'..'9' )+;

SYM_EMPTYSTRING: '"''"';
