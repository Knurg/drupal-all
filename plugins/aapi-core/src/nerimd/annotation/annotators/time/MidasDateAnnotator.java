package nerimd.annotation.annotators.time;

import java.util.Collections;
import java.util.Properties;
import java.util.HashMap;

import nerimd.annotation.AnnotationType;
import nerimd.annotation.TokenIterationAnnotator;

public class MidasDateAnnotator extends TokenIterationAnnotator {

	public MidasDateAnnotator(Properties p) {
		super(p);
	}

	@Override
	protected void annotateTokens(Token cur) {
		
		if (cur.surface.matches("\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d")) {	// YYYY.MM.DD
			
			makeAnno(AnnotationType.POINT_IN_TIME, Collections.singletonMap("when", cur.surface.replaceAll("\\.", "-")), cur, cur, null);
			
		}

	    if (cur.surface.matches("\\d\\d?\\.\\d\\d?\\.\\d\\d(\\d\\d)?")) { // dd.mm.yyYY (not Midas, but the prolog doesnt do it properly
	
	      String[] t = cur.surface.split("\\.");
	      int d = Integer.parseInt(t[0]);
	      int m = Integer.parseInt(t[1]);
	      int y = Integer.parseInt(t[2]);
	
	      if (d < 32 && m < 13) {
	
	        if (t[0].length() == 1) t[0] = "0" + t[0];
	        if (t[1].length() == 1) t[1] = "0" + t[1];
	        if (t[2].length() == 2) t[2] = ((y < 30) ? "20" : "19") + t[2];
	
			makeAnno(AnnotationType.POINT_IN_TIME, Collections.singletonMap("when", t[2] + "-" + t[1] + "-" + t[0]), cur, cur, null);
	      }
	
	    }
	
	    if (cur.surface.matches("\\d{4}/\\d{2}(\\d{2})?")) {	// YYYY/yyYY 
	
	      String[] t = cur.surface.split("/");
	      int j1 = Integer.parseInt(t[0]);
	      int j2 = Integer.parseInt(t[1]);
	      if (j2 < 100) j2 += (j1 / 100) * 100;
	      
	      HashMap<String, String> m = new HashMap<String, String>();
	      m.put("from", j1 + "-01-01");
	      m.put("to", j2 + "-12-31");
	
	      makeAnno(AnnotationType.TIMESPAN, m, cur, cur, null);
	
		}
		
		if (cur.surface.matches("\\d\\d?")	// centuries, dot as separate token
				&& cur.next() != null && ".".equals(cur.next().surface)
				&& cur.next().next() != null && cur.next().next().surface.matches("Jahrhunderts?|Jhd?s?\\.?")) {
				
				int j = (Integer.parseInt(cur.surface) - 1) * 100;
				
		      HashMap<String, String> m = new HashMap<String, String>();
		      m.put("from", j + "-01-01");
		      m.put("to", (j + 99) + "-12-31");
	
		      makeAnno(AnnotationType.TIMESPAN, m, cur, cur.next().next(), null);
	
		}
	
		if (cur.surface.matches("\\d\\d?\\.")	// centuries, dot in number token
			&& cur.next() != null && cur.next().surface.matches("Jahrhunderts?|Jhd?s?\\.?")) {
				
				int j = (Integer.parseInt(cur.surface) - 1) * 100;
				
		      HashMap<String, String> m = new HashMap<String, String>();
		      m.put("from", j + "-01-01");
		      m.put("to", (j + 99) + "-12-31");
	
		      makeAnno(AnnotationType.TIMESPAN, m, cur, cur.next(), null);
	
		}
	
	}
}
