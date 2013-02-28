package postprocessor.transformer;


import hmmtagger.api.TaggedResultWord;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

import util.Utility;

/**This class implements a rule-based Transformer*/
public class Transformer implements java.io.Serializable
{
	/**container class storing an error type and its frequency */
	class ErrorTriple
	{
		/**wrong tag*/
		int tag;  
		/**right tag*/
		int refTag;
		/**frequency*/
		int count;
	
		ErrorTriple(int t,int rt)
		{
			tag=t;
			refTag=rt;
			count=0;
		}
		
	    void incrCount()
	      {count++;}
		
	    public boolean equals(Object o)
	    {
	    	ErrorTriple arg=null;
	    	try
			{
	    		arg=(ErrorTriple) o;
			} catch (Exception e) {return false;}
			return ((arg.tag==tag) && (arg.refTag==refTag));
	    }
	    
	    public int hashCode()
	    {
	    	return tag+nTags*refTag;
	    }
	}
	
	/**container class storing a rule instance and the subset it's working on*/
	class Rule implements Serializable
	{
		RuleInstance r=null;
		int sub=0;
		
		Rule(RuleInstance rule, int s)
		{
			r=rule;
			sub=s;
		}
	}
	/**array of rules*/
	private Rule[] rules=null;
	/**number of tags*/
	private int nTags;
	/**tag subsets*/
	private int[][] tagSubSet=null;
	/**flag indicating if lex. prob. is to be respected by transformation rules*/
	private boolean lexical;
	     
    /**constructor
     * 
     * @param nTags number of tags
     * @param templates sets of rule templates (corresponding to <pre>tss</pre>)
     * @param tw initally tagged token sequence
     * @param refTags the right tags from the training corpus
     * @param delimiter sentence delimiter
     * @param threasHoldCount frequency threashold for error types
     * @param threasHoldScore score threashold for rule instances
     * @param tss tag subsets (corresponding to <pre>templates</pre>)
     * @param lex flag indicating if lex. prob. is to be respected by transformation rules
     * @throws Exception
     */
	public Transformer(int nTags, RuleTemplate[][] templates, TaggedResultWord[] tw, int[] refTags,String[] delimiter,int[] threasHoldCount,int[] threasHoldScore,int[][] tss,boolean lex) throws Exception
	{
		if (tw.length != refTags.length)
		   {throw new Exception("Transformer.constructor: inconsistent arguments");}
		this.nTags=nTags;
		lexical=lex;
		tagSubSet=tss;
		
		if (tw.length != refTags.length)
			{throw new Exception("Transformer.constructor: inconsistent arguments");}
		//calculate error triples
		Hashtable errors=new Hashtable();	
	    int t;
	    ArrayList twords=new ArrayList();
	    ArrayList rtags=new ArrayList();
	    for (int i=0; i<tw.length; i++)
	    {
		   t=tw[i].getBestIntTag();
		   if (t != refTags[i])
		   {	
			   ErrorTriple triple=new ErrorTriple(t,refTags[i]);
			   if (errors.containsKey(triple))
			     { ((ErrorTriple) errors.get(triple)).incrCount(); }
			   else
			   {
				errors.put(triple,triple);
			   } 
		   }  
	    }
		//splitting training sequence into sentences and clone them
		TaggedResultWord sentences[][]=null;
		TaggedResultWord sentencesOrig[][]=null;
		Integer[][] referenceTags=null;
        int start=0;
        ArrayList alW=new ArrayList();
        ArrayList alWOrig=new ArrayList();
        ArrayList alT=new ArrayList();
        for (int i=0; i<tw.length; i++)
        {

        	if ( (Utility.elementOf(delimiter,tw[i].getWord()) ) || (i==tw.length-1) )
        	{
        		TaggedResultWord[] s=new TaggedResultWord[i-start+1];
        		TaggedResultWord[] sOrig=new TaggedResultWord[i-start+1];
        		Integer[] tr=new Integer[i-start+1];
        		for (int j=start; j<=i; j++)
        		{
        			s[j-start] = (TaggedResultWord) tw[j].clone();
        			sOrig[j-start]=tw[j];
        			tr[j-start] = new Integer(refTags[j]);
        		}
        		alW.add(s);
        		alWOrig.add(sOrig);
        		alT.add(tr);
        		start=i+1;
        	}
        }
        sentences=new TaggedResultWord[alW.size()][];
        alW.toArray(sentences);
        alW=null;
        sentencesOrig=new TaggedResultWord[alWOrig.size()][];
        alWOrig.toArray(sentencesOrig);
        alWOrig=null;
        referenceTags=new Integer[alT.size()][];
        alT.toArray(referenceTags);
        alT=null;
        //for each tag subset: calculate a restricted token sequence
	    TaggedResultWord[][][] sentOrig=new TaggedResultWord[templates.length][][];
	    TaggedResultWord[][][] sentWork=new TaggedResultWord[templates.length][][];
	    Integer[][][] ref=new Integer[templates.length][][];
		for (int i=0; i<sentOrig.length; i++)
		{
			if (tagSubSet[i] == null)
			{
				sentWork[i]=sentences;
				sentOrig[i]=sentencesOrig;
				ref[i]=referenceTags;
			}
			else
			{
				sentWork[i]=new TaggedResultWord[sentences.length][];
				sentOrig[i]=new TaggedResultWord[sentences.length][];
				ref[i]=new Integer[sentences.length][];
				
				ArrayList alWordsW=new ArrayList();
				ArrayList alWordsO=new ArrayList();
				ArrayList alRef=new ArrayList();
				for (int j=0; j<sentences.length; j++)
				{
					alWordsW.clear();
					alWordsO.clear();
					alRef.clear();
					for (int k=0; k<sentences[j].length; k++)
					{
						t=sentences[j][k].getBestIntTag();
						if ( (Utility.elementOf(tagSubSet[i],t)) /*&& (Utility.elementOf(tagSubSet[i],referenceTags[j][k].intValue())) */)
					    {
						   alWordsW.add(sentences[j][k]);
						   alWordsO.add(sentencesOrig[j][k]);
						   alRef.add(referenceTags[j][k]);
					    }
					}
					sentWork[i][j] = new TaggedResultWord[alWordsW.size()];
					alWordsW.toArray(sentWork[i][j]);
					sentOrig[i][j] = new TaggedResultWord[alWordsO.size()];
					alWordsO.toArray(sentOrig[i][j]);
					ref[i][j] = new Integer[alRef.size()];
					alRef.toArray(ref[i][j]);
				}
			}
		}
		//for each tag subset: calculate a restricted set of error triples
		ErrorTriple[][] err=new ErrorTriple[templates.length][];
        Enumeration en=errors.keys();
        ArrayList[] al=new ArrayList[templates.length];
        for (int i=0; i<al.length; i++)
          {al[i]=new ArrayList();}
        while (en.hasMoreElements())
        {
        	ErrorTriple e=(ErrorTriple) en.nextElement();
        	for (int i=0; i<err.length; i++)
        	{
        		if ((e.count >threasHoldCount[i]))
        		{
        			if (tagSubSet[i]==null)
        			  {al[i].add(e);}
        			else
        			{
        			   if ( (Utility.elementOf(tagSubSet[i],e.refTag)) && (Utility.elementOf(tagSubSet[i],e.refTag))  )
             		      {al[i].add(e);}
        			}
        		}
        	}
        }
        for (int i=0; i<al.length; i++)
        {  
        	err[i]=new ErrorTriple[al[i].size()];
            al[i].toArray(err[i]);
            al[i]=null;
        }
        
		//determin optimal rules
		ArrayList ruleInstances=new ArrayList();
		for (int i=0; i<templates.length; i++) //for each tag subset /template set
		{
			for (int j=0; j<templates[i].length; j++)
			   {templates[i][j].setTagSubSet(tagSubSet[i]);}	
			for (int j=0; j<err[i].length; j++)   //for each error triple
			{
				int maxScore=0;
				RuleInstance maxInstance=null;
		  
				for (int k=0; k<templates[i].length; k++) //for each template of the current set
				{
					templates[i][k].reset(err[i][j].tag,err[i][j].refTag);
					RuleInstance[] instSet=null;
					while ( (instSet=templates[i][k].getNextInstances()) != null) 
					{                                         //for all instances
						for (int l=0; l<instSet.length; l++)  
						{
							int score=0;
							//apply instance to all sentences
							instSet[l].applyTo(sentWork[i],lexical);
							//determin score for current rule
							for (int m=0; m<sentWork[i].length; m++) 
							{
								for (int p=0; p<sentWork[i][m].length; p++)  
								{
									if (sentOrig[i][m][p].getBestIntTag()==err[i][j].tag)
									{
										if (sentWork[i][m][p].getBestIntTag()==err[i][j].refTag)
										{
											if (ref[i][m][p].intValue() == err[i][j].refTag)
											   {score++;}
											else
											{
												if (ref[i][m][p].intValue() == err[i][j].tag)
												   {score--;}	
											}
											sentWork[i][m][p].setBestTag(sentOrig[i][m][p].getBestIntTag());
										}
									}
								}
							}
							if  ( (score>threasHoldScore[i]) && (score > maxScore) )
							{
								maxScore=score;
				    			maxInstance=(RuleInstance) instSet[l].clone();
							}
						}
					}
				}
				//add best instance to list
			
				if (maxInstance != null)
				{
					ruleInstances.add(new Rule(maxInstance,i));
					maxInstance.applyTo(sentWork[i],lexical);
					maxInstance.applyTo(sentOrig[i],lexical);
				}		
			}
		}	
			
		rules=new Rule[ruleInstances.size()];
		ruleInstances.toArray(rules);
			
		
	}
	
	/**
	 * transforms the tags of a given sentence by applying the set of transformation rules to it
	 * @param sentence the sentence
	 */
	public void transform(TaggedResultWord[] sentence)
	{
		
	    TaggedResultWord[][] arg=new TaggedResultWord[1][];
		for (int i=0; i<rules.length; i++)
		{
		   arg[0]=sentence;
		
		   if (tagSubSet[rules[i].sub] != null)
		   {
			  arg[0]=Utility.project(sentence,tagSubSet[rules[i].sub]);
		   }
		   rules[i].r.applyTo(arg,lexical);
		}
	}
	
}
