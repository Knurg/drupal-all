package apus.tok;

import java.io.Reader;

import de.fau.cs8.mnscholz.util.options.Options;

public abstract class PostProcessor extends Tokenizer {
	
	protected Tokenizer tok;
	
	public PostProcessor(Tokenizer tok, Options options) {
		super(options);
		if (tok == null) throw new NullPointerException();
		this.tok = tok;
	}
	
	public abstract PostProcessor tokenize (Tokenizer tok);
	
	public String buffer () {
		return tok.buffer();
	}
	
	public PostProcessor tokenize (Reader r) {
		Tokenizer t = tok.tokenize(r);
		return tokenize(t);
	}
	
}
