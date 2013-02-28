package eu.wiss_ki.aapi;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.xml.sax.InputSource;

import de.fau.cs8.mnscholz.util.AbstractOptionedClass;
import de.fau.cs8.mnscholz.util.SQLUtil;
import de.fau.cs8.mnscholz.util.options.Options;

public class DBFuzzyVocabulary extends AbstractOptionedClass implements Vocabulary {

	Logger log;
	Connection dbc; 
	
	public DBFuzzyVocabulary(Options options) {
		super(options);
		
		log = Logger.getLogger(this.getClass().getCanonicalName() + "#" + getName());

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			log.log(Level.SEVERE, "db driver init", e);
		}
		
		establishConnection();
		
	}
	
	private void establishConnection() {
		try {
			String dburl = "jdbc:mysql://localhost/" + URLEncoder.encode(options.get("db.database"), "utf-8");
			
			if (options.exists("db.user")) {
				log.finer("url to database is " + dburl + "; user and password given");

				dbc = DriverManager.getConnection(dburl, options.get("db.user"), options.get("db.password", ""));
			} else {
				log.finer("url to database is " + dburl);
				dbc = DriverManager.getConnection(dburl);
			}
			
			log.finest("connection accepted");
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "db connection init", e);
		}
		
	}
	
	@Override
	public String getName() {
		return options.get("name", null);
	}
	
	@Override
	public String[] getTypes() {
		return new String[]{options.get("termType")};
	}

	@Override
	public FS lookupTermInfo(String entryID) {
		return lookupTermInfo(entryID, true);
	}
	
	public FS lookupTermInfo(String entryID, boolean retry) {
		
		try {
			
			Statement s = dbc.createStatement();
			String q = "select entries.xml from entries where uri = '" + SQLUtil.escape(entryID) + "'";
			if (getName() != null && !getName().isEmpty()) q += " and authname='" + SQLUtil.escape(getName()) + "'";
			q += ";";
			log.finest("executing query: " + q);
			ResultSet rs = s.executeQuery(q);
			
			if (! rs.next()) return null;
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(rs.getCharacterStream("xml")));
			
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
			
		} catch (SQLException e) {
			if (retry) {
				log.log(Level.WARNING, "invalid connection; retry", e);
				establishConnection();
				return lookupTermInfo(entryID, false);
			} else {
				log.log(Level.SEVERE, "invalid connection; fail", e);
			}
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
	public FS[] lookupTermsExactMatch (String termStart) {
		return lookupTerms(termStart, "exact");
	}
	
	private FS[] lookupTerms (String term, String mode) {
		return lookupTerms(term, mode, true);
	}
		
	private FS[] lookupTerms (String term, String mode, boolean retry) {
			
		term = SQLUtil.escapeLike(term);
		if ("part".equals(mode)) term = "%" + term + "%";
		else if ("start".equals(mode)) term = term + "%";
		else {
			log.warning("bad mode: " + mode);
			return new FS[0];
		}
		
		try {
			
			Statement s = dbc.createStatement();
			String q = "select term, uri, authname, type from terms where terms.term like '" + term + "'";
			if (getName() != null && !getName().isEmpty()) q += " and terms.authname='" + SQLUtil.escape(getName()) + "'";
			q += ";";
			log.finest("executing query: " + q);
			ResultSet rs = s.executeQuery(q);
			
			List<FS> fss = new ArrayList<FS>();
			
			while (rs.next()) {
				
				CFS fs = FSUtil.newCFS();
				
				fs.set("term", rs.getString("term"));
				fs.set("entryID", rs.getString("uri"));
				fs.set("vocabulary", rs.getString("authname"));
				fs.set("type", rs.getString("type"));
				fss.add(fs);

			}
			
			log.finer("query for terms containing '" + term + "' on authority " + getName() + " yielded " + fss);
			
			return fss.toArray(new FS[fss.size()]);
			
		} catch (SQLException e) {
			if (retry) {
				log.log(Level.WARNING, "invalid connection; retry", e);
				establishConnection();
				return lookupTerms(term, mode, false);
			} else {
				log.log(Level.SEVERE, "invalid connection; fail", e);
			}
		} catch (Exception e) {
			log.log(Level.WARNING, "exception parsing authority query answer", e);
		}
		
		return new FS[0];
		
	}
	
	
	public static void main (String[] args) {
		
		Options o = Options.create();
		o.set("db.database", "wisski");
		o.set("db.user", "wisskiuser");
		o.set("db.password", "wisski$db");
//    o.set("name", "Getty AAT");
		
		DBFuzzyVocabulary a = new DBFuzzyVocabulary(o);
		
		String[] terms = "Topla,aber,Fayence,ist,hat,topton,jetzt,guten,AAT root".split(",");
		
		for (String t: terms) {
			
			System.out.println();
			System.out.println("-------------");
			System.out.println(t);
			
			FS[] fss = a.lookupTermsStartingWith(t);
			System.out.println(fss.length);
			
			for (FS fs: fss) {
				String p = fs.getString("term");
				if (p != null && p.equalsIgnoreCase(t))
					System.out.println("+" + p);
        else
					System.out.println("-" + p);
			}
			
		}
	    
	}
	
}
