package postprocessor.api;


import hmmtagger.api.PosTagger;
import hmmtagger.api.ResultStream;
import hmmtagger.api.TaggedResultWord;
import hmmtagger.tagger.TaggedCorpus;
import hmmtagger.tagger.TaggedWord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import util.Corpus2Token;

import java.util.ArrayList;

import postprocessor.transformer.*;


/**Post-Processing by means of transformation rules*/
public class RuleBasedPostProcessor extends AbstractSentencePostProcessor 
{
	/**transformer object doing the actual work*/
	protected Transformer transformer=null;
	
	/**
	 * constructor, to be used if there is already a rule file
	 * @param rs result stream to be processed
	 * @param delim sentence delimiters
	 * @param ruleFile file containing the transformation rules
	 * @throws IOException
	 */
	public RuleBasedPostProcessor(ResultStream rs, String[] delim,File ruleFile) throws IOException
	{
		super(rs,delim);
		ObjectInputStream oin=new ObjectInputStream(new FileInputStream(ruleFile));
		try
		{
		transformer=(Transformer) oin.readObject();
		}
		catch (ClassNotFoundException e) {}
		oin.close();
	}
	
	/**
	 * constructor, to be used if there is already a rule file
	 * @param delim  sentence delimiters
	 * @param ruleFile file containing the transformation rules
	 * @throws IOException
	 */
	public RuleBasedPostProcessor(String[] delim,File ruleFile) throws IOException
	{
		this(null,delim,ruleFile);
	}
	
	/**constructor
	 * to be used if a new rule file has to be generated
	 * @param rs result stream to be processed
	 * @param delim sentence delimiters
	 * @param temps sets of rule templates (corresponding to <pre>tss</pre>)
	 * @param tagger tagger object for initial tagging
	 * @param corp training corpus
	 * @param nTags number of tags
	 * @param threasHoldCount error frequency threashold
	 * @param threasHoldScore score threashold for rules to be added to the set
	 * @param tss tag subsets (corresponding to <pre>temps</pre>); use null values for the whole tagset
	 * @param lex flag indicating if lexical prob. are to be respected
	 * @throws Exception
	 */
	public RuleBasedPostProcessor(ResultStream rs, String[] delim,RuleTemplate[][] temps,PosTagger tagger,TaggedCorpus corp,int nTags,int[] threasHoldCount,int[] threasHoldScore,int[][] tss,boolean lex) throws Exception
	{
		super(rs,delim);
		corp.initialize();
		ArrayList al=new ArrayList();
		TaggedWord tw=null;
		while ( (tw=corp.nextToken()) != null )
		{
			al.add(new Integer(tw.getIntTag()));
		}
		int[] refTags=new int[al.size()];
		for (int i=0; i<al.size(); i++)
		{
			refTags[i] = ((Integer) al.get(i)).intValue();
		}
		al=null;
		corp.initialize();
		ResultStream trainingResults=tagger.tag(new Corpus2Token(corp),delim,true);
		transformer=new Transformer(nTags,temps,trainingResults.toArray(),refTags,delim,threasHoldCount,threasHoldScore,tss,lex);
	}
	
	/**constructor
	 * to be used if a new rule file is to be generated
	 * @param delim sentence delimiters
	 * @param temps sets of rule templates (corresponding to <pre>tss</pre>)
	 * @param tagger tagger object for initial tagging
	 * @param corp training corpus
	 * @param nTags number of tags
	 * @param threasHoldCount error frequency threashold
	 * @param threasHoldScore score threashold for rules to be added to the set
	 * @param tss tag subsets (corresponding to <pre>temps</pre>); use null values for the whole tagset
	 * @param lex flag indicating if lexical prob. are to be respected
	 * @throws Exception
	 */
	public RuleBasedPostProcessor(String[] delim,RuleTemplate[][] temps,PosTagger tagger,TaggedCorpus corp,int nTags,int threasHoldCount[],int threasHoldScore[],int[][] tss,boolean lex) throws Exception
	{
		this(null,delim,temps,tagger,corp,nTags,threasHoldCount,threasHoldScore,tss,lex);
	}
	
	/**constructor, to be used if there is already a rule file
	 * 
	 * @param rs result stream to be processed
	 * @param delim sentence delimiters
	 * @param t transformer object
	 */
	protected RuleBasedPostProcessor(ResultStream rs, String[] delim,Transformer t)
	{
		super(rs,delim);
		transformer=t;
	}
	
	/**
	 * to be used if a new rule file is to be generated
	 * @param rs result stream to be processed
	 * @param delim sentence delimiters
	 * @param temps sets of rule templates (corresponding to <pre>tss</pre>)
	 * @param tagger tagger object for initial tagging
	 * @param corp training corpus
	 * @param nTags number of tags
	 * @param threasHoldCount error frequency threashold
	 * @param threasHoldScore score threashold for rules to be added to the set
	 * @param tss tag subsets (corresponding to <pre>temps</pre>); use null values for the whole tagset
	 * @param lex flag indicating if lexical prob. are to be respected
	 * @param ruleFile the rule file to be generated
	 * @throws Exception
	 */
	public RuleBasedPostProcessor(ResultStream rs, String[] delim,RuleTemplate[][] temps,PosTagger tagger,TaggedCorpus corp,int nTags,int[] threasHoldCount,int[] threasHoldScore,int[][] tss,boolean lex,File ruleFile) throws Exception
	{
		this(rs,delim,temps,tagger,corp,nTags,threasHoldCount,threasHoldScore,tss,lex);
		ObjectOutputStream oout=new ObjectOutputStream(new FileOutputStream(ruleFile));
		oout.writeObject(transformer);
		oout.flush();
		oout.close();
	}
	
	/**
	 * 
	 * @param delim sentence delimiters
	 * @param temps sets of rule templates (corresponding to <pre>tss</pre>)
	 * @param tagger tagger object for initial tagging
	 * @param corp training corpus
	 * @param nTags number of tags
	 * @param threasHoldCount error frequency threashold
	 * @param threasHoldScore score threashold for rules to be added to the set
	 * @param tss tag subsets (corresponding to <pre>temps</pre>); use null values for the whole tagset
	 * @param lex flag indicating if lexical prob. are to be respected
	 * @param ruleFile the rule file to be generated
	 * @throws Exception
	 */
	public RuleBasedPostProcessor(String[] delim,RuleTemplate[][] temps,PosTagger tagger,TaggedCorpus corp,int nTags,int[] threasHoldCount,int[] threasHoldScore,int[][] tss,boolean lex,File ruleFile) throws Exception
	{
		this(null,delim,temps,tagger,corp,nTags,threasHoldCount,threasHoldScore,tss,lex,ruleFile);
	}
	
	/**
	 * sets the result stream
	 * @param rs the new result stream
	 */
	public void setResultStream(ResultStream rs)
	{
		this.stream=rs;
		endOfStream=false;
	}
	
	/**
	 * processes the current sentence in the buffer by passing it to the transformer object
	 */
	protected final void processBuffer()
	{
		TaggedResultWord[] sentence=new TaggedResultWord[buffer.size()];
		buffer.toArray(sentence);
		transformer.transform(sentence);
	}
	
}
