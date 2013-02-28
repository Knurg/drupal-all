package eu.wiss_ki;

import java.util.UUID;

import de.fau.cs8.mnscholz.util.options.Options;

public class IDGeneratorUUID extends IDGenerator {

	public IDGeneratorUUID(Options o) {
		super(o);
	}

	@Override
	public String createID() {
		return UUID.randomUUID().toString();
	}
	

}
