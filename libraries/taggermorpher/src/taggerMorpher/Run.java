package taggerMorpher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import de.fau.cs.jill.feature.FeatureName;
import de.fau.cs.jill.feature.FeatureSequence;
import de.fau.cs.jill.feature.FeatureStructure;
import de.ittner.stream.FiniteEnumeration;
import eu.wiss_ki.util.FSUtil;

public class Run {

	
	public static void main (String[] args) throws Exception {
		
//		System.out.println("Hello World!");

		TaggerMorpher tmw = TaggerMorpher.getStandard();
		
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
		String line;
		while ((line = r.readLine()) != null) {
			
      line = line.trim();
			String[] tokens = line.split("\\s+");
			
			FeatureStructure[] infos = tmw.parsePOSFullMorph(tokens);
			
			Writer out = new OutputStreamWriter(System.out, "utf-8");
			out.write("sent([\n");
			for (int i = 0; i < tokens.length; i++) {
				out.write("w('" + tokens[i].replace("\\", "\\\\").replace("'", "\\'") + "', ");
				FSUtil.toGulp(infos[i], out);
				out.flush();
				out.write(")");
//				System.out.print(infos[i].get(FeatureName.forName("pos")).toString() + "\t");
//				
//				FiniteEnumeration e = ((FeatureSequence) infos[i].get(FeatureName.forName("morph"))).elements();
//				while (e.hasMoreElements()) {
//					System.out.print(e.nextElement() + "\t");
//				}
				out.write("\n");
			}
			out.write("]).\n");
			
		}
		
	}
	
}
