package eu.wiss_ki.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class RawAAPITest {
	
	public static void main (String[] main) {
		
		try {
			
			BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
			
			String buf = null;
			
			String host = "localhost";
			int port = 23000;
			
			do {
				
				buf = r.readLine();
				if (buf == null) break;
				
				if (buf.startsWith(":")) {
					host = buf.substring(1, buf.indexOf(':', 1));
					port = Integer.parseInt(buf.substring(buf.indexOf(':', 1) + 1));
					continue;
				}
				
				try {
					Socket s = new  Socket(host, port);
					
					System.err.println("send:'" + buf + "'");
					
					s.getOutputStream().write(buf.getBytes("utf-8"));
					s.shutdownOutput();
					BufferedReader i = new BufferedReader(new InputStreamReader(s.getInputStream()));
					int b;
					buf = "";
					while ((b = i.read()) > 0) {
						System.out.print((char) b);
					}
					System.out.println();
					
				} catch (IOException e) {
					System.out.println(e.getLocalizedMessage());
				}
				
			} while (true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.exit(1);
		
	}
	
}
