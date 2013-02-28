package lebada.marker;

import lebada.fs.CFS;
import lebada.fs.FSUtil;
import apus.tok.Token;
import de.fau.cs8.mnscholz.util.AbstractOptionedClass;
import de.fau.cs8.mnscholz.util.options.Options;

public abstract class Marker extends AbstractOptionedClass {

	protected Marker(Options options) {
		super(options);
		// TODO Auto-generated constructor stub
	}
	
	protected abstract TermOccurence[] markup0 (Token[] tokens);
	
	public String getName() {
		return options.get("name");
	}
	
	public TermOccurence[] markup (Token[] tokens) {
		
		TermOccurence[] tos = markup0(tokens);
		
		String name = this.getName();
		
		if (name != null) {
			// add marker name to term occurences
			for (int i = 0; i < tos.length; i++) {
				CFS cfs = FSUtil.newCFS(tos[i]);
				cfs.set("marker", name);
				tos[i] = new TermOccurence(tos[i].start, tos[i].end, cfs);
			}
		}
			
		return tos;
		
	}
	
}
