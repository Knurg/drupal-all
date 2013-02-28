package eu.wiss_ki.util.events;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.fau.cs8.mnscholz.util.StringUtil;

public abstract class EventCompiler {
	
	
	protected Logger log = Logger.getAnonymousLogger();
	
	protected Set<String> written = new HashSet<String>();
	
	protected EventCompiler() {}
	
	public void compile (InputStream in, OutputStream out) throws SAXException, IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
		
		Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
		NodeList events = d.getElementsByTagName("event");
		
		int ca = 0;
		
		for (int h = 0; h < events.getLength(); h++) {
			
			written.clear();
			
			Element event = (Element) events.item(h);
			NodeList nl = event.getElementsByTagName("synset");
			
			while (nl.getLength() > 0) {
				
				Element sn = (Element) nl.item(0);
				event.removeChild(sn);
				
				String pos = sn.getAttribute("pos");
				int sense = Integer.parseInt(sn.getAttribute("wordsense"));
				String lexeme = sn.getTextContent();
				
				String[][][] res = resolveSynset(pos, lexeme, sense); 
				if (res == null) res = new String[][][]{new String[][]{new String[]{lexeme}, new String[]{pos}}}; 
				
				int c = 0;
				for (String[][] phrase: res) {
					if (append(event, phrase[0], phrase[1])) c++;
				}
				
				ca += c;
				log.info("words/phrases for synset: " + c);
				
			}
			
		}

		log.info("words/phrases total: " + ca);
		
		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(d), new StreamResult(out));
		
	}
	
	
	private boolean append(Element en, String[] lexemes, String[] poss) {
		
		String combined = StringUtil.join(" ", lexemes);
		if (written.contains(combined)) return false;
		written.add(combined);
		
		if (lexemes.length == 1) {
			
			Element wn = en.getOwnerDocument().createElement("word");
			if (poss[0] != null) wn.setAttribute("pos", poss[0]);
			wn.setTextContent(lexemes[0]);
			en.appendChild(wn);
		
		} else {
			
			Element pn = en.getOwnerDocument().createElement("phrase");
			en.appendChild(pn);
			
			for (int i = 0; i < lexemes.length; i++) {
				
				Element wn = en.getOwnerDocument().createElement("word");
				if (poss[i] != null) wn.setAttribute("pos", poss[i]);
				wn.setTextContent(lexemes[i]);
				pn.appendChild(wn);
				
			}
			
		}
		
		return true;
		
	}
	
	/**
	 * 
	 * @param pos
	 * @param lexeme
	 * @param sense
	 * @return	array of phrases containing an array of two fields: [0] an array of words and [1] an array of same length of corresponding POS tags 
	 */
	protected abstract String[][][] resolveSynset (String pos, String lexeme, int sense);
	
}
