package eu.wiss_ki;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import apus.TaggerMorpher;
import de.fau.cs8.mnscholz.util.GetOpt;
import de.fau.cs8.mnscholz.util.conn.MiniServer;
import eu.wiss_ki.aapi.json.JSONSessionDispatcher;



public class AAPI {
	
	public static void main (String args[]) {
		
		GetOpt opts = new GetOpt("p:l:h", args);
		int port = opts.isSet('p') ? Integer.parseInt(opts.get('p')[0]) : 20736;
		String[] logsinks = opts.isSet('l') ? opts.get('l') : null;
		
		if (opts.isSet('h')) {
			System.out.println("AAPI [-h] [-p <port>] (-l [<log level>=]<log file>)*");
			System.out.println("Starts the WissKI Annotation API");
			System.out.println();
			System.out.println("<log file> may be '-' for stderr");
			System.exit(0);
		}
		
		try {
			
			for (Handler h: Logger.getLogger("").getHandlers()) Logger.getLogger("").removeHandler(h);
			Logger.getLogger("").setLevel(Level.ALL);
			
			Handler fh;
			
			if (logsinks != null) {
				for (String logsink: logsinks) {
					String[] ls = logsink.split("=", 2);
					if (ls.length != 2) ls = new String[]{ "ALL" ,logsink};
					Level level = Level.parse(ls[0]);
					
					if ("-".equals(ls[1])) fh = new ConsoleHandler();
					else fh = new FileHandler(ls[1], false);
					fh.setLevel(level);
					Logger.getLogger("").addHandler(fh);
				
				}
			} else {
				fh = new ConsoleHandler();
				fh.setLevel(Level.ALL);
				Logger.getLogger("").addHandler(fh);
			}
			
			(new Thread() {
				@Override
				public void run() {
					// load at start, for it takes a while...
					TaggerMorpher.getStandard("DE");
				}
			}).start();
			
			MiniServer serv = new MiniServer(port, new JSONSessionDispatcher());
			serv.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
