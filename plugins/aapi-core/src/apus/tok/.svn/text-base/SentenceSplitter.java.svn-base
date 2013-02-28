package apus.tok;

import java.util.Arrays;

import de.fau.cs8.mnscholz.util.options.Options;

public class SentenceSplitter extends PostProcessor {
	
	String[] delimiters;
	boolean delAsSeg;
	Token nextseg = null;
	Token previous = null;
	
	public SentenceSplitter(Tokenizer tok, Options options) {
		super(tok, options);
		delimiters = options.get("delimiters", ". ... , ; : ? !").trim().split("\\s+");
		delAsSeg = "true".equals(options.get("delimiterAsSEG", "false"));
		Arrays.sort(delimiters);
	}
	
	

	SentenceSplitter(Tokenizer tok, Options options, String[] delimiters, boolean delAsSeg) {
		super(tok, options);
		this.delimiters = delimiters;
		this.delAsSeg = delAsSeg;
	}



	@Override
	public PostProcessor tokenize(Tokenizer tok) {
		return new SentenceSplitter(tok, options, delimiters, delAsSeg);
	}

	@Override
	protected Token next() {
		
		if (nextseg != null) {
			Token s = nextseg;
			nextseg = null;
			return s;
		}
		
		Token t = tok.next();
		if (t == null) return null;
		if (previous != null && previous.surface.matches("\\d+") && t.surface.matches(":|.|/")) ;
		else if (Arrays.binarySearch(delimiters, t.surface) >= 0) {
			if (delAsSeg) t.type |= SEG;
			else nextseg = new Token("", SEG);
		}
		return t;
	}

}
