package eu.wiss_ki.aapi.editingSession;

import java.util.Arrays;
import java.util.Comparator;

import de.fau.cs8.mnscholz.util.Filter;
import de.fau.cs8.mnscholz.util.Span;

public final class AnnoUtil {
	
	private AnnoUtil () {}
	
	public static Comparator<TextSpan> FIRST_LENGTH_COMPARATOR = new Comparator<TextSpan>() {
		@Override
		public int compare(TextSpan arg0, TextSpan arg1) {
			int i = arg0.blockID.compareTo(arg1.blockID);
			if (i == 0) i = arg0.start - arg1.start;
			if (i == 0) i = arg0.end - arg1.end;
			return 0;
		}
	};
	
	public static Filter<Annotation> idFilter (final String... ids) {
		Arrays.sort(ids);
		return new Filter<Annotation>() {
			@Override
			public boolean passes(Annotation t) {
				return Arrays.binarySearch(ids, t.id) >= 0;
			}
		};
	}
	
	public static Filter<Annotation> blockFilter (final String... blockIDs) {
		Arrays.sort(blockIDs);
		return new Filter<Annotation>() {
			@Override
			public boolean passes(Annotation t) {
				return Arrays.binarySearch(blockIDs, t.blockID) >= 0;
			}
		};
	}
	
	public static Filter<Annotation> textSpanFilter (final TextSpan... spans) {
		Arrays.sort(spans, FIRST_LENGTH_COMPARATOR);
		return new Filter<Annotation>() {
			@Override
			public boolean passes(Annotation t) {
				return Arrays.binarySearch(spans, t, FIRST_LENGTH_COMPARATOR) >= 0;
			}
		};
	}
	
	public static Filter<Span> overlapsWithFilter (Span... spans) {
		final Span[] spans1 = spans.clone();
		return new Filter<Span>() {
			@Override
			public boolean passes(Span t) {
				for (Span s: spans1) if (s.overlapsWith(t)) return true;
				return false;
			}
		};
	}
	
	public static String toString (Annotation a) {
		
		StringBuilder buf = new StringBuilder();
		buf.append("<").append(a.blockID).append(":[").append(a.start).append(",").append(a.end).append("[ ");
		buf.append(a.id).append(", ").append(a.type).append(", ").append(a.infos);
		
		return buf.toString();
		
	}
	
}
