package eu.wiss_ki;

import java.util.Random;

import de.fau.cs8.mnscholz.util.StringUtil;
import de.fau.cs8.mnscholz.util.options.Options;

public class IDGenerator {
	
	private byte[] bytes;
	private static Random rand; 
	
	public IDGenerator (Options o) {
		bytes = new byte[Integer.parseInt(o.get("length", "16"))];
		if (rand == null) rand = new Random(); 
	}
	
	public synchronized String createID () {
		rand.nextBytes(bytes);
		return StringUtil.bytesToHexString(bytes);
	}
	
	
	
}
