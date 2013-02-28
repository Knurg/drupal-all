package eu.wiss_ki.aapi.editingSession;

import java.util.List;

import de.fau.cs8.mnscholz.util.Span;
import lebada.fs.FS;
import lebada.fs.FSUtil;

public class Annotation extends TextSpan implements FS {
	
	private static final long serialVersionUID = -781665655106438306L;
	
	public Annotation(int start, int end, String blockID, String id,
			String type, FS infos) {
		super(start, end, blockID);
		this.id = id;
		this.type = type;
		this.infos = FSUtil.newCFS(infos, "aid", id, "type", type);
	}
	
	public Annotation(TextSpan span, String id,
			String type, FS infos) {
		super(span);
		this.id = id;
		this.type = type;
		this.infos = FSUtil.newCFS(infos, "aid", id, "type", type);
	}
	
	public Annotation(Span span, String blockID, String id,
			String type, FS infos) {
		super(span, blockID);
		this.id = id;
		this.type = type;
		this.infos = FSUtil.newCFS(infos, "aid", id, "type", type);
	}
	
	public Annotation(Span span, String blockID, FS infos) {
		super(span, blockID);
		this.id = infos.getString("aid");
		this.type = infos.getString("type");
		if (this.id == null) throw new RuntimeException("no annotation ID: " + infos.toString());
		if (this.type == null) throw new RuntimeException("no type: " + infos.toString());
		this.infos = FSUtil.newCFS(infos, "aid", id, "type", type);
	}
	
	public Annotation(TextSpan span,FS infos) {
		super(span);
		this.id = infos.getString("aid");
		this.type = infos.getString("type");
		if (this.id == null) throw new RuntimeException("no annotation ID: " + infos.toString());
		if (this.type == null) throw new RuntimeException("no type: " + infos.toString());
		this.infos = FSUtil.newCFS(infos, "aid", id, "type", type);
	}
	
	
	
	public final String id;
	
	public final String type;
	
	public final FS infos;
	
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Annotation)) return false;
		return id.equals(((Annotation) obj).id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
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

	public String toString() {
		return AnnoUtil.toString(this);
	}

	public Boolean getBoolean(String key) {
		return infos.getBoolean(key);
	}
	
	
	
}
