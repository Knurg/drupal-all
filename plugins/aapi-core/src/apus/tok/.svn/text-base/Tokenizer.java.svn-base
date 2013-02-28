package apus.tok;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import de.fau.cs8.mnscholz.util.options.Options;

/**Base class for tokenizers. Contains factory methods for tokenizers.
 * 
 * @author simnscho
 *
 */
public abstract class Tokenizer {
	
	public static final int WHITE = 1;
	public static final int BLACK = 2;
	public static final int SIGN = 6;
	public static final int NUMBER = 42;	// + WORD
	public static final int WORD = 10;
	public static final int SEG = 0x80000000;
	
	protected static final Token EOF = new Token(null, 0);
	
	protected Options options;
	
	Tokenizer(Options options) {
		this.options = options;
	}
	
	public abstract String buffer ();

	public Token read () {
		Token t = next();
		return (t == EOF) ? null : t;
	}
	
	protected abstract Token next();
	
	public abstract Tokenizer tokenize (Reader r);
	
	public List<Tokenizer> getTokenizerChain() {
		return getTokenizerChain(new LinkedList<Tokenizer>());	
	}
	
	private List<Tokenizer> getTokenizerChain(List<Tokenizer> chain) {
		
		chain.add(this);
		if (this instanceof PostProcessor) ((PostProcessor) this).tok.getTokenizerChain(chain);
		return chain;
		
	}
	
	
	public Tokenizer tokenize (String s) {
		return tokenize(new StringReader(s));
	}
	
	public Tokenizer tokenize (InputStream in) {
		return tokenize(new InputStreamReader(in));
	}
	
	
	public static Tokenizer tokenize (Options o, Reader r) {
	
		try {
			String[] ta = o.get("tokenizers", BaseTokenizer.class.getCanonicalName()).trim().split("\\s+");
			if (ta == null || ta.length == 0) return null;
			
			Tokenizer tok = (Tokenizer) Class.forName(o.get("tokenizers." + ta[0] + ".class")).
				getConstructor(Reader.class, Options.class).
				newInstance(r, o.subset("tokenizers." + ta[0] + "."));
			
			for (int i = 1; i < ta.length; i++) {
				tok = (PostProcessor) Class.forName(o.get("tokenizers." + ta[i] + ".class")).
				getConstructor(Tokenizer.class, Options.class).
				newInstance(tok, o.subset("tokenizers." + ta[i] + "."));
			}
			
			return tok;
			
		} catch (Exception e) {
			throw new TokenizerException("initializing tokenizer", e);
		}

	}
	
	
	public static Tokenizer protoTokenizer () {
		
		Options o = Options.create();
		o.set("tokenizers", "base  post2 post5  post3 post4");
		o.set("tokenizers.base.class", BaseTokenizer.class.getCanonicalName());
		o.set("tokenizers.post1.class", LCAbbrev.class.getCanonicalName());
		o.set("tokenizers.post2.class", HyphenGlue.class.getCanonicalName());
		o.set("tokenizers.post5.class", DateGlue.class.getCanonicalName());
		o.set("tokenizers.post4.class", SentenceSplitter.class.getCanonicalName());
		o.set("tokenizers.post3.class", ListGlue.class.getCanonicalName());
		o.set("tokenizers.post3.combinedTokensFiles", "fixed_tokens.txt");
		return protoTokenizer(o);
		
	}
	
	
	public static Tokenizer protoTokenizer (Options o) {
		return tokenize(o, (Reader) null);
	}
		
	public static Tokenizer tokenize (Options o, String s) {
		return tokenize(o, new StringReader(s));
	}
		
	public static Tokenizer tokenize (Options o, InputStream in) {
		return tokenize(o, new InputStreamReader(in));
	}
	
	public static void main (String[] args) throws Exception {
		boolean stats = false;
		Tokenizer tok;
		
		if (args == null || args.length == 0) {
			tok = protoTokenizer();
		} else {
			Options o = Options.create();
			for (String a: args) {
				if (a.startsWith("-f")) {
					Properties p = new Properties();
					p.load(new FileReader(a.substring(2)));
					for (Object k: p.keySet()) o.set((String) k, p.getProperty((String) k));
				} else if (a.startsWith("-o")) {
					String[] p = a.substring(2).split("=", 2);
					o.set(p[0], p[1]);
				} else if (a.startsWith("-s")) {
					stats = ! stats;
				}
			}
			tok = protoTokenizer(o);
		}
		
		int i = 0;
		long t0 = System.currentTimeMillis(); 
		//tok = tok.tokenize(System.in);
		tok = tok.tokenize("Hans I Jamnitzer wurde am 12.3.1954 in Brüssel.1552 hat");
		Token t;
		if (stats) System.err.println("reading from stdin");
		while ((t = tok.read()) != null) {
			System.out.println("'" + t.surface + "' (type: " + Integer.toBinaryString(t.type) + ")");
			i++;
		}
		long t1 = System.currentTimeMillis();
		if (stats) System.err.println(i + " tokens in " + (t1 - t0) + "ms");
		
	}
	
}
