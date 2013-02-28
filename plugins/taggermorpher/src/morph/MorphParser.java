// $ANTLR : "morph_parser.g" -> "MorphParser.java"$

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

public class MorphParser extends antlr.LLkParser       implements MorphParserTokenTypes
 {

protected MorphParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public MorphParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected MorphParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public MorphParser(TokenStream lexer) {
  this(lexer,3);
}

public MorphParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
}

	public final Hashtable  eintrag_liste() throws RecognitionException, TokenStreamException {
		Hashtable h;
		
		
			LexemEintrag l;
		
			h = new Hashtable();
		
		
		try {      // for error handling
			{
			int _cnt338=0;
			_loop338:
			do {
				if ((LA(1)==LPAREN)) {
					l=eintrag();
					
									h.put( l.holeLexem(), l );
									// System.out.println( h.size() + ": " + l.holeLexem() );
								
				}
				else {
					if ( _cnt338>=1 ) { break _loop338; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt338++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return h;
	}
	
	public final LexemEintrag  eintrag() throws RecognitionException, TokenStreamException {
		LexemEintrag l;
		
		
			String key;
			Vector lesarten;
		
			l = null;
		
		
		try {      // for error handling
			match(LPAREN);
			key=lemmaname();
			lesarten=lesart_liste();
			match(RPAREN);
			
				 l = new LexemEintrag( key, lesarten );
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return l;
	}
	
	public final String  lemmaname() throws RecognitionException, TokenStreamException {
		String key;
		
		Token  sym = null;
		Token  symsing = null;
		Token  symdem = null;
		Token  symfl = null;
		Token  symfu = null;
		Token  symgen = null;
		Token  symgr = null;
		Token  symimp = null;
		Token  symind = null;
		Token  symint = null;
		Token  symn = null;
		Token  syminf = null;
		Token  sympers = null;
		Token  symref = null;
		Token  symrel = null;
		Token  symtyp = null;
		
			key = "";
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SYM_SYMBOL:
			{
				sym = LT(1);
				match(SYM_SYMBOL);
				key = sym.getText();
				break;
			}
			case SYM_SING:
			{
				symsing = LT(1);
				match(SYM_SING);
				key = symsing.getText();
				break;
			}
			case SYM_DEMON:
			{
				symdem = LT(1);
				match(SYM_DEMON);
				key = symdem.getText();
				break;
			}
			case SYM_FLEXION:
			{
				symfl = LT(1);
				match(SYM_FLEXION);
				key = symfl.getText();
				break;
			}
			case SYM_FUGE:
			{
				symfu = LT(1);
				match(SYM_FUGE);
				key = symfu.getText();
				break;
			}
			case SYM_GEN:
			{
				symgen = LT(1);
				match(SYM_GEN);
				key = symgen.getText();
				break;
			}
			case SYM_GRAD:
			{
				symgr = LT(1);
				match(SYM_GRAD);
				key = symgr.getText();
				break;
			}
			case SYM_IMP:
			{
				symimp = LT(1);
				match(SYM_IMP);
				key = symimp.getText();
				break;
			}
			case SYM_INDEF:
			{
				symind = LT(1);
				match(SYM_INDEF);
				key = symind.getText();
				break;
			}
			case SYM_INTERROGPRON:
			{
				symint = LT(1);
				match(SYM_INTERROGPRON);
				key = symint.getText();
				break;
			}
			case SYM_N:
			{
				symn = LT(1);
				match(SYM_N);
				key = symn.getText();
				break;
			}
			case SYM_INF:
			{
				syminf = LT(1);
				match(SYM_INF);
				key = syminf.getText();
				break;
			}
			case SYM_PERS:
			{
				sympers = LT(1);
				match(SYM_PERS);
				key = sympers.getText();
				break;
			}
			case SYM_REFL:
			{
				symref = LT(1);
				match(SYM_REFL);
				key = symref.getText();
				break;
			}
			case SYM_REL:
			{
				symrel = LT(1);
				match(SYM_REL);
				key = symrel.getText();
				break;
			}
			case SYM_TYP:
			{
				symtyp = LT(1);
				match(SYM_TYP);
				key = symtyp.getText();
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
		return key;
	}
	
	public final Vector  lesart_liste() throws RecognitionException, TokenStreamException {
		Vector v;
		
		
			Merkmal f;
			v = new Vector();
		
		
		try {      // for error handling
			{
			int _cnt344=0;
			_loop344:
			do {
				if ((LA(1)==LPAREN)) {
					f=lesart();
					v.add( f );
				}
				else {
					if ( _cnt344>=1 ) { break _loop344; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt344++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return v;
	}
	
	public final FlexivMerkmal  lesart() throws RecognitionException, TokenStreamException {
		FlexivMerkmal f;
		
		Token  s1 = null;
		Token  lex = null;
		Token  t = null;
		Token  vpr = null;
		Token  s2 = null;
		Token  s3 = null;
		Token  s8 = null;
		Token  s9 = null;
		Token  s4 = null;
		Token  s5 = null;
		Token  p = null;
		Token  n = null;
		Token  s6 = null;
		Token  s7 = null;
		Token  adj = null;
		
			KategorieMerkmal m;
			String s;
			boolean hatTvz = false, hatVpr = false;
			Vector v;
		
			f = null;
		
		
		try {      // for error handling
			match(LPAREN);
			{
			switch ( LA(1)) {
			case SYM_SYMBOL:
			{
				s1 = LT(1);
				match(SYM_SYMBOL);
				
								f = new FlexivklasseMerkmal( s1.getText() );
							
				break;
			}
			case SYM_VF:
			{
				match(SYM_VF);
				
								f = new FlexivVFMerkmal();
							
				break;
			}
			case SYM_ORTNUMSUF:
			{
				match(SYM_ORTNUMSUF);
				f = new FlexivORTNUMSUFMerkmal();
				break;
			}
			case SYM_EQ:
			{
				match(SYM_EQ);
				lex = LT(1);
				match(SYM_SYMBOL);
				f = new FlexivRefMerkmal( lex.getText() );
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop351:
			do {
				if ((LA(1)==LPAREN) && (LA(2)==SYM_ABK)) {
					s=abk();
					f.abkSetzen( s );
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_GF)) {
					s=gfstamm();
					f.grundformSetzen( s );
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_ALLO)) {
					v=allo();
					f.allomorphSetzen( v );
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_MWTRIGG)) {
					match(LPAREN);
					match(SYM_MWTRIGG);
					match(RPAREN);
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_INFPART)) {
					match(LPAREN);
					match(SYM_INFPART);
					match(RPAREN);
					
									f = new FlexivVFMerkmal( new InfPartKategorieMerkmal() );
								
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_TVZ)) {
					match(LPAREN);
					match(SYM_TVZ);
					{
					switch ( LA(1)) {
					case SYM_SYMBOL:
					{
						t = LT(1);
						match(SYM_SYMBOL);
						hatTvz = true;
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
					match(RPAREN);
					
									if( hatTvz ) f.hatTvzSetzen( t.getText() );
									else { f.tvzSetzen();
									       m = new TVZKategorieMerkmal(); 
					f.kategorieSetzen (m);}
								
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_VPR)) {
					match(LPAREN);
					match(SYM_VPR);
					{
					switch ( LA(1)) {
					case SYM_SYMBOL:
					{
						vpr = LT(1);
						match(SYM_SYMBOL);
						hatVpr = true;
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
					match(RPAREN);
					
									if( hatVpr ) f.hatVprSetzen( vpr.getText() );
									else f.vprSetzen();
								
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_SEM)) {
					match(LPAREN);
					match(SYM_SEM);
					s=sem();
					match(RPAREN);
					f.semSetzen( s );
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_SYN)) {
					match(LPAREN);
					match(SYM_SYN);
					s=sem();
					match(RPAREN);
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_MK)) {
					match(LPAREN);
					match(SYM_MK);
					{
					switch ( LA(1)) {
					case SYM_FUGE:
					{
						match(SYM_FUGE);
						f.fugeSetzen();
						break;
					}
					case SYM_KONFIXFUGE:
					{
						s2 = LT(1);
						match(SYM_KONFIXFUGE);
						f.mkSetzen( s2.getText() );
						break;
					}
					case SYM_LEHNSUF:
					{
						s3 = LT(1);
						match(SYM_LEHNSUF);
						f.mkSetzen( s3.getText() );
						break;
					}
					case SYM_PRAEFIX:
					{
						s8 = LT(1);
						match(SYM_PRAEFIX);
						f.mkSetzen( s8.getText() );
						break;
					}
					case SYM_SUFFIX:
					{
						s9 = LT(1);
						match(SYM_SUFFIX);
						f.mkSetzen( s9.getText() );
						break;
					}
					case SYM_KONFIX:
					{
						s4 = LT(1);
						match(SYM_KONFIX);
						f.mkSetzen( s4.getText() );
						break;
					}
					case SYM_NOUNSUF:
					{
						s5 = LT(1);
						match(SYM_NOUNSUF);
						f.mkSetzen( s5.getText() );
						break;
					}
					case SYM_PARTPRAEF:
					{
						p = LT(1);
						match(SYM_PARTPRAEF);
						f.mkSetzen( p.getText() );
						break;
					}
					case SYM_N:
					{
						n = LT(1);
						match(SYM_N);
						f.mkSetzen( n.getText() );
						break;
					}
					case SYM_NUMSUF:
					{
						s6 = LT(1);
						match(SYM_NUMSUF);
						f.mkSetzen( s6.getText() );
						break;
					}
					case SYM_ORTNUMSUF:
					{
						s7 = LT(1);
						match(SYM_ORTNUMSUF);
						f.mkSetzen( s7.getText() );
						break;
					}
					case SYM_VPR:
					{
						match(SYM_VPR);
						f.vprSetzen();
						break;
					}
					case SYM_ADJ:
					{
						adj = LT(1);
						match(SYM_ADJ);
						f.mkSetzen( adj.getText() );
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
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_BOUND)) {
					match(LPAREN);
					match(SYM_BOUND);
					match(RPAREN);
					f.boundSetzen();
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_WBSUBCAT)) {
					v=wbsubcat();
					f.wbsubcatSetzen( v );
				}
				else if ((LA(1)==LPAREN) && (_tokenSet_4.member(LA(2)))) {
					m=kategorie(f);
					
									f.kategorieSetzen( m );
								
				}
				else {
					break _loop351;
				}
				
			} while (true);
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return f;
	}
	
	public final String  abk() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  s1 = null;
		
			s = "";
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_ABK);
			{
			int _cnt358=0;
			_loop358:
			do {
				if ((LA(1)==SYM_SYMBOL)) {
					s1 = LT(1);
					match(SYM_SYMBOL);
					if( !s.equals( "" ) ) s += " ";
						     s += s1.getText();
						
				}
				else {
					if ( _cnt358>=1 ) { break _loop358; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt358++;
			} while (true);
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return s;
	}
	
	public final String  gfstamm() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  a = null;
		Token  b = null;
		
			s = "";
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_GF);
			{
			switch ( LA(1)) {
			case SYM_SYMBOL:
			{
				a = LT(1);
				match(SYM_SYMBOL);
				s = a.getText();
				break;
			}
			case SYM_SING:
			{
				b = LT(1);
				match(SYM_SING);
				s = b.getText();
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
			recover(ex,_tokenSet_5);
		}
		return s;
	}
	
	public final Vector  allo() throws RecognitionException, TokenStreamException {
		Vector v;
		
		Token  a = null;
		
			v = new Vector();
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_ALLO);
			{
			int _cnt361=0;
			_loop361:
			do {
				if ((LA(1)==SYM_SYMBOL)) {
					a = LT(1);
					match(SYM_SYMBOL);
					v.add( a.getText() );
				}
				else {
					if ( _cnt361>=1 ) { break _loop361; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt361++;
			} while (true);
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return v;
	}
	
	public final String  sem() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  sym = null;
		
			s = "";
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case SYM_SYMBOL:
			{
				match(SYM_SYMBOL);
				break;
			}
			case LPAREN:
			{
				match(LPAREN);
				match(SYM_SYMBOL);
				sym = LT(1);
				match(SYM_SYMBOL);
				match(RPAREN);
				
						s = sym.getText();
					
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
	
	public final Vector  wbsubcat() throws RecognitionException, TokenStreamException {
		Vector v;
		
		Token  catn = null;
		Token  catv = null;
		Token  catadj = null;
		Token  cats = null;
		
			String s;
			
			v = new Vector();
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_WBSUBCAT);
			{
			int _cnt354=0;
			_loop354:
			do {
				switch ( LA(1)) {
				case SYM_N:
				{
					catn = LT(1);
					match(SYM_N);
					s=catn.getText(); v.add(s);
					break;
				}
				case SYM_V:
				{
					catv = LT(1);
					match(SYM_V);
					s=catv.getText(); v.add(s);
					break;
				}
				case SYM_ADJ:
				{
					catadj = LT(1);
					match(SYM_ADJ);
					s=catadj.getText(); v.add(s);
					break;
				}
				case SYM_SYMBOL:
				{
					cats = LT(1);
					match(SYM_SYMBOL);
					s=cats.getText(); v.add(s);
					break;
				}
				default:
				{
					if ( _cnt354>=1 ) { break _loop354; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt354++;
			} while (true);
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
		return v;
	}
	
	public final KategorieMerkmal  kategorie(
		FlexivMerkmal stamm
	) throws RecognitionException, TokenStreamException {
		KategorieMerkmal m;
		
		Token  ngf = null;
		Token  vgf = null;
		Token  adjgf = null;
		Token  unfladjgf = null;
		Token  gf = null;
		Token  prongf = null;
		Token  s1 = null;
		Token  s2 = null;
		Token  s3 = null;
		Token  s4 = null;
		Token  s5 = null;
		Token  s6 = null;
		
			boolean interrog = false, kasrek = false, 
				posspron = false, pronGf = false;
			String kas, s;
			UnpackedFeatureStructure feat;
			FeatureStructure grad;
			FlexMerkmal f;
			Vector v = new Vector();
			boolean nFlex = false;
		
			m = null;
		
		
		try {      // for error handling
			match(LPAREN);
			{
			switch ( LA(1)) {
			case SYM_N:
			{
				match(SYM_N);
				{
				if ((LA(1)==RPAREN) && (LA(2)==LPAREN) && (_tokenSet_6.member(LA(3)))) {
					match(RPAREN);
					{
					if ((LA(1)==LPAREN) && (LA(2)==SYM_G)) {
						match(LPAREN);
						match(SYM_G);
						ngf = LT(1);
						match(SYM_SYMBOL);
						match(RPAREN);
						stamm.grundformSetzen( ngf.getText() );
					}
					else if ((LA(1)==LPAREN) && (_tokenSet_7.member(LA(2)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					match(LPAREN);
					{
					switch ( LA(1)) {
					case SYM_MASK:
					case SYM_FEM:
					case SYM_NEUT:
					case SYM_NIL:
					{
						feat=genus();
						{
						int _cnt370=0;
						_loop370:
						do {
							if ((LA(1)==LPAREN)) {
								grad=kasusnumerus(feat);
								v.add( grad );
							}
							else {
								if ( _cnt370>=1 ) { break _loop370; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt370++;
						} while (true);
						}
						
											m = new NKategorieMerkmal( new NFlexMerkmalEinfach( v ) );
										
						break;
					}
					case SYM_FLEXION:
					{
						match(SYM_FLEXION);
						match(LPAREN);
						feat=genus();
						{
						int _cnt372=0;
						_loop372:
						do {
							if ((LA(1)==LPAREN)) {
								grad=kasusnumerus(feat);
								v.add( grad );
							}
							else {
								if ( _cnt372>=1 ) { break _loop372; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt372++;
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
					nFlex = true;
				}
				else if ((LA(1)==RPAREN) && (LA(2)==LPAREN||LA(2)==RPAREN) && (_tokenSet_8.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				
								if( nFlex ) m = new NKategorieMerkmal( new NFlexMerkmalEinfach( v ) );
								else m = new NKategorieMerkmal();
							
				break;
			}
			case SYM_V:
			{
				match(SYM_V);
				match(RPAREN);
				match(LPAREN);
				match(SYM_G);
				vgf = LT(1);
				match(SYM_SYMBOL);
				match(RPAREN);
				{
				if ((LA(1)==LPAREN) && (LA(2)==SYM_HILFSVERB||LA(2)==SYM_MODALVERB)) {
					match(LPAREN);
					{
					switch ( LA(1)) {
					case SYM_HILFSVERB:
					{
						match(SYM_HILFSVERB);
						break;
					}
					case SYM_MODALVERB:
					{
						match(SYM_MODALVERB);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					match(SYM_SYMBOL);
					match(RPAREN);
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_FLEXION)) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				f=vflex();
				
								stamm.grundformSetzen( vgf.getText() );
								m = new VKategorieMerkmal( f );
				
				break;
			}
			case SYM_NUM:
			{
				match(SYM_NUM);
				m = new NumKategorieMerkmal();
				break;
			}
			case SYM_ADV:
			{
				match(SYM_ADV);
				{
				if ((LA(1)==RPAREN) && (LA(2)==LPAREN) && (LA(3)==SYM_INTERROGADV)) {
					match(RPAREN);
					match(LPAREN);
					{
					match(SYM_INTERROGADV);
					interrog = true;
					}
				}
				else if ((LA(1)==RPAREN) && (LA(2)==LPAREN||LA(2)==RPAREN) && (_tokenSet_8.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				
								if( interrog ) m = new InterrogKategorieMerkmal();
								else m = new AdvKategorieMerkmal();
							
				break;
			}
			case SYM_ADJ:
			{
				match(SYM_ADJ);
				{
				if ((LA(1)==RPAREN) && (LA(2)==LPAREN) && (LA(3)==SYM_G)) {
					match(RPAREN);
					match(LPAREN);
					match(SYM_G);
					adjgf = LT(1);
					match(SYM_SYMBOL);
					match(RPAREN);
					grad=gr();
					match(RPAREN);
					f=detflex();
					
										stamm.grundformSetzen( adjgf.getText() );
										m = new AdjGradKategorieMerkmal( grad, f );
									
				}
				else if ((LA(1)==RPAREN) && (LA(2)==LPAREN||LA(2)==RPAREN) && (_tokenSet_8.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				if( m == null ) m = new AdjGradKategorieMerkmal();
				break;
			}
			case SYM_UNFLADJ:
			{
				match(SYM_UNFLADJ);
				match(RPAREN);
				match(LPAREN);
				match(SYM_G);
				unfladjgf = LT(1);
				match(SYM_SYMBOL);
				match(RPAREN);
				grad=gr();
				
								stamm.grundformSetzen( unfladjgf.getText() );
								m = new UnflAdjGradKategorieMerkmal( grad );
				
				break;
			}
			case SYM_P:
			{
				match(SYM_P);
				{
				if ((LA(1)==RPAREN) && (LA(2)==LPAREN) && (LA(3)==SYM_KASREK)) {
					match(RPAREN);
					match(LPAREN);
					match(SYM_KASREK);
					match(LPAREN);
					{
					int _cnt380=0;
					_loop380:
					do {
						if ((_tokenSet_9.member(LA(1)))) {
							kas=kasus();
							feat = new UnpackedFeatureStructure();
													feat.add( FeatureName.forName( "kasrek" ),
														new StringValue( kas ) );
													v.add( feat.pack() );
												
						}
						else {
							if ( _cnt380>=1 ) { break _loop380; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt380++;
					} while (true);
					}
					match(RPAREN);
					{
					if ((LA(1)==RPAREN) && (LA(2)==LPAREN) && (LA(3)==SYM_KONTR)) {
						match(RPAREN);
						s=kontr();
						
					}
					else if ((LA(1)==RPAREN) && (LA(2)==LPAREN||LA(2)==RPAREN) && (_tokenSet_8.member(LA(3)))) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					kasrek = true;
						
				}
				else if ((LA(1)==RPAREN) && (LA(2)==LPAREN||LA(2)==RPAREN) && (_tokenSet_8.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				
								if( kasrek ) {
									m = new PKategorieMerkmal( new PFlexMerkmal( v ) );
								}
								else m = new PKategorieMerkmal();
							
				break;
			}
			case SYM_KONJ:
			{
				match(SYM_KONJ);
				m = new KonjKategorieMerkmal();
				break;
			}
			case SYM_INTERJ:
			{
				match(SYM_INTERJ);
				m = new InterjKategorieMerkmal();
				break;
			}
			case SYM_DET:
			{
				match(SYM_DET);
				match(RPAREN);
				match(LPAREN);
				match(SYM_G);
				gf = LT(1);
				match(SYM_SYMBOL);
				match(RPAREN);
				{
				if ((LA(1)==LPAREN) && (LA(2)==SYM_POSS)) {
					match(LPAREN);
					match(SYM_POSS);
					match(RPAREN);
					posspron = true;
				}
				else if ((LA(1)==LPAREN) && (LA(2)==SYM_FLEXION)) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				f=detflex();
				
								if( posspron )
									m = new PossPronKategorieMerkmal( gf.getText(), f );
								else m = new DetKategorieMerkmal( gf.getText(), f );
							
				break;
			}
			case SYM_PRON:
			{
				match(SYM_PRON);
				{
				if ((LA(1)==RPAREN) && (LA(2)==LPAREN) && (LA(3)==SYM_TYP||LA(3)==SYM_G)) {
					match(RPAREN);
					{
					if ((LA(1)==LPAREN) && (LA(2)==SYM_G)) {
						match(LPAREN);
						match(SYM_G);
						prongf = LT(1);
						match(SYM_SYMBOL);
						match(RPAREN);
						pronGf = true;
					}
					else if ((LA(1)==LPAREN) && (LA(2)==SYM_TYP)) {
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					
					}
					match(LPAREN);
					match(SYM_TYP);
					{
					switch ( LA(1)) {
					case SYM_PERS:
					{
						s1 = LT(1);
						match(SYM_PERS);
						
											if( pronGf ) {
												stamm.grundformSetzen( prongf.getText() );
											}	
											s = s1.getText();
										
						break;
					}
					case SYM_INTERROGPRON:
					{
						s2 = LT(1);
						match(SYM_INTERROGPRON);
						s = s2.getText();
						break;
					}
					case SYM_INDEF:
					{
						s3 = LT(1);
						match(SYM_INDEF);
						s = s3.getText();
						break;
					}
					case SYM_REL:
					{
						s4 = LT(1);
						match(SYM_REL);
						s = s4.getText();
						break;
					}
					case SYM_DEMON:
					{
						s5 = LT(1);
						match(SYM_DEMON);
						s = s5.getText();
						break;
					}
					case SYM_REFL:
					{
						s6 = LT(1);
						match(SYM_REFL);
						s = s6.getText();
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					match(RPAREN);
					v=pronflex(s);
				}
				else if ((LA(1)==RPAREN) && (LA(2)==LPAREN||LA(2)==RPAREN) && (_tokenSet_8.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				
								m = new PronKategorieMerkmal( v );
						
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
			recover(ex,_tokenSet_5);
		}
		return m;
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
			recover(ex,_tokenSet_2);
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
			recover(ex,_tokenSet_5);
		}
		return res;
	}
	
	public final FlexMerkmal  vflex() throws RecognitionException, TokenStreamException {
		FlexMerkmal m;
		
		
			Vector v = new Vector();
		
			m = null;
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_FLEXION);
			{
			int _cnt409=0;
			_loop409:
			do {
				if ((LA(1)==LPAREN)) {
					konjug(v);
				}
				else {
					if ( _cnt409>=1 ) { break _loop409; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt409++;
			} while (true);
			}
			
						m = new VFlexMerkmal( v );
					
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return m;
	}
	
	public final FeatureStructure  gr() throws RecognitionException, TokenStreamException {
		FeatureStructure res;
		
		
			UnpackedFeatureStructure r = new UnpackedFeatureStructure();
		
			res = null;
		
			// RPAREN muessen vom Regelaufrufer getestet werden
			// (anders in flexive_parser.g)
		
		
		try {      // for error handling
			if ((LA(1)==LPAREN) && (LA(2)==SYM_GRAD) && (LA(3)==SYM_POS)) {
				match(LPAREN);
				match(SYM_GRAD);
				match(SYM_POS);
				r.add( FeatureName.forName( "grad" ), new StringValue( "pos" ) );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_GRAD) && (LA(3)==SYM_KOMP)) {
				match(LPAREN);
				match(SYM_GRAD);
				match(SYM_KOMP);
				r.add( FeatureName.forName( "grad" ), new StringValue( "komp" ) );
						  res = r.pack();
						
			}
			else if ((LA(1)==LPAREN) && (LA(2)==SYM_GRAD) && (LA(3)==SYM_SUP)) {
				match(LPAREN);
				match(SYM_GRAD);
				match(SYM_SUP);
				r.add( FeatureName.forName( "grad" ), new StringValue( "sup" ) );
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
	
	public final FlexMerkmal  detflex() throws RecognitionException, TokenStreamException {
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
			int _cnt425=0;
			_loop425:
			do {
				if ((LA(1)==LPAREN)) {
					match(LPAREN);
					d=dektyp();
					{
					int _cnt424=0;
					_loop424:
					do {
						if ((LA(1)==LPAREN)) {
							match(LPAREN);
							gen=genus();
							{
							int _cnt423=0;
							_loop423:
							do {
								if ((LA(1)==LPAREN)) {
									res=kasusnumerus(gen);
									featureList.add( res );
								}
								else {
									if ( _cnt423>=1 ) { break _loop423; } else {throw new NoViableAltException(LT(1), getFilename());}
								}
								
								_cnt423++;
							} while (true);
							}
							match(RPAREN);
						}
						else {
							if ( _cnt424>=1 ) { break _loop424; } else {throw new NoViableAltException(LT(1), getFilename());}
						}
						
						_cnt424++;
					} while (true);
					}
					match(RPAREN);
					dektypTab.put( d, featureList );
						      featureList = new Vector();
				}
				else {
					if ( _cnt425>=1 ) { break _loop425; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt425++;
			} while (true);
			}
			flex = new NFlexMerkmalDektyp( dektypTab );
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return flex;
	}
	
	public final String  kasus() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  s1 = null;
		Token  s2 = null;
		Token  s3 = null;
		Token  s4 = null;
		
			s = "";
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case SYM_NOM:
			{
				s1 = LT(1);
				match(SYM_NOM);
				s = s1.getText();
				break;
			}
			case SYM_GEN:
			{
				s2 = LT(1);
				match(SYM_GEN);
				s = s2.getText();
				break;
			}
			case SYM_DAT:
			{
				s3 = LT(1);
				match(SYM_DAT);
				s = s3.getText();
				break;
			}
			case SYM_AKK:
			{
				s4 = LT(1);
				match(SYM_AKK);
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
			recover(ex,_tokenSet_10);
		}
		return s;
	}
	
	public final String  kontr() throws RecognitionException, TokenStreamException {
		String s;
		
		Token  k = null;
		
			s = "";
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_KONTR);
			k = LT(1);
			match(SYM_SYMBOL);
			match(SYM_SYMBOL);
			s = k.getText();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return s;
	}
	
	public final Vector  pronflex(
		String s
	) throws RecognitionException, TokenStreamException {
		Vector featList;
		
		
			UnpackedFeatureStructure feat = new UnpackedFeatureStructure();
			FeatureStructure f;
			String person;
		
			featList = new Vector();
			f = null;
			feat.add( FeatureName.forName( "prontyp" ), new StringValue( s ) );
		
		
		
		try {      // for error handling
			match(LPAREN);
			match(SYM_FLEXION);
			{
			if ((LA(1)==LPAREN) && (_tokenSet_9.member(LA(2)))) {
				{
				int _cnt391=0;
				_loop391:
				do {
					if ((LA(1)==LPAREN)) {
						f=kasusnumerus(feat);
						featList.add( f );
					}
					else {
						if ( _cnt391>=1 ) { break _loop391; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt391++;
				} while (true);
				}
				
				
			}
			else if ((LA(1)==LPAREN) && ((LA(2) >= SYM_MASK && LA(2) <= SYM_NIL))) {
				{
				int _cnt395=0;
				_loop395:
				do {
					if ((LA(1)==LPAREN)) {
						match(LPAREN);
						feat=genus();
						feat.add( FeatureName.forName( "prontyp" ),
												new StringValue( s ) );
										
						{
						int _cnt394=0;
						_loop394:
						do {
							if ((LA(1)==LPAREN)) {
								f=kasusnumerus(feat);
								featList.add( f );
							}
							else {
								if ( _cnt394>=1 ) { break _loop394; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt394++;
						} while (true);
						}
						match(RPAREN);
					}
					else {
						if ( _cnt395>=1 ) { break _loop395; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt395++;
				} while (true);
				}
			}
			else if ((LA(1)==LPAREN) && ((LA(2) >= SYM_EINS && LA(2) <= SYM_DREI))) {
				{
				int _cnt404=0;
				_loop404:
				do {
					if ((LA(1)==LPAREN)) {
						match(LPAREN);
						person=ziff();
						{
						if ((LA(1)==LPAREN) && ((LA(2) >= SYM_MASK && LA(2) <= SYM_NIL))) {
							{
							int _cnt401=0;
							_loop401:
							do {
								if ((LA(1)==LPAREN)) {
									match(LPAREN);
									feat=genus();
									
												  feat.add( FeatureName.forName( "prontyp" ),
													    new StringValue( s ) );
									feat.add( FeatureName.forName( "person" ),
									new StringValue( person ) );
									
									{
									int _cnt400=0;
									_loop400:
									do {
										if ((LA(1)==LPAREN)) {
											f=kasusnumerus(feat);
											featList.add( f );
										}
										else {
											if ( _cnt400>=1 ) { break _loop400; } else {throw new NoViableAltException(LT(1), getFilename());}
										}
										
										_cnt400++;
									} while (true);
									}
									match(RPAREN);
								}
								else {
									if ( _cnt401>=1 ) { break _loop401; } else {throw new NoViableAltException(LT(1), getFilename());}
								}
								
								_cnt401++;
							} while (true);
							}
						}
						else if ((LA(1)==LPAREN) && (_tokenSet_9.member(LA(2)))) {
							{
							int _cnt403=0;
							_loop403:
							do {
								if ((LA(1)==LPAREN)) {
									
											    	  feat.add(FeatureName.forName( "person" ),
											    	  	   new StringValue( person ) );
									
									f=kasusnumerus(feat);
									featList.add( f );
								}
								else {
									if ( _cnt403>=1 ) { break _loop403; } else {throw new NoViableAltException(LT(1), getFilename());}
								}
								
								_cnt403++;
							} while (true);
							}
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
						match(RPAREN);
					}
					else {
						if ( _cnt404>=1 ) { break _loop404; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt404++;
				} while (true);
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return featList;
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
				int _cnt412=0;
				_loop412:
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
						if ( _cnt412>=1 ) { break _loop412; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt412++;
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
			recover(ex,_tokenSet_5);
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
			recover(ex,_tokenSet_11);
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
				r.add( FeatureName.forName( "modus" ),new StringValue( "indikativ" ) );
					      r.add( FeatureName.forName( "tempus" ),
								   new StringValue( "praeteritum" ) );
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
			recover(ex,_tokenSet_2);
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
				int _cnt417=0;
				_loop417:
				do {
					if (((LA(1) >= SYM_EINS && LA(1) <= SYM_DREI))) {
						s=ziff();
						res = new UnpackedFeatureStructure();
									  res.add( FeatureName.forName( "person" ),
										        new StringValue( s ) );
									  v.add( res.pack() );
									
					}
					else {
						if ( _cnt417>=1 ) { break _loop417; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt417++;
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
			recover(ex,_tokenSet_3);
		}
		return v;
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
			recover(ex,_tokenSet_2);
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
		"\"SING\"",
		"\"DEMONSTRATIV\"",
		"\"FLEXION\"",
		"\"FUGE\"",
		"\"GEN\"",
		"\"GRAD\"",
		"\"IMPERATIV\"",
		"\"INDEFINIT\"",
		"\"INTERROGATIV\"",
		"\"N\"",
		"\"INFINITIV\"",
		"\"PERSONAL\"",
		"\"REFLEXIV\"",
		"\"RELATIV\"",
		"\"TYP\"",
		"\"VF\"",
		"\"ORTNUMSUF\"",
		"\"=\"",
		"\"MWTRIGG\"",
		"\"INFINITIVPARTIKEL\"",
		"\"TVZ\"",
		"\"VPR\"",
		"\"SEM\"",
		"\"SYN\"",
		"\"MK\"",
		"\"KONFIXFUGE\"",
		"\"LEHNSUF\"",
		"\"PRAEF\"",
		"\"SUF\"",
		"\"KONFIX\"",
		"\"NOUNSUF\"",
		"\"PARTPRAEF\"",
		"\"NUMSUF\"",
		"\"ADJ\"",
		"\"BOUND\"",
		"\"WB-SUBCAT\"",
		"\"V\"",
		"\"ABK\"",
		"\"ALLO\"",
		"\"GF_STAMM\"",
		"\"GF\"",
		"\"HILFSVERB\"",
		"\"MODALVERB\"",
		"\"NUM\"",
		"\"ADV\"",
		"\"INTERROGATIVADVERB\"",
		"\"UNFLADJ\"",
		"\"P\"",
		"\"KASREK\"",
		"\"KONJ\"",
		"\"INTERJ\"",
		"\"DET\"",
		"\"POSSESIVPRONOMEN\"",
		"\"PRON\"",
		"\"POS\"",
		"\"KOMP\"",
		"\"SUP\"",
		"\"KONTR\"",
		"\"NOM\"",
		"\"DAT\"",
		"\"AKK\"",
		"\"PARTIZIP-PERFEKT\"",
		"\"IND_PRAESENS\"",
		"\"KONJ_PRAESENS\"",
		"\"IND_PRAETERITUM\"",
		"\"KONJ_PRAETERITUM\"",
		"\"SG\"",
		"\"PL\"",
		"\"1\"",
		"\"2\"",
		"\"3\"",
		"\"MASK\"",
		"\"FEM\"",
		"\"NEUT\"",
		"SYM_NIL",
		"\"PLUR\"",
		"\"DEKTYPI\"",
		"\"DEKTYPII\"",
		"\"DEKTYPIII\"",
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
		long[] data = { 16L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 32L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 1687733855961808896L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 48L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 140737488355840L, 245760L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 512L, 245760L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 1687863602595299376L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 2048L, 14L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 2080L, 14L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 48L, 14336L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	
	}
