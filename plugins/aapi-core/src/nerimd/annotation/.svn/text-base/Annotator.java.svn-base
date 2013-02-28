package nerimd.annotation;

import java.util.Properties;


public abstract class Annotator {
	
	protected Properties properties;
	
	protected Annotator (Properties p) throws AnnotatorInitializationException {
		this.properties = (p == null) ? new Properties() : p;
	}
	
	public abstract void annotate (Text text);
	
}
