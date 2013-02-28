package nerimd.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DefaultText implements Text {
	
	protected final String surface;
	protected final List<Annotation> annotations;
	
	
	public DefaultText(final String surface) {
		super();
		this.surface = surface;
		this.annotations = new ArrayList<Annotation>();
	}

	public String surface () {
		return surface;
	}
	
	public Annotation addAnnotation (AnnotationType type, int start, int end, Map<String, String> attrs, Set<Annotation> enclosing) {
		if (type == null) throw new NullPointerException();
		
		Annotation a = new Annotation(type, start, end, attrs, enclosing, this);
		annotations.add(a);
		
		return a;
	}
	
	public Annotation[] getAnnotations (AnnotationType... types) {
		if (types == null || types.length == 0) return annotations.toArray(new Annotation[annotations.size()]);
		
		ArrayList<Annotation> l = new ArrayList<Annotation>();
		
		for (Annotation a: annotations)
			for (AnnotationType t: types)
				if (a.type == t) l.add(a);
		
		return l.toArray(new Annotation[l.size()]);
		
	}
	
	public Annotation[] getAnnotations (int offset, AnnotationType... types) {
		if (types == null || types.length == 0) return annotations.toArray(new Annotation[annotations.size()]);
		
		ArrayList<Annotation> l = new ArrayList<Annotation>();
		
		for (Annotation a: annotations)
			for (AnnotationType t: types)
				if (a.type == t && a.start <= offset && a.end > offset) l.add(a);
		
		return l.toArray(new Annotation[l.size()]);
		
	}
	
	public Annotation[] getAnnotations (int start, int end, AnnotationType... types) {
		if (types == null || types.length == 0) return annotations.toArray(new Annotation[annotations.size()]);
		
		ArrayList<Annotation> l = new ArrayList<Annotation>();
		
		for (Annotation a: annotations)
			for (AnnotationType t: types)
				if (a.type == t && a.start >= start && a.end <= end) l.add(a);
		
		return l.toArray(new Annotation[l.size()]);
		
	}
	
	
}
