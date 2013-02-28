package lebada.marker;

import java.util.HashMap;
import java.util.Map;

import lebada.fs.CFS;
import lebada.fs.FSUtil;
import nerimd.annotation.AnnotationType;
import nerimd.annotation.DefaultText;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.EToken;
import apus.tok.Token;

public abstract class LegacyMarker extends Marker {

	protected CFS infos = FSUtil.newCFS();
	

	public LegacyMarker(Options options) {
		super(options);

		Options tmpo = this.options.subset("termInfos.");
		for (String k: tmpo.getKeys()) {
			String v = tmpo.get(k);
			infos.set(k, ("true".equals(v) ? true : ("false".equals(v) ? false : v))); 
		}
		
	}

	
	@Override
	protected TermOccurence[] markup0(Token[] tokens) {
		
		StringBuilder surface = new StringBuilder();
		
		int start[] = new int[tokens.length];
		int end[] = new int[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			start[i] = surface.length();
			surface.append(tokens[i].surface);
			end[i] = surface.length();
			surface.append(" ");
		}
		
		DefaultText text = new DefaultText(surface.toString());
		
		for (int i = 0; i < tokens.length; i++) {
			String lemma = null;
			String pos = null;
			if (tokens[i] instanceof EToken) {
				Object o = ((EToken) tokens[i]).info.get("lemmata");
				if (o != null && o instanceof String[] && ((String[]) o).length > 0)
					lemma = ((String[]) o)[0];
				pos = ((EToken) tokens[i]).info.getString("pos");
			}
			Map<String, String> attrs = new HashMap<String, String>(); 
			attrs.put("lemma", (lemma == null || lemma.length() == 0) ? null : lemma);
			attrs.put("POS", (pos == null) ? "" : pos);
			
			text.addAnnotation(AnnotationType.TOKEN, start[i], end[i], attrs, null);
		}
				
		TermOccurence[] tos =  _markup(text, tokens);
		for (int i = 0; i < tos.length; i++) {
			CFS cfs = FSUtil.newCFS(infos);
			for (String key: tos[i].getFeatureNames()) cfs.set(key, tos[i].get(key));
			tos[i] = new TermOccurence(tos[i].start, tos[i].end, cfs);
		}
		
		return tos;
		
	}
	
	
	protected abstract TermOccurence[] _markup(DefaultText text, Token[] tokens);
	
	
	protected int[] char2tokenPos (Token[] tokens, int start, int end) {
	
		int s = -1, e = -1;
		int tl = 0;
		for (int i = 0; i < tokens.length; i++) {
			tl += tokens[i].surface.length();
			if (s < 0 && tl > start) s = i;
			tl++;	// one-sized whitespace!
			if (e < 0 && tl > end) {
				e = i + 1;
				break;
			}
		}
		if (s == -1) return null; // corrupt anno 
		if (e == -1) e = tokens.length;
		
		return new int[]{s,e};
		
	}
		
}
