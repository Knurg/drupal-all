// $ANTLR : "endung_parser.g" -> "EndungParser.java"$

package morph;
	import java.util.Vector;
	import java.util.Hashtable;
	import de.fau.cs.jill.feature.*;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class EndungParser extends antlr.LLkParser       implements EndungParserTokenTypes
 {

protected EndungParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public EndungParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected EndungParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public EndungParser(TokenStream lexer) {
  this(lexer,3);
}

public EndungParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
}

	public final Hashtable  endung_liste() throws RecognitionException, TokenStreamException {
		Hashtable d;
		
		
			EndungEintrag f;
		
			d = new Hashtable();
		
		
		try {      // for error handling
			{
			int _cnt525=0;
			_loop525:
			do {
				if ((LA(1)==LPAREN)) {
					f=endung_eintrag();
					d.put( f.holeEndung(), f );
				}
				else {
					if ( _cnt525>=1 ) { break _loop525; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt525++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return d;
	}
	
	public final EndungEintrag  endung_eintrag() throws RecognitionException, TokenStreamException {
		EndungEintrag f;
		
		
			Vector v = new Vector();
			KategorieMerkmal m;
			EndungAktion a;
			String s;
		
			a = null;
			f = null;
			m = null;
		
		
		try {      // for error handling
			match(LPAREN);
			s=endung();
			{
			int _cnt528=0;
			_loop528:
			do {
				if ((LA(1)==LPAREN)) {
					match(LPAREN);
					a=aktion();
					m=merkmal();
					match(RPAREN);
					v.add( new AktionMerkmal( a, m ) );
				}
				else {
					if ( _cnt528>=1 ) { break _loop528; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt528++;
			} while (true);
			}
			match(RPAREN);
			f = new EndungEintrag( s, v );
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return f;
	}
	
	public final String  endung() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  s1 = null;
		Token  s2 = null;
		Token  s3 = null;
		Token  s4 = null;
		Token  s5 = null;
		
			s = "";
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SYM_SYMBOL:
			{
				s1 = LT(1);
				match(SYM_SYMBOL);
				s = s1.getText();
				break;
			}
			case SYM_EN:
			{
				s2 = LT(1);
				match(SYM_EN);
				s = s2.getText();
				break;
			}
			case SYM_N:
			{
				s3 = LT(1);
				match(SYM_N);
				s = s3.getText();
				break;
			}
			case SYM_NIL:
			{
				s4 = LT(1);
				match(SYM_NIL);
				s = s4.getText();
				break;
			}
			case SYM_EMPTYSTRING:
			{
				s5 = LT(1);
				match(SYM_EMPTYSTRING);
				s = "";
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return s;
	}
	
	public final EndungAktion  aktion() throws RecognitionException, TokenStreamException {
		EndungAktion a;
		
		
			EndungAktion s;
			String e, o;
		
			a = null;
		
		
		try {      // for error handling
			match(LPAREN);
			{
			switch ( LA(1)) {
			case SYM_CONCAT:
			{
				match(SYM_CONCAT);
				s=aktion();
				e=endung();
				a = new ConcatAktion( s, e );
				break;
			}
			case SYM_CUT:
			{
				match(SYM_CUT);
				o=offset();
				a = new CutAktion( o );
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return a;
	}
	
	public final KategorieMerkmal  merkmal() throws RecognitionException, TokenStreamException {
		KategorieMerkmal res;
		
		
			String category;
			FeatureStructure grad = null, p;
			FlexMerkmal f;
		
			res = null;
		
		
		try {      // for error handling
			match(LPAREN);
			{
			switch ( LA(1)) {
			case SYM_EN:
			case SYM_N:
			{
				{
				switch ( LA(1)) {
				case SYM_N:
				{
					match(SYM_N);
					match(RPAREN);
					f=nflex();
					res = new NKategorieMerkmal( f );
					break;
				}
				case SYM_EN:
				{
					match(SYM_EN);
					match(RPAREN);
					f=nflex();
					res = new ENKategorieMerkmal( f );
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case SYM_ADV:
			{
				match(SYM_ADV);
				match(RPAREN);
				res = new AdvKategorieMerkmal();
				break;
			}
			case SYM_V:
			{
				{
				match(SYM_V);
				}
				match(RPAREN);
				f=vflex();
				res = new VKategorieMerkmal();
				break;
			}
			case SYM_UNFLADJ:
			{
				match(SYM_UNFLADJ);
				match(RPAREN);
				{
				if ((LA(1)==LPAREN) && (LA(2)==SYM_PARTPRAES)) {
					{
					match(LPAREN);
					match(SYM_PARTPRAES);
					match(RPAREN);
					}
					
										res = new UnflAdjPartKategorieMerkmal();
									
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_GRAD)) {
					grad=gr();
					res = new UnflAdjGradKategorieMerkmal( grad );
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				break;
			}
			case SYM_ADJ:
			{
				match(SYM_ADJ);
				match(RPAREN);
				{
				if ((LA(1)==LPAREN) && (LA(2)==SYM_GRAD)) {
					{
					grad=gr();
					res = new AdjGradKategorieMerkmal( grad );
					}
					f=aflex();
					res.setFlexRef( f );
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_PART_ADJ_PRAES||LA(2)==SYM_PART_ADJ_PERF)) {
					{
					p=partyp();
					res = new AdjPartKategorieMerkmal( p );
					}
					f=aflex();
					res.setFlexRef( f );
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_KARD||LA(2)==SYM_ORD||LA(2)==SYM_EQ)) {
					{
					if ((LA(1)==LPAREN) && (LA(2)==SYM_KARD||LA(2)==SYM_ORD)) {
						match(LPAREN);
						{
						switch ( LA(1)) {
						case SYM_KARD:
						{
							match(SYM_KARD);
							res = new AdjKardKategorieMerkmal();
							break;
						}
						case SYM_ORD:
						{
							match(SYM_ORD);
							res = new AdjOrdKategorieMerkmal();
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						match(RPAREN);
					}
					else if ((LA(1)==LPAREN) && (LA(2)==SYM_EQ)) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					f=aflex();
					res.setFlexRef(f);
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return res;
	}
	
	public final String  flexionsklasse() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  sym = null;
		
			s = "";
		
		
		try {      // for error handling
			sym = LT(1);
			match(SYM_SYMBOL);
			s = sym.getText();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return s;
	}
	
	public final String  offset() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  arg = null;
		Token  e = null;
		Token  z = null;
		Token  d = null;
		
			s = "";
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SYM_SYMBOL:
			{
				arg = LT(1);
				match(SYM_SYMBOL);
				s = arg.getText();
				break;
			}
			case SYM_EINS:
			{
				e = LT(1);
				match(SYM_EINS);
				s = e.getText();
				break;
			}
			case SYM_ZWEI:
			{
				z = LT(1);
				match(SYM_ZWEI);
				s = z.getText();
				break;
			}
			case SYM_DREI:
			{
				d = LT(1);
				match(SYM_DREI);
				s = d.getText();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return s;
	}
	
	public final FlexMerkmal  nflex() throws RecognitionException, TokenStreamException {
		FlexMerkmal flex;
		
		
			UnpackedFeatureStructure gen;
			FeatureStructure res;
			String d, fl, flk;
			Vector featureList = new Vector();
			Hashtable dektypTab = new Hashtable();
			
			flex = null;
		
		
		try {      // for error handling
			match(LPAREN);
			{
			switch ( LA(1)) {
			case SYM_FLEXION:
			{
				match(SYM_FLEXION);
				{
				if ((LA(1)==LPAREN) && (_tokenSet_5.member(LA(2)))) {
					match(LPAREN);
					gen=genus();
					{
					int _cnt554=0;
					_loop554:
					do {
						if ((LA(1)==LPAREN)) {
							res=kasusnumerus(gen);
							featureList.add( res );
						}
						else {
							if ( _cnt554>=1 ) { break _loop554; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt554++;
					} while (true);
					}
					match(RPAREN);
					match(RPAREN);
					flex = new NFlexMerkmalEinfach( featureList );
				}
				else if ((LA(1)==LPAREN) && ((LA(2) >= SYM_DEKTYPI && LA(2) <= SYM_DEKTYPIII))) {
					{
					int _cnt560=0;
					_loop560:
					do {
						if ((LA(1)==LPAREN)) {
							match(LPAREN);
							d=dektyp();
							{
							int _cnt559=0;
							_loop559:
							do {
								if ((LA(1)==LPAREN)) {
									match(LPAREN);
									gen=genus();
									{
									int _cnt558=0;
									_loop558:
									do {
										if ((LA(1)==LPAREN)) {
											res=kasusnumerus(gen);
											featureList.add( res );
										}
										else {
											if ( _cnt558>=1 ) { break _loop558; } else {throw new NoViableAltException(LT(1), getFilename());}
										}
										
										_cnt558++;
									} while (true);
									}
									match(RPAREN);
								}
								else {
									if ( _cnt559>=1 ) { break _loop559; } else {throw new NoViableAltException(LT(1), getFilename());}
								}
								
								_cnt559++;
							} while (true);
							}
							match(RPAREN);
							dektypTab.put( d, featureList );
							featureList = new Vector();
							
						}
						else {
							if ( _cnt560>=1 ) { break _loop560; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt560++;
					} while (true);
					}
					match(RPAREN);
					flex = new NFlexMerkmalDektyp( dektypTab );
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				break;
			}
			case SYM_EQ:
			{
				match(SYM_EQ);
				match(SYM_FLEXION);
				fl=endung();
				flk=flexionsklasse();
				match(RPAREN);
				flex = new NFlexRefMerkmal( fl, flk );
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return flex;
	}
	
	public final FlexMerkmal  vflex() throws RecognitionException, TokenStreamException {
		FlexMerkmal res;
		
		
		res = null;
			String fl, flk;
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_EQ);
			match(SYM_FLEXION);
			fl=endung();
			flk=flexionsklasse();
			match(RPAREN);
				
				  res = new VFlexRefMerkmal( fl, flk );
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return res;
	}
	
	public final FeatureStructure  gr() throws RecognitionException, TokenStreamException {
		FeatureStructure res;
		
		
			UnpackedFeatureStructure r = new UnpackedFeatureStructure();
		
			res = null;
		
		
		try {      // for error handling
			if ((LA(1)==LPAREN) && (LA(2)==SYM_GRAD) && (LA(3)==SYM_POS)) {
				match(LPAREN);
				match(SYM_GRAD);
				match(SYM_POS);
				match(RPAREN);
				r.add( FeatureName.forName( "grad" ), new StringValue( "pos" ) );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_GRAD) && (LA(3)==SYM_KOMP)) {
				match(LPAREN);
				match(SYM_GRAD);
				match(SYM_KOMP);
				match(RPAREN);
				r.add( FeatureName.forName( "grad" ), new StringValue( "komp" ) );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_GRAD) && (LA(3)==SYM_SUP)) {
				match(LPAREN);
				match(SYM_GRAD);
				match(SYM_SUP);
				match(RPAREN);
				r.add( FeatureName.forName( "grad" ), new StringValue( "sup" ) );
						  res = r.pack();
						
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		return res;
	}
	
	public final FlexMerkmal  aflex() throws RecognitionException, TokenStreamException {
		FlexMerkmal res;
		
		
		res = null;
			String fl, flk;
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_EQ);
			match(SYM_FLEXION);
			fl=endung();
			flk=flexionsklasse();
			match(RPAREN);
				
				  res = new AdjFlexRefMerkmal( fl, flk );
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return res;
	}
	
	public final FeatureStructure  partyp() throws RecognitionException, TokenStreamException {
		FeatureStructure res;
		
		
			UnpackedFeatureStructure r = new UnpackedFeatureStructure();
			res = null;
		
		
		try {      // for error handling
			if ((LA(1)==LPAREN) && (LA(2)==SYM_PART_ADJ_PRAES)) {
				match(LPAREN);
				match(SYM_PART_ADJ_PRAES);
				match(RPAREN);
				r.add( FeatureName.forName( "typ" ),
								 new StringValue( "partpraes" ) );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_PART_ADJ_PERF)) {
				match(LPAREN);
				match(SYM_PART_ADJ_PERF);
				match(RPAREN);
				r.add( FeatureName.forName( "typ" ),
							     new StringValue( "partperf" ) ); 
						  res = r.pack();
						
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		return res;
	}
	
	public final Vector  merkmal_liste() throws RecognitionException, TokenStreamException {
		Vector v;
		
		
			KategorieMerkmal k;
			
			v = new Vector();
		
		
		try {      // for error handling
			{
			int _cnt549=0;
			_loop549:
			do {
				if ((LA(1)==LPAREN)) {
					match(LPAREN);
					match(SYM_L);
					k=merkmal();
					match(RPAREN);
					v.add( k );
				}
				else {
					if ( _cnt549>=1 ) { break _loop549; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt549++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return v;
	}
	
	public final UnpackedFeatureStructure  genus() throws RecognitionException, TokenStreamException {
		UnpackedFeatureStructure res;
		
		
			res = new UnpackedFeatureStructure();
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SYM_MASK:
			{
				match(SYM_MASK);
				res.add( FeatureName.forName( "genus" ),
							       	    new StringValue( "mask" ) );
				break;
			}
			case SYM_FEM:
			{
				match(SYM_FEM);
				res.add( FeatureName.forName( "genus" ),
								       new StringValue( "fem" ) );
				break;
			}
			case SYM_NEUT:
			{
				match(SYM_NEUT);
				res.add( FeatureName.forName( "genus" ),
								        new StringValue( "neut" ) );
				break;
			}
			case SYM_NIL:
			{
				match(SYM_NIL);
				res.add( FeatureName.forName( "genus" ),
								       new StringValue( "nil" ) );
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		return res;
	}
	
	public final FeatureStructure  kasusnumerus(
		UnpackedFeatureStructure gen
	) throws RecognitionException, TokenStreamException {
		FeatureStructure res;
		
		
			UnpackedFeatureStructure r = (UnpackedFeatureStructure)gen.clone();
		
			res = null;
		
		
		try {      // for error handling
			if ((LA(1)==LPAREN) && (LA(2)==SYM_NOM) && (LA(3)==SYM_SING)) {
				match(LPAREN);
				match(SYM_NOM);
				match(SYM_SING);
				match(RPAREN);
				r.add( FeatureName.forName( "kasus" ), new StringValue("nom") );
					      r.add( FeatureName.forName( "numerus" ), new StringValue("sg") );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_GEN) && (LA(3)==SYM_SING)) {
				match(LPAREN);
				match(SYM_GEN);
				match(SYM_SING);
				match(RPAREN);
				r.add( FeatureName.forName( "kasus" ), new StringValue( "gen" ) );
						  r.add( FeatureName.forName( "numerus" ), new StringValue( "sg" ) );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_DAT) && (LA(3)==SYM_SING)) {
				match(LPAREN);
				match(SYM_DAT);
				match(SYM_SING);
				match(RPAREN);
				r.add( FeatureName.forName( "kasus" ), new StringValue( "dat" ) );
					      r.add( FeatureName.forName( "numerus" ), new StringValue("sg") );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_AKK) && (LA(3)==SYM_SING)) {
				match(LPAREN);
				match(SYM_AKK);
				match(SYM_SING);
				match(RPAREN);
				r.add( FeatureName.forName( "kasus" ), new StringValue( "akk") );
				r.add( FeatureName.forName( "numerus" ), new StringValue("sg") );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_NOM) && (LA(3)==SYM_PLUR)) {
				match(LPAREN);
				match(SYM_NOM);
				match(SYM_PLUR);
				match(RPAREN);
				r.add( FeatureName.forName( "kasus" ), new StringValue( "nom") );
					      r.add( FeatureName.forName( "numerus" ), new StringValue("pl") );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_GEN) && (LA(3)==SYM_PLUR)) {
				match(LPAREN);
				match(SYM_GEN);
				match(SYM_PLUR);
				match(RPAREN);
				r.add( FeatureName.forName( "kasus" ), new StringValue( "gen") );
					      r.add( FeatureName.forName( "numerus" ), new StringValue( "pl"));
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_DAT) && (LA(3)==SYM_PLUR)) {
				match(LPAREN);
				match(SYM_DAT);
				match(SYM_PLUR);
				match(RPAREN);
				r.add( FeatureName.forName( "kasus" ), new StringValue( "dat") );
					      r.add( FeatureName.forName( "numerus" ), new StringValue( "pl"));
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_AKK) && (LA(3)==SYM_PLUR)) {
				match(LPAREN);
				match(SYM_AKK);
				match(SYM_PLUR);
				match(RPAREN);
				r.add( FeatureName.forName( "kasus" ), new StringValue( "akk") );
					      r.add( FeatureName.forName( "numerus" ), new StringValue("pl"));
						  res = r.pack();
						
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		return res;
	}
	
	public final String  dektyp() throws RecognitionException, TokenStreamException {
		String s;
		
		
			s = "";
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SYM_DEKTYPI:
			{
				match(SYM_DEKTYPI);
				s = "dektypi";
				break;
			}
			case SYM_DEKTYPII:
			{
				match(SYM_DEKTYPII);
				s = "dektypii";
				break;
			}
			case SYM_DEKTYPIII:
			{
				match(SYM_DEKTYPIII);
				s = "dektypiii";
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		return s;
	}
	
	public final String  ziff() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  e = null;
		Token  z = null;
		Token  d = null;
		
			s = "";
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SYM_EINS:
			{
				e = LT(1);
				match(SYM_EINS);
				s = e.getText();
				break;
			}
			case SYM_ZWEI:
			{
				z = LT(1);
				match(SYM_ZWEI);
				s = z.getText();
				break;
			}
			case SYM_DREI:
			{
				d = LT(1);
				match(SYM_DREI);
				s = d.getText();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return s;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"LPAREN",
		"RPAREN",
		"SYM_SYMBOL",
		"\"EN\"",
		"\"N\"",
		"\"NIL\"",
		"SYM_EMPTYSTRING",
		"\"CONCAT\"",
		"\"CUT\"",
		"\"1\"",
		"\"2\"",
		"\"3\"",
		"\"ADV\"",
		"\"V\"",
		"\"UNFLADJ\"",
		"\"PARTIZIP-PRAESENS\"",
		"\"ADJ\"",
		"\"KARDINALZAHL\"",
		"\"ORDINALZAHL\"",
		"\"L\"",
		"\"FLEXION\"",
		"SYM_EQ",
		"\"DEKTYPI\"",
		"\"DEKTYPII\"",
		"\"DEKTYPIII\"",
		"\"MASK\"",
		"\"FEM\"",
		"\"NEUT\"",
		"\"NOM\"",
		"\"SING\"",
		"\"GEN\"",
		"\"DAT\"",
		"\"AKK\"",
		"\"PLUR\"",
		"\"GRAD\"",
		"\"POS\"",
		"\"KOMP\"",
		"\"SUP\"",
		"\"PART-ADJ-PRAESENS\"",
		"\"PART-ADJ-PERFEKT\"",
		"\"IMPERATIV\"",
		"\"INFINITIV\"",
		"\"PARTIZIP-PERFEKT\"",
		"\"IND_PRAESENS\"",
		"\"KONJ_PRAESENS\"",
		"\"IND_PRAETERITUM\"",
		"\"KONJ_PRAETERITUM\"",
		"\"SG\"",
		"\"PL\"",
		"WS"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 18L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 112L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 2000L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 32L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 3758096896L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 48L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 16L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	
	}
