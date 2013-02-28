package lebada.marker;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lebada.fs.FSUtil;
import apus.tok.Token;
import de.fau.cs8.mnscholz.util.LocaleUtil;
import de.fau.cs8.mnscholz.util.options.Options;

public class DateMarkerEN extends Marker {
	
	
	@SuppressWarnings("unchecked")
	private List<List<String>> monthNames = Arrays.asList(
			Arrays.asList("january", "jan.", "jan"),
			Arrays.asList("february", "feb.", "feb"),
			Arrays.asList("march", "mar.", "mar"),
			Arrays.asList("april", "apr.", "apr"),
			Arrays.asList("may"),
			Arrays.asList("june", "jun.", "jun"),
			Arrays.asList("july", "jul.", "jul"),
			Arrays.asList("august", "aug.", "aug"),
			Arrays.asList("september", "sep.", "sep"),
			Arrays.asList("october", "oct.", "oct"),
			Arrays.asList("november", "nov.", "nov"),
			Arrays.asList("december", "dec.", "dec")
//			Arrays.asList("Januar", "Jan."),
//			Arrays.asList("Februar", "Feb."),
//			Arrays.asList("März", "Mär"),
//			Arrays.asList("April", "Apr."),
//			Arrays.asList("Mai"),
//			Arrays.asList("Juni", "Jun."),
//			Arrays.asList("Juli", "Jul."),
//			Arrays.asList("August", "Aug."),
//			Arrays.asList("September", "Sep."),
//			Arrays.asList("Oktober", "Okt."),
//			Arrays.asList("November", "Nov."),
//			Arrays.asList("Dezember", "Dez.")
	);
	
	@SuppressWarnings("unchecked")
	private List<List<String>> ordinals = Arrays.asList(
			Arrays.asList("first", "1st"), 
			Arrays.asList("second", "2nd"), 
			Arrays.asList("third", "3rd"), 
			Arrays.asList("fourth"), 
			Arrays.asList("fifth"), 
			Arrays.asList("sixth"), 
			Arrays.asList("seventh"), 
			Arrays.asList("eighth"), 
			Arrays.asList("ninth"), 
			Arrays.asList("tenth"), 
			Arrays.asList("eleventh"), 
			Arrays.asList("twelfth"), 
			Arrays.asList("thirteenth"),
			Arrays.asList("fourteenth"),
			Arrays.asList("fifteenth"),
			Arrays.asList("sixteenth"),
			Arrays.asList("seventeenth"),
			Arrays.asList("eighteenth"),
			Arrays.asList("nineteenth"),
			Arrays.asList("twentieth"),
			Arrays.asList("twentyfirst")
	);
	
	
	@SuppressWarnings("unchecked")
	private List<List<String>> weekdayNames = Arrays.asList(
			Arrays.asList("monday", "mondays"),	
			Arrays.asList("tuesday", "tuesdays"),	
			Arrays.asList("wednesday", "wednesdays"),	
			Arrays.asList("thursday", "thursdays"),	
			Arrays.asList("friday", "fridays"),	
			Arrays.asList("saturday", "saturdays"),	
			Arrays.asList("sunday", "sundays")
	); 
	
	
	private DateFormat[] parsers;
	
	private char[] noblank = new char[]{'.', ',', ';', ':','/'};

	private String type;
	private String vocab;
	
	
	public DateMarkerEN(Options options) {
		super(options);
		Locale l = options.exists("locale") ? LocaleUtil.parseLocale(options.get("locale")) : null;
		if (l == null) l = Locale.ENGLISH;
		this.parsers = new DateFormat[5];
		this.parsers[4] = DateFormat.getDateInstance(DateFormat.FULL, l);
		this.parsers[4].setLenient("true".equalsIgnoreCase(options.get("lenientParsing", "true")));
		this.parsers[3] = DateFormat.getDateInstance(DateFormat.LONG, l);
		this.parsers[3].setLenient("true".equalsIgnoreCase(options.get("lenientParsing", "true")));
		this.parsers[2] = DateFormat.getDateInstance(DateFormat.MEDIUM, l);
		this.parsers[2].setLenient("true".equalsIgnoreCase(options.get("lenientParsing", "true")));
		this.parsers[1] = DateFormat.getDateInstance(DateFormat.DEFAULT, l);
		this.parsers[1].setLenient("true".equalsIgnoreCase(options.get("lenientParsing", "true")));
		this.parsers[0] = DateFormat.getDateInstance(DateFormat.SHORT, l);
		this.parsers[0].setLenient("true".equalsIgnoreCase(options.get("lenientParsing", "true")));
		Arrays.sort(this.noblank);
		
		type = options.get("termInfos.type", "Time");
		vocab = options.get("termInfos.vocabulary", "");

	}

	@Override
	protected TermOccurence[] markup0(Token[] tokens) {

		ArrayList<TermOccurence> ret = new ArrayList<TermOccurence>();

		
		// look for some self-defined formats
		for (int pos = 0; pos < tokens.length; pos++) {

			Token cur = tokens[pos];
			Token next = (pos + 1 < tokens.length) ? tokens[pos + 1] : null;
			Token next2 = (pos + 2 < tokens.length) ? tokens[pos + 2] : null;
			
			int d;
			int m;
			int y;
			
			if ((d = parseDayOfWeek(cur.surface.toLowerCase())) != -1) {
				String xsd = "xxxx-xx-xx";
				ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", xsd, "xsdend", xsd, "canonical", weekdayNames.get(d - 1).get(0), "term", cur.surface)));
			}
			
			if (cur.surface.toLowerCase().equals("today")) {
				Calendar c = Calendar.getInstance();
				d = c.get(Calendar.DAY_OF_MONTH);
				m = c.get(Calendar.MONTH) + 1;
				y = c.get(Calendar.YEAR);
				String xsd = String.format("%d-%02d-%02d", y, m, d);
				ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", xsd, "xsdend", xsd, "canonical", xsd, "term", cur.surface)));
			}

			if (cur.surface.toLowerCase().equals("yesterday")) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, -1);
				d = c.get(Calendar.DAY_OF_MONTH);
				m = c.get(Calendar.MONTH) + 1;
				y = c.get(Calendar.YEAR);
				String xsd = String.format("%d-%02d-%02d", y, m, d);
				ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", xsd, "xsdend", xsd, "canonical", xsd, "term", cur.surface)));
			}

			if (cur.surface.toLowerCase().equals("tomorrow")) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, 1);
				d = c.get(Calendar.DAY_OF_MONTH);
				m = c.get(Calendar.MONTH) + 1;
				y = c.get(Calendar.YEAR);
				String xsd = String.format("%d-%02d-%02d", y, m, d);
				ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", xsd, "xsdend", xsd, "canonical", xsd, "term", cur.surface)));
			}

			if (cur.surface.matches("\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d")) {	// YYYY-MM-DD	= ISO 8601

				ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", cur.surface, "xsdend", cur.surface, "canonical", cur.surface, "term", cur.surface)));

			}

			if (cur.surface.matches("\\d{1,2}\\.\\d{1,2}\\.('?\\d{2}|\\d{4})")) { // dd.mm.yyYY 

				String[] t = cur.surface.split("\\.");
				d = Integer.parseInt(t[0]);
				m = Integer.parseInt(t[1]);
				y = Integer.parseInt(cur.surface.length() == 3 ? t[2].substring(1) : t[2]);
				
				if (d < 32 && m < 13) {
					if (t[0].length() == 1) t[0] = "0" + t[0];
					if (t[1].length() == 1) t[1] = "0" + t[1];
					if (t[2].length() == 2) t[2] = ((y < 30) ? "20" : "19") + t[2];
					String xsd = t[2] + "-" + t[1] + "-" + t[0];
					ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", xsd, "xsdend", xsd, "canonical", xsd, "term", cur.surface)));
				}

			}

			if (cur.surface.matches("\\d{1,2}/\\d{1,2}/('?\\d{2}|\\d{4})")) { // mm/dd/yyYY 

				String[] t = cur.surface.split("/");
				m = Integer.parseInt(t[0]);
				d = Integer.parseInt(t[1]);
				y = Integer.parseInt(cur.surface.length() == 3 ? t[2].substring(1) : t[2]);
				
				if (d < 32 && m < 13) {
					if (t[0].length() == 1) t[0] = "0" + t[0];
					if (t[1].length() == 1) t[1] = "0" + t[1];
					if (t[2].length() == 2) t[2] = ((y < 30) ? "20" : "19") + t[2];
					String xsd = t[2] + "-" + t[1] + "-" + t[0];
					ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", xsd, "xsdend", xsd, "canonical", xsd, "term", cur.surface)));
				}

			}

    
      if (cur.surface.matches("-?\\d{3,}|'?\\d{2}")) {	// -?yyYY
				
				y = Integer.parseInt(cur.surface.startsWith("'") ? cur.surface.substring(1) : cur.surface);
				
				String from = y + "-01-01";
				String to = y + "-12-31";

				ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from + " - " + to, "term", cur.surface)));

			}
			
			if (next != null
					&& cur.surface.matches("\\d{3,}|'?\\d{2}")
					&& next.surface.matches("BCE?")) {	// yyYY BCE
				
				y = -Integer.parseInt(cur.surface.startsWith("'") ? cur.surface.substring(1) : cur.surface);
				
				String from = y + "-01-01";
				String to = y + "-12-31";

				ret.add(new TermOccurence(pos, pos + 2, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from + " - " + to, "term", cur.surface + " BCE")));

			}
		
			if (cur.surface.matches("-?\\d{3,}/(\\d{3,}|\\d{2})")) {	// -?YYYY/yyYY

				String[] t = cur.surface.split("/");
				int j1 = Integer.parseInt(t[0]);
				int j2 = Integer.parseInt(t[1]);
				if (j1 < 0) {
					if (j2 < 100) j2 -= (j1 / 100) * 100;
					else j2 = -j2;
				} else if (j2 < 100) j2 += (j1 / 100) * 100;
				
				String from = j1 + "-01-01";
				String to = j2 + "-12-31";

				ret.add(new TermOccurence(pos, pos + 1, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from + " - " + to, "term", cur.surface)));
				
			}
			
			
			if (next != null
				&& cur.surface.matches("\\d{3,}/(\\d{3,}|\\d{2})")
				&& next.surface.matches("BCE?")) {	// YYYY/yyYY BCE

				String[] t = cur.surface.split("/");
				int j1 = -Integer.parseInt(t[0]);
				int j2 = -Integer.parseInt(t[1]);
				if (j2 < 100) j2 += (j1 / 100) * 100;
				
				String from = j1 + "-01-01";
				String to = j2 + "-12-31";

				ret.add(new TermOccurence(pos, pos + 2, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from + " - " + to, "term", cur.surface + " BCE")));
				
			}
			
			if (next != null &&
				cur.surface.matches("-?\\d{3,}")
				&& next.surface.equals("/")
				&& next2.surface.matches("\\d{3,}|\\d{2}")) {	// -?YYYY/yyYY (BCE)?
				
				int j1 = Integer.parseInt(cur.surface);
				int j2 = Integer.parseInt(next2.surface);
				int p = 3;
				
				if (j1 > 0 && tokens.length > pos + 3 && tokens[pos + 3].surface.matches("BCE?")) {
					j1 = -j1;
					p++;
				}
				if (j1 < 0) {
					if (j2 < 100) j2 -= (j1 / 100) * 100;
					else j2 = -j2;
				} else if (j2 < 100) j2 += (j1 / 100) * 100;
				
				String from = j1 + "-01-01";
				String to = j2 + "-12-31";

				ret.add(new TermOccurence(pos, pos + p, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from + " - " + to, "term", cur.surface + "/" + next2.surface + ((p == 4) ? " BCE" : ""))));
				
			}
		
			
			if ((y = parseOrdinal(cur.surface)) != -1) {	// century
				
				if (next != null && ".".equals(next.surface)) next = next2;	// separate dot?
				if (next != null && next.surface.toLowerCase().matches("cent(ury)?")) {
					
					y = (y - 1) * 100;
	
					String from = y + "-01-01";
					String to = (y + 99) + "-12-31";
	
					ret.add(new TermOccurence(pos, pos + ((next == next2) ? 3 : 2), FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from + " - " + to, "term", cur.surface + ". " + next2.surface)));
					
				}
			}

			if ((d = parseOrdinal(cur.surface)) != -1	// dd Mon yyYY
					&& next != null && (m = parseMonth(next.surface.toLowerCase())) != -1
					&& next2 != null && next2.surface.matches("'?\\d{2}|\\d{3,}")) {
				
				y = Integer.parseInt(next2.surface.startsWith("'") ? next2.surface.substring(1) : next2.surface);
				if (y < 30) y += 2000;
				else if (y < 100) y += 1900;
				
				String from = String.format("%d-%02d-%02d", y, m, d);
				String to = String.format("%d-%02d-%02d", y, m, d);
				
				ret.add(new TermOccurence(pos, pos + 3, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from, "term", cur.surface + " " + next.surface + " " + next2.surface)));
				
			}
			
			if (tokens.length > pos + 3
					&& (d = parseOrdinal(cur.surface)) != -1	// dd Mon. yyYY
					&& next2.surface.equals(".")
					&& (m = parseMonth(next.surface.toLowerCase() + ".")) != -1
					&& tokens[pos + 3].surface.matches("'?\\d{2}|\\d{3,}")) {
				
				y = Integer.parseInt(tokens[pos + 3].surface.startsWith("'") ? tokens[pos + 3].surface.substring(1) : tokens[pos + 3].surface);
				if (y < 30) y += 2000;
				else if (y < 100) y += 1900;
				
				String from = String.format("%d-%02d-%02d", y, m, d);
				String to = String.format("%d-%02d-%02d", y, m, d);
				
				ret.add(new TermOccurence(pos, pos + 4, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from, "term", cur.surface + " " + next.surface + ". " + tokens[pos + 3].surface)));
				
			}
			
			if ((d = parseOrdinal(cur.surface)) != -1	// dd Mon
					&& next != null && (m = parseMonth(next.surface.toLowerCase())) != -1) {
				
				String from = String.format("xxxx-%02d-%02d", m, d);
				String to = String.format("xxxx-%02d-%02d", m, d);
				
				ret.add(new TermOccurence(pos, pos + 2, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from, "term", cur.surface + " " + next.surface)));
				
			}
			
			if ((d = parseOrdinal(cur.surface)) != -1	// dd Mon
				&& next2 != null && next2.surface.equals(".")
				&& (m = parseMonth(next.surface.toLowerCase() + ".")) != -1) {
				
				String from = String.format("xxxx-%02d-%02d", m, d);
				String to = String.format("xxxx-%02d-%02d", m, d);
				
				ret.add(new TermOccurence(pos, pos + 3, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from, "term", cur.surface + " " + next.surface + ".")));
				
			}
			
			if ((m = parseMonth(cur.surface.toLowerCase())) != -1  // Mon yyYY
				&& next != null && next.surface.matches("'?\\d{2}|\\d{4}")) {
				
				y = Integer.parseInt(next.surface.length() == 3 ? next.surface.substring(1) : next.surface);
				if (y < 30) y += 2000;
				else if (y < 100) y += 1900;
				
				String from = String.format("%d-%02d", y, m);
				String to = String.format("%d-%02d", y, m);
				
				ret.add(new TermOccurence(pos, pos + 2, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from, "term", cur.surface + " " + next.surface)));
				
			}
			
			if (next2 != null && next.surface.equals(".")
				&& (m = parseMonth(cur.surface.toLowerCase() + ".")) != -1  // Mon yyYY
				&& next2.surface.matches("'?\\d{2}|\\d{3,}")) {
					
					y = Integer.parseInt(next2.surface.length() == 3 ? next2.surface.substring(1) : next2.surface);
					if (y < 30) y += 2000;
					else if (y < 100) y += 1900;
					
					String from = String.format("%d-%02d-01", y, m);
					String to = String.format("%d-%02d-31", y, m);
					
					ret.add(new TermOccurence(pos, pos + 3, FSUtil.newFS("xsdstart", from, "xsdend", to, "canonical", from, "term", cur.surface + ". " + next2.surface)));
					
				}
				
		}
		
		// look for DateFormat-parsible substrings
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			buf.setLength(0);
			int end = -1;
			Date date = null;
			String dateString = null;
			for (int j = i; j < tokens.length; j++) {
				if (buf.length() != 0 && Arrays.binarySearch(noblank, tokens[j].surface.charAt(0)) < 0) buf.append(' ');
				buf.append(tokens[j].surface);
				for (int k = 0; k < parsers.length; k++) {
//System.err.print("parser " + k + ": " + buf);
					try {
						Date d = parsers[k].parse(buf.toString());
						if (date == null || !date.equals(d)) {
							end = j + 1;
							date = d;
							dateString = buf.toString();
//System.err.println(" ---> success");						
						} else {
//System.err.println(" ---> redundant");	
						}
					} catch (ParseException e) {
//System.err.println(" ---> fail");						
					}
				}
			}
			if (end != -1) {
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				String xsd = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				ret.add(new TermOccurence(i, end, FSUtil.newFS("xsdstart", xsd, "xsdend", xsd, "canonical", xsd, "term", dateString)));
			}
		}
    
    ArrayList<TermOccurence> ret2 = new ArrayList<TermOccurence>();
    for (TermOccurence to: ret) {
      ret2.add(new TermOccurence(to.start, to.end, FSUtil.newFS(to.infos, "type", type, "vocabulary", vocab))); 
    }
		
		// return all found occurences
		return ret2.toArray(new TermOccurence[ret.size()]);
		
	}
	
	private int parseOrdinal(String str) {
		if (str.matches("\\d+")) return Integer.parseInt(str);
		if (str.matches("\\d+th")) return Integer.parseInt(str.substring(0, str.length() - 2));
		if (str.matches("\\d+.")) return Integer.parseInt(str.substring(0, str.length() - 1));
		for (int i = 0; i < ordinals.size(); i++) {
			if (ordinals.get(i).contains(str)) return i + 1;
		}
		return -1;
	}
	
	private int parseMonth(String str) {
		for (int i = 0; i < monthNames.size(); i++) {
			if (monthNames.get(i).contains(str)) return i + 1;
		}
		return -1;
	}
	
	private int parseDayOfWeek(String str) {
		for (int i = 0; i < weekdayNames.size(); i++) {
			if (weekdayNames.get(i).contains(str)) return i + 1;
		}
		return -1;
	}

}
