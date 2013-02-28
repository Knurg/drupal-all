package eu.wiss_ki.aapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import lebada.fs.CFS;
import lebada.fs.FSUtil;
import lebada.marker.LegacyMarker;
import lebada.marker.TermOccurence;
import nerimd.annotation.Annotation;
import nerimd.annotation.AnnotationType;
import nerimd.annotation.DefaultText;
import nerimd.annotation.annotators.time.MidasDateAnnotator;
import nerimd.annotation.annotators.time.PrologTimeAnnotator;
import prolog.Prolog;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.EToken;
import apus.tok.Token;
import apus.tok.Tokenizer;

public class LegacyTimeMarker extends LegacyMarker {
	
	protected Prolog prolog;
	protected HashMap<Prolog, Integer> prologCnt = new HashMap<Prolog, Integer>();
	protected int prologCountDownOffset = 5;
	protected int prologCountDown = prologCountDownOffset;
	
	protected Properties prologProps = new Properties();
	protected Properties prologTimeProps = new Properties();
	protected Properties midasTimeProps = new Properties();
	
	public LegacyTimeMarker(Options options) {
		super(options);
		
		//defaults
		prologProps.setProperty("prolog.Prolog.command", "pl -q -tty -s scripts/time/regles2.pro");
		prologProps.setProperty("prolog.Prolog.inputCharset", "utf-8");
		prologProps.setProperty("prolog.Prolog.outputCharset", "utf-8");
		midasTimeProps.setProperty("nerimd.annotation.annotators.TokenAnnotator.posSet", "nerimd.annotation.annotators.posSets.STTSPOSSet");
		
		
		Options tmpo = this.options.subset("engine.");
		for (String k: tmpo.getKeys()) prologProps.setProperty(k, tmpo.get(k)); 
		
		tmpo = this.options.subset("prolog.");
		for (String k: tmpo.getKeys()) prologTimeProps.setProperty(k, tmpo.get(k)); 
		
		tmpo = this.options.subset("midas.");
		for (String k: tmpo.getKeys()) midasTimeProps.setProperty(k, tmpo.get(k)); 
		
	}
	
	
	
	
	
	@Override
	protected TermOccurence[] markup0(Token[] tokens) {

		// must be overridden to split dots from abbrevs and numbers.
		// the prolog rules expect the dot as separate token
		
		Map<Integer, Integer> posmap = new HashMap<Integer, Integer>(); 
		List<Token> splittok = new ArrayList<Token>();
		for (int i = 0, j = 0; i < tokens.length; i++, j++) {
			if (tokens[i].surface.endsWith(".") && tokens[i].surface.length() > 1) {
				splittok.add(new EToken(tokens[i].surface.substring(0, tokens[i].surface.length() - 1), Tokenizer.BLACK, (tokens[i] instanceof EToken) ? ((EToken) tokens[i]).info : FSUtil.newCFS()));
				splittok.add(new EToken(tokens[i].surface.substring(tokens[i].surface.length() - 1), Tokenizer.BLACK, FSUtil.newCFS()));
				posmap.put(j, i);
				posmap.put(++j, i);
			} else {
				posmap.put(j, i);
				splittok.add(tokens[i]);
			}
		}
		tokens = splittok.toArray(new Token[splittok.size()]);
		
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
			tos[i] = new TermOccurence(posmap.get(tos[i].start), posmap.get(tos[i].end), cfs);
		}
		
		return tos;
	}





	@Override
	protected TermOccurence[] _markup(DefaultText text, Token[] tokens) {
		
		Prolog p = acquireProlog();
		PrologTimeAnnotator pta = new PrologTimeAnnotator(p, prologTimeProps);
		MidasDateAnnotator mda = new MidasDateAnnotator(midasTimeProps);
		
		pta.annotate(text);
		mda.annotate(text);
		
		releaseProlog(p);
		
		List<TermOccurence> termOccs = new ArrayList<TermOccurence>();
		
		for (Annotation a: text.getAnnotations(AnnotationType.POINT_IN_TIME, AnnotationType.TIMESPAN)) {
			
			int[] dim = char2tokenPos(tokens, a.start, a.end);
			if (dim == null) continue;
			
			String term = a.surface();
			String tstart;
			String tend;
			String canonical;
			if (a.type == AnnotationType.POINT_IN_TIME) {
				tstart = a.attr("when");
				tend = tstart;
				canonical = tstart;
			} else {
				tstart = a.attr("from");
				tend = a.attr("to");
				if (tstart == null) tstart = "?";
				if (tend == null) tend = "?";
				canonical = tstart + " - " + tend;
			}
			
			TermOccurence to = new TermOccurence(dim[0], dim[1], FSUtil.newCFS(
					"xsdbegin", tstart,
					"xsdend", tend,
					"term", term,
					"canonical", canonical
			));
			
			termOccs.add(to);
			
		}
		
		return termOccs.toArray(new TermOccurence[termOccs.size()]);
		
	}
	
	private synchronized Prolog acquireProlog () {
		
//		if (prolog == null || prologCountDown <= 0) {
//			prolog = new Prolog(prologProps);
//			prologCnt.put(prolog, 1);
//			prologCountDown = prologCountDownOffset;
//		} else {
//			prologCnt.put(prolog, prologCnt.get(prolog) + 1);
//		}
//		prologCountDown--;
//		return prolog;
		
		return new Prolog(prologProps);
		
	}
	
	private synchronized void releaseProlog (Prolog prolog) {
		
//		int c = prologCnt.get(prolog);
//		if (prolog != this.prolog && c < 2) {
//			// alte und nicht mehr benutzte Prolog-Prozesse aufräumen
//			prologCnt.remove(prolog);
//			prolog.close();
//		} else {
//			prologCnt.put(prolog, prologCnt.get(prolog) - 1);
//		}
		
		prolog.close();
		
	}
	

}
