package nerimd.annotation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum AnnotationType {
	
	TOKEN("POS", "lemma"),
	
	PERSON(),
	GIVEN_NAME("name"),
	SURNAME("name"),
	NAME_LINK("name"),
	GEN_NAME("name"),
	
	SOCIAL_TITLE("name"),
	PROFESSION("name"),
	HUMAN_RELATION("name"),
	
	TIMESPAN(), // ("from", "to"), // to and from are optional
	POINT_IN_TIME("when"),
	
	NAMED_PLACE("name"),
	LOCATION("type", "name"),
	
	TERM("voc"),
	
	OTHER();
	
	public final Set<String> obligatoryAttributes;
	
	private AnnotationType (String... attrs) {
		Set<String> a = new HashSet<String>();
		for (String s: attrs) if (s != null && ! s.contains(" ")) a.add(s);
		this.obligatoryAttributes = Collections.unmodifiableSet(a);
	}
	
}
