package prolog;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 
 * Uses properties:<p>
 * <table border="1">
 * <tr><th>Name<th>Mandatory<th>Default<th>Description</tr>
 * <tr><td>prolog.Prolog.command<td>yes, if no	path given<td>none<td>the pathname including options and argument files</tr>
 * <tr><td>prolog.Prolog.inputCharset<td>no<td>default character set<td>the character set for reading data from prolog; uses default char set if unspecified</tr> 
 * <tr><td>prolog.Prolog.outputCharset<td>no<td>default character set<td>the character set for sending data to prolog; uses default char set if unspecified</tr>
 * </table> 
 * 
 * @author martini
 *
 */
public class Prolog {
	
	private final Process prolog;
	private Reader r;
	private Writer s;
	private Charset inCharset;
	private Charset outCharset;
	private Properties properties;
	
	public Prolog (String path) {
		this(path, null);
	}
	
	public Prolog (Properties props) {
		this(null, props);
	}
	
	public Prolog (String path, Properties props) {
		if (props == null && path == null) throw new NullPointerException("path and properties cannot both be null");
		
		this.properties = (props == null) ? new Properties() : props;
		if (path == null) path = props.getProperty("prolog.Prolog.command");
		
		inCharset = Charset.forName(this.properties.getProperty("prolog.Prolog.inputCharset", Charset.defaultCharset().name()));
		outCharset = Charset.forName(this.properties.getProperty("prolog.Prolog.outputCharset", Charset.defaultCharset().name()));
		
		try {
			this.prolog = Runtime.getRuntime().exec(path);
		} catch (Exception e) {
			throw new PrologException("could not launch prolog", e);
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() { prolog.destroy(); }
		});
		
		this.r = new InputStreamReader(prolog.getInputStream(), inCharset);		// needs extra stating of utf8 charset,
		this.s = new OutputStreamWriter(prolog.getOutputStream(), outCharset);	// otherwise send in ansi format
	}
	
	public void write (String str) {
		try {
			s.write("\n" + str + "\n");
			s.flush();
		} catch (Exception e) {
			throw new PrologException("sending data", e);
		}
	}
	
	/**This method succeeds if no error occured.
	 * Otherwise it throws a PrologException.
	 * 
	 *
	 */
	public void checkErrorStream () {
		
		String err = "";
		try {
			int i = prolog.getErrorStream().available();
			if (i > 0) {
				StringBuffer buf = new StringBuffer();
				for (; i > 0; i--) buf.append((char) prolog.getErrorStream().read());
				err = buf.toString();
			}
		} catch (Exception e) {
			throw new PrologException("reading from prolog error stream", e);
		}
		if (! err.matches("[\\n\\r\\s]*") && ! err.replaceAll("[\\n\\r\\s]", "").matches("(true|yes|\\.)+")) throw new PrologException("prolog error: '" + err + "'");
		
	}
	
	public String read () {
		// first check if errors occured
		checkErrorStream();
		
		try {
			// wait for output
			if (! r.ready()) {
				Thread.sleep(500);
				return read();
			}
			
			// finally read the data
			StringBuffer buf = new StringBuffer();
			for (int c = r.read(); c > 0; c = r.read()) {
				buf.append((char) c);
			}
			return buf.toString();
		} catch (Exception e) {
			throw new RuntimeException("recieving data", e);
		}
	}
	
	public boolean dataAvailable () {
		try {
			return r.ready();
		} catch (Exception e) {
			throw new RuntimeException("reading from prolog input stream", e);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		prolog.destroy();
	}
	
	public void close () {
		write("halt.");
		prolog.destroy();
		
	}
	
}
