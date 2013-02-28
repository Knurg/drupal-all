import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import apus.tok.Token;
import apus.tok.Tokenizer;
import de.fau.cs8.mnscholz.util.ArraysUtil;
import de.fau.cs8.mnscholz.util.LocaleUtil;
import de.fau.cs8.mnscholz.util.options.Options;
import lebada.marker.DateMarkerEN;






public class Test {
	
	public static void main (String[] args) throws Exception {
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, LocaleUtil.parseLocale("en"));
		df.setLenient(true);
		Date d = df.parse("11/3/2001");
System.err.println(DateFormat.getDateInstance(DateFormat.FULL, Locale.US).format(d));
		
		String text = "eleventh nov. 1923 or 12 jan. and 13 february 1924 otherwise 14 mar. 1925 !";
		
		List<Token> tokens = new ArrayList<Token>();
		Tokenizer tok = Tokenizer.protoTokenizer().tokenize(text);
		Token t;
		while ((t = tok.read()) != null) if (t.isType(Tokenizer.BLACK)) tokens.add(t);
		
		Options o = Options.create();
		o.set("locale", "en-US");
		o.set("lenientParsing", "true");
		
		DateMarkerEN dm = new DateMarkerEN(o);
		
		System.out.println(ArraysUtil.toString(dm.markup(tokens.toArray(new Token[0])), "", "", "\n",""));
		
	}
	
	
}
