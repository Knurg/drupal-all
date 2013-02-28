package apus.tok;

import java.util.Arrays;

import de.fau.cs8.mnscholz.util.options.Options;

public class LCAbbrev extends BufferedPostProcessor {

	private String[] abbrSym;
	private final static int ABBREV = 26;	// always a WORD
	
	public LCAbbrev(Tokenizer tok, Options options) {
		super(tok, options);
		abbrSym = options.get("abbrSym", ".").split("\\s+");
		Arrays.sort(abbrSym);
	}

	LCAbbrev(Tokenizer tok, Options options, String[] abbrSym) {
		super(tok, options);
		this.abbrSym = abbrSym;
		
	}

	@Override
	protected Token next() {
		Token t0 = bufferGet(0);
		if (t0 == null) return null;
		if (! t0.isType(WORD)) return bufferRemove();
		Token t1 = bufferGet(1);
		if (t1 == null) return bufferRemove();
		if (Arrays.binarySearch(abbrSym, t1.surface) < 0) return bufferRemove();
		Token t2;
		int i = 2;
		do t2 = bufferGet(i++); while (t2 != null && t2.isType(WHITE));
		if (t2 == null) return bufferRemove();
		if (! t2.isType(WORD) || ! Character.isLowerCase(t2.surface.charAt(0)))
			return bufferRemove();

		// now we have a word followed by a abbrev symbol followed by
		// optional ws followed by a sign or a lowercase word
		// => melt t0 and t1 to abbrev
		Token t = melt(1, ABBREV);
		return t;
		
		
	}

	@Override
	public PostProcessor tokenize(Tokenizer tok) {
		return new LCAbbrev(tok, options, abbrSym);
	}

	
}
