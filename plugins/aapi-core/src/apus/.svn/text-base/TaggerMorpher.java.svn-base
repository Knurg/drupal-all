package apus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lebada.fs.FS;
import apus.tok.EToken;
import de.fau.cs8.mnscholz.util.AbstractOptionedClass;
import de.fau.cs8.mnscholz.util.collection.CollectionUtil;
import de.fau.cs8.mnscholz.util.options.Options;

public abstract class TaggerMorpher extends AbstractOptionedClass {

	protected static final Logger log = Logger.getLogger(TaggerMorpher.class.getCanonicalName());

	public abstract void parsePOSLemmata(EToken[] toks);

	public abstract FS[] parsePOSLemmata(String[] toks);

	public abstract FS[] parsePOSFullMorph(String[] toks);

	private static Map<String, TaggerMorpher> standard = null;
	private static Options standardOptions = Options.create(CollectionUtil.fillMap(new HashMap<String, String>(),
				new Object[]{
			"de.tagger.ruleFile", "data/er_tagger/rulefile2.rule",
			"de.tagger.paramFile", "data/er_tagger/paramfile2.prm",
			"de.tagger.argmaxBSize", "100",
			"de.tagger.workingDir", "/tmp/",
			"de.tagger.beamWindow", "50",
			"de.morphology.dataPath", "data/morph/"
		}
		));

	@SuppressWarnings("unchecked")
	public static TaggerMorpher getStandard(String lang) {
		if (standard == null) standard = new HashMap<String, TaggerMorpher>();
		if (!standard.containsKey(lang.toLowerCase())) {
			try {
				Options o = standardOptions.subset(lang.toLowerCase() + ".");
				Class<? extends TaggerMorpher> cls = (Class<? extends TaggerMorpher>) Class.forName(TaggerMorpher.class.getCanonicalName() + lang.toUpperCase());
				standard.put(lang.toLowerCase(), cls.getConstructor(Options.class).newInstance(o));
			} catch (Exception e) {
				log.log(Level.WARNING, "could not find TaggerMorpher for language " + lang, e);
				return null;
			}
		}
		return standard.get(lang.toLowerCase());
	}

	public TaggerMorpher(Options options) {
		super(options);
	}

	public static void main (String[] args) throws Exception {
		
		TaggerMorpher tmw = TaggerMorpher.getStandard(args[0]);
		
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while ((line = r.readLine()) != null) {
			
			String[] tokens = line.split("\\s+");
			
			FS[] infos = tmw.parsePOSFullMorph(tokens);
			
			for (int i = 0; i < tokens.length; i++) {
				System.out.print(tokens[i] + "\t");
				System.out.print(infos[i].getString("pos") + "\t");
				for (FS fs: (FS[]) infos[i].get("morph")) {
					System.out.print(fs + "\t");
				}
				System.out.println();
				System.out.println("------");
			}
			System.out.println("======");
			
		}
		
	}

}