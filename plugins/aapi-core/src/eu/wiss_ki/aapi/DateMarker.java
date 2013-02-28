package eu.wiss_ki.aapi;

import apus.tok.Token;
import de.fau.cs8.mnscholz.util.options.Options;
import lebada.marker.DateMarkerEN;
import lebada.marker.Marker;
import lebada.marker.TermOccurence;

public class DateMarker extends Marker {
	
	private Marker m;
	
	public DateMarker(Options options) {
		super(options);
		String locale = options.get("locale", "de").toLowerCase();
		if (locale.startsWith("en")) {
			m = new DateMarkerEN(options);
		} else {
			m = new LegacyTimeMarker(options);
		}
		
	}

	@Override
	protected TermOccurence[] markup0(Token[] tokens) {
		return m.markup(tokens);
	}

}
