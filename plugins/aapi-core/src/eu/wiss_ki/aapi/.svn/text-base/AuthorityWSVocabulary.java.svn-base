package eu.wiss_ki.aapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;
import lebada.voc.Vocabulary;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.fau.cs8.mnscholz.util.AbstractOptionedClass;
import de.fau.cs8.mnscholz.util.options.Options;

public class AuthorityWSVocabulary extends AbstractOptionedClass implements Vocabulary {
	
	private Logger log;
	
	
	public AuthorityWSVocabulary(Options options) {
		super(options);
		if (! (options.exists("name")
			&& options.exists("services.lookupTermsContaining")
			&& options.exists("services.lookupTermInfo")))
			throw new RuntimeException("missing configuration options");
		if (log == null) log = Logger.getLogger(this.getClass().getCanonicalName() + "#" + options.get("name"));
    log.finest(options.toString() + " - " + Arrays.toString(options.getKeys()));
	}

	@Override
	public String getName() {
		return options.get("name");
	}

/*	@Override
	public FS[] getTermInfo(String term) {
		
		String url = prepareURL(options.get("services.getTermInfos"), term);
		
		try {
			URLConnection conn = (new URL(url)).openConnection();
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());
			
			if (doc.hasChildNodes() && "error".equals(doc.getFirstChild().getNodeName())) {
				log.warning("authority ws returned with error: " + doc.getFirstChild().getTextContent());
			}
			
			NodeList l = doc.getElementsByTagName("info");
			
			log.finest("term info number of entries: " + l.getLength());
			
			FS[] ret = new FS[l.getLength()];
			for (int i = l.getLength() - 1; i >= 0; i--) {
				Element k = (Element) l.item(i).getFirstChild();
				
				CFS fs = FSUtil.newCFS();
				
				while (k != null) {
					fs.set(k.getTagName(), k.getTextContent());
					k = (Element) k.getNextSibling();
				}
				
				ret[i] = fs;
				
			}
			
			log.finer("query for term info on '" + term + "' on authority " + getName() + " yielded " + Arrays.toString(ret));
			
			return ret;
			
		} catch (MalformedURLException e) {
			log.log(Level.WARNING, "misconfigured web service", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "cannot connect to authority ws", e);
		} catch (Exception e) {
			log.log(Level.WARNING, "exception parsing authority query answer", e);
		}
		
		return null;
		
	}*/

/*	@Override
	public String[] getTermsStartingWith(String term) {
		
//		String[] con = getTermsContaining(term);
//		for (int i = 0; i < con.length; i++) if (! con[i].startsWith(term)) con[i] = null;
//		return ArraysUtil.copyOmitNull(con);
		
		return getTermsContaining("^" + term); // works if authority supports regex!
		
	}*/

/*	@Override
	public String[] getTermsContaining(String term) {
		
		String url = prepareURL(options.get("services.getTermsContaining"), term);
		
		try {
			URLConnection conn = (new URL(url)).openConnection();
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());
			
			if (doc.hasChildNodes() && "error".equals(doc.getFirstChild().getNodeName())) {
				log.warning("authority ws returned with error: " + doc.getFirstChild().getTextContent());
			}
			
			NodeList l = doc.getElementsByTagName("term");
			
			String[] ret = new String[l.getLength()];
			for (int i = l.getLength() - 1; i >= 0; i--) {
				Element e = (Element) l.item(i);
				ret[i] = e.getTextContent();
			}
			
			log.finer("query for terms containing '" + term + "' on authority " + getName() + " yielded " + Arrays.toString(ret));
			
			return ret;
			
		} catch (MalformedURLException e) {
			log.log(Level.WARNING, "misconfigured web service", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "cannot connect to authority ws", e);
		} catch (Exception e) {
			log.log(Level.WARNING, "exception parsing authority query answer", e);
		}
		
		return null;
		
	}*/

	@Override
	public String[] getTypes() {
    log.finest(options.toString());
		return new String[]{options.get("termType")};
	}
	
	private String prepareURL (String url, String term) {
		try {
			url = url.replace("{$term}", URLEncoder.encode(term, "utf-8"));
			url = url.replace("{$authority}", URLEncoder.encode(getName(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			log.severe("SYSTEM DOES NOT SUPPORT UTF-8???!!!");
			throw new RuntimeException(e);
		}
		return url;
	}

	@Override
	public FS lookupTermInfo(String entryID) {
		
		String url = prepareURL(options.get("services.lookupTermInfo"), entryID);
		
		try {
			URLConnection conn = (new URL(url)).openConnection();
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());
			
			if (doc.hasChildNodes() && "error".equals(doc.getFirstChild().getNodeName())) {
				log.warning("authority ws returned with error: " + doc.getFirstChild().getTextContent());
				return null;
			}
			
			Element info = (Element) doc.getElementsByTagName("info").item(0);
			
			Element k = (Element) info.getFirstChild();
			
			CFS fs = FSUtil.newCFS();
			
			while (k != null) {
				fs.set(k.getTagName(), k.getTextContent());
				k = (Element) k.getNextSibling();
			}
				
			log.finer("query for term info on '" + entryID + "' on authority " + getName() + " yielded " + fs);
			
			return fs;
			
		} catch (MalformedURLException e) {
			log.log(Level.WARNING, "misconfigured web service", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "cannot connect to authority ws", e);
		} catch (Exception e) {
			log.log(Level.WARNING, "exception parsing authority query answer", e);
		}
		
		return null;
		
	}

	@Override
	public FS[] lookupTermsContaining (String termPart) {
		return lookupTerms(termPart, "part");
	}

	@Override
	public FS[] lookupTermsStartingWith (String termStart) {
		return lookupTerms(termStart, "start");
	}
	
	@Override
	public FS[] lookupTermsExactMatch (String termExact) {
		return lookupTerms(termExact, "exact");
	}
	
	
	private FS[] lookupTerms (String term, String mode) {

		String url = null;
		if ("part".equals(mode)) {
			url = prepareURL(options.get("services.lookupTermsContaining"), term);
		} else if ("start".equals(mode)) {
			url = prepareURL(options.get("services.lookupTermsStartingWith"), term);
		} else if ("exact".equals(mode)) {
			url = prepareURL(options.get("services.lookupTermsExactMatch"), term);
		}
		if (url == null) throw new IllegalArgumentException("mode: " + mode);
		
		try {
			URLConnection conn = (new URL(url)).openConnection();

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());
			
			if (doc.hasChildNodes() && "error".equals(doc.getFirstChild().getNodeName())) {
				log.warning(getName() + ": authority ws returned with error: " + doc.getFirstChild().getTextContent());
				return new FS[0];
			}
			
			NodeList nl = doc.getElementsByTagName("term");
			
			List<FS> fss = new ArrayList<FS>();
			int l = nl.getLength();
			for (int i = 0; i < l; i++) {
				Element e = (Element) nl.item(i);
				String term1 = e.getAttribute("term");
				NodeList nl1 = e.getElementsByTagName("entryID");
				int l1 = nl1.getLength();
				for (int j = 0; j < l1; j++) {
					String id = nl1.item(j).getTextContent();
					fss.add(FSUtil.newFS(
							"entryID", id,
							"term", term1,
							"vocabulary", getName(),
							"type", getTypes()[0]
					));
				}
			}
			
			log.finer("query for terms containing '" + term + "' on authority " + getName() + " yielded " + fss);
			
			return fss.toArray(new FS[fss.size()]);
			
		} catch (MalformedURLException e) {
			log.log(Level.WARNING, "misconfigured web service", e);
		} catch (IOException e) {
			log.log(Level.WARNING, "cannot connect to authority ws", e);
		} catch (Exception e) {
			log.log(Level.WARNING, "exception parsing authority query answer", e);
		}
		
		return new FS[0];
		
	}
	
	
	
}
