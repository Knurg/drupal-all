package apus;

import lebada.fs.FS;
import lebada.fs.FSUtil;
import stemmer.Stemmer;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.EToken;

public class TaggerMorpherEN extends TaggerMorpher {
	
	Stemmer stemmer;
	
	public TaggerMorpherEN(Options options) {
		super(options);
		stemmer = new Stemmer();
	}
	
	@Override
	public void parsePOSLemmata(EToken[] toks) {
		for (EToken t: toks) {
			if (t.info == null) t.info = FSUtil.newCFS();
			t.info.set("lemmata", new String[]{stemmer.stem(t.surface)});
		}
	}
	
	@Override
	public FS[] parsePOSLemmata(String[] toks) {
		FS[] fs = new FS[toks.length];
		for (int i = 0; i < toks.length; i++) {
			fs[i] = FSUtil.newFS("lemmata", new String[]{stemmer.stem(toks[i])});
		}
		return fs;
	}
	
	@Override
	public FS[] parsePOSFullMorph(String[] toks) {
		FS[] fs = new FS[toks.length];
		for (int i = 0; i < toks.length; i++) {
			fs[i] = FSUtil.newFS("morph", new FS[]{FSUtil.newFS("lemma", stemmer.stem(toks[i]))});
		}
		return fs;
	}
	
}
