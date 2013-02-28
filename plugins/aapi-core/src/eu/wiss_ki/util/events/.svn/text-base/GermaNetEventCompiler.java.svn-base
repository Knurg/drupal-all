package eu.wiss_ki.util.events;

import germanet.ConRel;
import germanet.GermaNet;
import germanet.LexUnit;
import germanet.Synset;
import germanet.WordCategory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.xml.stream.XMLStreamException;

public class GermaNetEventCompiler extends EventCompiler {
	
	public static void main (String[] args) throws Exception {
		
		GermaNetEventCompiler ec = new GermaNetEventCompiler(args[0]);
		
		InputStream in = System.in;
		OutputStream out = System.out;
		if (args.length > 1) in = new FileInputStream(args[1]);
		if (args.length > 2) out = new FileOutputStream(args[2]);
		
		ec.compile(in, out);
		
	}
	
	
	private GermaNet dic;
	
	
	
	public GermaNetEventCompiler(String pathToGermaNet) throws FileNotFoundException, XMLStreamException {
		super();
		dic = new GermaNet(pathToGermaNet);
	}
	
	
	
	
	@Override
	protected String[][][] resolveSynset(String pos, String lexeme, int sense) {
		
		WordCategory wc = pos.equals("a") ? WordCategory.adj : (pos.equals("n") ? WordCategory.nomen : (pos.equals("v") ? WordCategory.verben : null)); 
		
		Synset synset = null;
		for (LexUnit lu: dic.getLexUnits(lexeme, wc)) {
			if (lu.getSense() == sense) {
				synset = lu.getSynset();
				break;
			}
		}
		
		log.info("synset for lexeme " + lexeme + "(" + sense + ") in " + pos + ": " + synset);
		
		if (synset == null) return null;
			
		LinkedList<Synset> agenda = new LinkedList<Synset>(Collections.singletonList(synset));
		ArrayList<String[][]> res = new ArrayList<String[][]>();
		
		while (!agenda.isEmpty()) {
			
			Synset s = agenda.poll();
			
			for (String l: s.getAllOrthForms()) {
				String[] ls = l.split("\\s+");
				if (ls.length == 1) res.add(new String[][]{ls, new String[]{pos}});
				else res.add(new String[][]{ls, new String[ls.length]});
			}
			
			agenda.addAll(s.getRelatedSynsets(ConRel.hyponymy));
			
		}
		
		return res.toArray(new String[res.size()][][]);
		
	}
	
	
	
}
