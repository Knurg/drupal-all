package eu.wiss_ki.aapi.editingSession;

import lebada.fs.CFS;
import lebada.fs.FS;
import lebada.fs.FSUtil;
import lebada.voc.Vocabulary;

public class Util {
	
	public static Vocabulary getVocByName (Vocabulary[] vocs, String name) {
		
		for (Vocabulary v: vocs) {
			if ((name == null) ? v.getName() == null : name.equals(v.getName())) {
				return v;
			}
		}
		return null;
		
	}
	
	public static FS enrich (Vocabulary[] vocs, FS infos) {
		
		if (infos.hasFeatureName("vocabulary") && infos.hasFeatureName("entryID")) {
			
			CFS cfs = FSUtil.newCFS(infos);
			Vocabulary v = getVocByName(vocs, infos.getString("vocabulary"));
			FS fs = v.lookupTermInfo(infos.getString("entryID"));
			if (fs != null) {
				for (String key: fs.getFeatureNames()) cfs.set(key, fs.get(key));
			}
			return cfs;
			
		}
		
		return infos; 
		
	}
	
}
