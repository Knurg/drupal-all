package eu.wiss_ki;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs8.mnscholz.util.options.Options;

public class IDGeneratorWS extends IDGenerator {

	private URL url;
	private List<String> cache = null; 
	
	public IDGeneratorWS(Options o) {
		super(o);
		String a = o.get("cacheSize", "20");
		String u = o.get("service", null);
		try {
			this.url = (u == null) ? null : new URL(u.replace("{$amount}", a));
		} catch (MalformedURLException e) {
			this.url= null;
		}
		if (url != null) cache = new ArrayList<String>(Integer.parseInt(a));
	}

	@Override
	public synchronized String createID() {
		if (url == null) {
			return super.createID();
		} else if (cache.isEmpty()) {
			
			try {
				
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream(), "utf-8"));
				String id;
				while ((id = r.readLine()) != null) cache.add(id);
				
			} catch (Exception e) {
				return super.createID();
			}
			
		}
		
		return cache.remove(0);
		
	}
	
	
	
	
}
