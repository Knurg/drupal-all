// $ANTLR : "morph_parser.g" -> "MorphLexer.java"$

package morph;
	import java.util.Vector;
	import java.util.Hashtable;
	import de.fau.cs.jill.feature.*;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

public class MorphLexer extends antlr.CharScanner implements MorphParserTokenTypes, TokenStream
 {
public MorphLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public MorphLexer(Reader in) {
	this(new CharBuffer(in));
}
public MorphLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public MorphLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
	literals.put(new ANTLRHashString("MK", this), new Integer(31));
	literals.put(new ANTLRHashString("KONJ_PRAETERITUM", this), new Integer(72));
	literals.put(new ANTLRHashString("PERSONAL", this), new Integer(18));
	literals.put(new ANTLRHashString("MODALVERB", this), new Integer(49));
	literals.put(new ANTLRHashString("P", this), new Integer(54));
	literals.put(new ANTLRHashString("POSSESIVPRONOMEN", this), new Integer(59));
	literals.put(new ANTLRHashString("FEM", this), new Integer(79));
	literals.put(new ANTLRHashString("DET", this), new Integer(58));
	literals.put(new ANTLRHashString("BOUND", this), new Integer(41));
	literals.put(new ANTLRHashString("AKK", this), new Integer(67));
	literals.put(new ANTLRHashString("DEKTYPI", this), new Integer(83));
	literals.put(new ANTLRHashString("INTERJ", this), new Integer(57));
	literals.put(new ANTLRHashString("DAT", this), new Integer(66));
	literals.put(new ANTLRHashString("INFINITIV", this), new Integer(17));
	literals.put(new ANTLRHashString("PARTIZIP-PERFEKT", this), new Integer(68));
	literals.put(new ANTLRHashString("WB-SUBCAT", this), new Integer(42));
	literals.put(new ANTLRHashString("PARTPRAEF", this), new Integer(38));
	literals.put(new ANTLRHashString("GEN", this), new Integer(11));
	literals.put(new ANTLRHashString("SG", this), new Integer(73));
	literals.put(new ANTLRHashString("IMPERATIV", this), new Integer(13));
	literals.put(new ANTLRHashString("N", this), new Integer(16));
	literals.put(new ANTLRHashString("REFLEXIV", this), new Integer(19));
	literals.put(new ANTLRHashString("DEKTYPII", this), new Integer(84));
	literals.put(new ANTLRHashString("SUF", this), new Integer(35));
	literals.put(new ANTLRHashString("PRON", this), new Integer(60));
	literals.put(new ANTLRHashString("KASREK", this), new Integer(55));
	literals.put(new ANTLRHashString("PL", this), new Integer(74));
	literals.put(new ANTLRHashString("INTERROGATIVADVERB", this), new Integer(52));
	literals.put(new ANTLRHashString("RELATIV", this), new Integer(20));
	literals.put(new ANTLRHashString("IND_PRAETERITUM", this), new Integer(71));
	literals.put(new ANTLRHashString("TVZ", this), new Integer(27));
	literals.put(new ANTLRHashString("GF", this), new Integer(47));
	literals.put(new ANTLRHashString("INFINITIVPARTIKEL", this), new Integer(26));
	literals.put(new ANTLRHashString("3", this), new Integer(77));
	literals.put(new ANTLRHashString("KONFIXFUGE", this), new Integer(32));
	literals.put(new ANTLRHashString("DEKTYPIII", this), new Integer(85));
	literals.put(new ANTLRHashString("INDEFINIT", this), new Integer(14));
	literals.put(new ANTLRHashString("IND_PRAESENS", this), new Integer(69));
	literals.put(new ANTLRHashString("2", this), new Integer(76));
	literals.put(new ANTLRHashString("SUP", this), new Integer(63));
	literals.put(new ANTLRHashString("NEUT", this), new Integer(80));
	literals.put(new ANTLRHashString("VPR", this), new Integer(28));
	literals.put(new ANTLRHashString("FLEXION", this), new Integer(9));
	literals.put(new ANTLRHashString("1", this), new Integer(75));
	literals.put(new ANTLRHashString("NOUNSUF", this), new Integer(37));
	literals.put(new ANTLRHashString("V", this), new Integer(43));
	literals.put(new ANTLRHashString("FUGE", this), new Integer(10));
	literals.put(new ANTLRHashString("SING", this), new Integer(7));
	literals.put(new ANTLRHashString("=", this), new Integer(24));
	literals.put(new ANTLRHashString("NUMSUF", this), new Integer(39));
	literals.put(new ANTLRHashString("KONTR", this), new Integer(64));
	literals.put(new ANTLRHashString("GF_STAMM", this), new Integer(46));
	literals.put(new ANTLRHashString("SYN", this), new Integer(30));
	literals.put(new ANTLRHashString("INTERROGATIV", this), new Integer(15));
	literals.put(new ANTLRHashString("MWTRIGG", this), new Integer(25));
	literals.put(new ANTLRHashString("PLUR", this), new Integer(82));
	literals.put(new ANTLRHashString("NUM", this), new Integer(50));
	literals.put(new ANTLRHashString("KONFIX", this), new Integer(36));
	literals.put(new ANTLRHashString("POS", this), new Integer(61));
	literals.put(new ANTLRHashString("ABK", this), new Integer(44));
	literals.put(new ANTLRHashString("TYP", this), new Integer(21));
	literals.put(new ANTLRHashString("HILFSVERB", this), new Integer(48));
	literals.put(new ANTLRHashString("MASK", this), new Integer(78));
	literals.put(new ANTLRHashString("ALLO", this), new Integer(45));
	literals.put(new ANTLRHashString("NOM", this), new Integer(65));
	literals.put(new ANTLRHashString("ADJ", this), new Integer(40));
	literals.put(new ANTLRHashString("DEMONSTRATIV", this), new Integer(8));
	literals.put(new ANTLRHashString("ADV", this), new Integer(51));
	literals.put(new ANTLRHashString("VF", this), new Integer(22));
	literals.put(new ANTLRHashString("GRAD", this), new Integer(12));
	literals.put(new ANTLRHashString("ORTNUMSUF", this), new Integer(23));
	literals.put(new ANTLRHashString("UNFLADJ", this), new Integer(53));
	literals.put(new ANTLRHashString("PRAEF", this), new Integer(34));
	literals.put(new ANTLRHashString("LEHNSUF", this), new Integer(33));
	literals.put(new ANTLRHashString("KONJ_PRAESENS", this), new Integer(70));
	literals.put(new ANTLRHashString("SEM", this), new Integer(29));
	literals.put(new ANTLRHashString("KONJ", this), new Integer(56));
	literals.put(new ANTLRHashString("KOMP", this), new Integer(62));
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(true);
					theRetToken=_returnToken;
					break;
				}
				case '(':
				{
					mLPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case ')':
				{
					mRPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case '=':
				{
					mSYM_EQ(true);
					theRetToken=_returnToken;
					break;
				}
				case '-':  case '0':  case '1':  case '2':
				case '3':  case '4':  case '5':  case '6':
				case '7':  case '8':  case '9':  case ':':
				case 'A':  case 'B':  case 'C':  case 'D':
				case 'E':  case 'F':  case 'G':  case 'H':
				case 'I':  case 'J':  case 'K':  case 'L':
				case 'M':  case 'N':  case 'O':  case 'P':
				case 'Q':  case 'R':  case 'S':  case 'T':
				case 'U':  case 'V':  case 'W':  case 'X':
				case 'Y':  case 'Z':  case '_':  case '\u00c4':
				case '\u00d6':  case '\u00dc':
				{
					mSYM_SYMBOL(true);
					theRetToken=_returnToken;
					break;
				}
				default:
				{
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				throw new TokenStreamRecognitionException(e);
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		{
		switch ( LA(1)) {
		case ' ':
		{
			match(' ');
			break;
		}
		case '\r':
		{
			match('\r');
			break;
		}
		case '\n':
		{
			match('\n');
			break;
		}
		case '\t':
		{
			match('\t');
			break;
		}
		default:
		{
			throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
		}
		}
		}
		_ttype = Token.SKIP;
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LPAREN;
		int _saveIndex;
		
		match('(');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RPAREN;
		int _saveIndex;
		
		match(')');
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSYM_EQ(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SYM_EQ;
		int _saveIndex;
		
		match("=");
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSYM_SYMBOL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SYM_SYMBOL;
		int _saveIndex;
		
		{
		int _cnt436=0;
		_loop436:
		do {
			switch ( LA(1)) {
			case '_':
			{
				match('_');
				break;
			}
			case ':':
			{
				match(':');
				break;
			}
			case '-':
			{
				match('-');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				break;
			}
			case '\u00dc':
			{
				match('Ü');
				break;
			}
			case '\u00c4':
			{
				match('Ä');
				break;
			}
			case '\u00d6':
			{
				match('Ö');
				break;
			}
			default:
			{
				if ( _cnt436>=1 ) { break _loop436; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
			}
			}
			_cnt436++;
		} while (true);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	
	}
