package lebada.marker;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lebada.fs.CFS;
import lebada.fs.FSUtil;
import de.fau.cs8.mnscholz.util.ArraysUtil;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.EToken;
import apus.tok.Token;

public class TimeMarker extends WindowMarker {
	
	protected class SimpleDateFormat extends Window {
		
		public SimpleDateFormat(Pattern pattern, int year, int month, int day, int yearOffset) {
			super();
			this.pattern = pattern;
			this.year = year;
			this.month = month;
			this.day = day;
			this.yearOffset = yearOffset;
		}

		public SimpleDateFormat(String pattern, int year, int month, int day, int yearOffset) {
			super();
			this.pattern = Pattern.compile(pattern);
			this.year = year;
			this.month = month;
			this.day = day;
			this.yearOffset = yearOffset;
		}
		
		public final Pattern pattern;
		public final int year;
		public final int month;
		public final int day;
		public final int yearOffset;
		
		@Override
		public TermOccurence[] markup(Token[] tokens, int offset) {
			
			Matcher m = pattern.matcher(tokens[offset].surface); 
			if (m.matches()) {
				
				String date = (this.year == 0) ? "xxxx" : String.format("%04d", yearOffset + Integer.parseInt(m.group(this.year)));  
				date += (this.month == 0) ? ((this.day == 0) ? "" : "-xx-") : String.format("-%02d-", Integer.parseInt(m.group(this.month)));
				date += (this.day == 0) ? "" : String.format("%02d", Integer.parseInt(m.group(this.day)));
				
				
				CFS fs = FSUtil.newCFS();
				fs.set("xsdbegin", date);
				fs.set("xsdend", date);
				fs.set("canonical", tokens[offset].surface);
				fs.set("vocabulary", vocalias);
				fs.set("type", termType);
				fs.set("entryID", date);
				fs.set("term", tokens[offset].surface);
				
				return new TermOccurence[]{new TermOccurence(offset, offset + 1, fs)};
				
			} else {
				return null;
			}
				
		}
		
	}
	
	protected class SimpleMonthNameDate extends Window {

		@Override
		public TermOccurence[] markup(Token[] tokens, int offset) {
			
			Integer days = null;
			Integer month = null;
			Integer year = null;
			
			if (tokens[offset].surface.matches("\\d\\d?") &&
				tokens[offset + 1].surface.equals(".")) {
				days = isDay(tokens[offset].surface);
				offset += 2;
			} else if (tokens[offset].surface.matches("\\d\\d?\\.")) {
				days = Integer.parseInt(tokens[offset].surface.substring(0, tokens[offset].surface.length() - 1));
				offset++;
			}
			
			for (int i = 0; i < months.length; i++) {
				if (Arrays.binarySearch(months[i], tokens[offset].surface) >= 0) {
					month = i;
				} else if (tokens[offset] instanceof EToken) {
					for (String l: (String[]) ((EToken) tokens[offset]).info.get("lemmata")) {
						if (Arrays.binarySearch(months[i], l) >= 0) {
							month = i;
							break;
						}
					}
				}
			}
			
			if (month == -1) return null;
			offset++;
			
			if (tokens[offset].surface.matches("-?\\d\\d\\d+")) {
				year = Integer.parseInt(tokens[offset].surface);
			} else if (tokens[offset].surface.matches("-?\\d\\d")) {
				year = Integer.parseInt(tokens[offset].surface);
			} else if (tokens[offset].surface.matches("-?\\d")) {
				year = Integer.parseInt(tokens[offset].surface);
			} else if (tokens[offset].surface.matches("'?\\d\\d")) {
				year = Integer.parseInt(tokens[offset].surface) + 1900;
			}
			 
			
			return null;
		}
		
	}
	
	
	String[][] months = new String[][]{new String[]{"Januar", "Jan."}, new String[]{"Februar", "Feb."}, new String[]{"März", "Mär."}, new String[]{"April", "Apr."}, new String[]{"Mai"}, new String[]{"Juni", "Jun."}, new String[]{"Juli", "Jul."}, new String[]{"August", "Aug."}, new String[]{"September", "Sep."}, new String[]{"Oktober", "Okt."}, new String[]{"November", "Nov."}, new String[]{"Dezember", "Dez."}}; 
	
	Window[] dateformats = new SimpleDateFormat[]{new SimpleDateFormat("(\\d\\d?)\\.(\\d\\d?)\\.(\\d\\d\\d?\\d?)", 3, 2, 1, 0), new SimpleDateFormat("(\\d\\d?)\\.(\\d\\d?)\\.", 0, 2, 1, 0), new SimpleDateFormat("(\\d\\d\\d\\d)\\.(\\d\\d?)\\.(\\d\\d?)", 1, 2, 3, 0), new SimpleDateFormat("(\\d\\d?)\\.(\\d\\d?)\\.(\\d\\d)", 3, 2, 1, 1900)};
	
	String vocalias;
	
	String termType;
	
	public TimeMarker (Options options) {
		super(options);
		
		for (String[] m: months) Arrays.sort(m); 
		
		vocalias = options.get("vocabularyName", "<unknown>");
		termType = options.get("termType", "termType");
		
		windows = ArraysUtil.concat(windows, dateformats);
		
	}
	
	
	public Integer isDay (String day) {
		try {
			int d;
			if (! day.matches("\\d\\d?\\.?")) return null;
			if (day.endsWith(".")) d = Integer.parseInt(day.substring(0, day.length() - 1));
			else d = Integer.parseInt(day);
			return (d > 0 && d < 32) ? d : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public Integer isMonth (String mon) {
		try {
			int m = 0;
			if (mon.matches("\\d\\d?")) m = Integer.parseInt(mon);
			else if (mon.matches("\\d\\d?\\.")) m = Integer.parseInt(mon.substring(0, mon.length() - 1));
			else {
				for (int m1 = 1; m1 > months.length; m1++) {
					for (String mn: months[m1]) {
						if (mn.equalsIgnoreCase(mon)) m = m1;
					}
				}
			}
			// monatsnamen?
			return (m > 0 && m < 13) ? m : null;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
}
