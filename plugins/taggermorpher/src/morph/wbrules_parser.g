header {
package morph;
	import java.util.Vector;
	import java.util.Hashtable;
}

class WBRulesParser extends Parser;

rules returns [Hashtable h]
{
	WBRule r;
	h = new Hashtable();
}
	: (r=rule { h.put( r.getName(), r ); })+
	;

rule returns [WBRule r]
{
	String ruleName = "";
	WBRuleTransition t;
	Hashtable ht = new Hashtable();

	r = null;
}
	: LPAREN
	  s:SYM_SYMBOL
	  (t=transition { ht.put( t.getName(), t ); })+
	  RPAREN
	{
		ruleName = s.getText();
		r = new WBRule( ruleName, ht );
	}
	;

transition returns [WBRuleTransition t]
{
	WBRuleAction a;

	Vector v = new Vector();
	t = null;
}
	: LPAREN
	  s:SYM_SYMBOL
	  (a=action { v.add( a ); })*
	  RPAREN
	{
		t = new WBRuleTransition( s.getText(), v );
	}
	;

action returns [WBRuleAction a]
{
	Vector v = new Vector();

	a = null;
}
	: LPAREN
	( s0:ORTSNAME ( d0:SYM_DIGIT { v.add( d0.getText() ); } )+
	  {
		a = new WBRuleActionOrtsname( s0.getText(), v );
	  }
	| s1:AGREEKATWB ( d1:SYM_DIGIT { v.add( d1.getText() ); } )+
	  {
		a = new WBRuleActionAgreeKatWb( s1.getText(), v );
	  }
	| s2:AGREEWBKAT ( d2:SYM_DIGIT { v.add( d2.getText() ); } )+
	  {
		a = new WBRuleActionAgreeWbKat( s2.getText(), v );
	  }
	| s3:FUGE ( d3:SYM_DIGIT { v.add( d3.getText() ); } )+
	  {
		a = new WBRuleActionFuge( s3.getText(), v );
	  }
	| s4:HEADPRAEF ( d4:SYM_DIGIT { v.add( d4.getText() ); } )+
	  {
		a = new WBRuleActionHeadPraef( s4.getText(), v );
	  }
	| s5:KONJ ( d5:SYM_DIGIT { v.add( d5.getText() ); } )+
	  {
		a = new WBRuleActionKonj( s5.getText(), v );
	  }
	| s6:NICHTALLO ( d6:SYM_DIGIT { v.add( d6.getText() ); } )+
	  {
		a = new WBRuleActionNichtAllo( s6.getText(), v );
	  }
	| s7:NICHTAUCHVERB ( d7:SYM_DIGIT { v.add( d7.getText() ); } )+
	  {
		a = new WBRuleActionNichtAuchVerb( s7.getText(), v );
	  }
	| s8:NICHTVERBZUS ( d8:SYM_DIGIT { v.add( d8.getText() ); } )+
	  {
		a = new WBRuleActionNichtVerbZus( s8.getText(), v );
	  }
	| s9:NICHTWORTANF ( d9:SYM_DIGIT { v.add( d9.getText() ); } )+
	  {
		a = new WBRuleActionNichtWortAnf( s9.getText(), v );
	  }
	| s10:PRAEF ( d10:SYM_DIGIT { v.add( d10.getText() ); } )+
	  {
		a = new WBRuleActionPraef( s10.getText(), v );
	  }
	| s11:NICHTADJFLEKT ( d11:SYM_DIGIT { v.add( d11.getText() ); } )+
	  {
		a = new WBRuleActionNichtAdjFlekt( s11.getText(),v );	
	  }
        )
        RPAREN
	;

class WBRulesLexer extends Lexer;

tokens {
	NICHTADJFLEKT = "nicht_adjflek";
	ORTSNAME = "=ortsname";
	AGREEKATWB = "agree_kat_wb-subcat";
	AGREEWBKAT = "agree_wb-subcat_kat";
	FUGE = "fuge=-";
	HEADPRAEF = "headpraef";
	KONJ = "konj=und";
	NICHTALLO = "nicht_allo";
	NICHTAUCHVERB = "nicht_auch_verb";
	NICHTVERBZUS = "nicht_verbzus";
	NICHTWORTANF = "nicht_wortanfang";
	PRAEF = "praef=un";
}

WS: ( ' ' |
	'\r' |
      '\n' |
      '\t' ) { _ttype = Token.SKIP; };

LPAREN: '(';

RPAREN: ')';

SYM_SYMBOL: ( '=' | '_' | ':' | '-' | 'A'..'Z' | 'a' .. 'z' )+;

SYM_DIGIT: ( '0'..'9' )+;
