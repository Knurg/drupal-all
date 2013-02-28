package nerimd.annotation;

import java.util.Properties;

public abstract class TokenIterationAnnotator extends TokenAnnotator {
	
	public TokenIterationAnnotator(Properties p) {
		super(p);
	}

	@Override
	public final void annotateTokens(Token[] tokens) {
		
		for (Token cur: tokens) annotateTokens(cur);
		
	}
	
	protected abstract void annotateTokens (Token cur);
	
}
