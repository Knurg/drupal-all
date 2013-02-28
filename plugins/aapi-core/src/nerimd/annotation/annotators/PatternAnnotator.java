package nerimd.annotation.annotators;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import nerimd.annotation.AnnotationType;
import nerimd.annotation.TokenAnnotator;

public class PatternAnnotator extends TokenAnnotator {

	public PatternAnnotator(Properties p) {
		super(p);
		makePatterns();
		type = properties.getProperty("type");
	}
	
	private void makePatterns() {
		
		List<Pattern[]> pats = new ArrayList<Pattern[]>();
		List<boolean[]> els = new ArrayList<boolean[]>();
		
		for (String s: properties.getProperty("patterns").split("\\s+")) {
			String[] sa = properties.getProperty("pattern." + s).split("\\s+");
			Pattern[] pa = new Pattern[sa.length];
			boolean[] ba = new boolean[sa.length];
			for (int i = 0; i < sa.length; i++) {
				ba[i] = sa[i].startsWith("E");
				pa[i] = Pattern.compile(sa[i].substring(1));
			}
			pats.add(pa);
			els.add(ba);
		}
		
		patterns = pats.toArray(new Pattern[pats.size()][]);
		emptylemma = els.toArray(new boolean[els.size()][]);
		
	}
	
	protected Pattern[][] patterns;		// patterns sorted by length
	protected boolean[][] emptylemma;	// same size as patterns; when true, the pattern requires an empty lemma field
	protected boolean overlap;			// if true annotations will not overlap; ie. annotated text will be skipped
	protected String type;
	
	@Override
	protected void annotateTokens(Token[] tokens) {
		
		for (int pos = 0; pos < tokens.length; pos++) {
			
for01:
			for (int i = 0; i < patterns.length; i++) {
				Pattern[] pa = patterns[i];
				if (pa.length + pos > tokens.length) continue;
				
				for (int j = 0; j < pa.length; j++) {
					Token t = tokens[pos + j];
					if (emptylemma[i][j] != (t.lemma == null || t.lemma.length() == 0)) continue for01;
					if (pa[j].matcher(t.surface).matches()) continue;
					if ((t.lemma != null && t.lemma.length() != 0) &&
						pa[j].matcher(t.lemma).matches()) continue;
					continue for01;
				}
				
				makeAnno(AnnotationType.valueOf(type), null, tokens[pos], tokens[pos + pa.length - 1], null);
				if (! overlap) pos +=pa.length - 1;
				
			}
			
		}
		
	}


}
