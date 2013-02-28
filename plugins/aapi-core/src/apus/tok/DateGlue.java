package apus.tok;

import de.fau.cs8.mnscholz.util.options.Options;

public class DateGlue extends BufferedPostProcessor {
	
	public DateGlue(Tokenizer tok, Options options) {
		super(tok, options);
	}
	
	@Override
	public PostProcessor tokenize(Tokenizer tok) {
		return new DateGlue(tok, options);
	}

	@Override
	protected Token next() {
		
		Token t0 = bufferGet(0);
//System.out.println(">>> t0=" + t0);
		if (t0 == null) return null;
		if (! t0.surface.matches("\\d+")) return bufferRemove();

		Token t1 = bufferGet(1);
//System.out.println(">>> t1=" + t0);		
		if (t1 == null) return bufferRemove();
		if (! t1.surface.equals(".") && ! t1.surface.equals("/")) return bufferRemove();
		
		Token t2 = bufferGet(2);
//System.out.println(">>> t2=" + t0);		
		if (t2 == null) return bufferRemove();
		if (! t2.surface.matches("\\d+")) return bufferRemove();
		
		Token t3 = bufferGet(3);
//System.out.println(">>> t3=" + t0);		
		if (t3 == null) return bufferRemove();
		if (! t3.surface.equals(t1.surface)) return bufferRemove();
		
		Token t4 = bufferGet(4);
//System.out.println(">>> t4=" + t0);		
		if (t4 == null) return bufferRemove();
		if (! t4.surface.matches("\\d+")) return bufferRemove();
		
		return melt(4, WORD);
		
	}

}
