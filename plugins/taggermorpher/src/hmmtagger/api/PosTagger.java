package hmmtagger.api;


import hmmtagger.tagger.*;

import java.io.*;

import util.*;
/**
 * HMM Tagger Class. This class is NOT thread-safe, but provides methods for constructing tagging threads
 */
public final class PosTagger 
{
    /**nummber of tag sequences to be returned*/
	private int nRanks;
	/**parameter object*/
	private HMMParams parameters=null;
	/**tag table*/
	private TagTable table=null;
	/**working directory*/
    private String workingDir=null;
    /**initial size of argmax array*/
    private int argmaxBufSize=0;
	/**instance of the class <pre>BeamViterbi</pre> used by this instance*/
	private BeamViterbi viterbi;
	/**beam factor*/
    private float beamWindow;

	
	/**constructor
	 * 
	 * @param nRanks number of ranks
	 * @param p parameter object
	 * @param argmaxBSize initial size of argmax array
	 * @param wdir working directory
	 * @param beamWin beam factor
	 * @throws IOException 
	 * @throws Exception
	 */
	public PosTagger(int nRanks,HMMParams p,int argmaxBSize,String wdir,float beamWin) throws IOException,Exception
	{
    	init(nRanks,p,argmaxBSize,wdir,beamWin);
	}
   
	/**constructor
	 * 
	 * @param nRanks number of ranks
	 * @param paramFile parameter file
	 * @param argmaxBSize initial size of argmax array
	 * @param wdir working directory
	 * @param beamWin beam factor
	 * @throws IOException
	 * @throws Exception
	 */
    public PosTagger(int nRanks,File paramFile,int argmaxBSize,String wdir,float beamWin,String morphPath) throws IOException, Exception
	{
    	ObjectInputStream s = new ObjectInputStream(new BufferedInputStream(new FileInputStream(paramFile)));
    	HMMParams p=(HMMParams) s.readObject();
    	p.getLexicon().getMorphologizer().setMorphologyPath(morphPath);
    	init(nRanks,p,argmaxBSize,wdir,beamWin);
    	s.close();
	}
    
    /**constructor
     * 
     * @param nRanks number of ranks
     * @param p parameter object
     * @param paramFile parameter file
     * @param argmaxBSize initial size of argmax array
     * @param wdir working directory
     * @param beamWin beam factor
     * @throws IOException
     * @throws Exception
     */
    public PosTagger(int nRanks, HMMParams p,File paramFile,int argmaxBSize,String wdir,float beamWin) throws IOException,Exception
    {
    	init(nRanks,p,argmaxBSize,wdir,beamWin);
    	ObjectOutputStream s = new ObjectOutputStream( new BufferedOutputStream(new FileOutputStream(paramFile)));
    	s.writeObject(p);
    	s.flush();
    	s.close();
    }
    /**
     * constructor
     * @param p parameter object
     * @param argmaxBSize initial size of argmax array
     * @param wdir working directory
     * @param beamWin beam factor
     * @throws IOException
     * @throws Exception
     */
	public PosTagger(HMMParams p,int argmaxBSize,String wdir,float beamWin) throws IOException,Exception
	{
		this(1,p,argmaxBSize,wdir,beamWin);
	}
   
	/**constructor
	 * 
	 * @param paramFile parameter file
	 * @param argmaxBSize initial size of argmax array
	 * @param wdir working directory
	 * @param beamWin beam factor
	 * @throws IOException
	 * @throws Exception
	 */
    public PosTagger(File paramFile,int argmaxBSize,String wdir,float beamWin,String morphPath) throws IOException, Exception
	{
        this(1,paramFile,argmaxBSize,wdir,beamWin,morphPath);
        
	}
    /**constructor
     * 
     * @param p parameter object
     * @param paramFile parameter file
     * @param argmaxBSize initial size of argmax array
     * @param wdir working directory
     * @param beamWin beam factor
     * @throws IOException
     * @throws Exception
     */
    public PosTagger(HMMParams p,File paramFile,int argmaxBSize,String wdir, float beamWin) throws IOException,Exception
    {
    	this(1,p,paramFile,argmaxBSize,wdir,beamWin);
    }
  
    private final void init(int nRanks,HMMParams p,int argBuf,String wdir,float beamWin) throws Exception
    {
		this.nRanks=nRanks;
		parameters=p;
		table=p.getTagTable();
		argmaxBufSize=argBuf;
		workingDir=wdir;
		beamWindow=beamWin;
		viterbi=new BeamViterbi(nRanks,p,argBuf,wdir,beamWin);   	
    }
	/**
	 * tags an array of tokens
	 * @param s the token array
	 * @param delimiters array of sentence delimiters
	 * @param lexInfo flag for getting additional lexical information
	 * @return an instance of the interface <pre>ResultStream</pre>, containing the results
	 * @throws IOException
	 */
	public final ResultStream tag(String[] s,String[] delimiters,boolean lexInfo) throws IOException,Exception
	{   
		return viterbi.run(new ArrayStream(s),delimiters,true,lexInfo);
	}
	/**
	 * tags a stream of token
	 * @param ts the token stream
	 * @param delimiters array of sentence delimiters
	 * @param lexInfo flag for getting additional lexical information
	 * @throws IOException
	 */
	public final ResultStream tag(TokenStream ts,String[] delimiters,boolean lexInfo) throws IOException,Exception
	{  
		return viterbi.run(ts,delimiters,false,lexInfo);	
	}
	/**
	 * returns an instance of <pre>TaggingTask</pre>, for tagging a stream of tokens
	 * @param ts the token stream
	 * @param delimiters an array of sentence delimiters
	 * @param nRanks number of tag sequences to be calculated
	 * @param lexInfo flag for getting additional lexical information
	 * @return the task
	 * @throws IOException
	 */
	public final TaggingTask getTask(TokenStream ts,String[] delimiters,int nRanks,boolean lexInfo) throws IOException,Exception
	{  	
	     return new DiskTask(new BeamViterbi(nRanks,parameters,argmaxBufSize,workingDir,beamWindow),
	   		                 ts,
							 delimiters,
							 lexInfo);		
	}
	/**
	 * returns an instance of <pre>TaggingTask</pre>, for tagging an array of tokens
	 * @param s the token array
	 * @param delimiters an array of sentence delimiters
	 * @param nRanks number of tag sequences to be calculated
	 * @param lexInfo flag for getting additional lexical information
	 * @return the task
	 * @throws IOException
	 */
	public final TaggingTask getTask(String[] s, String[] delimiters,int nRanks,boolean lexInfo) throws IOException, Exception
	{  
		return new MemTask(new BeamViterbi(nRanks,parameters,argmaxBufSize,workingDir,beamWindow),
				              s,
						      delimiters,
							  lexInfo);
		
				
	}
	
	/**sets field <pre>nRanks</pre>
	 * 
	 * @param n the new value
	 * @throws Exception
	 */
	
	public final void setNRanks(int n) throws Exception
	{
		viterbi.setNRanks(n);
		nRanks=n;
	}
	
	/**returns parameter object
	 * 
	 * @return the parameter object
	 */
	public final HMMParams getParams()
	  {return parameters;}
}
