package nerimd.annotation.annotators.person;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import nerimd.annotation.Annotation;
import nerimd.annotation.AnnotationType;
import nerimd.annotation.AnnotatorInitializationException;
import nerimd.annotation.TokenAnnotator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import de.fau.cs8.mnscholz.util.ArraysUtil;
import de.fau.cs8.mnscholz.util.xml.DOMUtil;

/**
 * {@code PersonAnnotator} supports following name patterns:
 * <br>
 * (sn: surname, gn: givenname, gp: generation particle (like IV) , nc: copula (like van))
 * <br>
 * <pre>
 * sn
 * sn gp
 * gn+
 * gn+ sn
 * gn+ gp
 * gn+ gp sn
 * gn+ nc sn
 * gn+ gp nc sn
 * sn, gn+
 * sn, gn+ nc
 * sn, gn+ gp 
 * sn, gn+ gp nc
 * </pre>
 * <p>
 * Uses properties:<p>
 * <table border="1">
 * <tr><th>Name<th>Mandatory<th>Default<th>Description</tr>
 * <tr><td>nerimd.annotation.annotators.person.PersonAnnotator.skipTokensWithAnnotations<td>no<td>empty list<td>Names of the annotations that shall not be overridden/included in a PERSON annotation and thus the corresponding tokens shall be skipped.<br>A whitespace-seprarated list</tr>
 * <tr><td>nerimd.annotation.annotators.person.keywordDBFiles<td>no<td>empty list<td>Names of the files that contain positive and/or negative key word patterns.<br>A ";"-separated list</tr>
 * <tr><td>nerimd.annotation.annotators.person.PersonAnnotator.namePatternFiles<td>no<td>empty list<td>Names of the files that contain the name patterns.<br>A ";"-separated list</tr>
 * <tr><td>nerimd.annotation.annotators.person.PersonAnnotator.attributeNameMappingFiles<td>no<td>empty list<td>Names of the files that contain the atribute name mappings from the "child" annotations' attributes to that of the person annotation.<br>A ";"-separated list</tr>
 * </table>
 *
 * @author martini
 *
 */
public class PersonAnnotator extends TokenAnnotator {
	
	private static int GammaBZFactor = 1;
	private Map<Pattern, Integer> negatives;
	private Map<Pattern, Integer> positives;
	private NamePattern[] namepatterns;
	private Map<AnnotationType, Map<String, String>> attrnamemap;
	
	
	public PersonAnnotator(Properties p) {
		super(p);
		loadDB();
	}
	
	private class NamePatternResult {
		
		public final NamePattern producer;
		public final Token firsttoken, lasttoken;
		public final Map<String, String> attrs;
		public final Set<Annotation> enclosing;

		public NamePatternResult(final NamePattern producer, final Token firsttoken, final Token lasttoken, final Map<String, String> attrs, final Set<Annotation> enclosing) {
			super();
			this.producer = producer;
			this.firsttoken = firsttoken;
			this.lasttoken = lasttoken;
			this.attrs = attrs;
			this.enclosing = enclosing;
		}
		
	}
	
	private class NamePattern {
		
		/**this represents the "automaton", the succession of annotations that must hold to identify it as a name
		 * index 0 is the annotation type (one of AnnotationType) or the string "," for a comma
		 * index 1 is a boolean that states if the part can be applied several times (1-inf)
		 * 
		 */
		ArrayList<Object[]> pattern;
		
		public NamePattern(ArrayList<Object[]> pattern) {
			super();
			this.pattern = pattern;
		}
		
		public String toString () {
			StringBuffer buf = new StringBuffer();
			for (Object[] o: pattern) buf.append(o[0]).append((((Boolean) o[1]) == true) ? "+ " : " ");
			return buf.substring(0, buf.length() - 1);
		}
		
		
		@SuppressWarnings("unchecked")
		public NamePatternResult applyTo (Token t, int likeliness) {
			
			HashMap<String, String> attrs = new HashMap<String, String>();
			HashSet<Annotation> encls = new HashSet<Annotation>();
			Token ft = t;
			// a backtrackpoint comprises 0: current token t, 1: pattern index i, 2: attribute map attrs, 3: likeliness, 4: enclosed annos set encls
			ArrayList<Object[]> backtrackpoints = new ArrayList<Object[]>();
			
loop_pattern:
			for (int i = 0; i < pattern.size(); i++) {
				Object[] o = pattern.get(i);
				if (t == null) return null;
				
				if (o[0].toString().equals(",")) {
					
					if (! t.token.equals(",")) return null;
					t = t.next();
					
				} else {
					
					for (int j = 0; t != null; j++) {
						
						// if we can jump from here to the next pattern part,
						// we mark this position as a backtrackpoint
						// we can only jump if we passed this part at least once
						if (j > 0 && pattern.size() > i+1) {
							backtrackpoints.add(0, new Object[]{t, i, attrs.clone(), likeliness, encls.clone()});
						}
						
						Token newt = consumeAnnotation(t, (AnnotationType) o[0], attrs, (attrnamemap.get(o[0]) == null) ? new HashMap<String, String>() : attrnamemap.get(o[0]), encls);
						
						if (newt == t) {
							
							// if this is likely to be a name, use a "joker"
							// likeliness is the number of jokers we have
							// if we play a joker, a noun with unknown lemma or a named entity can be used
							// as a givenname or surname even though it has no such annotation
							if (2 * likeliness > i + j &&
								(o[0] == AnnotationType.GIVEN_NAME || o[0] == AnnotationType.SURNAME) &&
								(t.isNamedEntity() || t.isNoun() && t.lemma == null) &&
								t.getSurroundingAnnotations(AnnotationType.GEN_NAME, AnnotationType.NAME_LINK).length == 0
								) {
								
								likeliness--;
								if (o[0] == AnnotationType.GIVEN_NAME) {
									Annotation a = makeAnno(AnnotationType.GIVEN_NAME, Collections.singletonMap("name", t.token), t, t, null);
									attrs.put("givennames", (attrs.get("givennames") == null) ? t.token : attrs.get("givennames") + " " + t.token);
									encls.add(a);
								}
								else if (o[0] == AnnotationType.SURNAME) {
									Annotation a = makeAnno(AnnotationType.SURNAME, Collections.singletonMap("name", t.token), t, t, null);
									attrs.put("surnames", (attrs.get("surnames") == null) ? t.token : attrs.get("surnames") + " " + t.token);
									encls.add(a);
								}
								newt = t.next();
								
							} else { 
								
								if (j > 0 && pattern.size() == i + 1) {
									// if last pattern part that matched once yet, this is a legal end of name
									t = t.prev();	// shift back, as this token does not belong to the name
									break loop_pattern;
								} else if (backtrackpoints.size() > 0) {
									// otherwise backtrack
//System.err.println("inner backtrack from: " + ((t == null) ? null : t.surface) +","+i+","+attrs+","+likeliness);
									Object[] b = backtrackpoints.remove(0);
									t = (Token) b[0];
									i = (Integer) b[1];
									attrs = (HashMap<String, String>) b[2];
									likeliness = (Integer) b[3];
									encls = (HashSet<Annotation>) b[4];
//System.err.println("innnr backtrack to: " + t.surface +","+i+","+attrs+","+likeliness);
									continue loop_pattern;
								} else {
									// or fail
									return null;
								}
								
							}
						}
						
						t = newt;
						
						if (((Boolean) o[1]) == false) {	// this pattern part matches only once
							if (pattern.size() == i + 1 && t != null) t = t.prev(); // shift back once
							break;
						}
						
					}
					
					if (t == null) {	// end of text, we have to decide
						
						if (pattern.size() == i + 1) {
							// if last pattern part that matched once yet, this is a legal end of name
							// shift to the end
							t = ft;
							while (true) if (t.next() == null) break; else t = t.next();
							break loop_pattern;
						} else if (backtrackpoints.size() > 0) {
							// otherwise backtrack
							Object[] b = backtrackpoints.remove(0);
//System.err.println("null backtrack to: " + ((t == null) ? null : t.surface) +","+i+","+attrs+","+likeliness);
							t = (Token) b[0];
							i = (Integer) b[1];
							attrs = (HashMap<String, String>) b[2];
							likeliness = (Integer) b[3];
							encls = (HashSet<Annotation>) b[4];
//System.err.println("null backtrack to: " + t.surface +","+i+","+attrs+","+likeliness);
							continue loop_pattern;
						} else {
							// or fail
							return null;
						}
						
					}
					
				}
			}
			
			return new NamePatternResult(this, ft, t, attrs, encls);
			
		}
		
		
		/**Consumes as many tokens as possible and returns the first token not consumed
		 * Stops at the first token that has no annotation of type type in common with t.
		 * 
		 * @param t the first token that shall be consumed
		 * @param type the type of annotation that binds the tokens
		 * @param attrs the new annotation attributes
		 * @param attrnamemap mapping of the attrib names of the existing annotation to the new annotation attributes in attrs
		 * @return the first token not consumed. Thus, if this method could not consume anything, it returns t.
		 */
		private Token consumeAnnotation (Token t, AnnotationType type, Map<String, String> attrs, Map<String, String> attrnamemap, Set<Annotation> encls) {
			Annotation[] aa = t.getSurroundingAnnotations(type);
			if (aa.length == 0) return t;
			Token last;
			// find the longest annotation
loop_consume:
			while (true) {
				last = t;
				t = last.next();
				
				if (t != null)
					for (Annotation a: t.getSurroundingAnnotations(type))
						if (ArraysUtil.indexOf(aa, a) != -1) continue loop_consume;
				break;
			}
			// find longest annotation and copy attributes using the key mapping of attrnamemap
			// if the attribute exists, appends the new value using blank as separator
			// add the annotation to the seet of enclosed annotations
			for (Annotation a: last.getSurroundingAnnotations(type))
				if (ArraysUtil.indexOf(aa, a) != -1) {
					encls.add(a);
					for (Entry<String, String> e: attrnamemap.entrySet())
						if (a.attributes.containsKey(e.getKey())) {
							attrs.put(e.getValue(), (attrs.get(e.getValue()) != null) ? attrs.get(e.getValue()) + " " + a.attr(e.getKey()) : a.attr(e.getKey()));
						}
					break;
				}
			return t;
		}
		
	}
	
	
	
	@Override
	protected void annotateTokens(Token[] tokens) {
		
		NamePatternResult[] npra = new NamePatternResult[namepatterns.length];
		
		for (int i = 0; i < tokens.length; i++) {
			
			Map<String, String> attrs = new HashMap<String, String>();
			for (String a: AnnotationType.PERSON.obligatoryAttributes) attrs.put(a, null);
			
			int eval = nameStartEvaluation(tokens[i], attrs);
			if (eval == 0) continue;
			
			// apply all name patterns and remember the longest pattern
			// which will be taken, at last
			int lp = -1;	// longest pattern
			for (int p = 0; p < namepatterns.length; p++) {
				npra[p] = namepatterns[p].applyTo(tokens[i], eval + GammaBZFactor);
				if (npra[p] != null && (lp == -1 || npra[p].lasttoken.getOffset(npra[lp].lasttoken) < 0)) {
					lp = p;
				}
			}
			
			if (lp > 0 || npra[0] != null) {
				NamePatternResult npr = npra[lp];
				attrs.putAll(npr.attrs);
				attrs.put("namePattern", npr.producer.toString());
				makeAnno(AnnotationType.PERSON, attrs, npr.firsttoken, npr.lasttoken, npr.enclosing);
			}
			
		}
		
	}
	
	
	/**
	 * 
	 * @param cur
	 * @param attr map to store additional attributes for the new annotation
	 * @return 0: definitely no name start, 1: no likely name start, 2+: quite likely name start
	 */
	private int nameStartEvaluation (Token cur, Map<String, String> attr) {
		
		// check if this was already annotated
		String[] sa = properties.getProperty(this.getClass().getCanonicalName() + ".skipTokensWithAnnotations", "").split("\\s+");
		AnnotationType[] types = new AnnotationType[sa.length + 1];
		for (int i = 0; i < sa.length; i++) types[i] = (sa[i].length() == 0) ? null : AnnotationType.valueOf(sa[i]);
		types[types.length - 1] = AnnotationType.PERSON;
		if (cur.getSurroundingAnnotations(types).length > 0) return 0;
		
		// check for tabu patterns
		for (Pattern s: negatives.keySet()) {
			Token t = cur.sibling(negatives.get(s));
			if (t != null && s.matcher(t.token).matches()) return 0;
		}
		
		int ret = 0;
		Annotation[] nc1;
		Annotation[] nc2;
		Token prev1 = cur.prev();
		Token prev2 = (prev1 == null) ? null : prev1.prev();
		
		// check existing name part
		nc1 = cur.getSurroundingAnnotations(AnnotationType.GIVEN_NAME, AnnotationType.SURNAME);
		if (nc1.length != 0) {
			attr.put("trigger", nc1[0].type.toString() + ":" + nc1[0].surface());
			ret += 1;
		}
		
		// check profession pattern
		if (prev1 != null) {
			nc1 = prev1.getSurroundingAnnotations(AnnotationType.PROFESSION);
			if (nc1.length != 0) {
				attr.put("trigger", "PROFESSION:"+nc1[0].surface());
				ret += 1;
			}
		}
		
		// check relation pattern
		if (prev2 != null) {
			nc1 = prev1.getSurroundingAnnotations(AnnotationType.HUMAN_RELATION);
			nc2 = prev2.getSurroundingAnnotations(AnnotationType.HUMAN_RELATION);
			if (nc1.length != 0 && prev2.pos != null && prev2.pos.matches("PPOSAT|APPR")) {
				attr.put("trigger", "HUMAN_RELATION:"+nc1[0].surface());
				ret += 1;
			} else if (nc2.length != 0 && prev1.token.matches("d|von")) {
				attr.put("trigger", "HUMAN_RELATION:"+nc2[0].surface());
				ret += 1;
			} else if (nc2.length != 0 && prev1.token.equals(":")) {
				attr.put("trigger", "HUMAN_RELATION:"+nc2[0].surface());
				ret += 1;
			}
		}
		
		// check title pattern
		if (prev1 != null) {
			nc1 = prev1.getSurroundingAnnotations(AnnotationType.SOCIAL_TITLE);
			if (nc1.length != 0) {
				attr.put("trigger", "SOCIAL_TITLE:"+nc1[0].surface());
				ret += 1;
			}
		}
		
		// check positive keyword patterns from file 
		for (Pattern s: positives.keySet()) {
			Token t = cur.sibling(positives.get(s));
			if (t == null) continue;
			if (s.matcher(t.token).matches()) {
				attr.put("trigger", "keyword:"+t.token);
				ret += 1;
			}
		}
		
		return ret;
		
	}
	
	
	
	private void loadDB () {
		
		positives = new HashMap<Pattern, Integer>();
		negatives = new HashMap<Pattern, Integer>();
		attrnamemap = new HashMap<AnnotationType, Map<String,String>>();
		
		for (String file: properties.getProperty(this.getClass().getPackage().getName() + ".namePatternDBFiles", "").split(";")) {
			if (file == null || file.length() == 0) continue;
			loadNamePatternFile(file);
		}

		for (String file: properties.getProperty(this.getClass().getPackage().getName() + ".namePatternDBFiles", "").split(";")) {
			if (file == null || file.length() == 0) continue;
			loadAttrnameMapFile(file);
		}
		
		for (String file: properties.getProperty(this.getClass().getPackage().getName() + ".namePatternDBFiles", "").split(";")) {
			if (file == null || file.length() == 0) continue;
			loadKeywordDBFile(file);
		}
		
	}
	
	
	private void loadAttrnameMapFile (String file) {
		
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(file));
			
			for (Element n: DOMUtil.allElements(doc, "//attrMappings/mapping")) {
				
				String s = n.getAttribute("source");
				String t = n.getAttribute("target");
				for (String at: n.getAttribute("types").split("\\s+")) {
					AnnotationType type = AnnotationType.valueOf(at);
					if (! attrnamemap.containsKey(type)) attrnamemap.put(type, new HashMap<String, String>());
					attrnamemap.get(type).put(s, t);
				}
				
			}
			
		} catch (Exception e) {
			throw new AnnotatorInitializationException(e);
		}
	}
	
	private void loadNamePatternFile (String file) {
		
		ArrayList<NamePattern> patterns = new ArrayList<NamePattern>();
		
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(file));
			
			for (Element n: DOMUtil.allElements(doc, "//namePatterns/pattern")) {
				
				ArrayList<Object[]> patt = new ArrayList<Object[]>();
				
				for (Element e: DOMUtil.allElements(n, "annotation|comma")) {
					if ("comma".equals(e.getNodeName())) {
						patt.add(new Object[]{",", false});
					} else {
						if (! e.getAttribute("occurances").matches("once|multiple")) continue;
						patt.add(new Object[]{AnnotationType.valueOf(e.getAttribute("type")), "multiple".equals(e.getAttribute("occurances"))});
					}
				}
				
				patterns.add(new NamePattern(patt));
				
			}
		} catch (Exception e) {
			throw new AnnotatorInitializationException(e);
		}
	
		if (patterns.size() == 0) throw new AnnotatorInitializationException("no name patterns found");
		namepatterns = patterns.toArray(new NamePattern[patterns.size()]);

	}

	
	
	private void loadKeywordDBFile (String file) {
		
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new FileInputStream(file));
			Element[] list = DOMUtil.allElements(doc, "//keywords/pattern");
			
			for (Element n: list) {
				
				NamedNodeMap atts = n.getAttributes();
				String type = null;
				int pos = 0;
				Map<String, Object> props = new HashMap<String, Object>();
				
				for (int j = 0; j < atts.getLength(); j++) {
					Node att = atts.item(j);
					if (att.getNodeName().equals("type"))
						type = att.getNodeValue();
					else if (att.getNodeName().equals("position"))
						pos = new Integer(att.getNodeValue());
					else 
						props.put(att.getNodeName(), att.getNodeValue());
				}
				
				if (type == null) continue;
				if (n.getTextContent() == null || n.getTextContent().length() == 0) continue;
				if (type.equals("positive")) {
					positives.put(Pattern.compile(n.getTextContent()), pos);
				} else if (type.equals("negative")) {
					negatives.put(Pattern.compile(n.getTextContent()), pos);
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			throw new AnnotatorInitializationException(e);
		}
		
	}

}
