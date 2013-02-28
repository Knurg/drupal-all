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
 * <tr><td>nerimd.annotation.annotators.person.relationDBFiles<td>no<td>empty list<td>comma-separated list containing the paths to the files containing the lists of human relations</tr>
 * </table> 
 * 
 * @author simnscho
 *
 */
public class RelationAnnotator extends TokenIterationAnnotator {
	
	private Map<String, Map<String, Object>> relations;
	
	public RelationAnnotator (Properties p) throws AnnotatorInitializationException {
		super(p);
		loadDB();
	}
	
	
	
	@Override
	protected void annotateTokens(Token cur) throws AnnotationFailedException {
		
		try {
			
			if (relations.containsKey(cur.token))
				makeAnno(AnnotationType.HUMAN_RELATION, Collections.singletonMap("name", cur.token), cur, cur, null);
			
		} catch (Exception e) {
			throw new AnnotationFailedException(e);
		}
	}
	
	
	private void loadDB () {
		
		relations = new HashMap<String, Map<String,Object>>();
		
		for (String file: properties.getProperty("nerimd.annotation.annotators.person.relationDBFiles", "").split(";")) {
			if (file == null || file.length() == 0) continue;
			loadDBFile(file);
		}
		
	}
	
	
	private void loadDBFile (String file) {
		
		try { 
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(file));
			Element[] list = DOMUtil.allElements(doc, "//relation");
			
			for (Element n: list) {
				
				NamedNodeMap atts = n.getAttributes();
				Map<String, Object> props = new HashMap<String, Object>();
				
				for (int j = 0; j < atts.getLength(); j++) {
					Node att = atts.item(j);
					props.put(att.getNodeName(), att.getNodeValue());
				}
				
				if (n.getTextContent() == null || n.getTextContent().length() == 0) continue;
				relations.put(n.getTextContent(), props);
				
			}
			
		} catch (Exception e) {
			throw new AnnotatorInitializationException(e);
		}
		
	}
	
}
