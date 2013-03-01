package test;


import hmmtagger.api.*;
import hmmtagger.tagger.*;

import java.io.*;


import java.util.Arrays;
import java.util.Hashtable;
import java.util.Enumeration;

import postprocessor.api.RuleBasedPostProcessor;

import util.CorpusPortion;
import util.IniFile;
import util.NegraCorpus;
import util.NegraTagSetDicManipulator;


/**Class for testing with a pretagged corpus*/
public class TestRun implements TokenStream 
{
	/**container class for error statistics*/
	class ErrorTriple implements Comparable
	{    
		/**wrong tag*/
		int tag;     
		/**right tag*/
		int refTag;
		/**frequencey*/
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
	    
	    public int compareTo(Object o)
	    {
	    	ErrorTriple a=(ErrorTriple) o;
	    	int diff=this.refTag-a.refTag;
	    	if (diff != 0) return diff;
	    	diff=this.tag-a.tag;
	    	return diff;
	    }
	}
	
	
   /**sentence delimiter*/	
   protected String[] delimiter = {";",".","?","!",":"};
   /**tagger object*/
   protected PosTagger tagger=null;	
   /**training corpus*/
   protected TaggedCorpus corpus=null; 
   /**output stream*/
   protected ObjectOutputStream outstream=null;
   /**the working directory*/
   protected String workingDir=null;
   /**result file*/
   protected File resFile=null;
   /**tag table*/
   protected TagTable table=null;
   /**number of tags*/
   protected int nTags;
   
   /** constructor
    * 
    * @param v tagger object to be tested
    * @param c the preteagged corpus to be used
    * @throws IOException
    */
   public TestRun(PosTagger t,TaggedCorpus c,File f,TagTable tab) throws IOException, Exception
   {
   	   workingDir=IniFile.getIniFile().getStringVal("workingDir") + File.separator;
   	   corpus=c;
   	   tagger=t;
   	   resFile=f;
   	   table=tab;
   	   nTags=tab.getNTags();
   	   c.initialize();
   	   outstream=new ObjectOutputStream(new FileOutputStream(new File(workingDir + "test.mem")));
   }
	
   /**
    * @see hmmtagger.api.TokenStream
    * @return the next token
    */
   public String nextToken() throws Exception,IOException
   {    
   	    TaggedWord w=corpus.nextToken();
   	    if (w==null)
   	    {
   	    	outstream.flush();
   	    	outstream.close();
   	    	return null;
   	    }
   	    outstream.writeObject(w);
		return w.getWord();
   }

   /**performs the test
    * 
    * @param refFile results of another tagger for comparing (does NOT obligatorily need to be specified)
    * @param nRanks number of tag sequences to be written to the result file
    * @param lexInf flag indicating if lex. info is to be written to the result file
    * @param lexProb indicating if lex. prob. are to be written to the result file
    * @param pp post-processor
    * @return the tagging accuracy
    * @throws Exception
    * @throws IOException
    */
   public float runTest(File refFile,int nRanks,boolean lexInf,boolean lexProb,RuleBasedPostProcessor pp) throws Exception,IOException
   {  
   	   //initialisation
   	   BufferedReader ref = null;
   	   if (refFile != null) ref = new BufferedReader(new InputStreamReader(new FileInputStream(refFile)));
   	   PrintWriter pw = new PrintWriter(new FileOutputStream(resFile));
   	   ObjectInputStream instream=new ObjectInputStream(new FileInputStream(new File(workingDir+"test.mem")));
   	   float res=(float) 0.0;
   	   String top = "Token || ";
   	   for (int i=1; i<=nRanks; i++)
   	     {top=top + "Erl-Tag" + i + " || ";}
   	   if (refFile != null)
   	     {top=top + "other Tag || ";}
   	   top = top + "right tag || eval1" ;
	   if (refFile != null) top=top + " || eval2";
	   if (lexInf) top = top + " || LexInfo";  
   	   pw.println(top);
   	   pw.println("----------------------------------------------------------------------------------------------------------------");
   	   //call the tagger
   	   System.out.println("tagging");
   	   
   	   long startTime=System.currentTimeMillis();
   	   ResultStream resTags =tagger.tag(this,delimiter,true);
   	   long endTime=System.currentTimeMillis();
   	   System.out.println("eval");
   	   if (pp!=null)
   	   {
   	   	  pp.setResultStream(resTags);
   	   	  resTags=pp;
   	   }	  
   	   //calculate/return accuracy and ouput extra information
   	   TaggedResultWord twres=null;
   	   float z=0;
   	   float unknownError=0;
   	   Hashtable errorStat=new Hashtable();
   	   Hashtable errorStatTree=new Hashtable();
   	   String treeTag=null;
   	   float resTree= (float) 0.0;
   	   int nMorphAnalyzed=0;
   	   int nUnknownWords=0;
   	   float ambig=0;
   	   float ambigCorrect=0;
   	   while ( (twres = resTags.nextWord()) != null )
   	   {   
   	   	   boolean ambigous=false;
   	   	   char lexInfo=twres.getLexicalInfo().getInfo();
   	   	   if (twres.getLexicalInfo().getProb().length>1) 
   	   	      {ambig++; ambigous=true;}
   	   	   if (refFile != null) treeTag = ref.readLine();
   	   	   z++;
   	   	   if (lexInfo=='m')   nMorphAnalyzed++;
   	   	   if (lexInfo=='u')   nUnknownWords++;
   	       TaggedWord tw = (TaggedWord) instream.readObject();
   	       String x="";
   	       if (refFile != null) x=treeTag + " || ";
   	       String line=twres.getWord()+ " || "; 
   	       for (int i=0; i<nRanks; i++) 
   	       	  {line = line + twres.getStringTag(i) + " || ";}
   	       line = line + x + table.getTagName(tw.getIntTag()) + " || ";
   	       	 
   	   	   if ( tw.getIntTag() == twres.getBestIntTag() )
   	   	   {
   	   	   	  if (ambigous) ambigCorrect++;
   	   	   	  res++;
   	   	   	  line=line+"!!" ;
   	   	   }
   	   	   else
   	   	   {
   	   	   	  if (twres.getLexicalInfo().getInfo() == 'u') {unknownError++;}
   	   	   	  ErrorTriple triple=new ErrorTriple(twres.getBestIntTag(),tw.getIntTag());
   	   	   	  if (errorStat.containsKey(triple))
   	   	   	  {
   	   	   	  	triple= (ErrorTriple) errorStat.get(triple);
   	   	   	  	triple.incrCount();
   	   	   	  }
   	   	   	  else
   	   	   	  {
   	   	   	  	triple.incrCount();
   	   	   	  	errorStat.put(triple,triple);
   	   	   	  }
   	   	   	  line=line+"??" ;
   	   	   }
   	   	   if (refFile != null)
   	   	   {	
   	   	     if (tw.getIntTag()  == table.getTagCode(treeTag))
		     {
		   	    resTree++;
		   	    line = line + " || " + "!!";
		     }
		     else
		     {
		     	ErrorTriple triple=new ErrorTriple(table.getTagCode(treeTag),tw.getIntTag());
		     	if (errorStatTree.containsKey(triple))
		     	{
		     		triple= (ErrorTriple) errorStatTree.get(triple);
	   	   	   	  	triple.incrCount();
		     	}
		     	else
		     	{
		     		triple.incrCount();
	   	   	   	  	errorStatTree.put(triple,"");
		     	}
		   	    line = line + " || "+ "??";
		     }
   	   	   }  
   	   	   if (lexInf) line = line + " || " + lexInfo; 
   	   	   if (lexProb)
   	   	   {
   	   	   	 line=line+" || ";
   	   	   	 LexProb[] probs=twres.getLexicalInfo().getProb();
   	   	   	 for (int i=0; i<probs.length; i++)
   	   	   	 {
   	   	   	 	line=line+"<"+table.getTagName(probs[i].s)+"=" + probs[i].p+">";
   	   	   	 }
   	   	   }
   	   	   pw.println(line);
   	   }
   	   pw.println("---------------------------Statistics--------------------------------------------");
   	   pw.println("Accuracy Erlangen-Tagger  : " + res / z);
   	   pw.println("Accuracy for ambigious words: " + (ambigCorrect/ambig) );
   	   if (refFile != null) pw.println("Accuracy Other Tagger: " + resTree / z);
   	   pw.println("Number of Words: " + z);
   	   pw.println("Time: " +   ( ( (double)endTime-(double) startTime) ) /(double) 1000 );
	   pw.println();
   	   pw.println("Detailed Error Statistics (Erlangen-Tagger)");
   	   pw.println("Right Tag || Tag || Frequ");
   	   pw.println();
   	   Enumeration en=errorStat.keys();
   	   ErrorTriple[] errr=new ErrorTriple[errorStat.size()];
   	   for (int i=0; i<errorStat.size(); i++)
   	   {
   	   	  errr[i]=(ErrorTriple) en.nextElement();
   	   }
   	   Arrays.sort(errr);
	   for (int i=0; i<errorStat.size(); i++)
	   {
	   	  pw.println(table.getTagName(errr[i].refTag) + " || " + table.getTagName(errr[i].tag) + " || " + errr[i].count);
	   }
   	  
   	   if (refFile != null)
   	   {	
   	     pw.println();
   	     pw.println("Detailed Error Statistics (other Tagger)");
   	     pw.println();
   	     en=errorStatTree.keys();
  	     errr=new ErrorTriple[errorStatTree.size()];
  	     for (int i=0; i<errorStatTree.size(); i++)
  	     {
  	   	    errr[i]=(ErrorTriple) en.nextElement();
  	     }
  	     Arrays.sort(errr);
	     for (int i=0; i<errorStatTree.size(); i++)
	     {
	   	    pw.println(table.getTagName(errr[i].refTag) + " || " + table.getTagName(errr[i].tag) + " || " + errr[i].count);
	     }
   	   }  
   	   pw.flush();
   	   pw.close();
   	   instream.close();
   	   return ( res / z );
   }
   
   /**test with an array of pretagged words
    * 
    * @param words the words
    * @return
    * @throws IOException
    * @throws Exception
    */
   public float runTest(TaggedWord[] words) throws IOException,Exception
   {    
   	    String[] s=new String[words.length];
   	    for (int i=0; i<s.length; i++)
   	      {s[i] = words[i].getWord();}	
        ResultStream st=tagger.tag(s,delimiter,true);
        s=null;
        TaggedResultWord[] resW = st.toArray();
        float res=0;
        for (int i=0; i<words.length; i++)
        {
        	if (words[i].getIntTag() == resW[i].getBestIntTag()) {res++;}
        }
        System.out.println("Number of Words: " + words.length);
   	    return res / ((float) words.length); 
   }
   /**
    * Used for performing the tests reported in the thesis
    * @param NTrainSentences number of training sentences
    * @param order HMM-order
    * @param quick flag indicating if quick training is to be performed
    * @param morphFirst flag indicating if morph. bib is to be used first, when analysing unknown words
    * @param beamWindow beam factor
    * @param argmaxSize size of argmax array
    * @param parameterFile parameter file
    * @param resFile result file
    * @param tth information gain threashold (ngram decision tree)
    * @return tagging accuracy
    * @throws Exception
    */
   public static float test(int NTrainSentences,int order,boolean quick,boolean morphFirst,int beamWindow,int argmaxSize,File parameterFile,File resFile,float tth) throws Exception
   {
    System.gc();
   	String corpusFile=IniFile.getIniFile().getStringVal("corpusLocation");
	//training corpus object
	TaggedCorpus corp=null;
	//construct map between integer and String representations of tags
	TagTable table=	new TagTable(NegraCorpus.nTags,NegraCorpus.tagNames);
    //tags representing open word classes
    int[] openTags = new int[NegraCorpus.openTag.length];
    for (int i=0; i<openTags.length; i++)
    {
    	openTags[i] = table.getTagCode(NegraCorpus.openTag[i]);
    }
    //tags representing capitalized words
    int[] capTags = new int[NegraCorpus.capitalizedTags.length];
    for (int i=0; i<capTags.length; i++)
    {
    	capTags[i] = table.getTagCode(NegraCorpus.capitalizedTags[i]);
    }
    //map between application tagset and morph. tagset
    TagSynonymSet tagSyn=new TagSynonymSet(table);
    for (int i=0; i<NegraCorpus.morphSynonyms.length; i++)
    {
    	tagSyn.addSynonym(NegraCorpus.morphSynonyms[i][0],NegraCorpus.morphSynonyms[i][1]);
    }
    String morphPath=util.IniFile.getIniFile().getStringVal("morphologyPath");
    NegraTagSetDicManipulator dm= new NegraTagSetDicManipulator(table,tagSyn,capTags,morphPath);
    SimpleLexicon lex = null;
    SimpleTransParameters tp=null;
    PosTagger t=null;
    String workingDir=IniFile.getIniFile().getStringVal("workingDir");
    
		
	   corp = new CorpusPortion(
              new NegraCorpus(new File(corpusFile),table),
			  0,
			  NTrainSentences,
			  53);
       corp.initialize();
       lex = new SimpleLexicon(corp,53,table.getTagCode("CARD"),table.getTagCode("ADJA"),openTags,capTags,NegraCorpus.nTags,tagSyn,dm,morphPath,5,5,(float)1.2,(float)8,(float)8,(float)0.05,morphFirst);
	   corp.initialize();
	   if (quick)
	   {
	   	  tp = new SimpleTransParameters(corp,NegraCorpus.nTags,order,53,(float) 0.1,(float)0.25,2);	   	
	   }
	   else
	   {
	   	  tp = new TreeTransParameters(corp,NegraCorpus.nTags,order,53,3,tth,(float)0.1,(float)0.1,2);
	   }
	   HMMParams hp = new HMMParams(lex,tp,table);
	   t=new PosTagger(1,hp,parameterFile,argmaxSize,workingDir,beamWindow);
	   TestRun run=new TestRun(t,
            new CorpusPortion(new NegraCorpus(new File(corpusFile),table),39000,40042,53),
			resFile,
			table
            );
       float acc=run.runTest((File) null,1,true,true,null);
       return acc;    
  }
   
}
