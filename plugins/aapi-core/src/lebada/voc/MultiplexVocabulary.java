package lebada.voc;

import java.util.ArrayList;
import java.util.List;

import lebada.fs.FS;

public class MultiplexVocabulary implements Vocabulary {
	
	protected Vocabulary[] vocs;
	
	public MultiplexVocabulary(Vocabulary[] vocs) {
		super();
		this.vocs = vocs;
	}

	@Override
	public String getName() {
		return "__multiplexer__";
	}

/*	@Override
	public FS[] getTermInfo(String term) {
		List<FS> fss = new ArrayList<FS>();
		for (Vocabulary v: vocs)
			for (FS fs: v.getTermInfo(term)) fss.add(fs);
		return fss.toArray(new FS[fss.size()]);
	}*/
	
/*	@Override
	public String[] getTermsStartingWith(String word) {
		List<String> terms = new ArrayList<String>();
		for (Vocabulary v: vocs)
			for (String term: v.getTermsStartingWith(word)) terms.add(term);
		return terms.toArray(new String[terms.size()]);
	}*/
	
/*	@Override
	public String[] getTermsContaining(String word) {
		List<String> terms = new ArrayList<String>();
		for (Vocabulary v: vocs)
			for (String term: v.getTermsContaining(word)) terms.add(term);
		return terms.toArray(new String[terms.size()]);
	}*/
	
	@Override
	public String[] getTypes() {
		List<String> types = new ArrayList<String>();
		for (Vocabulary v: vocs)
			for (String type: v.getTypes()) types.add(type);
		return types.toArray(new String[types.size()]);
	}

	@Override
	public FS lookupTermInfo(String entryID) {
		for (Vocabulary v: vocs) {
			FS info = v.lookupTermInfo(entryID);
			if (info != null) return info;
		}
		return null;
	}

	@Override
	public FS[] lookupTermsContaining(String termPart) {
		List<FS> map = new ArrayList<FS>();
		for (Vocabulary v: vocs) {
			for (FS e: v.lookupTermsContaining(termPart)) {
				map.add(e);
			}
		}
		return map.toArray(new FS[map.size()]);
	}

	@Override
	public FS[] lookupTermsStartingWith(String termStart) {
		List<FS> map = new ArrayList<FS>();
		for (Vocabulary v: vocs) {
			for (FS e: v.lookupTermsStartingWith(termStart)) {
				map.add(e);
			}
		}
		return map.toArray(new FS[map.size()]);
	}

	@Override
	public FS[] lookupTermsExactMatch(String termExact) {
		List<FS> map = new ArrayList<FS>();
		for (Vocabulary v: vocs) {
			for (FS e: v.lookupTermsExactMatch(termExact)) {
				map.add(e);
			}
		}
		return map.toArray(new FS[map.size()]);
	}

	
}
