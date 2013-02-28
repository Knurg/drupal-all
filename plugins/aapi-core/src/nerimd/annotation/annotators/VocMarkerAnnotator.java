package nerimd.annotation.annotators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import lebada.fs.FSUtil;
import lebada.marker.TermOccurence;
import lebada.marker.VocabularyMarker;
import lebada.voc.Vocabulary;
import lebada.voc.VocabularyFactory;
import nerimd.annotation.AnnotationType;
import nerimd.annotation.TokenAnnotator;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.Tokenizer;

public class VocMarkerAnnotator extends TokenAnnotator {
	
	Options options;
	VocabularyMarker m;

	public VocMarkerAnnotator(Properties p) {
		super(p);

		options = Options.create();
		for (String k : p.stringPropertyNames()) {
			options.set(k, p.getProperty(k));
		}
		options = options
				.subset("nerimd.annotation.annotators.VocMarkerAnnotator.");
		// DEBUG
		System.err.println(Arrays.toString(options.getKeys()));
		// END DEBUG

		List<Vocabulary> v = new ArrayList<Vocabulary>();
		if (options.exists("vocabularies"))
			for (String vn : options.get("vocabularies", "").split("\\s+"))
				if (!vn.isEmpty())
					v.add(VocabularyFactory.create(options
							.subset("vocabularyData." + vn + ".")));

		m = new VocabularyMarker(options.subset("markerData."), v
				.toArray(new Vocabulary[v.size()]));

	}

	@Override
	protected void annotateTokens(Token[] tokens) {

		apus.tok.Token[] tl = new apus.tok.Token[tokens.length];
		for (int i = 0; i < tl.length; i++) {
			tl[i] = new apus.tok.EToken(tokens[i].surface, Tokenizer.BLACK,
					FSUtil.newCFS(
							"lemmata", 
							(tokens[i].lemma == null) ? new String[0] : tokens[i].lemma.split("\\|"),
							"pos",
							tokens[i].pos));
		}

		for (TermOccurence to : m.markup0(tl)) {
			
			HashMap<String, String> a = new HashMap<String, String>();
			a.put("name", to.getString("term"));
			a.put("canonical", to.getString("canonical"));
			a.put("voc", ((Vocabulary) to.get("vocabulary")).getName());
			a.put("termID", to.getString("id"));
			String scheme =  to.getString("scheme");
			if (scheme != null) a.put("SkosScheme", scheme);
			// DEBUG
			System.err.println("///Scheme:"+scheme);
			//END DEBUG
			makeAnno(AnnotationType.valueOf(
					to.getString("type")),
					a, tokens[to.start], tokens[to.end], null);
			
		}
		
	}

}
