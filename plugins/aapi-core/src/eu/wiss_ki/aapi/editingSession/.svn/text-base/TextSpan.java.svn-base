package eu.wiss_ki.aapi.editingSession;

import de.fau.cs8.mnscholz.util.Span;

public class TextSpan extends Span {

	public final String blockID;

	public TextSpan(int start, int end, String blockID) {
		super(start, end);
		this.blockID = blockID;
	}
	
	public TextSpan(Span span, String blockID) {
		super(span.start, span.end);
		this.blockID = blockID;
	}
	
	protected TextSpan(TextSpan span) {
		super(span.start, span.end);
		this.blockID = span.blockID;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TextSpan && ((TextSpan) o).blockID.equals(blockID)) return super.equals(o);
		return false;
	}

	@Override
	public int hashcode() {
		return blockID.hashCode() + super.hashcode();
	}
	
}
