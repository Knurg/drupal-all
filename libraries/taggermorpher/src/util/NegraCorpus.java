package util;

import hmmtagger.api.TagTable;
import hmmtagger.tagger.TaggedCorpus;
import hmmtagger.tagger.TaggedWord;

import java.io.*;



/**
Implementation of <pre>TaggedCorpus</pre> for accessing corpus files in Negra export format
@see hmmtagger.tagger.TaggedCorpus
 */
public class NegraCorpus implements TaggedCorpus
{   
	/**the corpus file*/
	private File file=null;
	/**tag table*/
	private TagTable tagTab=null;
	/**stream for accessing the file*/
	private BufferedReader stream=null;
	/**number of tags*/
    public static final int nTags = 56;
    /**array containing names of the tags used in negra corpus*/
	public static final String[] tagNames = {"UNKNOWN",
   		                             "ADJA",
		                             "ADJD",
		                             "ADV",
		                             "APPR",
		                             "APPRART",
		                             "APPO",
		                             "APZR",
		                             "ART",
		                             "CARD",
		                             "FM",
		                             "ITJ",
		                             "KOUI",
		                             "KOUS",
		                             "KON",
		                             "KOKOM",
		                             "NN",
		                             "NE",
		                             "PDS",
		                             "PDAT",
		                             "PIS",
		                             "PIAT",
		                             "--",
		                             "PPER",
		                             "PPOSS",
		                             "PPOSAT",
		                             "PRELS",
		                             "PRELAT",
		                             "PRF",
		                             "PWS",
		                             "PWAT",
		                             "PWAV",
		                             "PROAV",
		                             "PTKZU",
		                             "PTKNEG",
		                             "PTKVZ",
		                             "PTKANT",
		                             "PTKA",
		                             "TRUNC",
		                             "VVFIN",
		                             "VVIMP",
		                             "VVINF",
		                             "VVIZU",
		                             "VVPP",
		                             "VAFIN",
		                             "VAIMP",
		                             "VAINF",
		                             "VAPP",
		                             "VMFIN",
		                             "VMINF",
		                             "VMPP",
		                             "XY",
		                             "$,",
		                             "$.",
		                             "$(",
		                             "NNE"};
	/**tags representing open word classes*/
	public static final String[] openTag = {
										"ADJA",
										"ADJD",
										"ADV",
										"FM",
										"NN",
										"NE",
										"TRUNC",
										"VVFIN",
										"VVIMP",
										"VVINF",
										"VVIZU",
										"VVPP",
										"VAFIN",
										"VAIMP",
										"VAINF",
										"VAPP",
										"VMFIN",
										"VMINF",
										"VMPP",
	                                  };
	/**tags for word classes containing capitalized words only*/
	public static final String[] capitalizedTags = {"NE","NN"};
	/**mapping between STTS and the tagset of the morph bib.*/ 
	public static final String[][] morphSynonyms = { {"ADJA","ADJA"},
			                                    {"ADJD","ADJD"},
												{"ADJA","Ordinalzahl"},
	                                            {"CARD","Kardinalzahl"},
	                                            {"ADV","ADV"},
	                                            {"PROAV","ADV"},
	                                            {"ART","DET"},
												{"PIAT","DET"}, 
	                                            {"NE","EN"},
												{"NN","N"}, 
	                                            {"PTKZU","INFINITIVPARTIKEL"},
	                                            {"ITJ","INTERJ"},   
												{"PWAV","INTERROG"},
												{"KOUI","KONJ"},
												{"KOUS","KONJ"},
												{"KON","KONJ"},
												{"KOKOM","KONJ"},
												{"CARD","NUM"},
												{"APPR","P"},
												{"APPRART","P"},
												{"APPO","P"},
												{"APZR","P"},
												{ "PPOSS","POSSPRON"}, 
												{"PPOSAT","poss"},
												{ "PDS"  , "PRON"},
												{"PDAT","PRON"},
												{"PIS","PRON"},
												{"PIAT","PRON"},
												{"PPER","PRON"},
												{"PDAT","PRON"},
												{"PRELS","PRON"},
												{"PRELAT","PRON"},
												{"PRF","PRON"},
												{"PWS","PRON"},
												{"PWAT","PRON"},
												{"PTKVZ","TVZ"},
												{"VVFIN"  ,"V"},
												{"VVIMP"  ,"Vimperativ"},
												{"VVINF"  ,"Vinfinitiv"},
												{"VVIZU"  ,"Vinfinitiv"},
												{"VVPP"  ,"Vppp"}
	                                            };
												
	/**constructor
	 * 
	 * 
	 * @param f corpus file
	 * @param t tag table
	 */
	public NegraCorpus(File f, TagTable t)
	{
		file=f;
		tagTab= t;
	}
	
	/**
	 * @see hmmtagger.tagger.TaggedCorpus
	 */
	
	public final void initialize() throws IOException
	{   
		stream = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String s=null;
		do
		{
			s=stream.readLine();
			if (s==null) {break;}
			if (s.length()>=4)
			{
				if (s.substring(0,4).equals("#BOS")) {break;}
			}
		} while (true);
			
	}
   
	/**
	 * @see hmmtagger.tagger.TaggedCorpus
	 */
    public final TaggedWord nextToken() throws IOException,Exception
    {   
    	if (stream==null) throw new Exception("NegraCorpus.nextToken : not initialized");
    	String s=null;
    	do
    	{
    		s=stream.readLine();
    		if (s==null) break;
    		if (s.substring(0,4).equals("#EOS"))
    		  {return new TaggedWord("~",-1);}
    		
    	}	while ( (s.substring(0,1).equals("#")) || (s.substring(0,2).equals("%%")) );
		if (s==null) return null;
		int pos=0;
		while (s.charAt(pos) != (char) 9)
		  {pos++;}
	    String word=s.substring(0,pos);
	    while (s.charAt(pos) == (char) 9 )
	      {pos++;}
	    int tagStart = pos;
	    while (s.charAt(pos) != (char) 9 )
	      {pos++;}
	    String tag=s.substring(tagStart,pos);
	    int tagCode=tagTab.getTagCode(tag);
	   
		return new TaggedWord(word,tagCode);
    }
	
}
