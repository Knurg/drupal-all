package eu.wiss_ki.aapi.json;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.logging.Logger;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;
import de.fau.cs8.mnscholz.util.URLUtil;

public class ClassesInfoConverter {
	
	public static Logger log = Logger.getLogger(ClassesInfoConverter.class.getCanonicalName());
	
//	private static String[] filter = new String[]{"deleted", "tt", "aid", "hasmore", "new", "exists", "vocabulary", "entryID", "term", "canonical", "xsdbegin", "xsdend"}; 
	
	public static FS decode (String classes[]) {
		
		CFS infos = FSUtil.newCFS();
		
		for (String c: classes) {
			if (c.startsWith("wisski_tt_")) {
				infos.set("type", urldecode(c.substring(10)));
			} else if (c.startsWith("wisski_aid_")) {
				infos.set("aid", c.substring(11));	// no urldecode! this is no problem to date, as neither counter-IDs nor UUIDs contain critical chars
			} else if (c.startsWith("wisski_")) {
				int i = c.indexOf('(');
				if (i == -1) {
					infos.set(c.substring(7), true);
				} else {
					infos.set(c.substring(7, i), urldecode(c.substring(i + 1, c.length() - 1)));
				}
			}
			
		}
		
		if (infos.hasFeatureName("deleted")) {
			for (String key: infos.getFeatureNames()) infos.unset(key);
			infos.set("deleted", true);
			infos.set("type", "");
			infos.set("aid", "");
		}
		
		log.finer("converted classes " + Arrays.toString(classes) + " to " + infos);
		
		return infos;
	}
	
	public static String[] encode (FS infos) {
		
		String[] keys = infos.getFeatureNames();
		String[] classes = new String[keys.length + 1];
		
		for (int i = 0; i < keys.length; i++) {

			String k = keys[i];
			
			if ("type".equals(k)) {
				classes[i] = "wisski_tt_" + urlencode(infos.getString(k));
			} else if ("aid".equals(k)) {
				classes[i] = "wisski_aid_" + infos.getString(k);	// no urlencode! this is no problem to date, as neither counter-IDs nor UUIDs contain critical chars
			} else {
				Object o = infos.get(k);
				if (o instanceof Boolean) {
					if ((Boolean) o) classes[i] = "wisski_" + k;
				} else {
					classes[i] = "wisski_" + k + "(" + urlencode(o.toString()) + ")";
				}
			}
		}
		
		if (infos.hasFeatureName("new")) {
			String[] cl1 = new String[keys.length];
			System.arraycopy(classes, 0, cl1, 0, cl1.length);
			classes = cl1;
		} else {
			StringBuilder buf = new StringBuilder("wisski_display(");
			if (infos.hasFeatureName("matchingTerm")) {
				buf.append(urlencode(infos.getString("matchingTerm")));
			} else {
				buf.append(urlencode(infos.getString("term")));
			}
			buf.append(urlencode(" (")).append(urlencode(infos.getString("vocabulary")));
			String entryID = infos.getString("entryID");
			entryID = entryID.substring(Math.max(entryID.lastIndexOf('/'), entryID.lastIndexOf('#')) + 1);
			buf.append(urlencode(", ID: ")).append(urlencode(entryID)).append(urlencode(")"));
			buf.append(")");
			classes[classes.length - 1] = buf.toString();
		}
		
		log.finer("converted infos " + infos + " to " + Arrays.toString(classes));
		
		return classes;
		
	}
	
	
	private static String urlencode (String str) {
		try {
			return URLUtil.encode(str);
		} catch (UnsupportedEncodingException e) {
			log.severe("THIS SYSTEM DOES NOT SUPPORT UTF-8!!!???");
			return null;
		}
	}
	
	private static String urldecode (String str) {
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.severe("THIS SYSTEM DOES NOT SUPPORT UTF-8!!!???");
			return null;
		}
	}
	
}
