package apus;

import hmmtagger.api.PosTagger;
import hmmtagger.api.ResultStream;
import hmmtagger.api.TaggedResultWord;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;
import morph.KategorieMerkmal;
import morph.MorphAnalysis;
import morph.MorphModule;
import postprocessor.api.RuleBasedPostProcessor;
import apus.tok.EToken;
import de.fau.cs8.mnscholz.util.options.Options;

public class TaggerMorpherDE extends TaggerMorpher {
	
	final PosTagger tagger;
	final File rulefile;
	MorphModule morph;
	
	String[] delim = ", . ! ? :".split(" ");
	
	
	
	public TaggerMorpherDE (Options options) {
		super(options);
		Options to = options.subset("tagger.");
		Options mo = options.subset("morphology.");
		
		String rf = to.get("ruleFile", null);
		rulefile = (rf == null) ? null : new File(rf);
		File paramFile = new File(to.get("paramFile"));
		int argmaxBSize = Integer.parseInt(to.get("argmaxBSize"));
		String wdir = to.get("workingDir");
		float beamWin = Float.parseFloat(to.get("beamWindow"));
		
		String morphPath = mo.get("dataPath");

		try {
			tagger = new PosTagger(paramFile, argmaxBSize, wdir, beamWin, morphPath);
		} catch (Exception e) {
			throw new RuntimeException("error initializing tagger", e);
		}
		
		
		morph = new MorphModule(morphPath);
		morph.setFlag("*ALLE_WBSEG*");
		
	}
	
	
	
	@Override
	public FS[] parsePOSFullMorph (String[] toks) {
		if (toks == null) throw new NullPointerException();
		if (toks.length == 0) return new FS[0];
		
		FS[] ret = new FS[toks.length];
		TaggedResultWord[] trwa = null;
		try {
			ResultStream rs = tag(toks);
			rs=new RuleBasedPostProcessor(rs,delim,rulefile);
			trwa = rs.toArray();
			
			// morph analyse pro wort
			for (int i = 0; i < trwa.length; i++) {
				FS[] l = getFullMorph(toks[i], trwa[i].getBestStringTag());
				ret[i] = FSUtil.newFS("pos", trwa[i].getBestStringTag(), "morph", l);
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "could not tag " + Arrays.toString(toks), e);
		}
		
		return ret;
		
	}
	
	
	private synchronized FS[] getFullMorph(String word, String pos) {
		
		List<FS> ls = new ArrayList<FS>();
		morph.analyze(word);
		while (morph.moreAnalyses()) {
			MorphAnalysis a = morph.nextAnalysis();
			KategorieMerkmal m = a.getAnalysis();
			String cat = m.category();
			if (isCongruent(pos, cat)) {
				// only add morph analysis if tagger pos and morph cat are mappable
				CFS cfs = FSUtil.newCFS();
				cfs.set("lemma", m.getBaseForm());
				cfs.set("category", cat);
				cfs.set("constInfo", m.getConstInfo());
				cfs.set("flexInfo", m.getFlexInfo());
				cfs.set("ref", m.getRef());
				ls.add(FSUtil.newFS(cfs));
			}
		}	
		
		return ls.toArray(new FS[ls.size()]);
		
	}
	
	
	@Override
	public FS[] parsePOSLemmata (String[] toks) {
		if (toks == null) throw new NullPointerException();
		if (toks.length == 0) return new FS[0];
		
		FS[] ret = new FS[toks.length];
		TaggedResultWord[] trwa = null;
		try {
			ResultStream rs = tag(toks);
			rs=new RuleBasedPostProcessor(rs,delim,rulefile);
			trwa = rs.toArray();
			
			// morph analyse pro wort
			for (int i = 0; i < trwa.length; i++) {
				String[] l = getLemmata(toks[i], trwa[i].getBestStringTag());
				ret[i] = FSUtil.newFS("pos", trwa[i].getBestStringTag(), "lemmata", l);
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "could not tag " + Arrays.toString(toks), e);
		}
		
		return ret;
		
	}

	
	@Override
	public void parsePOSLemmata (EToken[] toks) {
		if (toks == null) throw new NullPointerException();
		if (toks.length == 0) return;
		
		String[] words = new String[toks.length];
		for (int i = 0; i < words.length; i++) words[i] = toks[i].surface;
		try {
			ResultStream rs = tag(words);
			rs=new RuleBasedPostProcessor(rs,delim,rulefile);
			TaggedResultWord tw;
			
			for (int i = 0; (tw = rs.nextWord()) != null; i++) {
				EToken t = toks[i];
				// morph analyse pro wort
				String[] l = getLemmata(words[i], tw.getBestStringTag());
				if (t.info == null) t.info = FSUtil.newCFS();
				t.info.set("pos", tw.getBestStringTag());
				t.info.set("lemmata", l);
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "could not tag " + Arrays.toString(toks), e);
		}
		
	}

	private synchronized ResultStream tag (String[] toks) {
		try {
			return tagger.tag(toks, delim, false);
		} catch (Exception e) {
			log.log(Level.SEVERE, "could not tag " + Arrays.toString(toks), e);
e.printStackTrace();
			return null;
		}
	}
	
	private synchronized String[] getLemmata(String word, String pos) {
		
		List<String> ls = new ArrayList<String>();
		if (pos.equals("ART") && word.startsWith("d")) {
			return new String[]{"d"};
		} else if (pos.equals("ART") && word.startsWith("e")) {
			return new String[]{"ein"};
		} else {
			morph.analyze(word);
//System.err.println("word="+word);
			while (morph.moreAnalyses()) {
				MorphAnalysis a = morph.nextAnalysis();
				KategorieMerkmal m = a.getAnalysis();
				String cat = m.category();
//System.err.println("cat="+cat);
//System.err.println("lem="+m.getBaseForm());
				if (isCongruent(pos, cat)) {
					// only add lemma if tagger pos and morph cat are mappable
					ls.add(m.getBaseForm());
				}
			}	
		}	
		return ls.toArray(new String[ls.size()]);
	}
	
	
	
	
	
	/**Returns true if a STTS pos tag and a Hanrieder morph category
	 * are congruent.
	 * This is a Java-Version of Martin Hackers C morph_disambiguation algorithm.
	 * 
	 * @param pos
	 * @param cat
	 * @return
	 */
	private boolean isCongruent (String pos, String cat) {
		cat = cat.toUpperCase();
		pos = pos.toUpperCase();
		if (cat.equals(pos)) return true;

		boolean mADJ = cat.equals("ADJ") || cat.equals("UNFLADJ");
		boolean mPKONTR = cat.equals("PKONTR");
		boolean mP = cat.equals("P");
		boolean mDET = cat.equals("DET");
		boolean mADV = cat.equals("ADV");
		boolean mINTERJ = cat.equals("INTERJ");
		boolean mKONJ = cat.equals("KONJ");
		boolean mPRON = cat.equals("PRON");
		boolean mKARD = cat.equals("KARDINALZAHL");

		if (pos.startsWith("V") && cat.equals("V")) return true;
		if (pos.startsWith("ADJ") && mADJ) return true;
		if (pos.equals("APPRART") && mPKONTR) return true;
		if (pos.equals("APPR") && mP) return true;
		if (pos.equals("APPO") && mP) return true;
		if (pos.equals("APZR") && mP) return true;
		if (pos.equals("ART") && mDET) return true;
		if (pos.equals("CARD") && mKARD) return true;
		if (pos.equals("ITJ")) {
			if(mINTERJ) return true;
			if (cat.equals("MWTRIGG")) return true;
		}
		if (pos.equals("KOUI") && mP) return true;
		if (pos.equals("KOUS") && mKONJ) return true;
		if (pos.equals("KON") && mKONJ) return true;
		if (pos.equals("KOKOM") && mKONJ) return true;
		if (pos.equals("NN")) {
			if (cat.equals("N")) return true;
			if (cat.equals("ORDINALZAHL")) return true;
		}
		if (pos.equals("NE") && cat.equals("EN")) return true;
		if (pos.equals("PDS") && mPRON) return true;
		if (pos.equals("PDAT") && mDET) return true;
		if (pos.equals("PIS") && mPRON) return true;
		if (pos.equals("PIAT") && mDET) return true;
		if (pos.equals("PIDAT") && mADJ) return true;
		if (pos.startsWith("PP") && mPRON) return true;
		if (pos.startsWith("PR") && mPRON) return true;
		if (pos.equals("PRF") && mADV) return true;
		if (pos.equals("PWS") && mPRON) return true;
		if (pos.equals("PWAT") && mPRON) return true;
		if (pos.equals("PWAV") && cat.equals("INTERROGATIVADVERB")) return true;
		if (pos.equals("PAV") && mADV) return true;
		if (pos.equals("PTKZU") && cat.equals("INFINITIVPARTIKEL")) return true;
		if (pos.equals("PTKNEG") && mADV) return true;
		if (pos.equals("PTKVZ") && cat.equals("TVZ")) return true;
		if (pos.equals("PTKANT") && (mINTERJ || mADV)) return true;

		return false;
	
	}
	
	

}
