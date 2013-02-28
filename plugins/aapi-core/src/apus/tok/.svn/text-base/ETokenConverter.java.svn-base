package apus.tok;

import de.fau.cs8.mnscholz.util.options.Options;


public class ETokenConverter extends PostProcessor {

	int typeincl;
	
	public ETokenConverter(Tokenizer tok, Options options) {
		super(tok, options);
		typeincl = Integer.parseInt(options.get("typeIncl", "ffffff").toLowerCase(), 16);
		
	}

	@Override
	public PostProcessor tokenize(Tokenizer tok) {
		return new ETokenConverter(tok, options);
	}

	@Override
	protected Token next() {
		Token t = tok.read();
		if (t == null) return null;
		if (t.isType(typeincl)) return new EToken(t.surface, t.type);
		return t;
	}
	
}
