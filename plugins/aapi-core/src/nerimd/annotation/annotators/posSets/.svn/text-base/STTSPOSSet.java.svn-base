package nerimd.annotation.annotators.posSets;

import nerimd.annotation.POSSet;

public class STTSPOSSet implements POSSet {

	public boolean isAdjective(String pos) {
		return pos.equals("ADJA");
	}
	
	public boolean isAdverb(String pos) {
		return pos.equals("ADV") || pos.equals("ADJD");
	}
	
	public boolean isDetArticle(String pos) {
		return pos.equals("ART") || pos.equals("APPRART"); 
	}
	
	public boolean isNamedEntity(String pos) {
		return pos.equals("NE");
	}
	
	public boolean isNoun(String pos) {
		return pos.equals("NN");
	}
	
	public boolean isPossPronoun(String pos) {
		return pos.equals("PPOSAT");
	}
	
	public boolean isPreposition(String pos) {
		return pos.equals("APPR") || pos.equals("APPRART");
	}

	public boolean isPronoun(String pos) {
		return pos.equals("PPER");
	}

	public boolean isUndetArticle(String pos) {
		return pos.equals("ART");
	}

	public boolean isVerb(String pos) {
		return pos.equals("VVFIN") || pos.equals("VVIMP") || pos.equals("VVINF") || pos.equals("VVIZU") || pos.equals("VVPP") || pos.equals("VAFIN") || pos.equals("VAIMP") || pos.equals("VAINF") || pos.equals("VAPP") || pos.equals("VMFIN") || pos.equals("VMINF") || pos.equals("VMPP");
	}

	public boolean isNumber(String pos) {
		return pos.equals("CARD");
	}
	

}
