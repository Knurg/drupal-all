package apus.tok;

import java.util.Arrays;

import de.fau.cs8.mnscholz.util.options.Options;

public class HyphenGlue extends BufferedPostProcessor {
	
	private String[] hyphens;
	private boolean rTruncs;
	private boolean lTruncs;
	
	public HyphenGlue(Tokenizer tok, Options options) {
		super(tok, options);
		hyphens = options.get("hyphens", "-").split("\\s+");
		Arrays.sort(hyphens);
		String tr = options.get("truncs", "lr");
		lTruncs = tr.contains("l");
		rTruncs = tr.contains("r");
	}
	
	protected HyphenGlue(Tokenizer tok, Options options, String[] hyphens,
			boolean rTruncs, boolean lTruncs) {
		super(tok, options);
		this.hyphens = hyphens;
		this.rTruncs = rTruncs;
		this.lTruncs = lTruncs;
	}
	
	@Override
	public PostProcessor tokenize(Tokenizer tok) {
		return new HyphenGlue(tok, options, hyphens, rTruncs, lTruncs);
	}

	@Override
	protected Token next() {
	
		int i = 0;
		
		if (lTruncs) {
			Token t0 = bufferGet(0);
			if (t0 == null) return null;
			if (t0.isType(WHITE)) return bufferRemove();
			if (t0.isType(SIGN) && Arrays.binarySearch(hyphens, t0.surface) >= 0) {
				Token t1 = bufferGet(1);
				if (t1 == null || ! t1.isType(WORD)) return bufferRemove();
				i++;
			}
		}
		
		Token t0 = bufferGet(i);
		if (t0 == null) return null;
		if (! t0.isType(WORD)) return bufferRemove();
		i++;
		
		while (true) {
			Token t1 = bufferGet(i);
			if (t1 == null
				|| ! t1.isType(SIGN)
				|| Arrays.binarySearch(hyphens, t1.surface) < 0)
				return melt(i - 1, WORD);
			i++;
			Token t2 = bufferGet(i);
			if (t2 == null || ! t2.isType(WORD)) {
				if (! rTruncs) i--;
				return melt(i - 1, WORD);
			}
			i++;
		}
		
	}

}
