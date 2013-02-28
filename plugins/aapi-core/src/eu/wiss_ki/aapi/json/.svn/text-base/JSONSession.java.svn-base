package eu.wiss_ki.aapi.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import lebada.fs.FS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.wiss_ki.aapi.Configuration;
import eu.wiss_ki.aapi.editingSession.Annotation;
import eu.wiss_ki.aapi.editingSession.EditingSession;

import de.fau.cs8.mnscholz.util.Span;
import de.fau.cs8.mnscholz.util.StringUtil;
import de.fau.cs8.mnscholz.util.collection.CollectionUtil;

public class JSONSession implements JSONEventConstants {
	
	private static final Map<String, String> EVENT_TO_METHOD_MAP = CollectionUtil.fillMap(new HashMap<String, String>(),
			JSON_EVENT_TYPE_LIST, "handleListEvent", 
			JSON_EVENT_TYPE_SEQ, "handleEventSeq",
			JSON_EVENT_TYPE_TEXT, "handleTextEvent",
			JSON_EVENT_TYPE_NULL, "handleNulEvent",
			JSON_EVENT_TYPE_ANNO, "handleAnnoEvent",
			JSON_EVENT_TYPE_DELETE_BLOCK, "handleBlockDeletionEvent",
			JSON_EVENT_TYPE_INIT, "handleInitEvent"
	);
	
	private final Logger log;  
	private final EditingSession es;
	private long lastEventTime;
	private boolean inited = false;
	private final String sessid;
	private final Configuration config;
	
	public JSONSession (String sessid, Configuration conf) {
		es = new EditingSession(conf);
		log = Logger.getLogger(this.getClass().getCanonicalName() + "@" + sessid);
		lastEventTime = System.currentTimeMillis();
		this.sessid = sessid;
		this.config = conf;
	}
	
	
	
	public JSONObject handleJSONObject (JSONObject event) {
		log.finest("blocks before event handling: " + Arrays.toString(es.getBlockIDs()));	
		lastEventTime = System.currentTimeMillis();
		JSONObject ret = makeReturnEvents(_handleEvent(event));
		lastEventTime = System.currentTimeMillis();
		log.finest("blocks after event handling: " + Arrays.toString(es.getBlockIDs()));	
		return ret;
	}
	
	
	private JSONObject[] _handleEvent (JSONObject event) {
		JSONObject[] ret = null;
		String type = event.optString(JSON_EVENT_KEY_TYPE, null);
		String ms = EVENT_TO_METHOD_MAP.get(type);
		if (ms == null) return eventStructureErrorEvent("No such event type", null, event);
		log.fine("handling event " + event);
		log.finer("event type " + type + ": calling handler " + ms);
		
		try {
			Method m = this.getClass().getMethod(ms,JSONObject.class);
			ret = (JSONObject[]) m.invoke(this, event);
		} catch (NoSuchMethodException e) {
			log.log(Level.SEVERE, "Corrupt handler function map!", e);
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "unexpected exception", e.getCause());
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, "Should not have happened!", e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, "Should not have happened!", e);
		} catch (ClassCastException e) {
			log.log(Level.SEVERE, "Should not have happened!", e);
		}
		
		return ret;
	}
	
	
	public JSONObject[] handleEventSeq (JSONObject event) {
		
		JSONArray a = event.optJSONArray(JSON_EVENT_SEQ);
		List<JSONObject> ret = new ArrayList<JSONObject>();
		if (a == null) {
			log.info("empty event seq");
			return null;
		}
		for (int i = 0; i < a.length(); i++) {
			JSONObject[] oa = _handleEvent(a.optJSONObject(i));
			if (oa != null) for (JSONObject o: oa) if (o != null) ret.add(o);
		}
		
		return ret.toArray(new JSONObject[ret.size()]);		
	}
	
	
	public JSONObject[] handleTextEvent (JSONObject event) {
		
		String bid = event.optString(JSON_EVENT_KEY_BLOCK_ID, null);
		if (bid == null) {
			return eventStructureErrorEvent("No block id for text event", null, event);
		}
		
		int rid = event.optInt(JSON_EVENT_KEY_REVISION_ID, -1);
		if (rid == -1) {
			return eventStructureErrorEvent("No revision for text event", null, event);
			}
		String text = event.optString(JSON_EVENT_KEY_TEXT, null);
		if (text == null) {
			return eventStructureErrorEvent("No text for text event", null, event);
		}
		
		if (! es.hasBlock(bid)) es.createBlock(bid);
		
		es.append(bid, rid, text);
		return null;
		
	}
	
	public JSONObject[] handleListEvent (JSONObject event) {
		
		List<Annotation> annos = new ArrayList<Annotation>();
		
		JSONArray ja = event.optJSONArray(JSON_EVENT_KEY_SELECTION_RANGE);
		int selstart = (ja == null) ? 0 : ja.optInt(0, 0);
		int selend = (ja == null) ? 100 : ja.optInt(1, 100);
		
		String aid = event.optString(JSON_EVENT_KEY_ANNO_ID, null);
		String bid = event.optString(JSON_EVENT_KEY_BLOCK_ID, null);
		Span span = null;
		
		boolean ordered = event.optBoolean(JSON_EVENT_KEY_ORDERED, false);
		
		String[][] termtypes = null;
		final String tts = event.optString(JSON_EVENT_KEY_TERMTYPE, "").trim();
		String[] tta = tts.split("\\s+");
		if (tta == null || tta.length == 0 || (tta.length == 1 && tta[0].equals(""))) {
			tta = config.termTypes; 
		}

		if (ordered) { 
			termtypes = new String[tta.length][1];
			for (int i = 0; i < tta.length; i++) termtypes[i][0] = tta[i];
		} else {
			termtypes = new String[1][];
			termtypes[0] = tta;
		}
		
		
		for (String[] tt: termtypes) {
			
			if (aid != null) {	// anno id alone is sufficient
				
				annos.addAll(es.getAlternateAnnotations(aid, tt, selstart, selend + 2));
				
			} else {
			
				if (bid == null) return eventStructureErrorEvent("Neither annotation ID nor block ID given", null, event);
				if (! es.hasBlock(bid)) return eventStructureErrorEvent("No block with ID " + bid, null, event);
				
				ja = event.optJSONArray(JSON_EVENT_KEY_RANGE);
				if (ja == null || ja.length() != 2) return eventStructureErrorEvent("Bad range", null, event);
				span = new Span(ja.optInt(0, -1), ja.optInt(1, -1));
				if (span.start == -1 || span.end == -1) return eventStructureErrorEvent("Bad range", null, event);
				
				annos.addAll(es.proposeAnnotations(bid, span, tt, selstart, selend + 2));
				
			}
			
		}
		
		boolean hasmore = false;
		if (annos.size() > selend - selstart + 1) {
			// list one bigger than required => has more elements!
//			Annotation a = annos.get(annos.size() - 2);
//			annos.set(annos.size() - 2, new Annotation(a, FSUtil.newFS(a, JSON_EVENT_CLASS_HAS_MORE, true)));
			annos = annos.subList(0, annos.size() - 1);
			hasmore = true;
		}
		
		
		
		List<JSONObject> ret = new ArrayList<JSONObject>(annos.size());
		for (Annotation a: annos) ret.add(annoToEvent(a));
		
		JSONObject list  = new JSONObject();
		try {
			list.put(JSON_EVENT_KEY_TYPE, JSON_EVENT_TYPE_LIST);
			list.put(JSON_EVENT_SEQ, ret);
			list.put(JSON_EVENT_CLASS_HAS_MORE, hasmore);
			if (event.has(JSON_EVENT_KEY_SELECTION_RANGE)) {
				list.put(JSON_EVENT_KEY_SELECTION_RANGE, event.opt(JSON_EVENT_KEY_SELECTION_RANGE));
			}
			if (aid != null) {
				list.put(JSON_EVENT_KEY_ANNO_ID, aid);
				if (bid != null) list.put(JSON_EVENT_KEY_BLOCK_ID, bid);
			} else {
				list.put(JSON_EVENT_KEY_BLOCK_ID, bid);
				list.append(JSON_EVENT_KEY_RANGE, span.start);
				list.append(JSON_EVENT_KEY_RANGE, span.end);
			}
			list.put(JSON_EVENT_KEY_TERMTYPE, tts);
		} catch (JSONException e) {
			log.log(Level.SEVERE, "Should not have happened!", e);
		}
		
		return new JSONObject[]{list};
		
	}
	
	
	public JSONObject[] handleBlockDeletionEvent (JSONObject event) {
		
		String bid = event.optString(JSON_EVENT_KEY_BLOCK_ID, null);
		if (bid == null) return eventStructureErrorEvent("No block ID", null, event);
		
		es.destroyBlock(bid);
		
		return new JSONObject[0];
		
	}
	
//	
//	private String[] JSONArrayToStringArray (JSONArray a) {
//		if (a == null) return new String[0];
//		try {
//			String[] sa = new String[a.length()];
//			for (int i  = 0; i < sa.length; i++) {
//				sa[i] = a.getString(i);
//			}
//			return sa;
//		} catch (JSONException e) {}
//		return new String[0];
//	}
//	
	
	
	public JSONObject[] handleAnnoDeleteEvent (JSONObject event) {
		
		String bid = event.optString(JSON_EVENT_KEY_BLOCK_ID, null);
		if (bid == null) return eventStructureErrorEvent("No block ID", null, event);
		
		String aid = event.optString(JSON_EVENT_KEY_ANNO_ID, null);
		if (aid == null) return eventStructureErrorEvent("No anno ID", null, event);
		
		es.resetAnnotation(bid, aid);
		
		return new JSONObject[0];
		
	}
	
	
	public JSONObject[] handleAnnoEvent (JSONObject event) {
		
		String[] classes = event.optString(JSON_EVENT_KEY_ANNO_CLASSES, "").split("\\s+");
		FS infos = ClassesInfoConverter.decode(classes);
		
		String aid;
		
		if (infos.hasFeatureName("deleted")) {
			
			log.finer("got tabu zone annotation");
			aid = "deleted_" + config.idgen.createID();
			
		} else {
			
			aid = event.optString(JSON_EVENT_KEY_ANNO_ID, null);
			if (aid == null || aid.equals("")) return eventStructureErrorEvent("No annotation ID", null, event);
			
		}
			
		String bid = event.optString(JSON_EVENT_KEY_BLOCK_ID, null); 
		if (bid == null || bid.equals("")) return eventStructureErrorEvent("No block ID", null, event);
		
		JSONArray jspan = event.optJSONArray(JSON_EVENT_KEY_RANGE);
		if (jspan == null || jspan.length() != 2) return eventStructureErrorEvent("Bad range", null, event);
		Span span = new Span(jspan.optInt(0, -1), jspan.optInt(1, -1));
		if (span.start == -1 || span.end == -1 || span.start > span.end)
			return eventStructureErrorEvent("Bad range", null, event);
		
		es.setAnnotation(new Annotation(span, bid, infos));
		
		return null;
		
	}
	
	
	public JSONObject[] handleNulEvent (JSONObject event) {
		return null;
	}
	
	
	public JSONObject[] handleInitEvent (JSONObject event) {
		return null;
	}
	
	
	
	private JSONObject makeReturnEvents (JSONObject[] adds) {
		List<Annotation> annos = es.getNewAnnotations();
		List<JSONObject> events = new ArrayList<JSONObject>(annos.size() + ((adds == null) ? 0 : adds.length));
		
		log.finer("makeReturnEvents: inited = " + inited + " annos = " + annos + " adds = " + events);
		if (! inited) {
			try {
				JSONObject o = new JSONObject();
				o.put(JSON_EVENT_KEY_TYPE, JSON_EVENT_TYPE_INIT);
				o.put(JSONSessionConstants.JSS_KEY_SESSION_ID, sessid);
				events.add(o);
				inited = true;
			} catch (JSONException e) {
				log.log(Level.SEVERE, "Should not have happened!", e);
			}
		}
		
		for (Annotation a: annos) {
			JSONObject e = annoToEvent(a);
			try {
				e.put(JSON_EVENT_KEY_REVISION_ID, es.getBlockForID(a.blockID).getRevision());
			} catch (JSONException e1) {
				log.severe(e1.getLocalizedMessage());
			}
			if (e != null) events.add(e);
		}
		if (adds != null) for (JSONObject e: adds) if (e != null) events.add(e);
		
		if (events.isEmpty()) return null;
		if (events.size() == 1) return events.get(0);
		
		try {
			JSONObject o = new JSONObject();
			o.put(JSON_EVENT_KEY_TYPE, JSON_EVENT_TYPE_SEQ);
			for (JSONObject e: events) {
				o.append(JSON_EVENT_SEQ, e);
			}
			return o;
		} catch (JSONException e) {
			log.log(Level.SEVERE, "Should not have happened!", e);
		}
		return null;
		
	}
	
	private JSONObject annoToEvent (Annotation a) {
		
		try {
			JSONObject o = new JSONObject();
			o.put(JSON_EVENT_KEY_TYPE, JSON_EVENT_TYPE_ANNO);
			o.put(JSON_EVENT_KEY_BLOCK_ID, a.blockID);
			o.accumulate(JSON_EVENT_KEY_RANGE, a.start);
			o.accumulate(JSON_EVENT_KEY_RANGE, a.end);
			o.put(JSON_EVENT_KEY_ANNO_ID, a.id);
			
			String[] classes = ClassesInfoConverter.encode(a);
			
			if (classes != null && classes.length != 0)
				o.put(JSON_EVENT_KEY_ANNO_CLASSES, StringUtil.join(" ", classes));
			
			return o;
			
		} catch (JSONException e) {
			log.log(Level.SEVERE, "Should not have happened!", e);
		}
		return null;
		
	}
	
	
	public long getIdleTime () {
		return System.currentTimeMillis() - lastEventTime;
	}
	
	
	private JSONObject[] eventStructureErrorEvent (String msg, Throwable t, Object... prms) {
		LogRecord r = new LogRecord(Level.WARNING, msg);
		r.setThrown(t);
		r.setParameters(prms);
		log.log(r);
		try {
			JSONObject o = new JSONObject();
			o.put(JSON_EVENT_KEY_TYPE, JSON_EVENT_TYPE_ERROR);
			o.put(JSON_EVENT_KEY_TEXT, msg);
			if (prms != null && prms.length != 0)
				for (Object p: prms) o.accumulate(JSON_EVENT_KEY_ERROR_PARAMS, p);
			return new JSONObject[]{o};
		} catch (JSONException e) {
			log.log(Level.SEVERE, "Should not have happened!", e);
			return null;
		}
	}
	
}
