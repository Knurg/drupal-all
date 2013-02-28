package lebada.marker;

import java.util.List;

import lebada.fs.FS;
import de.fau.cs8.mnscholz.util.Span;

public class TermOccurence extends Span implements FS {
	
	private static final long serialVersionUID = -5409870129886739806L;
	
	public final FS infos;

	public TermOccurence(int start, int end, FS infos) {
		super(start, end);
		this.infos = infos;
	}
	
	public String toString () {
		return "{" + super.toString() + " " + infos.toString() + "}";
	}

	public Object get(String key) {
		return infos.get(key);
	}

	public Double getDouble(String key) {
		return infos.getDouble(key);
	}

	public String[] getFeatureNames() {
		return infos.getFeatureNames();
	}

	public FS getFS(String key) {
		return infos.getFS(key);
	}

	public FS[] getFSArray(String key) {
		return infos.getFSArray(key);
	}

	public List<? extends FS> getFSList(String key) {
		return infos.getFSList(key);
	}

	public Integer getInt(String key) {
		return infos.getInt(key);
	}

	public Long getLong(String key) {
		return infos.getLong(key);
	}

	public String getString(String key) {
		return infos.getString(key);
	}

	public boolean hasFeatureName(String key) {
		return infos.hasFeatureName(key);
	}

	public Boolean getBoolean(String key) {
		return infos.getBoolean(key);
	}
	
}
