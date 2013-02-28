package apus.tok;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import de.fau.cs8.mnscholz.util.options.Options;
import apus.LexTree;

public class ListGlue extends BufferedPostProcessor {

	private static final Object CONT = new Object(){};
	
	private LexTree singles;

	
	public ListGlue(Tokenizer tok, Options options) {
		super(tok, options);
		singles = new LexTree.HashMapImpl();
		fillSingles();
	}
	
	

	ListGlue(Tokenizer tok, Options options, LexTree singles) {
		super(tok, options);
		this.singles = singles;
	}



	@Override
	public PostProcessor tokenize(Tokenizer tok) {
		return new ListGlue(tok, options, singles);
	}

	@Override
	protected Token next() {

		int last = 0;
		LexTree now = singles;
		
		for (int i = 0;; i++) {
			Token t = bufferGet(i);
			if (t == null) return bufferRemove();
			if (t.isType(WHITE)) {	// collapse ws
				now = now.get(" ");
			} else if (t.isType(BLACK)) {
				now = now.get(t.surface);
			} else {
				break;
			}
			if (now == null) break;
			if (now.info == CONT) last = i;
		}
		
		return melt(last, WORD);
	}
	
	private void fillSingles () {
		singlesFromLSVFiles(options.get("combinedTokensFiles", "").split(","));
	}
	
	private void singlesFromLSVFiles (String[] fna) {
		String l;
		BufferedReader r;
		singles.info = CONT;
		
		for (String fn: fna) {
			File f = new File(fn);
			if (!f.exists() || !f.canRead()) throw new RuntimeException("no such file: " + fn);
			try {
				r = new BufferedReader(new FileReader(f));
				while ((l = r.readLine()) != null) {
					singles.ensure(l).info = CONT;
				}	
			} catch (Exception e) {
				throw new RuntimeException("loading lexicon", e);
			}
		}
	
	}
	
	
}
