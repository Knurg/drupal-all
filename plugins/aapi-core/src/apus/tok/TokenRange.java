package apus.tok;

public class TokenRange {
	
	public final Token[] tokens;
	public final int startOffset, endOffset;
	
	TokenRange(Token[] tokens, int startOffset, int endOffset) {
		super();
		if (tokens == null) throw new NullPointerException();
		if (tokens.length == 0) throw new IllegalArgumentException("no tokens");
		if (startOffset < 0 ||
			startOffset >= tokens[0].surface.length() ||
			0 < endOffset ||
			endOffset > tokens[tokens.length - 1].surface.length() ||
			(tokens.length == 1 && startOffset > endOffset))
			throw new IllegalArgumentException("no tokens");
		
		this.tokens = tokens;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}



	public String getText() {
		if (tokens.length == 1)
			return tokens[0].surface.substring(startOffset, endOffset);
		
		StringBuilder b = new StringBuilder(tokens[0].surface.substring(startOffset));
		for (int i = 1; i < tokens.length - 1; i++) {
			b.append(tokens[i].surface);
		}
		b.append(tokens[tokens.length - 1]);
		return b.toString();
	}
	
}
