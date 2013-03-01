package hmmtagger.tagger;

import hmmtagger.api.ResultStream;
import hmmtagger.api.TagTable;
import hmmtagger.api.TaggedResultWord;
import hmmtagger.api.TokenStream;

import java.util.*;
import java.io.*;

import util.ArrayObjStream;
import util.FileObjStream;
import util.ResStream;

/**Intermediate class between the API and the core algortihm*/
public class BeamViterbi 
{  
			
   /**negativ infinity*/	
   protected final static float negInfinity= Float.NEGATIVE_INFINITY;
   /**associated instance of <pre>BeamViterbiAlgorithm</pre>*/
   protected BeamViterbiAlgorithm alg=null; 
   /**counter for result files*/
   protected static long resFileId=-1;
   /**instance counter*/
   protected static long instanceCounter=-1;
   /**working directory*/
   protected String workingDir=null;
   /**initial size of argmax array*/
   protected int argmaxBufferSize;
   /**tag table*/
   protected TagTable tagTable=null;
   /**ID of this instance*/
   protected long instanceNr;
   /**beam factor*/
   protected float beamWindow;
   /**number of ranks*/
   protected int nRanks;
   
   
   /**constructor
    * 
    * @param nRanks number of tag sequences to be calculated
    * @param pm parameter object
    * @param argmaxBufSize size of argmax array
    * @param wdir working directory
    * @param beamWin beam factor
    * @throws Exception
    * @throws IOException
    */
   public BeamViterbi(int nRanks,HMMParams pm,int argmaxBufSize,String wdir,float beamWin) throws Exception,IOException
   {   
   	
       tagTable=pm.getTagTable();
       instanceNr=getInstanceNr();
       this.nRanks=nRanks;
      
   	   ArgmaxArrayImpl am=new ArgmaxArrayImpl(argmaxBufSize);
   	   workingDir=wdir;
   	   argmaxBufferSize=argmaxBufSize;
   	   beamWindow=beamWin;
       alg=new BeamViterbiAlgorithm(nRanks,pm,am,beamWin);
       am.setAlgInstance(alg);
   }
   
 
   
   /**
    * Tagging of the words read from a token stream. Sentence splitting at sentence delimiters.
    * @param stream the token stream
    * @param delimiters array of sentence delimiters
    * @param memory flag indicating whether the results are to be written to the disk (false) or stored in memory (true)
    * @param lexInfo flag indicating whether lex info is to be returned (true)
    * @return result stream
    * @throws Exception
    * @throws IOException
    */
   public final ResultStream run(TokenStream stream, String[] delimiters,boolean memory,boolean lexInfo) throws Exception,IOException
   {  
   	   //initialization
   	   ArrayList  memResTags=null;
   	   File  diskResTags=null;
   	   ObjectOutputStream out=null;
   	   Vector currentSentence = new Vector();
   	   float[] scores=new float[nRanks];
   	   for (int i=0; i<nRanks; i++)
   	     {scores[i]=0;}
   	   String token=null;
   	   if (memory)
   	   {
   	   	   memResTags=new ArrayList();
   	   }
   	   else
   	   {
   	   	   diskResTags=new File(workingDir + File.separator + "result" + getResFileId() + ".res");
   	   	   out=new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(diskResTags)));
   	   }
   	   
       //tagging, sentence by sentence
   	   boolean emptyStream=true;
   	   do
   	   {
   	      token=stream.nextToken();
   	      boolean d=false;
   	      if (token != null)
		  {
   	      	emptyStream=false;
   	   	    for (int i=0; i<delimiters.length; i++)
   	   	    {
   	   	  	  if (token.equals(delimiters[i])) {d=true; break;}	
   	   	    }
   	   	    currentSentence.add(token);
		  }  
   	   	  if ( ( ( (d) || (token==null) ) ) && (currentSentence.size() > 0) ) 
   	   	  {   
   	   	  	  //tag current sentence in buffer
   	   	  	  int l=currentSentence.size();
   	   	  	  String[] arg=new String[l];
   	   	  	  ReadOutRes tags=null;
   	   	  	  currentSentence.toArray(arg);
   	   	  	  LexicalEntry[] le=alg.run(arg);
   	   	  	  tags=alg.readOut();
   	   	  	  for (int i=0; i<nRanks; i++)
   	   	  	  {
   	   	  	  	scores[i] += tags.scores[i];
   	   	  	  }
   	   	  	  //save results
   	   	  	  if (memory)
   	   	  	  {	
   	   	  	    for (int i=0; i<l; i++)
   	   	  	    {
   	   	  	       if  (lexInfo)
   	   	  	  	     {memResTags.add(new TaggedResultWord(arg[i],tags.tags[i],tagTable,le[i] ));}
   	   	  	       else
   	   	  	         {memResTags.add(new TaggedResultWord(arg[i],tags.tags[i],tagTable));}
   	   	  	    }   
   	   	  	  }
   	   	  	  else
   	   	  	  {
   	   	        for (int i=0; i<l; i++)
	   	  	    {
	   	  	       if  (lexInfo)
	   	  	  	     {out.writeObject(new TaggedResultWord(arg[i],tags.tags[i],tagTable,le[i] ));}
	   	  	       else
	   	  	         {out.writeObject(new TaggedResultWord(arg[i],tags.tags[i],tagTable));}
	   	  	    }   
   	   	  	  }
   	   	  	  currentSentence.removeAllElements();
   	   	  }
   	   }  while ( token != null);
   	   if (emptyStream) return null;
   	   //construct the ResultStream object to be returned
   	   ResStream res=null;
   	   if (memory)
   	   {
   	   	  res= new ResStream(new ArrayObjStream(memResTags.toArray()),scores);
   	   }
   	   else
   	   {
   	   	  out.flush();
   	   	  out.close();
   	   	  res=new ResStream(new FileObjStream(diskResTags),scores);
   	   }
   	   return res;
   }
   
   /** sets number of tag sequences to be returned
    * 
    * @param n new value
    * @throws Exception
    */
   public void setNRanks(int n) throws Exception
   {
   	   if (nRanks==n) return;
   	   this.nRanks=n;
    
 	   ArgmaxArrayImpl am=new ArgmaxArrayImpl(argmaxBufferSize);
       alg=new BeamViterbiAlgorithm(n,alg.params,am,beamWindow);
       am.setAlgInstance(alg);
   }
   
   /**generates new instance number
    * 
    * @return the number
    */
   static synchronized long getInstanceNr()
   {
   	  if (instanceCounter==Long.MAX_VALUE)
	  {
   	  	instanceCounter=-1;
	  }
   	  return ++instanceCounter;
   }
   
   /**generates new result file ID
    * 
    * @return the ID
    */
   static synchronized long getResFileId()
   {
   	  if (resFileId==Long.MAX_VALUE)
	  {
   	  	resFileId=-1;
	  }
   	  return ++resFileId;
   }
   
   
}   

