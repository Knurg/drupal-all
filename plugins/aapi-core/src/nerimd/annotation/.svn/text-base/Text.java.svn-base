package nerimd.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface Text {
	
	
	public String surface ();
	
	public Annotation addAnnotation (AnnotationType type, int start, int end, Map<String, String> attrs, Set<Annotation> enclosing);
	
	public Annotation[] getAnnotations (AnnotationType... types);
	
	public Annotation[] getAnnotations (int offset, AnnotationType... types);
	
	public Annotation[] getAnnotations (int start, int end, AnnotationType... types);
	
	
}
