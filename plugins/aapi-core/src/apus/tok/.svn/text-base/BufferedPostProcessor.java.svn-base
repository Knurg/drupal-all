package apus.tok;

import java.util.ArrayList;

import de.fau.cs8.mnscholz.util.options.Options;

public abstract class BufferedPostProcessor extends PostProcessor {

	private ArrayList<Token> tokbuf = new ArrayList<Token>();
	
	public BufferedPostProcessor(Tokenizer tok, Options options) {
		super(tok, options);
	}

	protected Token bufferGet (int i) {
		
		if (tokbuf.size() <= i) {
			if (tok == null) {
				
				return null; 
			}
			do {
				Token t = tok.read();
				if (t == null) {
					tok = null;
					return null;
				}
				tokbuf.add(t);
			} while (tokbuf.size() <= i);
		}
		
		return tokbuf.get(i);
		
	}
	
	protected Token bufferRemove () {
		return tokbuf.isEmpty() ? null : tokbuf.remove(0);
	}
	
	
	protected Token melt (int i, int type) {
		if (i == 0) return tokbuf.remove(0);
		String s = "";
		for (int j = 0; j <= i; j++) s += tokbuf.get(j).surface;
		tokbuf.subList(0, i + 1).clear();
		return new Token(s, type);
	}
	
}
