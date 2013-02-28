package apus.tok;

import java.util.List;
import java.util.logging.Logger;

import de.fau.cs8.mnscholz.util.Filter;
import de.fau.cs8.mnscholz.util.Span;

public final class TokenUtil {
	
	private static Filter<Token> btFilter = null;
	
	public static Filter<Token> blackTokenFilter () {
		if (btFilter == null) btFilter = new Filter<Token> () {
			public boolean passes(Token t) { return t.isType(Tokenizer.BLACK); }
		};
		return btFilter;
	}
	
	public static String toString (Token t) {
		StringBuilder b = new StringBuilder();
		if (t.surface == null) b.append("null");
		else b.append("'").append(t.surface).append("'");
		b.append(" (").append(t.type).append(")");
		if (t instanceof EToken)
			b.append(" info: ").append(((EToken) t).info);
		return b.toString();
	}
	
	
	public static Span getTokenSpan (Iterable<Token> refTokens, Token token) {
		int p = 0;
		for (Token t: refTokens) {
			if (t == token) return new Span(p, p + t.surface.length());
			p += t.surface.length();
		}
		throw new IllegalArgumentException("token = " + TokenUtil.toString(token));
	}
	
	
	public static Span convertTokenSpanToCharSpan (List<? extends Token> refTokens, Span span) {
		if (span.start < 0 || span.start > refTokens.size() || span.end > refTokens.size())
			throw new ArrayIndexOutOfBoundsException("refTokens size = " + refTokens.size() + ", span = " + span.toString());
		
		// convert token position to character position (TermOccurence has token position!)
		int s = 0;
		int e = 0;
		int i = 0;
		for (Token t: refTokens) {
			int l = t.surface.length();
			if (i < span.start) s += l;
			if (i < span.end) e += l;
			else break;
			i++;
		}
		Logger.getAnonymousLogger().finest("start="+span.start+" end="+span.end+"\nreftokens="+refTokens+"\ns="+s+" e="+e);
		
		return new Span(s, e);
		
	}
	
	
	
	
	
	public static Span getTokenSpan (Iterable<Token> refTokens, int tpos) {
		int i = 0;
		int p = 0;
		for (Token t: refTokens) {
			if (tpos == i) {
				return new Span(p, p + t.surface.length());
			}
			p += t.surface.length();
			i++;
		}
		throw new IllegalArgumentException("tpos = " + tpos + "; refTokens length = " + i);
	}
	
}
