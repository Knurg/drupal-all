package taggerMorpher;

import hmmtagger.api.PosTagger;
import hmmtagger.api.ResultStream;
import hmmtagger.api.TaggedResultWord;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import postprocessor.api.RuleBasedPostProcessor;

import morph.FlexMerkmal;
import morph.KategorieMerkmal;
import morph.MorphAnalysis;
import morph.MorphModule;
import morph.AdjGradKategorieMerkmal;
import morph.UnflAdjGradKategorieMerkmal;

import de.fau.cs.jill.feature.FeatureName;
import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureStructure;
import de.fau.cs.jill.feature.StringValue;
import de.fau.cs.jill.feature.UnpackedFeatureStructure;

public class TaggerMorpher {
	
	static final Logger log = Logger.getLogger(TaggerMorpher.class.getCanonicalName());
	
	private static TaggerMorpher standard = null;
	
	public static TaggerMorpher getStandard () {
		if (standard == null) {
			Properties p = new Properties();
			p.put("tagger.ruleFile", "rulefile2.rule");
			p.put("tagger.paramFile", "paramfile2.prm");
			p.put("tagger.argmaxBSize", "100");
			p.put("tagger.workingDir", "/tmp/");
			p.put("tagger.beamWindow", "50");
			p.put("morph.dataPath", "");
			standard = new TaggerMorpher(p);
		}
		return standard;
	}
	
	
	private final PosTagger tagger;
	private final File rulefile;
	private final MorphModule morph;
	private final String[] delim = ", . ! ? :".split(" ");
	
	
	
	public TaggerMorpher (Properties p) {
		
		String rf = p.getProperty("tagger.ruleFile", null);
		rulefile = (rf == null) ? null : new File(rf);
		File paramFile = new File(p.getProperty("tagger.paramFile"));
		int argmaxBSize = Integer.parseInt(p.getProperty("tagger.argmaxBSize"));
		String wdir = p.getProperty("tagger.workingDir");
		float beamWin = Float.parseFloat(p.getProperty("tagger.beamWindow"));
		
		String morphPath = p.getProperty("morph.dataPath");

		try {
			tagger = new PosTagger(paramFile, argmaxBSize, wdir, beamWin, morphPath);
		} catch (Exception e) {
			throw new RuntimeException("error initializing tagger", e);
		}
		
		
		morph = new MorphModule(morphPath);
		if (p.containsKey("morph.flag.*ALLE_WBSEG*"))
			if ("true".equalsIgnoreCase(p.getProperty("morph.flag.*ALLE_WBSEG*", "false")))
				morph.setFlag("*ALLE_WBSEG*");
			else morph.clearFlag("*ALLE_WBSEG*");
		
	}
	
	
	
	public FeatureStructure[] parsePOSFullMorph (String[] toks) {
		if (toks == null) throw new NullPointerException();
		if (toks.length == 0) return new FeatureStructure[0];
		
		FeatureStructure[] ret = new FeatureStructure[toks.length];
		TaggedResultWord[] trwa = null;
		try {
			ResultStream rs = tag(toks);
			rs=new RuleBasedPostProcessor(rs,delim,rulefile);
			trwa = rs.toArray();
			
			// morph analyse pro wort
			for (int i = 0; i < trwa.length; i++) {
				FeatureStructure[] l = getFullMorph(toks[i], trwa[i].getBestStringTag());
				UnpackedFeatureStructure ufs = new UnpackedFeatureStructure();
				ufs.add(FeatureName.forName("pos"), new StringValue(trwa[i].getBestStringTag()));
				ufs.add(FeatureName.forName("morph"), new FeatureSequence(l));
				ret[i] = ufs.pack();
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "could not tag " + Arrays.toString(toks), e);
		}
		
		return ret;
		
	}
	
	
	private synchronized FeatureStructure[] getFullMorph(String word, String pos) {
		
		List<FeatureStructure> ls = new ArrayList<FeatureStructure>();
		morph.analyze(word);
		while (morph.moreAnalyses()) {
			MorphAnalysis a = morph.nextAnalysis();
			KategorieMerkmal m = a.getAnalysis();
			String cat = m.category();
			FlexMerkmal fm = m.getFlexInfo();
			if (isCongruent(pos, cat)) {	// only add morph analysis if tagger pos and morph cat are mappable
				UnpackedFeatureStructure ufs = new UnpackedFeatureStructure();
				ufs.add(FeatureName.forName("lemma"), m.getBaseForm() == null ? null : new StringValue(m.getBaseForm()));
				ufs.add(FeatureName.forName("category"), new StringValue(cat));
				if (m instanceof AdjGradKategorieMerkmal) ufs.add(FeatureName.forName("steigerungsform"), ((AdjGradKategorieMerkmal) m).typ);
				if (m instanceof UnflAdjGradKategorieMerkmal) ufs.add(FeatureName.forName("steigerungsform"), ((UnflAdjGradKategorieMerkmal) m).grad);
					 
				ufs.add(FeatureName.forName("flexInfoClass"), fm == null ? null : new StringValue(fm.getClass().getName()));
				ufs.add(FeatureName.forName("flexInfoString"), fm == null ? null : new StringValue(fm.toString()));
				ufs.add(FeatureName.forName("flexInfo"), (fm == null) ? new FeatureSequence() : fm.asFeatureValue(morph.flexivLex));
//				ufs.add(FeatureName.forName("flexInfo"),
//										(fm == null || fm.getFeatures() == null || fm.getFeatures().isEmpty())
//						? new FeatureSequence()
//						: new FeatureSequence(m.getFlexInfo().getFeatures()));
				if (m.getConstInfo() != null) ufs.add(FeatureName.forName("constInfo"), new StringValue(m.getConstInfo().toString()));

				ls.add(ufs.pack());
			}
		}	
		
		return ls.toArray(new FeatureStructure[ls.size()]);
		
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
