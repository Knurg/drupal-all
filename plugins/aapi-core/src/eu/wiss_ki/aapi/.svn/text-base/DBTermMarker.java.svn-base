package eu.wiss_ki.aapi;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;
import lebada.marker.Marker;
import lebada.marker.TermOccurence;
import de.fau.cs8.mnscholz.util.SQLUtil;
import de.fau.cs8.mnscholz.util.Span;
import de.fau.cs8.mnscholz.util.StringUtil;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.Token;

public class DBTermMarker extends Marker {
	
	public static abstract class Hasher {
		public abstract int hash (String word);
	}
	public static abstract class WordDistanceFilter {
		public abstract boolean similar (String word1, String word2);
	}
	
	
	protected Logger log;
	private String table;
	private Connection dbc; 
	private Hasher hasher;
	private Locale locale;
	private WordDistanceFilter distance;
	private String wordsSeparator;
	private int deltaWordExact;
	private int deltaWordSimilar;
	private int deltaFold;
	
	protected DBTermMarker(Options options) {
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
	protected TermOccurence[] markup0(Token[] tokens) {
		
		return null;
		
	}
	
	
	public FS[] markup(String[][] tokens) {
		
		Map<Integer, Map<String, Set<Integer>>> words = new TreeMap<Integer, Map<String, Set<Integer>>>();
		List<FS> ret = new ArrayList<FS>();
		Map<String, List<Object[]>> occur = new HashMap<String, List<Object[]>>();
		
		for (int i = 0; i < tokens.length; i++) {
			for (String t: tokens[i]) {
				t = t.toLowerCase(locale);
				int h = hasher.hash(t);
				if (!words.containsKey(h)) words.put(h, new TreeMap<String, Set<Integer>>());
				if (!words.get(h).containsKey(t)) words.get(h).put(t, new TreeSet<Integer>());
				words.get(h).get(t).add(i);
			}
		}

		for (Entry<Integer, Map<String, Set<Integer>>> e1: words.entrySet()) {
			try {

				Statement st = dbc.createStatement();
				String q = String.format("select hash, word, words_before, words_after, words_anywhere, id, data from %s where hash = %d", SQLUtil.escape(table), e1.getKey());
				log.finest("executing query: " + q);
				ResultSet rs = st.executeQuery(q);
				
				while (rs.next()) {
					
					String word = rs.getString("word");
					
					for (Entry<String, Set<Integer>> e2: e1.getValue().entrySet()) {
					
						if (! distance.similar(word, e2.getKey())) continue;
						
						String[] wordsBefore = StringUtil.split(rs.getString("words_before"), wordsSeparator, -1);
						String[] wordsAfter = StringUtil.split(rs.getString("words_after"), wordsSeparator, -1);
//						String[] wordsAnywhere = StringUtil.split(rs.getString("words_anywhere"), wordsSeparator, -1);
						String id = rs.getString("id");
						int wbl = wordsBefore.length, wal = wordsAfter.length/*, wwl = wordsAnywhere.length*/;
						
						for (int pos: e2.getValue()) {
							
							int rank = 0;
							for (String t: tokens[pos]) {
								if (t.toLowerCase(locale).equals(e2.getKey())) {
									rank+=deltaWordExact; // honour exact match!
									break;	// but only once for all possible word forms
								}
							}
							
							boolean matches = true;
							for (int j = 1; j <= wbl; j++) {
								matches = false;
								for (String t: tokens[pos - j]) {
									if (t.toLowerCase(locale).equals(wordsBefore[wbl - j])) {
										rank+=deltaWordExact; // honour exact match!
										matches = true;
										break;	// but only once for all possible word forms
									}
								}
								if (!matches) {
									for (String t: tokens[pos - j]) {
										if (distance.similar(t.toLowerCase(locale), wordsBefore[wbl - j])) {
											rank+=deltaWordSimilar;
											matches = true;
											break;
										}
									}
									if (!matches) break;
								}
							}
							if (!matches) continue;	// preceding words did not match
							
							for (int j = 0; j < wal; j++) {
								matches = false;
								for (String t: tokens[pos + j + 1]) {
									if (t.toLowerCase(locale).equals(e2.getKey())) {
										rank+=deltaWordExact; // honour exact match!
										matches = true;
										break;	// but only once for all possible word forms
									}
								}
								if (!matches) {
									for (String t: tokens[pos - j]) {
										if (distance.similar(t.toLowerCase(locale), e2.getKey())) {
											rank+=deltaWordSimilar;
											matches = true;
											break;
										}
									}
									if (!matches) break;
								}
							}
							if (!matches) continue; // following words did not match
							
							// all tokens matched somehow
							// now temporarily store
							Span span = new Span(pos - wbl, pos + wal);
							
							matches = occur.containsKey(id);
							
							if (matches) {	
								matches = false;
								
								for (Object[] o: occur.get(id)) {
									if (span.overlapsWith((Span) o[0])) {
										((CFS) o[1]).set("rank", ((CFS) o[1]).getInt("rank") + rank + deltaFold);
									}
								}
								
							}
							
							if (!matches) {	// first finding on this position
								
								Properties p = new Properties();
								p.loadFromXML(new ByteArrayInputStream(rs.getBytes("data")));
								CFS d = FSUtil.newCFS();
								for (Entry<Object, Object> de: p.entrySet())
									d.set((String) de.getKey(), de.getValue());
								d.set("rank", d.hasFeatureName("rank") ? d.getInt("rank") + rank : rank);
								
								ArrayList<Object[]> l = new ArrayList<Object[]>();
								l.add(new Object[]{span, d});
								occur.put(id, l);
								
							}
							
						}
						
					}
				}
				
			} catch (Exception e) {
				
			}
		}
		
		return ret.toArray(new FS[ret.size()]);
		
	}
	
	
}
