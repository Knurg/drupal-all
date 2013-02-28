/*
 * Copyright (c) 2004 SJTU APEX Lab.
 * All rights reserved.
 * 
 * $$Id: OWLDIGTranslator.java,v 1.13 2004/05/27 05:02:27 priest Exp $$
 */
package sjtu.apex.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 * @author christie
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OWLDIGTranslator {

	// translate temporariy class description
	private static Node translateClass(OntClass c, Document doc) {
		if (!c.isAnon()) {
			Element e = doc.createElement("catom");
			e.setAttribute("name", c.getURI());
			return e;
		} else {
			return translateClassDescription(c, doc);
		}
	}

	// Get appropriate description of class including anonymous class and other class
	private static Node translateClassDescription(OntClass c, Document doc) {
		Node e = null;
		if (c.isRestriction()) {
			e = translateRestriction(c.asRestriction(), doc);
		}
		if (c.isIntersectionClass()) {
			e = translateBooleanClass(c.asIntersectionClass(), "and", doc);
		}
		if (c.isUnionClass()) {
			e = translateBooleanClass(c.asUnionClass(), "or", doc);
		}
		if (c.isComplementClass()) {
			e = translateBooleanClass(c.asComplementClass(), "not", doc);
		}
		if (c.isEnumeratedClass()) {
			e = translateOneOf(c.asEnumeratedClass(), doc);
		}
		if (!c.isAnon() && e == null) {
			e = translateClass(c, doc);
		}
		return e;
	}

	private static Node translateOneOf(EnumeratedClass c, Document doc) {
		Element e = doc.createElement("iset");
		Iterator it = c.listOneOf();
		while (it.hasNext()) {
			e.appendChild(
				translateIndividual(((OntResource) it.next()).getURI(), doc));
		}
		return e;
	}

	private static Node translateBooleanClass(
		BooleanClassDescription c,
		String operator,
		Document doc) {
		Element e = doc.createElement(operator);
		Iterator it = c.listOperands();
		while (it.hasNext()) {
			Node node = translateClass((OntClass) it.next(), doc);
			e.appendChild(node);
		}
		return e;
	}

	private static Node translateRestriction(Restriction r, Document doc) {
		Element e;
		if (r.isAllValuesFromRestriction()) {
			AllValuesFromRestriction afr = r.asAllValuesFromRestriction();
			e = doc.createElement("all");
			e.appendChild(translateProperty(r.getOnProperty(), doc));
			e.appendChild(
				translateClass((OntClass) afr.getAllValuesFrom(), doc));
			return e;
		}
		if (r.isSomeValuesFromRestriction()) {
			SomeValuesFromRestriction sfr = r.asSomeValuesFromRestriction();
			e = doc.createElement("some");
			e.appendChild(translateProperty(r.getOnProperty(), doc));
			e.appendChild(
				translateClass((OntClass) sfr.getSomeValuesFrom(), doc));
			return e;
		}
		if (r.isCardinalityRestriction()) {
			CardinalityRestriction cr = r.asCardinalityRestriction();
			e = doc.createElement("and");
			e.appendChild(
				translateNumberCardinality(
					r.getOnProperty(),
					cr.getCardinality(),
					true,
					doc));
			e.appendChild(
				translateNumberCardinality(
					r.getOnProperty(),
					cr.getCardinality(),
					false,
					doc));
			return e;
		}
		if (r.isMaxCardinalityRestriction()) {
			return translateNumberCardinality(
				r.getOnProperty(),
				r.asMaxCardinalityRestriction().getMaxCardinality(),
				false,
				doc);
		}
		if (r.isMinCardinalityRestriction()) {
			return translateNumberCardinality(
				r.getOnProperty(),
				r.asMinCardinalityRestriction().getMinCardinality(),
				true,
				doc);
		}
		if (r.isHasValueRestriction()) {
			HasValueRestriction hvr = r.asHasValueRestriction();
			e = doc.createElement("some");
			e.appendChild(translateProperty(r.getOnProperty(), doc));
			Element iset = doc.createElement("iset");
			iset.appendChild(
				translateIndividual(
					((Resource) hvr.getHasValue()).getURI(),
					doc));
			e.appendChild(iset);
			return e;
		}
		return null;
	}

	// translate cardinality
	private static Node translateNumberCardinality(
		OntProperty property,
		int r,
		boolean min,
		Document doc) {
		Element e;
		if (min) {
			e = doc.createElement("atleast");
		} else {
			e = doc.createElement("atmost");
		}
		e.setAttribute("num", r + "");
		e.appendChild(translateProperty(property, doc));
		e.appendChild(doc.createElement("top"));
		return e;
	}

	private static void translateClassAxioms(
		OntClass c,
		Node root,
		Document doc) {
		Iterator it = c.listSuperClasses();
		translateClassAxiom(c, it, "impliesc", root, doc);
		it = c.listEquivalentClasses();
		translateClassAxiom(c, it, "equalc", root, doc);
		it = c.listDisjointWith();
		translateClassAxiom(c, it, "disjoint", root, doc);
	}

	//	translate property definitions
	private static Node translateProperty(OntProperty p, Document doc) {
		Element e;
		if (p.isObjectProperty()) {
			e = doc.createElement("ratom");
		} else {
			e = doc.createElement("attribute");
		}
		e.setAttribute("name", p.getURI());
		return e;
	}

	private static void translatePropertyAxioms(
		OntProperty p,
		Node root,
		Document doc) {
		Element e, f;
		if (p.isObjectProperty()) {
			e = doc.createElement("defrole");
		} else {
			e = doc.createElement("defattribute");
		}
		e.setAttribute("name", p.getURI());
		root.appendChild(e);
		if (p.isFunctionalProperty()) {
			root.appendChild(translatePropertyType(p, "functional", doc));
		}
		if (p.isInverseFunctionalProperty()) {
			e = doc.createElement("functional");
			e.appendChild(translatePropertyType(p, "inverse", doc));
			root.appendChild(e);
		}
		if (p.isSymmetricProperty()) {
			e = doc.createElement("equalr");
			e.appendChild(translateProperty(p, doc));
			e.appendChild(translatePropertyType(p, "inverse", doc));
			root.appendChild(e);
		}
		if (p.isTransitiveProperty()) {
			root.appendChild(translatePropertyType(p, "transitive", doc));
		}
		Iterator it = p.listDomain();
		while (it.hasNext()) {
			try {
				OntClass cl = ((OntResource) it.next()).asClass();
				e = doc.createElement("domain");
				e.appendChild(translateProperty(p, doc));
				e.appendChild(translateClass(cl, doc));
				root.appendChild(e);
			} catch (Exception ex) {
				System.err.println(
					"Property:" + p.getURI() + " has invalid domain.");
			}
		}
		it = p.listRange();
		while (it.hasNext()) {
			OntResource resource = (OntResource) it.next();
			if (p.isObjectProperty()) {
				OntClass cl = resource.asClass();
				e = doc.createElement("range");
				e.appendChild(translateProperty(p, doc));
				e.appendChild(translateClass(cl, doc));
				root.appendChild(e);
			}
			if (p.isDatatypeProperty()) {
				if (resource.equals(XSD.xstring)) {
					e = doc.createElement("rangestring");
				} else {
					e = doc.createElement("rangeint");
				}
				e.appendChild(translateProperty(p, doc));
				root.appendChild(e);
			}

		}
		it = p.listInverse();
		while (it.hasNext()) {
			OntProperty pr = (OntProperty) it.next();
			e = doc.createElement("equalr");
			e.appendChild(translateProperty(p, doc));
			e.appendChild(translatePropertyType(pr, "inverse", doc));
			root.appendChild(e);
		}
		it = p.listSuperProperties(true);
		while (it.hasNext()) {
			OntProperty pr = (OntProperty) it.next();
			e = doc.createElement("impliesr");
			e.appendChild(translateProperty(p, doc));
			e.appendChild(translateProperty(pr, doc));
			root.appendChild(e);
		}
		it = p.listEquivalentProperties();
		while (it.hasNext()) {
			OntProperty pr = (OntProperty) it.next();
			e = doc.createElement("equalr");
			e.appendChild(translateProperty(p, doc));
			e.appendChild(translateProperty(pr, doc));
			root.appendChild(e);
		}
	}

	private static boolean isMeta(Resource r) {
		String uri = r.getNameSpace();
		if (uri.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#")
			|| uri.equals("http://www.w3.org/2000/01/rdf-schema#")
			|| uri.equals("http://www.w3.org/2002/07/owl#"))
			return true;
		return false;
	}
	private static void translateIndividualInfo(
		Individual ind,
		Iterator pit,
		Node root,
		Document doc) {
		Element e;
		e = doc.createElement("defindividual");
		e.setAttribute("name", ind.getURI());
		root.appendChild(e);
		Iterator it = ind.listRDFTypes(true);
		while (it.hasNext()) {
			e = doc.createElement("instanceof");
			e.appendChild(translateIndividual(ind.getURI(), doc));
			Element f = doc.createElement("catom");
			f.setAttribute("name", ((Resource) it.next()).getURI());
			e.appendChild(f);
			root.appendChild(e);
		}
		StmtIterator vit = ind.listProperties();
		while (vit.hasNext()) {
			Statement stmt = vit.nextStatement();
			Object obj = stmt.getObject();
			if (isMeta(stmt.getPredicate())
				&& !stmt.getPredicate().getLocalName().equals("label")) {
				continue;
			}
			OntProperty pro =
				(OntProperty) stmt.getPredicate().as(OntProperty.class);
			if (obj instanceof Literal) {
				if (pro.isObjectProperty()) {
					System.err.println(
						"ObjectProperty:"
							+ pro.getURI()
							+ " has datatype value.");
					continue;
				}
				Literal literal = (Literal) obj;
				e = doc.createElement("value");
				e.appendChild(translateIndividual(ind.getURI(), doc));
				e.appendChild(translateProperty(pro, doc));
				Element f = doc.createElement("sval");
				f.appendChild(doc.createTextNode(literal.getLexicalForm()));
				e.appendChild(f);
				root.appendChild(e);
			} else {
				if (pro.isDatatypeProperty()) {
					System.err.println(
						"DatatypeProperty:"
							+ pro.getURI()
							+ " has object value.");
					continue;
				}
				e = doc.createElement("related");
				e.appendChild(translateIndividual(ind.getURI(), doc));
				e.appendChild(translateProperty(pro, doc));
				e.appendChild(
					translateIndividual(((Resource) obj).getURI(), doc));
				root.appendChild(e);
			}
		}
	}

	public static void translateOWL(OntModel m, String kburi, Document doc) {
		Element root = createRoot("tells", kburi, doc);
		Set set = new HashSet();
		Iterator it = m.listObjectProperties();
		while (it.hasNext()) {
			OntProperty p = (OntProperty) it.next();
			set.add(p);
			translatePropertyAxioms(p, root, doc);
		}
		it = m.listDatatypeProperties();
		while (it.hasNext()) {
			OntProperty p = (OntProperty) it.next();
			set.add(p);
			translatePropertyAxioms(p, root, doc);
		}
		it = m.listNamedClasses();
		while (it.hasNext()) {
			OntClass cl = (OntClass) it.next();
			Node e = translateClassDescription(cl, doc);
			if (!cl.isAnon()) {
				Element f;
				if (e == null) {
					f = doc.createElement("defconcept");
					f.setAttribute("name", cl.getURI());
				} else {
					f = doc.createElement("equalc");
					f.appendChild(translateClass(cl, doc));
					f.appendChild(e);
				}
				root.appendChild(f);
			} else {
				root.appendChild(e);
			}
			translateClassAxioms(cl, root, doc);
		}
		it = m.listIndividuals();
		while (it.hasNext()) {
			Individual i = (Individual) it.next();
			translateIndividualInfo(i, set.iterator(), root, doc);
		}
	}

	private static String getID(Resource n) {
		if (n.isAnon()) {
			return n.getId().toString();
		} else {
			return n.getURI();
		}
	}

	public static void translateOntClass(
		OntClass cl,
		String kburi,
		Document doc) {
		Element root = createRoot("tells", kburi, doc);
		Node e, f;
		e = translateClassDescription(cl, doc);
		f = doc.createElement("equalc");
		f.appendChild(translateClass(cl, doc));
		f.appendChild(e);
		root.appendChild(f);
		return;
	}

	private static Element createRoot(
		String roottag,
		String kburi,
		Document doc) {
		Element root;
		root = doc.createElement(roottag);
		root.setAttribute("xmlns", "http://dl.kr.org/dig/lang");
		if (kburi != null)
			root.setAttribute("uri", kburi);
		doc.appendChild(root);
		return root;
	}

	private static Node translatePropertyType(
		OntProperty p,
		String type,
		Document doc) {
		Element e = doc.createElement(type);
		e.appendChild(translateProperty(p, doc));
		return e;
	}

	// translate class axioms
	private static void translateClassAxiom(
		OntClass first,
		Iterator it,
		String operator,
		Node root,
		Document doc) {
		Element e;
		while (it.hasNext()) {
			OntClass cl = (OntClass) it.next();
			e = doc.createElement(operator);
			e.appendChild(translateClass(first, doc));
			e.appendChild(translateClass(cl, doc));
			root.appendChild(e);
		}
		return;
	}

	private static Node translateIndividual(String uri, Document doc) {
		Element e = doc.createElement("individual");
		e.setAttribute("name", uri);
		return e;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.out.println(
				"Usage: java sjtu.apex.sports.kb.impl.OWLDIGTranslator [OWL File] [Dig File] {kb-uri}");
			System.exit(-1);
		}
		if (!(new File(args[0]).exists())) {
			System.out.println("Source OWL file must exist!");
			System.out.println(
				"Usage: java sjtu.apex.sports.kb.impl.OWLDIGTranslator [OWL File] [Dig File] {kb-uri}");
		}
		String kburi;
		if (args.length == 2)
			kburi = null;
		else
			kburi = args[2];

		OntModel model =
			ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, null);
		model.read(new FileInputStream(args[0]), "");
		Document doc =
			DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.newDocument();
		OWLDIGTranslator.translateOWL(model, kburi, doc);
		OutputFormat format = new OutputFormat(doc);
		format.setIndenting(true);
		XMLSerializer serial =
			new XMLSerializer(new FileOutputStream(args[1]), format);
		serial.asDOMSerializer();
		serial.serialize(doc.getDocumentElement());
		System.out.println("OWL to DIG translate successfully!");
	}
}
