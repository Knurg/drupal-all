package eu.wiss_ki.aapi.json;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.wiss_ki.aapi.Configuration;

import de.fau.cs8.mnscholz.util.conn.MiniServer;
import de.fau.cs8.mnscholz.util.conn.MiniServer.ServerWorker;
import de.fau.cs8.mnscholz.util.options.Options;


public class JSONSessionDispatcher implements ServerWorker, JSONSessionConstants {
	
	private final Logger log = Logger.getLogger(this.getClass().getCanonicalName() + "#" + this.hashCode());
	
	private Map<String, JSONSession> sessionPool = new HashMap<String, JSONSession>();
	
	private long maxIdleTime = 10000000; // approx 3h
	private Random rand = new Random();
	
	
	public JSONSessionDispatcher() {
		super();
	}
	
	
	private String createSID () {
		synchronized (sessionPool) {
			String sid;
			do {
				sid = Long.toString(Math.abs(rand.nextLong()));
			} while (sessionPool.containsKey(sid));
			return sid;
		}
	}
	

	public JSONObject dispatch (final JSONObject json) {
		
		try {
		  log.finest("available sessions: " + sessionPool.keySet());

			log.finer("dispatching json object " + json.toString(2));
			
			// contains configuration update
			JSONObject cfg = json.optJSONObject(JSS_KEY_CONFIG_STRUCT);
			if (cfg != null) {
				log.finer("carries config update");
				Configuration.updateConfiguration(jsonToOptions(cfg));
				if (json.length() == 1) return null;
			}
			
			/* ** process event ** */
			
			// get session
			String sid = json.optString(JSS_KEY_SESSION_ID, null);
			
			final JSONSession s;
			if (sid == null || ! sessionPool.containsKey(sid)) {
				
				// determine configuration
				String cfgid = json.optString(JSS_KEY_CONFIG_ID);
				Configuration conf = Configuration.getConfiguration(cfgid);
				if (conf == null) {
					log.warning("unknown configuration id: " + cfgid);
					return JSS_RETURN_UNKNOWN_CONFIG_ID;
				}
				
				if (sid == null) {
					sid = createSID();
					log.info("JSON object " + json + " has no session ID, created new ID " + sid);
				}
				
				s = new JSONSession(sid, conf);
				sessionPool.put(sid, s);
				log.info("created new session with ID " + sid);
				
			} else {
			
				s = sessionPool.get(sid);
				
			}
			
			// process incoming events and fetch outgoing events
//			try {	

				JSONObject o = /*Executors.newFixedThreadPool(1).invokeAny(
						Collections.singleton(new Callable<JSONObject>() {
							public JSONObject call () {
								return*/ s.handleJSONObject(json);/*
							}
						}), 70l, TimeUnit.SECONDS);*/
				if (o != null) o.put(JSS_KEY_SESSION_ID, sid);	// only top event gets sessionid
				return o;
/*
			} catch (Exception e) {
				log.log(Level.WARNING, "aborted event handling", e);
				return null;
			}
*/			
			
		} catch (Exception e) {
			log.log(Level.WARNING, "could not dispatch " + json.toString(), e);
		}
		
		return null;
		
	}

	
	public String work (MiniServer serv, SocketAddress addr, String msg) {
		
		if (msg.startsWith("shutdown")) {
      log.info("Shutting down");
			serv.shutdown();
      System.exit(0);
		}
		
		try {
			JSONObject json = new JSONObject(msg);
			json = dispatch(json);
			killIdleSessions();
			return (json == null) ? "" : json.toString();
		} catch (JSONException e) {
			log.log(Level.WARNING, "bad JSON in message " + msg , e);
		} catch (Exception e) {
			log.log(Level.WARNING, "error processing json object " + msg, e);
		}
		return null;
		
	}
	
	
	private void killIdleSessions () {
		synchronized (sessionPool) {
			Iterator<JSONSession> it = sessionPool.values().iterator();
			while (it.hasNext())
				if (it.next().getIdleTime() > maxIdleTime) it.remove();
		}
	}
	
	
	private Options jsonToOptions (Object json, Options... oa) {
		Options o = (oa != null && oa.length > 1) ? oa[0] : Options.create();
		jsonToOptionsRecur(json, o, true);
		return o;
	}
	
	
	@SuppressWarnings("unchecked")
	private void jsonToOptionsRecur (Object j, Options o, boolean first) {
		
		if (j instanceof JSONObject) {
			
			Iterator<Object> it = ((JSONObject)j).keys();
			while (it.hasNext()) {
				String k = (String) it.next();
				Object v = ((JSONObject)j).opt(k);
				jsonToOptionsRecur(v, o.subset((first ? "" : ".") + k), false);
			}
			
		} else if (j instanceof JSONArray) {
			
			for (int i = ((JSONArray) j).length() - 1; i >= 0; i--) {
				
				Object v = ((JSONArray) j).opt(i);
				jsonToOptionsRecur(v, o.subset((first ? "" : ".") + i), false);
				
			}
			
		} else {
			
			o.set("", (j == null) ? null : j.toString());
			
		}
		
	}
	
}
