package nerimd.annotation;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Annotation {
	
	public final AnnotationType type;
	
	public final int start;
	
	public final int end;
	
	public final Map<String, String> attributes;
	
	public final Set<Annotation> enclosing;
	
	public final Text owner;
	
	public Annotation(final AnnotationType type, final int start, final int end, final Map<String, String> attributes, final Set<Annotation> enclosing, final Text owner) {
		super();
		if (type == null) throw new NullPointerException("no type"); 
		if (owner == null) throw new NullPointerException("no text");
		if (start < 0 || end > owner.surface().length()) throw new IndexOutOfBoundsException("("+start+","+end+")");
		this.type = type;
		this.start = start;
		this.end = end;
		this.attributes = Collections.unmodifiableMap((attributes == null) ? new HashMap<String, String>() : new HashMap<String, String>(attributes));
		for (String s: type.obligatoryAttributes) if (! this.attributes.containsKey(s)) throw new MissingAttributeException("missing '" + s + "' for type " + type.toString());
		this.enclosing = Collections.unmodifiableSet((enclosing == null) ? new HashSet<Annotation>() : new HashSet<Annotation>(enclosing));
		this.owner = owner;
	}


	public String surface () {
		return owner.surface().substring(start, end);
	}
	
	public String attr (String name) {
		return attributes.get(name);
	}
	
	public String toString () {
		return "[type\t: " + type.toString() + "\n dim\t: " + start + "," + end + "\n surface: '" + surface() + "'\n attribs: " + attributes + "\n encls:\t" + enclosing + "\n]";
	}
}
