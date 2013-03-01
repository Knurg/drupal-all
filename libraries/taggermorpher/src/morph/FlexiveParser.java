// $ANTLR : "flexive_parser.g" -> "FlexiveParser.java"$

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

public class FlexiveParser extends antlr.LLkParser       implements FlexiveParserTokenTypes
 {

protected FlexiveParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public FlexiveParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected FlexiveParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public FlexiveParser(TokenStream lexer) {
  this(lexer,3);
}

public FlexiveParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
}

	public final Hashtable  flexiv_liste() throws RecognitionException, TokenStreamException {
		Hashtable d;
		
		
			FlexivEintrag f;
		
			d = new Hashtable();
		
		
		try {      // for error handling
			{
			int _cnt440=0;
			_loop440:
			do {
				if ((LA(1)==LPAREN)) {
					f=flexiv_eintrag();
					d.put( f.holeFlexiv(), f );
				}
				else {
					if ( _cnt440>=1 ) { break _loop440; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt440++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return d;
	}
	
	public final FlexivEintrag  flexiv_eintrag() throws RecognitionException, TokenStreamException {
		FlexivEintrag f;
		
		
			Vector v = new Vector(), k, m;
			String s;
		
			f = null;
			k = null;
			m = null;
		
		
		try {      // for error handling
			match(LPAREN);
			s=flexiv();
			{
			int _cnt443=0;
			_loop443:
			do {
				if ((LA(1)==LPAREN)) {
					match(LPAREN);
					k=klassen();
					m=merkmale();
					match(RPAREN);
					v.add( new KlassenMerkmale( k, m ) );
				}
				else {
					if ( _cnt443>=1 ) { break _loop443; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt443++;
			} while (true);
			}
			match(RPAREN);
			f = new FlexivEintrag( s, v );
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return f;
	}
	
	public final String  flexiv() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  s1 = null;
		Token  s2 = null;
		Token  s3 = null;
		Token  s4 = null;
		
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
	
	public final Vector  klassen() throws RecognitionException, TokenStreamException {
		Vector v;
		
		
			String s;
		
			v = new Vector();
		
		
		try {      // for error handling
			match(LPAREN);
			{
			int _cnt448=0;
			_loop448:
			do {
				if ((LA(1)==SYM_SYMBOL)) {
					s=flexionsklasse();
					v.add( s );
				}
				else {
					if ( _cnt448>=1 ) { break _loop448; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt448++;
			} while (true);
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return v;
	}
	
	public final Vector  merkmale() throws RecognitionException, TokenStreamException {
		Vector v;
		
		
			KategorieMerkmal k;
		
			v = null;
		
		
		try {      // for error handling
			if ((LA(1)==LPAREN) && (LA(2)==SYM_L)) {
				v=merkmal_liste();
			}
			else if ((LA(1)==LPAREN) && (_tokenSet_4.member(LA(2)))) {
				k=merkmal();
				v = new Vector(); v.add( k );
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return v;
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
			recover(ex,_tokenSet_6);
		}
		return s;
	}
	
	public final Vector  merkmal_liste() throws RecognitionException, TokenStreamException {
		Vector v;
		
		
			KategorieMerkmal k;
			
			v = new Vector();
		
		
		try {      // for error handling
			{
			int _cnt462=0;
			_loop462:
			do {
				if ((LA(1)==LPAREN)) {
					match(LPAREN);
					match(SYM_L);
					k=merkmal();
					match(RPAREN);
					v.add( k );
				}
				else {
					if ( _cnt462>=1 ) { break _loop462; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt462++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return v;
	}
	
	public final KategorieMerkmal  merkmal() throws RecognitionException, TokenStreamException {
		KategorieMerkmal res;
		
		Token  nCat = null;
		Token  enCat = null;
		
			String category, fl, flk;
			FlexMerkmal f;
			FeatureStructure grad = null, p;
			
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
					nCat = LT(1);
					match(SYM_N);
					category = nCat.getText();
					break;
				}
				case SYM_EN:
				{
					enCat = LT(1);
					match(SYM_EN);
					category = enCat.getText();
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				match(RPAREN);
				f=nflex();
				if( category.equals( "N" ) ) res = new NKategorieMerkmal( f ); else res = new ENKategorieMerkmal( f );
				break;
			}
			case SYM_V:
			{
				match(SYM_V);
				match(RPAREN);
				f=vflex();
				res = new VKategorieMerkmal( f );
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
					grad=gr();
					f=aflex();
					res = new AdjGradKategorieMerkmal( grad, f );
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_PART_ADJ_PRAES||LA(2)==SYM_PART_ADJ_PERF)) {
					p=partyp();
					f=aflex();
					res = new AdjPartKategorieMerkmal( p, f );
				}
				else if ((LA(1)==LPAREN||LA(1)==RPAREN) && (_tokenSet_7.member(LA(2)))) {
					{
					switch ( LA(1)) {
					case LPAREN:
					{
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
						{
						switch ( LA(1)) {
						case LPAREN:
						{
							match(LPAREN);
							match(SYM_EQ);
							match(SYM_FLEXION);
							fl=flexiv();
							flk=flexionsklasse();
							match(RPAREN);
							
														((AdjKategorieMerkmal)res).setFlexRef( new AdjFlexRefMerkmal( fl, flk ) );
													
							break;
						}
						case RPAREN:
						{
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
					case RPAREN:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
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
			recover(ex,_tokenSet_5);
		}
		return res;
	}
	
	public final FlexMerkmal  nflex() throws RecognitionException, TokenStreamException {
		FlexMerkmal flex;
		
		
			UnpackedFeatureStructure gen;
			FeatureStructure res;
			String d;
			Vector featureList = new Vector();
			Hashtable dektypTab = new Hashtable();
			
			flex = null;
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_FLEXION);
			{
			if ((LA(1)==LPAREN) && (_tokenSet_8.member(LA(2)))) {
				match(LPAREN);
				gen=genus();
				{
				int _cnt466=0;
				_loop466:
				do {
					if ((LA(1)==LPAREN)) {
						res=kasusnumerus(gen);
						featureList.add( res );
					}
					else {
						if ( _cnt466>=1 ) { break _loop466; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt466++;
				} while (true);
				}
				match(RPAREN);
				match(RPAREN);
				flex = new NFlexMerkmalEinfach( featureList );
							
			}
			else if ((LA(1)==LPAREN) && ((LA(2) >= SYM_DEKTYPI && LA(2) <= SYM_DEKTYPIII))) {
				{
				int _cnt472=0;
				_loop472:
				do {
					if ((LA(1)==LPAREN)) {
						match(LPAREN);
						d=dektyp();
						{
						int _cnt471=0;
						_loop471:
						do {
							if ((LA(1)==LPAREN)) {
								match(LPAREN);
								gen=genus();
								{
								int _cnt470=0;
								_loop470:
								do {
									if ((LA(1)==LPAREN)) {
										res=kasusnumerus(gen);
										featureList.add( res );
									}
									else {
										if ( _cnt470>=1 ) { break _loop470; } else {throw new NoViableAltException(LT(1), getFilename());}
									}
									
									_cnt470++;
								} while (true);
								}
								match(RPAREN);
							}
							else {
								if ( _cnt471>=1 ) { break _loop471; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt471++;
						} while (true);
						}
						match(RPAREN);
						dektypTab.put( d, featureList );
							                          featureList = new Vector();
								 	
					}
					else {
						if ( _cnt472>=1 ) { break _loop472; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt472++;
				} while (true);
				}
				match(RPAREN);
				flex = new NFlexMerkmalDektyp( dektypTab );
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return flex;
	}
	
	public final FlexMerkmal  vflex() throws RecognitionException, TokenStreamException {
		FlexMerkmal m;
		
		
			Vector v = new Vector();
		
			m = null;
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_FLEXION);
			{
			int _cnt490=0;
			_loop490:
			do {
				if ((LA(1)==LPAREN)) {
					konjug(v);
				}
				else {
					if ( _cnt490>=1 ) { break _loop490; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt490++;
			} while (true);
			}
			match(RPAREN);
			
						m = new VFlexMerkmal( v );
					
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return m;
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
			recover(ex,_tokenSet_9);
		}
		return res;
	}
	
	public final FlexMerkmal  aflex() throws RecognitionException, TokenStreamException {
		FlexMerkmal m;
		
		
			UnpackedFeatureStructure gen;
			FeatureStructure res;
			Vector featureList = new Vector();
			Hashtable dektypTab = new Hashtable();
			String d, f, fk;
		
			m = null;
		
		
		try {      // for error handling
			match(LPAREN);
			{
			switch ( LA(1)) {
			case SYM_FLEXION:
			{
				{
				match(SYM_FLEXION);
				{
				int _cnt484=0;
				_loop484:
				do {
					if ((LA(1)==LPAREN)) {
						match(LPAREN);
						d=dektyp();
						{
						int _cnt483=0;
						_loop483:
						do {
							if ((LA(1)==LPAREN)) {
								match(LPAREN);
								gen=genus();
								{
								int _cnt482=0;
								_loop482:
								do {
									if ((LA(1)==LPAREN)) {
										res=kasusnumerus(gen);
										featureList.add( res );
									}
									else {
										if ( _cnt482>=1 ) { break _loop482; } else {throw new NoViableAltException(LT(1), getFilename());}
									}
									
									_cnt482++;
								} while (true);
								}
								match(RPAREN);
							}
							else {
								if ( _cnt483>=1 ) { break _loop483; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt483++;
						} while (true);
						}
						match(RPAREN);
						dektypTab.put( d, featureList );
											  featureList = new Vector();
											
					}
					else {
						if ( _cnt484>=1 ) { break _loop484; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt484++;
				} while (true);
				}
				match(RPAREN);
				}
				m = new AdjFlexMerkmal( dektypTab );
				break;
			}
			case SYM_EQ:
			{
				{
				match(SYM_EQ);
				match(SYM_FLEXION);
				f=flexiv();
				fk=flexionsklasse();
				match(RPAREN);
				}
				m = new AdjFlexRefMerkmal( f, fk );
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
			recover(ex,_tokenSet_5);
		}
		return m;
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
			recover(ex,_tokenSet_3);
		}
		return res;
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
			recover(ex,_tokenSet_3);
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
			recover(ex,_tokenSet_9);
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
			recover(ex,_tokenSet_3);
		}
		return s;
	}
	
	public final void konjug(
		Vector v
	) throws RecognitionException, TokenStreamException {
		
		
			FeatureStructure m, n, f;
			UnpackedFeatureStructure res;
			Vector p;
			int i, len, start = v.size();
		
		
		try {      // for error handling
			if ((LA(1)==LPAREN) && (LA(2)==SYM_INF)) {
				match(LPAREN);
				match(SYM_INF);
				match(RPAREN);
				res = new UnpackedFeatureStructure();
					   res.add( FeatureName.forName( "typ" ),new StringValue("infinitiv"));
				v.add( res.pack() );
					
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_PPP)) {
				match(LPAREN);
				match(SYM_PPP);
				match(RPAREN);
				res = new UnpackedFeatureStructure();
					   res.add( FeatureName.forName( "typ" ),new StringValue("ppp"));
				v.add( res.pack() );
					
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_IMP)) {
				match(LPAREN);
				match(SYM_IMP);
				n=num();
				match(RPAREN);
				res = new UnpackedFeatureStructure();
					   res.add( FeatureName.forName( "typ" ),new StringValue("imperativ"));
					   f = res.pack();
				v.add( f.unification( n ) );
					
			}
			else if ((LA(1)==LPAREN) && ((LA(2) >= SYM_IND_PRAES && LA(2) <= SYM_KONJ_PRAET))) {
				match(LPAREN);
				m=modustempus();
				{
				int _cnt493=0;
				_loop493:
				do {
					if ((LA(1)==LPAREN)) {
						match(LPAREN);
						n=num();
						p=person();
						match(RPAREN);
						
										len = p.size();
										for( i = 0; i < len; i++ ) {
											f = (FeatureStructure)p.elementAt( i );
											v.add( f.unification( n ) );
										}
									
					}
					else {
						if ( _cnt493>=1 ) { break _loop493; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt493++;
				} while (true);
				}
				match(RPAREN);
				
							// modustempus zu jedem Listenelement hinzufuegen
							len = v.size();
							for( i = start; i < len; i++ ) {
								f = (FeatureStructure)v.elementAt( i );
								v.setElementAt( f.unification( m ), i );
							}
						
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_9);
		}
	}
	
	public final FeatureStructure  num() throws RecognitionException, TokenStreamException {
		FeatureStructure res;
		
		
			UnpackedFeatureStructure r = new UnpackedFeatureStructure();
		
			res = null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SYM_SG:
			{
				match(SYM_SG);
				r.add( FeatureName.forName( "numerus" ),
								 new StringValue( "sg" ) );
						  res = r.pack();
						
				break;
			}
			case SYM_PL:
			{
				match(SYM_PL);
				r.add( FeatureName.forName( "numerus" ),
								 new StringValue( "pl" ) );
						  res = r.pack();
						
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
			recover(ex,_tokenSet_10);
		}
		return res;
	}
	
	public final FeatureStructure  modustempus() throws RecognitionException, TokenStreamException {
		FeatureStructure res;
		
		
			UnpackedFeatureStructure r = new UnpackedFeatureStructure();
		
			res = null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SYM_IND_PRAES:
			{
				match(SYM_IND_PRAES);
				r.add( FeatureName.forName( "modus" ), new StringValue("indikativ"));
						  r.add( FeatureName.forName( "tempus" ), new StringValue("praesens"));
						  res = r.pack();
						
				break;
			}
			case SYM_KONJ_PRAES:
			{
				match(SYM_KONJ_PRAES);
				r.add( FeatureName.forName( "modus" ),
								 new StringValue( "konjunktiv" ) );
						  r.add( FeatureName.forName( "tempus" ),
								 new StringValue( "praesens" ) );
						  res = r.pack();
						
				break;
			}
			case SYM_IND_PRAET:
			{
				match(SYM_IND_PRAET);
				r.add( FeatureName.forName( "modus" ), new StringValue( "indikativ" ) );
					      r.add( FeatureName.forName( "tempus" ), new StringValue( "praeteritum" ) );
						  res = r.pack();
						
				break;
			}
			case SYM_KONJ_PRAET:
			{
				match(SYM_KONJ_PRAET);
				r.add( FeatureName.forName( "modus" ),
								   new StringValue( "konjunktiv" ) );
					      r.add( FeatureName.forName( "tempus" ),
								   new StringValue( "praeteritum" ) );
						  res = r.pack();
						
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
			recover(ex,_tokenSet_3);
		}
		return res;
	}
	
	public final Vector  person() throws RecognitionException, TokenStreamException {
		Vector v;
		
		
			String s;
			UnpackedFeatureStructure res;
		
			v = new Vector();
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SYM_EINS:
			case SYM_ZWEI:
			case SYM_DREI:
			{
				s=ziff();
				res = new UnpackedFeatureStructure();
					      res.add( FeatureName.forName( "person" ),
								   new StringValue( s ) );
						  v.add( res.pack() );
						
				break;
			}
			case LPAREN:
			{
				match(LPAREN);
				{
				int _cnt498=0;
				_loop498:
				do {
					if (((LA(1) >= SYM_EINS && LA(1) <= SYM_DREI))) {
						s=ziff();
						res = new UnpackedFeatureStructure();
									  res.add( FeatureName.forName( "person" ),
										        new StringValue( s ) );
									  v.add( res.pack() );
									
					}
					else {
						if ( _cnt498>=1 ) { break _loop498; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt498++;
				} while (true);
				}
				match(RPAREN);
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
			recover(ex,_tokenSet_5);
		}
		return v;
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
			recover(ex,_tokenSet_11);
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
		"\"V\"",
		"\"UNFLADJ\"",
		"\"PARTIZIP-PRAESENS\"",
		"\"ADJ\"",
		"\"KARDINALZAHL\"",
		"\"ORDINALZAHL\"",
		"SYM_EQ",
		"\"FLEXION\"",
		"\"L\"",
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
		"\"DEKTYPI\"",
		"\"DEKTYPII\"",
		"\"DEKTYPIII\"",
		"\"PART-ADJ-PRAESENS\"",
		"\"PART-ADJ-PERFEKT\"",
		"\"INFINITIV\"",
		"\"PARTIZIP-PERFEKT\"",
		"\"IMPERATIV\"",
		"\"IND_PRAESENS\"",
		"\"KONJ_PRAESENS\"",
		"\"IND_PRAETERITUM\"",
		"\"KONJ_PRAETERITUM\"",
		"\"SG\"",
		"\"PL\"",
		"\"1\"",
		"\"2\"",
		"\"3\"",
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
		long[] data = { 80L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 16L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 11648L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 32L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 96L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 49200L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 3670528L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 48L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 492581209243696L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 492581209243680L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	
	}
