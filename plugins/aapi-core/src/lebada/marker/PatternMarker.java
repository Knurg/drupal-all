package lebada.marker;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.fau.cs8.mnscholz.util.StringUtil;
import de.fau.cs8.mnscholz.util.options.Options;
import apus.tok.EToken;
import apus.tok.Token;

public class PatternMarker extends Marker {
	
	// Object[][][] is list of and-restrictions on token sequence
	// Object[] is
	// {String feature ("" for token), int type, String/Pattern contraint}
	// type may be 0: regex, 1: equals, 2: startswith, 3: contains, 4: endwith
	private List<Map<List<Map<String, Pattern>>, FS>> patterns;
	
	
	public PatternMarker(Options options) {
		super(options);
		patterns = new ArrayList<Map<List<Map<String, Pattern>>, FS>>();
		loadFromOptions();
System.err.println(patterns);
	}

	
	@Override
	protected TermOccurence[] markup0(Token[] ts0) {
		
		String[] tokens = new String[ts0.length];
		FS[] infos = new FS[ts0.length];
		
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = ts0[i].surface;
			infos[i] = (ts0[i] instanceof EToken) ? ((EToken) ts0[i]).info : FSUtil.EMPTY_FS;
		}
		
		FS[] res = markup(tokens, infos);
		TermOccurence[] ret = new TermOccurence[res.length];
		for (int i = 0; i < res.length; i++) {
			CFS cfs = FSUtil.newCFS(res[i]);
			int s = cfs.getInt("start"), e = cfs.getInt("end"); 
			cfs.unset("start");
			cfs.unset("end");
			ret[i] = new TermOccurence(s, e, cfs);
		}
		
		return ret;
	}
	
	
	public FS[] markup (String tokens[], FS[] infos) {
		
		List<FS> ret = new ArrayList<FS>();
		
		for (int ti = 0; ti < tokens.length; ti++) {
			for (int pi = patterns.size() - 1; pi >= 0; pi--) {
				for (Entry<List<Map<String, Pattern>>, FS> e: patterns.get(pi).entrySet()) {
					
					if (e.getKey().size() > tokens.length - ti) continue;
					
					int pti = ti;
					for (Map<String, Pattern> cs: e.getKey()) {
						for (Entry<String, Pattern> c: cs.entrySet()) {
							
							if (! (	// match surface or a feature; feature must exists for match to be successful
									(c.getKey().equals("") && c.getValue().matcher(tokens[pti]).matches())
									|| (infos[pti].hasFeatureName(c.getKey())
										&& c.getValue().matcher(infos[pti].get(c.getKey()).toString()).matches()))) {
								pti = -1;
								break;
							}
						}
						
						if (pti == -1) break;
						pti++;
						
					}
					
					if (pti == -1) {
						break;
					} else {
						ret.add(FSUtil.newFS(e.getValue(), "start", ti, "end", pti, "term", StringUtil.join(" ", Arrays.copyOfRange(tokens, ti, pti))));
					}
					
				}
			}
			
		}
		
		return ret.toArray(new FS[ret.size()]);
		
	}
	
	
	private void loadFromOptions() {
	
		Options o = options.subset("patterns");
		
		for (String k: o.getKeys()) loadXML(new ByteArrayInputStream(o.get(k).getBytes())); 
		
	}
	
	
	private void loadXML(InputStream is) {
	
		try {
			Document d = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			NodeList nl = d.getElementsByTagName("token");
			
			List<Map<String, Pattern>> l = new ArrayList<Map<String,Pattern>>(nl.getLength());
			for (int i = 0; i < nl.getLength(); i++) {
				NodeList nl2 = ((Element) nl.item(i)).getElementsByTagName("regex");
				Map<String, Pattern> m = new TreeMap<String, Pattern>();
				l.add(m);
				for (int j = 0; j < nl2.getLength(); j++) 
					m.put(((Element) nl2.item(j)).getAttribute("feature"),
							Pattern.compile(((Element) nl2.item(j)).getTextContent()));
			}
			
			nl = ((Element) d.getElementsByTagName("features").item(0)).getElementsByTagName("feature");
			CFS cfs = FSUtil.newCFS();
			for (int i = 0; i < nl.getLength(); i++)
				cfs.set(((Element) nl.item(i)).getAttribute("name"),
						((Element) nl.item(i)).getTextContent());
			
			patterns.add(Collections.singletonMap(l, FSUtil.newFS(cfs))); 
			
		} catch (Exception e) {
			throw new RuntimeException("exception while loading patterns", e);
		}
		
	}
	

}
