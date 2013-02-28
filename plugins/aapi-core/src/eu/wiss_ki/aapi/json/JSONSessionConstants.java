package eu.wiss_ki.aapi.json;

import java.util.Collections;

import org.json.JSONObject;

public interface JSONSessionConstants {
	
	public static final String JSS_KEY_SESSION_ID = "sid"; 
	
	public static final String JSS_KEY_CONFIG_ID = "configID"; 
	
	public static final String JSS_KEY_CONFIG_STRUCT = "configUpdate";
	
	public static final String JSS_KEY_ERROR = "error";
		
	public static final JSONObject JSS_RETURN_UNKNOWN_CONFIG_ID = new JSONObject(Collections.singletonMap(JSS_KEY_ERROR, JSS_KEY_CONFIG_ID + " unknown"));
	
}
