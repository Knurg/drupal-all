header {
package morph;
	import java.util.Vector;
	import java.util.Hashtable;
}

class FlexClassParser extends Parser;

flexclasslist returns [Hashtable h]
{
	Vector v;

	h = new Hashtable();
}
	: (s:SYM_SYMBOL v=flexclass { h.put( s.getText(), v ); })+
	;

flexclass returns [Vector h]
{
	h = new Vector();
}
	: LPAREN
	  (s:SYM_SYMBOL { h.add( s.getText() ); })+
	  RPAREN
	;

class FlexClassLexer extends Lexer;

WS: ( ' ' |
      '\r' |
      '\n' |
      '\t' ) { _ttype = Token.SKIP; };

LPAREN: '(';

RPAREN: ')';

SYM_SYMBOL: ( '=' | '_' | ':' | '-' | 'A'..'Z' | 'a' .. 'z' | '0'..'9' )+;
