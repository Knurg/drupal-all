package apus.tok;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.fau.cs8.mnscholz.util.options.Options;


public class PatternGlue extends BufferedPostProcessor {
	
	private static final Object WS = new Object() {};
	private static final Object OWS = new Object() {};
	
	Object[][] patterns;
	
	public PatternGlue(Tokenizer tok, Options options) {
		super(tok, options);
		initPatterns();
	}
	
	
	
	PatternGlue(Tokenizer tok, Options options, Object[][] patterns) {
		super(tok, options);
		this.patterns = patterns;
	}



	@Override
	public PostProcessor tokenize(Tokenizer tok) {
		return new PatternGlue(tok, options, patterns);
	}

	@Override
	protected Token next() {
		// TODO eigentlich routine
		return bufferRemove();
	}
	
	
	private void initPatterns () {
		String tmp = options.get("pattern.xmlfile", null);
		List<Object[]> patterns = new ArrayList<Object[]>();
		if (tmp != null) {
			for (String file: tmp.split(";"))
				loadXMLPatterns(file, patterns);
		}
		
		this.patterns = patterns.toArray(new Object[patterns.size()][]);
		
	}
	
	private void loadXMLPatterns (String file, List<Object[]> patterns) {
		
		try {
			Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(file));
			XPath xp = XPathFactory.newInstance().newXPath();
			NodeList l0 = (NodeList) xp.evaluate("/patterns/pattern", d, XPathConstants.NODESET);
			List<Object[]> p0 = new ArrayList<Object[]>();
			List<Object> p1 = new ArrayList<Object>();
			
			for (int i = 0; i < l0.getLength(); i++) {
				Element e0 = (Element) l0.item(i);
				NodeList l1 = (NodeList) xp.evaluate("/patterns/pattern", e0, XPathConstants.NODESET);
				
				for (int j = 0; j < l1.getLength(); j++) {
					Element e1 = (Element) l0.item(i);
					if (e1.getNodeName().equals("token")) p1.add(Pattern.compile(e1.getTextContent()));
					if (e1.getNodeName().equals("ws")) {
						if (e1.getAttribute("optional").equals("true")) p1.add(WS);
						else p1.add(OWS);
					}
				}
				
				if (! p1.isEmpty()) p0.add(p1.toArray(new Object[p1.size()]));
				p1.clear();
				
			}
			
		} catch (Exception e) {}
		
	}
	
}
