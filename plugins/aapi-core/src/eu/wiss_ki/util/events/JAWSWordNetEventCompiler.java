package eu.wiss_ki.util.events;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;

public class JAWSWordNetEventCompiler extends EventCompiler {
	
	public static void main (String[] args) throws Exception {
		
		System.setProperty("wordnet.database.dir", args[0]);
		
		InputStream in = System.in;
		OutputStream out = System.out;
		if (args.length > 1) in = new FileInputStream(args[1]);
		if (args.length > 2) out = new FileOutputStream(args[2]);
		
		EventCompiler jaws = new JAWSWordNetEventCompiler();
		jaws.compile(in, out);
		
	}
	
	
	private WordNetDatabase dic;
	
	public JAWSWordNetEventCompiler() {
		super();
		dic = WordNetDatabase.getFileInstance(); 
	}


	private static List<Synset> getHyponyms (Synset s) {
		if (s instanceof NounSynset) return Arrays.asList((Synset[]) ((NounSynset) s).getHyponyms());
		if (s instanceof VerbSynset) return Arrays.asList((Synset[]) ((VerbSynset) s).getTroponyms());
		return Collections.emptyList();
	}

	@Override
	protected String[][][] resolveSynset(String pos, String lexeme, int sense) {
		
		SynsetType wnpos = pos.equals("a") ? SynsetType.ADJECTIVE : (pos.equals("n") ? SynsetType.NOUN : (pos.equals("v") ? SynsetType.VERB : null));
		Synset[] synsets = dic.getSynsets(lexeme, wnpos);
		Synset synset = (synsets == null || synsets.length <= sense) ? null : synsets[sense];
		
		log.info("synset for lexeme " + lexeme + "(" + sense + ") in " + pos + ": " + synset);
		
		if (synset == null) return null;
			
		LinkedList<Synset> agenda = new LinkedList<Synset>(Collections.singletonList(synset));
		ArrayList<String[][]> res = new ArrayList<String[][]>();
		
		while (!agenda.isEmpty()) {
			
			Synset s = agenda.poll();
			
			for (String l: s.getWordForms()) {
				String[] ls = l.split("\\s+");
				if (ls.length == 1) res.add(new String[][]{ls, new String[ls.length]});
				else res.add(new String[][]{ls, new String[ls.length]});
			}
			
			agenda.addAll(getHyponyms(s));
			
		}
		
		return res.toArray(new String[res.size()][][]);
	}
	
}
