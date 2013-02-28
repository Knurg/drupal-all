package nerimd.annotation.annotators.time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nerimd.annotation.Annotation;
import nerimd.annotation.AnnotationFailedException;
import nerimd.annotation.AnnotationType;
import nerimd.annotation.Annotator;
import nerimd.annotation.AnnotatorInitializationException;
import nerimd.annotation.Text;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import prolog.Prolog;
import de.fau.cs8.mnscholz.util.xml.DOMUtil;

/**
 * Uses properties:<p>
 * <table border="1">
 * <tr><th>Name<th>Mandatory<th>Default<th>Description</tr>
 * <tr><td>nerimd.annotation.annotators.time.PrologTimeAnnotator.prologInitFiles<td>yes<td>none<td>The file names which contain the prolog rules or other files that should be passed via the '-s' option.<br>A ";"-separated list. </tr>
 * </table>
 * 
 * @author martini
 *
 */
public class PrologTimeAnnotator extends Annotator {
	
	private static String OPEN_TIMESPAN1 = "1111";
	private static String OPEN_TIMESPAN2 = "2222";
	
	
	private Prolog prolog;
	
	
	public PrologTimeAnnotator(Properties p) {
		this(null, p);
	}
	
	public PrologTimeAnnotator (Prolog prolog, Properties p) {
		super(p);
		
		if (prolog == null) {
			String[] files = this.properties.getProperty(this.getClass().getCanonicalName() + ".prologInitFiles", "").split(";");
			StringBuffer buf = new StringBuffer(p.getProperty("prolog.Prolog.command"));
			if (files.length > 0 && ! files[0].equals(""))
				for (String file: files) buf.append(" -s ").append(file);
			Properties pp = new Properties(this.properties);
			pp.setProperty("prolog.Prolog.command", buf.toString());
			this.prolog = new Prolog(pp);
		} else {
			this.prolog = prolog;
		}
		
		try {
			Thread.sleep(500);
			this.prolog.checkErrorStream();
		} catch (Exception e) { throw new AnnotatorInitializationException(e); }
		
	}
	
	
	public void annotate(Text text) throws AnnotationFailedException {
		
		prolog2annos(_annotate(text), text);
		
	}

	private synchronized String _annotate (Text text) {
    		
		String tokenstr = tokens2prolog(text.getAnnotations(AnnotationType.TOKEN));
		prolog.write("analyseTokens(exprTemp, " + tokenstr + ").");
		String resstr = prolog.read();
		while (resstr == null) {
			try { Thread.sleep(1000); } catch (Exception e) {}
			resstr = prolog.read();
		}

		return resstr;

	}

	
	
	private String tokens2prolog (Annotation[] tokens) {
		if (tokens == null || tokens.length == 0) return "[]";
		
		int i = 0;
		StringBuffer buf = new StringBuffer("[");
		for (Annotation t: tokens) {
			buf.append("token('").append(escape(t.surface())).append("', ");
			buf.append("token_position:'").append(i++).append("'..");	// use position
			buf.append("lemma:'").append((t.attr("lemma") == null) ? "" : escape(t.attr("lemma"))).append("'..tag:'");
			buf.append(escape(t.attr("POS"))).append("'), ");
		}
		buf.setLength(buf.length() - 2);	// delete last ", "
		buf.append("]");
		
		return buf.toString();
	}
	
	
	private String escape (String str) {
		return str.replaceAll("['\\\\]", "\\\\$0");
	}
	
	/*parse the prolog return xml string and convert to annos which are added to text 
	 * 
	 */
	private void prolog2annos (String prostr, Text text) {

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(prostr.getBytes()));
			Element[] elems = DOMUtil.allElements(doc, "//annotations/anno");
			
			if (elems.length == 0) return;
			
			Annotation[] tokens = text.getAnnotations(AnnotationType.TOKEN);
			
			for (Element timenode: elems) {
				
				int start = text.surface().length(), end = 0;
				
				// ** mark tokens as enclosed annotations
				String[] idxs = timenode.getAttribute("tokens").split(" ");
				Set<Annotation> encl = new HashSet<Annotation>(idxs.length);
				for (int i = 0; i < idxs.length; i++) {
					Annotation t = tokens[Integer.parseInt(idxs[i])];
					encl.add(t);
					if (i == 0) start = t.start;
					if (i == idxs.length - 1) end = t.end;
				}
				
				// ** extract the type, from and to attribute values
				Map<String, String> attrs = new HashMap<String, String>(3);
				Element typenode = DOMUtil.firstElement(timenode, "value/temporal/periode|value/temporal/exactdate|value/temporal/relatif");
				
				ByteArrayOutputStream oe = new ByteArrayOutputStream();
				TransformerFactory.newInstance().newTransformer().transform(new DOMSource(timenode) , new StreamResult(oe));			
				
				if (typenode == null) {
					throw new AnnotationFailedException("unknown type of time annotation " + oe.toString());
				} else {
					attrs.put("features", oe.toString());
				}
				
				// type
				AnnotationType type = null;
				
				if (typenode.getNodeName().equals("periode") || typenode.getNodeName().equals("exactdate") || typenode.getNodeName().equals("relatif")) {
					// from
					String day1 = DOMUtil.firstString(typenode, "intpr/begin/day");
					String month1 = DOMUtil.firstString(typenode, "intpr/begin/month");
					String year1 = DOMUtil.firstString(typenode, "intpr/begin/year");
					// to
					String day2 = DOMUtil.firstString(typenode, "intpr/end/day");
					String month2 = DOMUtil.firstString(typenode, "intpr/end/month");
					String year2 = DOMUtil.firstString(typenode, "intpr/end/year");
					
					if (year1.equals(year2) && month1.equals(month2) && day1.equals(day2)) {
						type = AnnotationType.POINT_IN_TIME;
						attrs.put("when", year1 + "-" + ((month1.length() == 1) ? "0" + month1 : month1) + "-" + ((day1.length() == 1) ? "0" + day1 : day1));
					} else {
						type = AnnotationType.TIMESPAN;
						if (! OPEN_TIMESPAN1.equals(year1)) attrs.put("from", year1 + "-" + ((month1.length() == 1) ? "0" + month1 : month1) + "-" + ((day1.length() == 1) ? "0" + day1 : day1));
						if (! OPEN_TIMESPAN2.equals(year2)) attrs.put("to", year2 + "-" + ((month2.length() == 1) ? "0" + month2 : month2) + "-" + ((day2.length() == 1) ? "0" + day2 : day2));
					}
					
				} else {
					
				}
				
				// ** make annotation
				if (type == null) {
					throw new AnnotationFailedException("unknown type of time annotation " + oe.toString());
				}
				text.addAnnotation(type, start, end, attrs, encl);
				
			}
			
		} catch (Exception e) {
			throw new AnnotationFailedException("prolog returned: " + prostr, e);
		}
		
	}
	
}

