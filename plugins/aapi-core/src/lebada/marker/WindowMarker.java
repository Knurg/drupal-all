package lebada.marker;

import java.util.ArrayList;
import java.util.List;

import lebada.fs.CFS;
import lebada.fs.FSUtil;

import de.fau.cs8.mnscholz.util.ArraysUtil;
import de.fau.cs8.mnscholz.util.options.Options;

import apus.tok.Token;

public class WindowMarker extends Marker {
	
	public WindowMarker(Options options, Window... windows) {
		super(options);
		this.windows = ArraysUtil.copyOmitNull(windows);
		
	}

	public static abstract class Window {
		
		public abstract TermOccurence[] markup (Token[] tokens, int offset);
		
	}
	
	protected Window[] windows;
	
	@Override
	public TermOccurence[] markup0(Token[] tokens) {
		
		List<TermOccurence> tos = new ArrayList<TermOccurence>();
		
		for (int i = 0; i < tokens.length; i++) {
			for (Window w: windows) {
				TermOccurence[] toa = w.markup(tokens, i);
				if (toa != null && toa.length > 0) {
					for (TermOccurence to: toa) {
						CFS fs = FSUtil.newCFS(to);
						fs.set("marker", this);
						tos.add(new TermOccurence(to.start, to.end, fs));
					}
				}
			}
		}
		
		return tos.toArray(new TermOccurence[tos.size()]);
		
	}

	@Override
	public String getName() {
		return options.get("name");
	}
	
	

}
