// $ANTLR : "wbrules_parser.g" -> "WBRulesParser.java"$

package morph;
	import java.util.Vector;
	import java.util.Hashtable;

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

public class WBRulesParser extends antlr.LLkParser       implements WBRulesParserTokenTypes
 {

protected WBRulesParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public WBRulesParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected WBRulesParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public WBRulesParser(TokenStream lexer) {
  this(lexer,1);
}

public WBRulesParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final Hashtable  rules() throws RecognitionException, TokenStreamException {
		Hashtable h;
		
		
			WBRule r;
			h = new Hashtable();
		
		
		try {      // for error handling
			{
			int _cnt292=0;
			_loop292:
			do {
				if ((LA(1)==LPAREN)) {
					r=rule();
					h.put( r.getName(), r );
				}
				else {
					if ( _cnt292>=1 ) { break _loop292; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt292++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return h;
	}
	
	public final WBRule  rule() throws RecognitionException, TokenStreamException {
		WBRule r;
		
		Token  s = null;
		
			String ruleName = "";
			WBRuleTransition t;
			Hashtable ht = new Hashtable();
		
			r = null;
		
		
		try {      // for error handling
			match(LPAREN);
			s = LT(1);
			match(SYM_SYMBOL);
			{
			int _cnt295=0;
			_loop295:
			do {
				if ((LA(1)==LPAREN)) {
					t=transition();
					ht.put( t.getName(), t );
				}
				else {
					if ( _cnt295>=1 ) { break _loop295; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt295++;
			} while (true);
			}
			match(RPAREN);
			
					ruleName = s.getText();
					r = new WBRule( ruleName, ht );
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return r;
	}
	
	public final WBRuleTransition  transition() throws RecognitionException, TokenStreamException {
		WBRuleTransition t;
		
		Token  s = null;
		
			WBRuleAction a;
		
			Vector v = new Vector();
			t = null;
		
		
		try {      // for error handling
			match(LPAREN);
			s = LT(1);
			match(SYM_SYMBOL);
			{
			_loop298:
			do {
				if ((LA(1)==LPAREN)) {
					a=action();
					v.add( a );
				}
				else {
					break _loop298;
				}
				
			} while (true);
			}
			match(RPAREN);
			
					t = new WBRuleTransition( s.getText(), v );
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return t;
	}
	
	public final WBRuleAction  action() throws RecognitionException, TokenStreamException {
		WBRuleAction a;
		
		Token  s0 = null;
		Token  d0 = null;
		Token  s1 = null;
		Token  d1 = null;
		Token  s2 = null;
		Token  d2 = null;
		Token  s3 = null;
		Token  d3 = null;
		Token  s4 = null;
		Token  d4 = null;
		Token  s5 = null;
		Token  d5 = null;
		Token  s6 = null;
		Token  d6 = null;
		Token  s7 = null;
		Token  d7 = null;
		Token  s8 = null;
		Token  d8 = null;
		Token  s9 = null;
		Token  d9 = null;
		Token  s10 = null;
		Token  d10 = null;
		Token  s11 = null;
		Token  d11 = null;
		
			Vector v = new Vector();
		
			a = null;
		
		
		try {      // for error handling
			match(LPAREN);
			{
			switch ( LA(1)) {
			case ORTSNAME:
			{
				s0 = LT(1);
				match(ORTSNAME);
				{
				int _cnt302=0;
				_loop302:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d0 = LT(1);
						match(SYM_DIGIT);
						v.add( d0.getText() );
					}
					else {
						if ( _cnt302>=1 ) { break _loop302; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt302++;
				} while (true);
				}
				
						a = new WBRuleActionOrtsname( s0.getText(), v );
					
				break;
			}
			case AGREEKATWB:
			{
				s1 = LT(1);
				match(AGREEKATWB);
				{
				int _cnt304=0;
				_loop304:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d1 = LT(1);
						match(SYM_DIGIT);
						v.add( d1.getText() );
					}
					else {
						if ( _cnt304>=1 ) { break _loop304; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt304++;
				} while (true);
				}
				
						a = new WBRuleActionAgreeKatWb( s1.getText(), v );
					
				break;
			}
			case AGREEWBKAT:
			{
				s2 = LT(1);
				match(AGREEWBKAT);
				{
				int _cnt306=0;
				_loop306:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d2 = LT(1);
						match(SYM_DIGIT);
						v.add( d2.getText() );
					}
					else {
						if ( _cnt306>=1 ) { break _loop306; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt306++;
				} while (true);
				}
				
						a = new WBRuleActionAgreeWbKat( s2.getText(), v );
					
				break;
			}
			case FUGE:
			{
				s3 = LT(1);
				match(FUGE);
				{
				int _cnt308=0;
				_loop308:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d3 = LT(1);
						match(SYM_DIGIT);
						v.add( d3.getText() );
					}
					else {
						if ( _cnt308>=1 ) { break _loop308; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt308++;
				} while (true);
				}
				
						a = new WBRuleActionFuge( s3.getText(), v );
					
				break;
			}
			case HEADPRAEF:
			{
				s4 = LT(1);
				match(HEADPRAEF);
				{
				int _cnt310=0;
				_loop310:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d4 = LT(1);
						match(SYM_DIGIT);
						v.add( d4.getText() );
					}
					else {
						if ( _cnt310>=1 ) { break _loop310; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt310++;
				} while (true);
				}
				
						a = new WBRuleActionHeadPraef( s4.getText(), v );
					
				break;
			}
			case KONJ:
			{
				s5 = LT(1);
				match(KONJ);
				{
				int _cnt312=0;
				_loop312:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d5 = LT(1);
						match(SYM_DIGIT);
						v.add( d5.getText() );
					}
					else {
						if ( _cnt312>=1 ) { break _loop312; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt312++;
				} while (true);
				}
				
						a = new WBRuleActionKonj( s5.getText(), v );
					
				break;
			}
			case NICHTALLO:
			{
				s6 = LT(1);
				match(NICHTALLO);
				{
				int _cnt314=0;
				_loop314:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d6 = LT(1);
						match(SYM_DIGIT);
						v.add( d6.getText() );
					}
					else {
						if ( _cnt314>=1 ) { break _loop314; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt314++;
				} while (true);
				}
				
						a = new WBRuleActionNichtAllo( s6.getText(), v );
					
				break;
			}
			case NICHTAUCHVERB:
			{
				s7 = LT(1);
				match(NICHTAUCHVERB);
				{
				int _cnt316=0;
				_loop316:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d7 = LT(1);
						match(SYM_DIGIT);
						v.add( d7.getText() );
					}
					else {
						if ( _cnt316>=1 ) { break _loop316; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt316++;
				} while (true);
				}
				
						a = new WBRuleActionNichtAuchVerb( s7.getText(), v );
					
				break;
			}
			case NICHTVERBZUS:
			{
				s8 = LT(1);
				match(NICHTVERBZUS);
				{
				int _cnt318=0;
				_loop318:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d8 = LT(1);
						match(SYM_DIGIT);
						v.add( d8.getText() );
					}
					else {
						if ( _cnt318>=1 ) { break _loop318; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt318++;
				} while (true);
				}
				
						a = new WBRuleActionNichtVerbZus( s8.getText(), v );
					
				break;
			}
			case NICHTWORTANF:
			{
				s9 = LT(1);
				match(NICHTWORTANF);
				{
				int _cnt320=0;
				_loop320:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d9 = LT(1);
						match(SYM_DIGIT);
						v.add( d9.getText() );
					}
					else {
						if ( _cnt320>=1 ) { break _loop320; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt320++;
				} while (true);
				}
				
						a = new WBRuleActionNichtWortAnf( s9.getText(), v );
					
				break;
			}
			case PRAEF:
			{
				s10 = LT(1);
				match(PRAEF);
				{
				int _cnt322=0;
				_loop322:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d10 = LT(1);
						match(SYM_DIGIT);
						v.add( d10.getText() );
					}
					else {
						if ( _cnt322>=1 ) { break _loop322; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt322++;
				} while (true);
				}
				
						a = new WBRuleActionPraef( s10.getText(), v );
					
				break;
			}
			case NICHTADJFLEKT:
			{
				s11 = LT(1);
				match(NICHTADJFLEKT);
				{
				int _cnt324=0;
				_loop324:
				do {
					if ((LA(1)==SYM_DIGIT)) {
						d11 = LT(1);
						match(SYM_DIGIT);
						v.add( d11.getText() );
					}
					else {
						if ( _cnt324>=1 ) { break _loop324; } else {throw new NoViableAltException(LT(1), getFilename());}
					}
					
					_cnt324++;
				} while (true);
				}
				
						a = new WBRuleActionNichtAdjFlekt( s11.getText(),v );	
					
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
			recover(ex,_tokenSet_2);
		}
		return a;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"LPAREN",
		"SYM_SYMBOL",
		"RPAREN",
		"\"=ortsname\"",
		"SYM_DIGIT",
		"\"agree_kat_wb-subcat\"",
		"\"agree_wb-subcat_kat\"",
		"\"fuge=-\"",
		"\"headpraef\"",
		"\"konj=und\"",
		"\"nicht_allo\"",
		"\"nicht_auch_verb\"",
		"\"nicht_verbzus\"",
		"\"nicht_wortanfang\"",
		"\"praef=un\"",
		"\"nicht_adjflek\"",
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
	
	}
