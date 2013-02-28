package nerimd.annotation.annotators.person;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;

import nerimd.annotation.Annotation;
import nerimd.annotation.AnnotationFailedException;
import nerimd.annotation.AnnotationType;
import nerimd.annotation.AnnotatorInitializationException;
import nerimd.annotation.TokenIterationAnnotator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.fau.cs8.mnscholz.util.xml.DOMUtil;

/**
 * Uses properties:<p>
 * <table border="1">
 * <tr><th>Name<th>Mandatory<th>Default<th>Description</tr>
 * <tr><td>nerimd.annotation.annotators.person.nameDBFiles<td>no<td>empty list<td>comma-separated list containing the paths to the files containing the lists of names</tr>
 * </table> 
 * 
 * @author simnscho
 *
 */
public class NamePartAnnotator extends TokenIterationAnnotator {
	
	private class InverseArrayLengthComp implements Comparator<Object[]> {
		public int compare(Object[] a, Object[] b) {
			if (a == null && b == null) return 0;
			if (a == null) return 1;
			if (b == null) return -1;
			return b.length - a.length;
		}
		
	}
	
	private Map<String, Map<String, Object>> givennames;
	private Map<String, Map<String, Object>> surnames;
	private Map<String, Map<String, Object>> copulae;
	private Map<String, Set<String[]>> copulaeBegin;
	
	
	public NamePartAnnotator (Properties p) throws AnnotatorInitializationException {
		super(p);
		loadDB();
	}
	
	
	@Override
	protected void annotateTokens(Token cur) {
		
		try {
			
			// exclude surface patterns with digits or without letters  
			if (cur.surface.matches(".*\\d.*")) return;
			if (cur.surface.matches("[^\\p{javaLowerCase}\\p{javaUpperCase}]+")) return;
			
			// match givennames and surnames
			// combined names like Hans-Jürgen, Müller-Schmitt will
			// be annotated as a name if a single constituent is recognized as such
			boolean g = false, s = false;
			if (cur.lemma != null) {
				String[] sa = cur.lemma.split("-");
				if (sa.length < 3) {	// more than one dash not usual in person names
					for (String p: sa) {
						if (givennames.containsKey(p)) g = true;
						if (surnames.containsKey(p)) s = true;
					}
				}
				if (g == true) makeGNAnno(cur);
				if (s == true) makeSNAnno(cur);
			}
			if (! g && ! s) {
				String[] sa = cur.surface.split("-");
				if (sa.length < 3) {
					for (String p: sa) {
						if (givennames.containsKey(p)) g = true;
						if (surnames.containsKey(p)) s = true;
					}
				}
				if (g == true) makeGNAnno(cur);
				if (s == true) makeSNAnno(cur);
			}
			// also try this:
			// maybe a gn/sn with genitive 's';
			// this is mainly to identify genitives of unknowns
			if (! g && ! s && cur.token.endsWith("s")) {
				for (String p: cur.token.substring(0, cur.token.length() - 1).split("-")) {
					if (givennames.containsKey(p)) g = true;
					if (surnames.containsKey(p)) s = true;
				}
				if (g == true) makeGNAnno(cur);
				if (s == true) makeSNAnno(cur);
			}
			
			// match copulae (can be several tokens)
			if (copulaeBegin.containsKey(cur.token)) {
				String[][] copulae = copulaeBegin.get(cur.token).toArray(new String[0][]);
				
				Token tmp = cur;
				int i = 0;
				int remaining = copulae.length;
				
				do {
					for (int j = 0; j < copulae.length; j++) {
						String[] co = copulae[j];
						if (co == null) continue;
						if (! tmp.surface.equals(co[i])) {
							copulae[j] = null; 
							remaining--;
						} else if (co.length == i + 1) {	// all tokens matched => we've found a copula
							makeCPAnno(cur, tmp);
							copulae[j] = null;
							remaining--;
						}
					}
					i++;
				} while ((tmp = tmp.next()) != null && remaining > 0);
				
			}
			
			// generation part like Charles II, Schmidt jun., oder Gustav d. Ältere
			// match trailing name number (only 1st token)
			if (cur.token.matches("[XIV]+\\.?") || cur.token.matches("(jun|sen)(\\.|ior)|jr.")) {
				makeGenNAnno(cur, cur);
			} else if (cur.isDetArticle()) {
				Token n = cur.next();
				if (n != null && ((n.lemma == null) ? n.surface : n.lemma).matches("[Jj]üngere.?|[äÄ]ltere.?"))
					makeGenNAnno(cur, n);
			}
			
		} catch (Exception e) {
			throw new AnnotationFailedException(e);
		}
		
	}

	private Annotation makeGNAnno (Token token) {
		return makeAnno(AnnotationType.GIVEN_NAME, Collections.singletonMap("name", token.token), token, token, null);
	}
	
	private Annotation makeSNAnno (Token token) {
		return makeAnno(AnnotationType.SURNAME, Collections.singletonMap("name", token.token), token, token, null);
	}
	
	private Annotation makeCPAnno (Token firsttoken, Token lasttoken) {
		Token tmp = firsttoken;
		StringBuffer buf = new StringBuffer(tmp.token);
		while (tmp != lasttoken) { tmp = tmp.next(); buf.append(" ").append(tmp.token); }
		return makeAnno(AnnotationType.NAME_LINK, Collections.singletonMap("name", buf.toString()), firsttoken, lasttoken, null);
	}
	
	private Annotation makeGenNAnno (Token firsttoken, Token lasttoken) {
		Token tmp = firsttoken;
		StringBuffer buf = new StringBuffer(tmp.token);
		while (tmp != lasttoken) { tmp = tmp.next(); buf.append(" ").append(tmp.token); }
		return makeAnno(AnnotationType.GEN_NAME, Collections.singletonMap("name", buf.toString()), firsttoken, lasttoken, null);
	}
	
	
	
	private void loadDB () {
		
		givennames = new HashMap<String, Map<String,Object>>();
		surnames = new HashMap<String, Map<String,Object>>();
		copulae = new HashMap<String, Map<String,Object>>();
		copulaeBegin = new HashMap<String, Set<String[]>>();
		
		for (String file: properties.getProperty("nerimd.annotation.annotators.person.nameDBFiles", "").split(";")) {
			if (file == null || file.length() == 0) continue;
			loadDBFile(file);
		}
		
	}
	
	
	private void loadDBFile (String file) {
		
		try { 
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(file));
			Element[] list = DOMUtil.allElements(doc, "//name");
			
			for (Element n: list) {
				
				String type = null;
				Map<String, Object> attrs = new HashMap<String, Object>();
				
				NamedNodeMap atts = n.getAttributes();
				for (int j = 0; j < atts.getLength(); j++) {
					Node att = atts.item(j);
					if (att.getNodeName().equals("type"))
						type = att.getNodeValue();
					else
						attrs.put(att.getNodeName(), att.getNodeValue());
				}
				
				if (type == null) continue;
				if (n.getTextContent() == null || n.getTextContent().length() == 0) continue;
				if (type.equals("givenname")) {
					givennames.put(n.getTextContent(), attrs);
				} else if (type.equals("surname")) {
					surnames.put(n.getTextContent(), attrs);
				} else if (type.equals("copula")) {
					String[] ws = n.getTextContent().split("\\s");
					if (! copulaeBegin.containsKey(ws[0]))
						copulaeBegin.put(ws[0], new TreeSet<String[]>(new InverseArrayLengthComp()));
					copulaeBegin.get(ws[0]).add(ws);
					copulae.put(n.getTextContent().replaceAll("\\s", " "), attrs);
				} else {
					continue;
				}
				
			}
		
		} catch (Exception e) {
			throw new AnnotatorInitializationException(e);
		}
		
	}
	
}
