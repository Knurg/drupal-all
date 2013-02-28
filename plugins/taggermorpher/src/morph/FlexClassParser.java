// $ANTLR : "flexclass_parser.g" -> "FlexClassParser.java"$

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

public class FlexClassParser extends antlr.LLkParser       implements FlexClassParserTokenTypes
 {

protected FlexClassParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public FlexClassParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected FlexClassParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public FlexClassParser(TokenStream lexer) {
  this(lexer,1);
}

public FlexClassParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final Hashtable  flexclasslist() throws RecognitionException, TokenStreamException {
		Hashtable h;
		
		Token  s = null;
		
			Vector v;
		
			h = new Hashtable();
		
		
		try {      // for error handling
			{
			int _cnt511=0;
			_loop511:
			do {
				if ((LA(1)==SYM_SYMBOL)) {
					s = LT(1);
					match(SYM_SYMBOL);
					v=flexclass();
					h.put( s.getText(), v );
				}
				else {
					if ( _cnt511>=1 ) { break _loop511; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt511++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return h;
	}
	
	public final Vector  flexclass() throws RecognitionException, TokenStreamException {
		Vector h;
		
		Token  s = null;
		
			h = new Vector();
		
		
		try {      // for error handling
			match(LPAREN);
			{
			int _cnt514=0;
			_loop514:
			do {
				if ((LA(1)==SYM_SYMBOL)) {
					s = LT(1);
					match(SYM_SYMBOL);
					h.add( s.getText() );
				}
				else {
					if ( _cnt514>=1 ) { break _loop514; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt514++;
			} while (true);
			}
			match(RPAREN);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return h;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"SYM_SYMBOL",
		"LPAREN",
		"RPAREN",
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
	
	}
