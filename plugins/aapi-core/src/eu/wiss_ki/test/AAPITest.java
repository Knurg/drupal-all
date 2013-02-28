package eu.wiss_ki.test;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;


public class AAPITest {
	
	public static void main (String[] main) {
		
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		
		String buf = "";
		
		do {
			
			try {
				
				buf = r.readLine();
				if (buf == null) break;
				
				Object o = new JSONObject(buf.toString());
			
				HttpURLConnection c = (HttpURLConnection) (new URL("http://faui8184/devwisski/wisski/editor/conn?json=" + URLEncoder.encode(o.toString(), "utf-8"))).openConnection();
				c.setRequestMethod("POST");
				c.connect();
				
				System.out.println(c.getHeaderFields());
				BufferedReader i = new BufferedReader(new InputStreamReader(c.getInputStream()));
				int b;
				buf = "";
				while ((b = i.read()) > 0) {
					System.out.print((char) b);
				}
				System.out.println();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} while (true);
		
	}
	
}
