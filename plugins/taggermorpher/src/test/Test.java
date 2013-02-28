package test;


import hmmtagger.api.*;
import hmmtagger.tagger.*;

import java.io.*;
import java.util.*;

import postprocessor.api.*;
import postprocessor.transformer.RuleTemplate;
import postprocessor.transformer.templates.*;

import util.*;
/** testing...the code might serve as an example.*/
public class Test 
{

	private static final int firstTrainingSentence=0;
	private static final int lastTrainingSentence=38000;
	private static final int firstTestSentence=39000;
	private static final int lastTestSentence=40042;
	private static final int firstPPTrainingSentence=38000;
	private static final int lastPPTrainingSentence=39000;
	public static final String[] delimiter = {";",".","?","!",":"};
	
	public static void main(String[] args) 
	{  
		try
		{
	    //first of all, construct an instance of class PosTagger
		//to do so, instantiate a lexicon object and a parameter object and assemble them to form an instance of HMMParams 
	    //OR use an existing parameter file
			
		//location of the training corpus
		String corpusFile=IniFile.getIniFile().getStringVal("corpusLocation");
		   //training corpus object
		TaggedCorpus corp=null;
		    //construct map between Integer and String representations of tags
		TagTable table=	new TagTable(NegraCorpus.nTags,NegraCorpus.tagNames);
		
		PosTagger t=null;
		  //check if parameter file already exists
		String workingDir=IniFile.getIniFile().getStringVal("workingDir");
		int argmaxBufSize=IniFile.getIniFile().getIntVal("argmaxBufferSize").intValue();
		File pFile = new File(workingDir + File.separator + "paramFile2.prm");
		if (!(pFile.exists()))  //check if parameter file already exists
		{	
				//perform training, if it does not exist
				//construct training corpus object
				corp = new CorpusPortion(
						                              new NegraCorpus(new File(corpusFile),table),
													  firstTrainingSentence,
													  lastTrainingSentence,
													  53);
				corp.initialize();
				//construct parameter object 
				System.out.println("calculating transition parameters");
				SimpleTransParameters tp = new TreeTransParameters(corp,NegraCorpus.nTags,3,53,15,(float)50,(float)0.1,(float)0.1,2);
				//SimpleTransParameters tp = new SimpleTransParameters(corp,NegraCorpus.nTags,2,53,(float) 0.1,(float)0.25,2);
				
				//reset corpus
				corp.initialize();
				//tags representing open word classes
			    int[] openTags = new int[NegraCorpus.openTag.length];
			    for (int i=0; i<openTags.length; i++)
			    {
			    	openTags[i] = table.getTagCode(NegraCorpus.openTag[i]);
			    }
			    //tags representing classes of capitalized words only
			    int[] capTags = new int[NegraCorpus.capitalizedTags.length];
			    for (int i=0; i<capTags.length; i++)
			    {
			    	capTags[i] = table.getTagCode(NegraCorpus.capitalizedTags[i]);
			    }
			    //construct lexicon object
			    TagSynonymSet tagSyn=new TagSynonymSet(table);
			    for (int i=0; i<NegraCorpus.morphSynonyms.length; i++)
			    {
			    	tagSyn.addSynonym(NegraCorpus.morphSynonyms[i][0],NegraCorpus.morphSynonyms[i][1]);
			    }
			    
			    System.out.println("calculating lexicon");
			    String morphPath=util.IniFile.getIniFile().getStringVal("morphologyPath");
			    NegraTagSetDicManipulator dm= new NegraTagSetDicManipulator(table,tagSyn,capTags,morphPath);
			    SimpleLexicon lex = new SimpleLexicon(corp,53,table.getTagCode("CARD"),table.getTagCode("ADJA"),openTags,capTags,NegraCorpus.nTags,tagSyn,dm,morphPath,5,5,(float)8,(float)8,(float)0.05,(float) 0.05,false);
				//construct parameter object
				HMMParams hp = new HMMParams(lex,tp,table);
				corp=null;
				System.out.println("training terminated");
				//construct tagger object
				t=new PosTagger(1,hp,pFile,argmaxBufSize,workingDir,50);
		}
		else  //IF PARAMETER FILE ALREADY EXISTS 
		{
				//simple construction of the tagger object by using the appropriate constructor
			    String morphPath=util.IniFile.getIniFile().getStringVal("morphologyPath");
				t=new PosTagger(1,pFile,argmaxBufSize,workingDir,50,morphPath);
				System.out.println("parameters loaded");
		}
		//CONSTRUCTION OF POSTPROCESSOR
		//check existence of rule file
		File rFile = new File(IniFile.getIniFile().getStringVal("workingDir") + File.separator + "ruleFile.rule");
		RuleBasedPostProcessor pp=null;
		if (rFile.exists()) //use existing rule file
		{
			pp=new RuleBasedPostProcessor(delimiter,rFile);
			System.out.println("postprocessing rules loaded");
		}
		else
		{
			int n=NegraCorpus.nTags;
			//define tag subset
			String[] tags={"VVFIN",
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
				    "$,",
					"PTKZU",
				    "PTKNEG",
					"$."
					};
			int[] inttags=new int[tags.length];
			for (int i=0; i<inttags.length; i++)
		    {
		        	inttags[i]=table.getTagCode(tags[i]);
		    }
			
			//instantiate templates
			RuleTemplate[] temp= new RuleTemplate[17];
			
			
			temp[0]=new OnePositionConstraint(n,-1,true); 
			temp[2]=new OnePositionConstraint(n,1,true);
			temp[1]=new OnePositionConstraint(n,-2,true);
			temp[3]=new OnePositionConstraint(n,2,true);
			temp[4]=new OneTagRangeConstraint(n,2,true);
			temp[5]=new OneTagRangeConstraint(n,-2,true);
			temp[6]=new OneTagRangeConstraint(n,10,true);
			temp[7]=new OneTagRangeConstraint(n,-10,true);
			temp[8]=new TwoPositionConstraint(n,-1,1,true);
			temp[9]=new TwoPositionConstraint(n,-2,1,true);
			temp[10]=new TwoPositionConstraint(n,-1,2,true);
			temp[11]=new CapConstraint(n,0,true);
			temp[12]=new CapConstraint(n,0,false);
			temp[13]=new CapConstraint(n,-1,true);
			temp[14]=new CapConstraint(n,-1,false);
			temp[15]=new OneTagRangeConstraint(n,3,true);
			temp[16]=new OneTagRangeConstraint(n,-3,true);
			
			//instantiate training corpus
			TaggedCorpus corp2 = new CorpusPortion(
                                            new NegraCorpus(new File(corpusFile),table),
					                        firstPPTrainingSentence,
					                        lastPPTrainingSentence,
					                         53);
			RuleTemplate[][] temp2=new RuleTemplate[2][]; temp2[0]=temp; temp2[1]=temp;
			int[] th={5,5};
			int[][] tss=new int[2][]; tss[0]=null; tss[1]=inttags;
			System.out.println("calculating postprocessing rules");
			//instantiate Post-Processor which is passed to (and used by) TestRun.runTest
		    pp=new RuleBasedPostProcessor(delimiter,temp2,t,corp2,NegraCorpus.nTags,th,th,tss,true,rFile);
		    
		}
		//Usage of the PosTagger/Postprocessor instance....
		//Single-Threaded...TestRun.runTest simply calls one of the tagging methods of the PosTagger object it is passed
		System.out.println("Test1 (Single-threaded tagging)");    
	
	    TestRun run=new TestRun(t,
	    		                new CorpusPortion(new NegraCorpus(new File(corpusFile),table),firstTestSentence,lastTestSentence,53),
								new File(IniFile.getIniFile().getStringVal("workingDir") + File.separator + "resultfile2.txt"),
								table
	    		                ); 
	    System.out.println("Accuracy: "+ run.runTest((File) null,1,true,true,pp)); 
	 
	    Vector vec=new Vector();
		TaggedWord tw=null;
		corp = new CorpusPortion(
                                      new NegraCorpus(new File(corpusFile),table),
				                      35000,
				                      35010,									  
				                      53);
		corp.initialize();
		while ((tw = corp.nextToken()) != null)
		{
			vec.add(tw);
		}
		TaggedWord[] twa = new TaggedWord[vec.size()];
		vec.toArray(twa);
		System.out.println("Test2"); 
		t.setNRanks(1);
		System.out.println("Accuracy: "+run.runTest(twa)); 
		//Multi-threaded (parallel tagging of two different small portions of the test/training corpus)
		System.out.println("Test3 (multi-threaded tagging)");
		TaggedCorpus c1=null;
		TaggedCorpus c2=null;
		c1=new CorpusPortion(
                new NegraCorpus(new File(corpusFile),table),
                35100,
                35106,
                53);
        c1.initialize();
        vec=new Vector();
		tw=null;
		while ((tw = c1.nextToken()) != null)
		{
			vec.add(tw);
		}
		TaggedWord[] twa1 = new TaggedWord[vec.size()];
		vec.toArray(twa1);
		c2=new CorpusPortion(
                new NegraCorpus(new File(corpusFile),table),
                35106,
                35111,
                53);
        c2.initialize();
        vec=new Vector();
		tw=null;
		while ((tw = c2.nextToken()) != null)
		{
			vec.add(tw);
		}
		TaggedWord[] twa2 = new TaggedWord[vec.size()];
		vec.toArray(twa2);
		
		String[] s1 = new String[twa1.length];
		for (int i=0; i<twa1.length; i++)
		  {s1[i]=twa1[i].getWord();}
		
		String[] s2 = new String[twa2.length];
		for (int i=0; i<twa2.length; i++)
		  {s2[i]=twa2[i].getWord();} 
		
		c1.initialize();
		c2.initialize();
		//GET TaggingTask INSTANCES IMPLEMENTING INTERFACE Runnable
		TaggingTask t1=t.getTask(new Corpus2Token(c1),delimiter,5,true);
		TaggingTask t2=t.getTask(new Corpus2Token(c2),delimiter,5,true);
		//CREATE THREADS AND START THEM
        Thread th1 = new Thread(t1);
        Thread th2 = new Thread(t2);
		th1.start();
		th2.start();
		//WAIT FOR THE TWO THREADS TO BE FINISHED
		while (! ((t1.isReady()) && (t2.isReady()) ))  
		  {Thread.sleep(200);}		
		float res1=0;
		//GET THE RESULT STREAMS
        ResultStream rs1 = t1.getResult();
        for (int i=0; i<twa1.length; i++)
        {  
        	TaggedResultWord u = rs1.nextWord();
        	if (twa1[i].getIntTag() == u.getBestIntTag() ) res1++;
        }
        rs1.nextWord(); //DELETES UNDERLYING FILE
        System.out.println("Accuracy1: " + res1/(float) twa1.length);
        
        float res2=0;
        ResultStream rs2 = t2.getResult();
        for (int i=0; i<twa2.length; i++)
        {  
        	TaggedResultWord u = rs2.nextWord();
        	if (twa2[i].getIntTag() == u.getBestIntTag() ) res2++;
        }
        rs2.nextWord(); //DELETES UNDERLYING FILE
        System.out.println("Accuracy2: " + res2/(float) twa2.length); 
		} catch (Exception e) {System.out.println( e.getMessage() + ":" + e.getClass().getName() );}	
	}   
	
}		
