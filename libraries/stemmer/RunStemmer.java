import stemmer.Stemmer;
import java.io.*;

public class RunStemmer {
  public static void main (String[] a) throws Exception {
    BufferedReader r = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
    PrintWriter w = new PrintWriter(new OutputStreamWriter(System.out, "utf-8"));
    Stemmer s = new Stemmer();
    String l;
    while ((l = r.readLine()) != null) {
      for (String t: l.split("\\s+")) {
        if (t.length() != 0) {
          w.println(s.stem(t));
          w.flush();
        }
      }
    }
  }
}

