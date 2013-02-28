package apus.tok;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import de.fau.cs8.mnscholz.util.options.Options;

public class BaseTokenizer extends ReaderTokenizer {
	
	private StringBuilder buf = new StringBuilder();
	private char[] singles;
	private char[] ws;
	
	public BaseTokenizer(Reader r, Options o) {
		super(r, o);
		singles = options.get("singleChars", "!\"#$%&'()*+,-./:;<=>?[\\]_{|}§").toCharArray();
		Arrays.sort(singles);
		// properties file: \\uXXXX werden von einem präprocessor umgewandelt
		ws = options.get("wsChars", "\t\n\u000b\f\r \u00A0\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A\u200B\u202F\u205F\u3000\uFEFF").toCharArray();
		Arrays.sort(ws);
		
	}
	
	
	protected BaseTokenizer(Reader r, Options options, 
			char[] singles, char[] ws) {
		super(r, options);
		this.singles = singles;
		this.ws = ws;
	}
	
	
	public BaseTokenizer tokenize (Reader r) {
		return new BaseTokenizer(r, options, singles, ws);
	}
	
	
	public String buffer () {
		return buf.toString();
	}
	
	
	protected Token next () {
		
		if (r == null) return EOF;
		int c = -1;
		
		try {
			if (buf.length() == 0) {
				c = r.read();
				if (c == -1) return EOF;
				buf.append((char) c);
			}
			
			if (Arrays.binarySearch(singles, buf.charAt(0)) >= 0) {
				// single char
				Token t = new Token(buf.substring(0, 1), SIGN); 
				buf.setLength(0);
				
				return t;
				
			} else if (Arrays.binarySearch(ws, buf.charAt(0)) >= 0) {
				// ws
				String s;
				
				while (Arrays.binarySearch(ws, buf.charAt(buf.length() - 1)) >= 0) {
					c = r.read();
					if (c == -1) {
						s = buf.toString();
						buf.setLength(0);
						return new Token( s, WHITE);
					}
					buf.append((char) c);
				}
				
				s = buf.substring(0, buf.length() - 1);
				buf.delete(0, buf.length() - 1);
				return new Token(s, WHITE);
				
			} else {
				// word
				String s;
				
				while (Arrays.binarySearch(ws, buf.charAt(buf.length() - 1)) < 0
					&& Arrays.binarySearch(singles, buf.charAt(buf.length() - 1)) < 0) {
					
					c = r.read();
					if (c == -1) {
						s = buf.toString();
						buf.setLength(0);
						return new Token(s, WORD);
					}
					buf.append((char) c);
				}
				
				s = buf.substring(0, buf.length() - 1);
				buf.delete(0, buf.length() - 1);
				return new Token(s, WORD);
					
			}
		} catch (IOException e) {
			throw new TokenizerException(e);
		}
		
	}
	
	
}
