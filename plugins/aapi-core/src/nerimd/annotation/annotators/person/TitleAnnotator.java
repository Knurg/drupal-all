package nerimd.annotation.annotators.person;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import nerimd.annotation.AnnotationFailedException;
import nerimd.annotation.AnnotationType;
import nerimd.annotation.AnnotatorInitializationException;
import nerimd.annotation.TokenIterationAnnotator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.fau.cs8.mnscholz.util.xml.DOMUtil;

/**Though it's just used as a trigger for person names, make real annotations. Maybe it may be useful one day...
 * 
 * 
 * 
 * 
 * Uses properties:<p>
 * <table border="1">
 * <tr><th>Name<th>Mandatory<th>Default<th>Description</tr>
 * <tr><td>nerimd.annotation.annotators.person.titleDBFiles<td>no<td>empty list<td>comma-separated list containing the paths to the files containing the lists of social titles</tr>
 * </table> 
 * 
 * @author simnscho
 *
 */
public class TitleAnnotator extends TokenIterationAnnotator {
	
	private Map<String, Map<String, Object>> titles;
	
	public TitleAnnotator (Properties p) throws AnnotatorInitializationException {
		super(p);
		loadDB();
	}
	
	@Override
	protected void annotateTokens(Token cur) throws AnnotationFailedException {
		
		try {
			
			if (titles.containsKey(cur.token))
				makeAnno(AnnotationType.SOCIAL_TITLE, Collections.singletonMap("name", cur.token), cur, cur, null);
			
		} catch (Exception e) {
			throw new AnnotationFailedException(e);
		}
	}
	
	
	private void loadDB () {
		
		titles = new HashMap<String, Map<String,Object>>();
		
		for (String file: properties.getProperty("nerimd.annotation.annotators.person.titleDBFiles", "").split(";")) {
			if (file == null || file.length() == 0) continue;
			loadDBFile(file);
		}
		
	}
	
	
	private void loadDBFile (String file) {
		
		try { 
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(file));
			Element[] list = DOMUtil.allElements(doc, "//title");
			
			for (Element n: list) {
				
				NamedNodeMap atts = n.getAttributes();
				Map<String, Object> props = new HashMap<String, Object>();
				
				for (int j = 0; j < atts.getLength(); j++) {
					Node att = atts.item(j);
					props.put(att.getNodeName(), att.getNodeValue());
				}
				
				if (n.getTextContent() == null || n.getTextContent().length() == 0) continue;
				titles.put(n.getTextContent(), props);
				
			}
			
		} catch (Exception e) {
			throw new AnnotatorInitializationException(e);
		}
		
	}
		
}
