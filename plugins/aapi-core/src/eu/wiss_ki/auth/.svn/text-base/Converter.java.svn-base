package eu.wiss_ki.auth;

import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import apus.LexTree;

public class Converter {
	
	public void convert (String voc, String urlTerms, String urlInfos) throws Exception {
		
		String sDbUrl = "jdbc:mysql://localhost/wisski";
		String sUsr = "wisski";
		String sPwd = "W1ssk1.";
	    
		Class.forName("com.mysql.jdbc.Driver");
	    Connection cn = DriverManager.getConnection( sDbUrl, sUsr, sPwd );
	    
	    Statement st = cn.createStatement();
	    st.executeUpdate("delete from terms where authname='" + voc.replace("\\", "\\\\").replace("'", "\\'") + "';");
	    st.executeUpdate("delete from entries where authname='" + voc.replace("\\", "\\\\").replace("'", "\\'") + "';");
	    
	    char[] chars = "abcdefghijklmnopqrstuvwxyzäöüß".toCharArray();
		
	    Set<String> allids = new HashSet<String>();
	    
		for (char c1: chars) {
			for (char c2: chars) {
				
				URLConnection c = (new URL(urlTerms + URLEncoder.encode("" + c1 + c2, "utf-8"))).openConnection();
System.err.println(c.getURL().toString());

				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(c.getInputStream());


				Set<String> ids = new HashSet<String>();
				
				NodeList nl = doc.getElementsByTagName("entryID");
				for (int i = 0; i < nl.getLength(); i++)
					ids.add(nl.item(i).getTextContent());

System.out.println("for " + c1 + c2 + " got " + ids.size());
				ids.removeAll(allids);
				allids.addAll(ids);
				
				for (String id: ids) {
//System.err.println("id = " + id);

					c = (new URL(urlInfos + URLEncoder.encode(id, "utf-8"))).openConnection();
					try {
						doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(c.getInputStream());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

					String type = doc.getElementsByTagName("type").item(0).getTextContent();
					
					Set<String> terms = new HashSet<String>();
					terms.add(doc.getElementsByTagName("canonical").item(0).getTextContent());
					nl = doc.getElementsByTagName("alternates");
					for (int i = 0; i < nl.getLength(); i++) terms.add(nl.item(i).getTextContent());
					
					StringWriter xml = new StringWriter();
					TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(xml));
					
					st = cn.createStatement();
					st.executeUpdate( "insert into `entries` values ('" + id + "', '" + voc.replace("\\", "\\\\").replace("'", "\\'") + "', '" + type.replace("\\", "\\\\").replace("'", "\\'") + "', '" + xml.toString().replace("\\", "\\\\").replace("'", "\\'") + "');");
				  	
					
					for (String term: terms) {
//System.err.println("term = " + term);

						st = cn.createStatement();
						st.executeUpdate( "insert into `terms` values('" + term.replace("\\", "\\\\").replace("'", "\\'") + "', '" + id + "', '" + voc.replace("\\", "\\\\").replace("'", "\\'") + "', '" + type.replace("\\", "\\\\").replace("'", "\\'") + "');");
					    	
					}
					
					
				}
				
				
			}
		}
		    
	}
	
	
	public void convert (String urlTerms, String urlInfos) throws Exception {
		
		System.out.println(Runtime.getRuntime().freeMemory());
	    
		String sDbUrl = "jdbc:mysql://localhost/wisski";
		String sUsr = "wisskiuser";
		String sPwd = "wisski$db";
	    
		Class.forName("com.mysql.jdbc.Driver");
	    Connection cn = DriverManager.getConnection( sDbUrl, sUsr, sPwd );
	    
	    Statement st = cn.createStatement();
	    ResultSet s = st.executeQuery( "select term from `terms`;");
	  	
	    LexTree l = new LexTree.TreeMapImpl();
	    
	    while (s.next()) {
	    	
	    	String t = s.getString("term");
	    	l.ensure(t);
	    	
	    }
	    
	    System.out.println(Runtime.getRuntime().freeMemory());
	    System.gc();
	    System.out.println(Runtime.getRuntime().freeMemory());
	    
	}
	
	
	public static void main (String[] args) throws Exception {
		
		Converter c = new Converter();
		
		c.convert(args[0], args[1], args[2]);
		
	}
	
}
