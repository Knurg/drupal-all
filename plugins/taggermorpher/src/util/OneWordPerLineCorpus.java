package util;


import hmmtagger.api.TagTable;
import hmmtagger.tagger.TaggedCorpus;
import hmmtagger.tagger.TaggedWord;

import java.io.IOException;

import java.io.*;

/**
Class for accessing a training corpus in one word per line format. Tags and words must be separated by TAB.
 */
public class OneWordPerLineCorpus implements TaggedCorpus 
{
    /**corpus file*/
	private File file=null;
	/**stream for accessing the corpus*/
	private BufferedReader instr=null;
	/**tag table*/
	private TagTable table=null;
	
	/**constructor
	 * 
	 * @param f corpus file
	 * @param tab tag table
	 */
	public OneWordPerLineCorpus(File f,TagTable tab)
	{
		file=f;
		table=tab;
	}
	
	/**
	 * @see hmmtagger.tagger.TaggedCorpus#initialize()
	 */
	public final void initialize() throws IOException 
	{
		instr=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}

	/** 
	 * @see hmmtagger.tagger.TaggedCorpus#nextToken()
	 */
	public final TaggedWord nextToken() throws IOException, Exception 
	{
		if (instr==null) throw new Exception("OneWordPerLineCorpus.nexToken: not initialized");
		String line=instr.readLine();
		if (line==null)
		{
			return null;
		}
		else
		{
			String[] pair=line.split(new Character((char)9).toString());
			if (pair.length != 2) throw new Exception("OneWordPerLineCorpus.nexToken: incorrect line");
			return new TaggedWord(pair[0],table.getTagCode(pair[1]));
		}
	}

}
