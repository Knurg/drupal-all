package frontend;


import hmmtagger.api.*;
import hmmtagger.tagger.HMMParams;
import hmmtagger.tagger.SimpleLexicon;
import hmmtagger.tagger.SimpleTransParameters;
import hmmtagger.tagger.TaggedCorpus;
import hmmtagger.tagger.TreeTransParameters;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;

import postprocessor.api.RuleBasedPostProcessor;
import postprocessor.transformer.RuleTemplate;
import postprocessor.transformer.templates.*;


import util.IniFile;
import util.NegraTagSetDicManipulator;
import util.OneWordPerLineCorpus;
import util.OneWperLTS;
/**
 * Static class, providing the frontend methods
 */
public final class MainClass 
{
	/**parameter names*/
	private static final String[] progParams=   { "argmaxBufSize","wDir","nRanks","delimiters","inFile","outFile","paramFile","ruleFile","quickTraining","memory","contextSize","morphPath","gammaIni","gammaTrans","ngrTreeGain","ngrTreeBinSizeTH","prefLen","suffLen","prefGain","suffGain","prefGamma","suffGamma","ppErrCountTH","ppRuleScoreTH","taggerTrainFile","PPTrainFile","openClassesFile","capClassesFile","tagMapFile","eosTag","numberTag","ordNumberTag","morphFirst","taggerOutFile","PPOutFile" ,"beamWindow" ,"tagSubSetFiles" };
	/**default values*/
	private static final String[] defaultValues={ "100",            null,  "1",     ";$.$?$!$:", null,    null,    null,        null,     "yes",          "2",     "2",           null,      "0.1",     "0.25",      "50",        "15",              "5",      "5",      "8",       "8",       "0.05",     "0.05",      "3",          "3",            null,             null,         null,             null,             null,        "$.",   "CARD",     "ADJA",       "no"             , null,      null,       "50",          null};

	/**main methods, serves as a dispatcher*/
	public static void main(String[] args) 
	{
		if (args==null) {System.out.println("Please read the manual for usage instructions."); System.exit(0);}
		if (args.length==0) {System.out.println("Please read the manual for usage instructions."); System.exit(0);}
		String arg="";
		for (int i=1; i<args.length; i++)
		{
			arg=arg+" "+args[i];
		}
		arg=arg.trim();
		String[] args2=arg.split(",");
		
		if (args[0].equals("train"))
	    {
			train(args2);
			System.exit(0);
	    }
		if (args[0].equals("tag"))
		{
			tag(args2);
			System.exit(0);
		}
		if (args[0].equals("install"))
		{   
			if (args.length<3) {System.out.println("Please read the manual for usage instructions."); System.exit(0);}
			String installDir="";
			for (int i=2; i<args.length; i++)
			 {installDir = installDir + " " + args[i];}	
			installDir=installDir.trim();
			install(args[1],installDir);
			System.exit(0);
		}
		if (args[0].equals("installLinux"))
		{   
			if (args.length<3) {System.out.println("Please read the manual for usage instructions."); System.exit(0);}
			String installDir="";
			for (int i=2; i<args.length; i++)
			 {installDir = installDir + " " + args[i];}	
			installDir=installDir.trim();
			installLinux(args[1],installDir);
			System.exit(0);
		}		
		//should never be reached, since the program is called by appropriate batch files
		System.out.println("Please read the manual for usage instructions.");
		System.exit(1);
	}
	
	/**tagging*/
	private final static void tag(String args[])
	{
		try
		{
			//load default values for parameters
			Hashtable params=new Hashtable();
			loadParameters(params);
			//parse command line
			parseParameters(args,params);
			
			//get values
			File infile=new File(getParam("inFile",params));
			File outfile=new File(getParam("outFile",params));
			File paramFile=new File(getParam("paramFile",params));
			String ruleF=null;
			try
			 {ruleF = getParam("ruleFile",params);}
			catch (Exception e) {}
			File ruleFile=null;
			if (ruleF != null) ruleFile=new File(ruleF);
			int ambs=Integer.parseInt(getParam("argmaxBufSize",params));
			int nRanks=Integer.parseInt(getParam("nRanks",params));
			float beamWin=Float.parseFloat(getParam("beamWindow",params));
			String wdir=getParam("wDir",params);
			String morphPath=getParam("morphPath",params);
			//CONSTRUCT THE TAGGER OBJECT
			PosTagger tagger=new PosTagger(nRanks,paramFile,ambs,wdir,beamWin,morphPath);
			TokenStream ts=new OneWperLTS(infile);
			String del=getParam("delimiters",params);
			String[] delimit=split(del,'$');
			System.out.println("tagging...");
			//CALL THE TAGGING METHOD
			ResultStream rs=tagger.tag(ts,delimit,true); 
			//CONSTRUCT THE POSTPROCESSOR AND WRAP IT AROUND THE RESULTSTREAM OBJECT, 
			//IF A RULE FILE HAS BEEN SPECIFIED
			if (ruleFile!=null)
			{
				RuleBasedPostProcessor pp=new RuleBasedPostProcessor(rs,delimit,ruleFile);
				rs=pp;
			}
			//generate result file
			TaggedResultWord tw=null;
			PrintWriter pw=new PrintWriter(new FileOutputStream(outfile));
			while ((tw=rs.nextWord()) != null)
			{
				String line="";
				String tab=new Character((char)9).toString();
				line=line+tw.getWord();
				line=line+tab;
				for (int i=0; i<nRanks; i++)
				{
					line=line+tw.getStringTag(i);
					if (i!=nRanks-1) line=line+tab;
				}
				pw.println(line);
			}
			pw.flush();
			pw.close();
			System.out.println("done");
			
		}
		catch (Exception e) {System.out.println("Error: " + e.getMessage() + "(" + e.getClass() +")"); e.printStackTrace();}
	}
	
	
	
	
	/**training*/
	private final static void train(String args[])
	{
		try
		{
        //load default values for parameters
		Hashtable params=new Hashtable();
		loadParameters(params);
		//parse command line
		parseParameters(args,params);
		
        String corpusFile=getParam("taggerTrainFile",params);
        String PPTrainFile=null;
        try
		{
        	PPTrainFile=getParam("PPTrainFile",params);
		} catch (Exception e) {PPTrainFile=null;}
        String openClassesFile=getParam("openClassesFile",params);
        String capClassesFile=getParam("capClassesFile",params);
        String tagMapFile=getParam("tagMapFile",params);
        String eosTag=getParam("eosTag",params);
        String numberTag=getParam("numberTag",params);
        String ordNumberTag=getParam("ordNumberTag",params);
        String taggerOutputFile=getParam("taggerOutFile",params);
        String PPOutputFile=null;
        String[] tssFiles=null;
        if (PPTrainFile != null)
        {      	
        	PPOutputFile=getParam("PPOutFile",params);
        	tssFiles=split(getParam("tagSubSetFiles",params),'$');
        	
        }	
	
		//collect all tags
		Hashtable tags=new Hashtable();
		File tgTFile=new File(corpusFile);
		BufferedReader instr=new BufferedReader(new InputStreamReader(new FileInputStream(tgTFile)));
		String line=null;
		int lineNumber=0;
		while  ( (line=instr.readLine()) != null)
		{
			String[] pair=line.split(new Character((char)9).toString());
			if (pair.length != 2) throw new Exception("incorrect line (nr "+lineNumber + ") in tagger training file");
			tags.put(pair[1],"");
			lineNumber++;
		}
		instr.close();
		
		instr=new BufferedReader(new InputStreamReader(new FileInputStream(new File(tagMapFile))));
		line=null;
		lineNumber=0;
		while  ( (line=instr.readLine()) != null)
		{
			String[] pair=line.split(new Character((char)9).toString());
			if (pair.length != 2) throw new Exception("incorrect line (nr "+lineNumber + ") in tag map file");
			tags.put(pair[0],"");
			lineNumber++;
		}
		instr.close();
		
		if (PPTrainFile !=null)
		{
			instr=new BufferedReader(new InputStreamReader(new FileInputStream(new File(PPTrainFile))));
			line=null;
			lineNumber=0;
			while  ( (line=instr.readLine()) != null)
			{
				String[] pair=line.split(new Character((char)9).toString());
				if (pair.length != 2) throw new Exception("incorrect line (nr "+lineNumber + ") in post-processor training file");
				tags.put(pair[1],"");
				lineNumber++;
			}
			instr.close();
			
			for (int i=0; i<tssFiles.length; i++)
			{
				if (!(tssFiles[i].equals("*")))
				{
					instr=new BufferedReader(new InputStreamReader(new FileInputStream(new File(tssFiles[i]))));
					line=null;
					while  ( (line=instr.readLine()) != null)
					{
						tags.put(line,"");
					}
					instr.close();
				}
			}
		}
		
		
		
		//read open tag file
		ArrayList openTagsList=new ArrayList();
		instr=new BufferedReader(new InputStreamReader(new FileInputStream(new File(openClassesFile))));
		line=null;
		while  ( (line=instr.readLine()) != null)
		{
			tags.put(line,"");
			openTagsList.add(line);
		}
		instr.close();
		//read file containing tags for classes of capitalized words only
		ArrayList capTagsList=new ArrayList();
		instr=new BufferedReader(new InputStreamReader(new FileInputStream(new File(capClassesFile))));
		line=null;
		while  ( (line=instr.readLine()) != null)
		{
			tags.put(line,"");
			capTagsList.add(line);
		}
		instr.close();
		tags.put(eosTag,"");
		tags.put(ordNumberTag,"");
		tags.put(numberTag,"");
		//construct tag table
		TagTable table=new TagTable(tags.size());
		Enumeration en=tags.keys();
		for (int i=0; i<tags.size(); i++)
		{
			table.setTagName((String)en.nextElement(),i);
		}
		
		//construct Lexicon and parameter object
		///parameter
		int order=Integer.parseInt(getParam("contextSize",params));
		int mem=Integer.parseInt(getParam("memory",params));
		float gamTra=Float.parseFloat(getParam("gammaTrans",params));
		float gamIni=Float.parseFloat(getParam("gammaIni",params));
		
		int eosTagInt=table.getTagCode(eosTag);
		TaggedCorpus tc=new OneWordPerLineCorpus(new File(corpusFile),table); 
		tc.initialize();
		
		SimpleTransParameters parameters=null;
		if (getParam("quickTraining",params).equals("no"))
		{
			//get parameter values
			int biS=Integer.parseInt(getParam("ngrTreeBinSizeTH",params));
			float tG=Float.parseFloat(getParam("ngrTreeGain",params));
		
            System.out.println("calculating Ngram tree...this might take a while");
			parameters=new TreeTransParameters(tc,table.getNTags(),order,eosTagInt,biS,tG,gamIni,gamTra,mem);
		}
		else
		{
			System.out.println("calculating quick transition parameters");
		    parameters=new SimpleTransParameters(tc,table.getNTags(),order,eosTagInt,gamIni,gamTra,mem);	
		}
		
		//lexicon
		tc.initialize();
		int[] ot=new int[openTagsList.size()];
		int[] ct=new int[capTagsList.size()];
		for (int i=0; i<ot.length; i++)
		  {ot[i]=table.getTagCode((String) openTagsList.get(i));}
		for (int i=0; i<ct.length; i++)
		  {ct[i]=table.getTagCode((String) capTagsList.get(i));}	
	    TagSynonymSet tagSyn=new TagSynonymSet(table);
	    instr=new BufferedReader(new InputStreamReader(new FileInputStream(new File(tagMapFile))));
		line=null;
		lineNumber=0;
		while  ( (line=instr.readLine()) != null)
		{
			String[] pair=line.split(new Character((char)9).toString());
			if (pair.length != 2) throw new Exception("incorrect line (nr "+lineNumber + ") in tag map");
			tagSyn.addSynonym(pair[0],pair[1]);
			lineNumber++;
		}
	    instr.close();
		
	    String morphPath=getParam("morphPath",params);
	    NegraTagSetDicManipulator dm= new NegraTagSetDicManipulator(table,tagSyn,ct,morphPath);
	    
	    int plen=Integer.parseInt(getParam("prefLen",params));
		int slen=Integer.parseInt(getParam("suffLen",params));
		float prefG=Float.parseFloat(getParam("prefGain",params));
		float suffG=Float.parseFloat(getParam("suffGain",params));
		float prefGa=Float.parseFloat(getParam("prefGamma",params));
		float suffGa=Float.parseFloat(getParam("suffGamma",params));
	    String morphFirst=getParam("morphFirst",params); 
		boolean mf;
		if (morphFirst.equals("no"))
		  {mf=false;}
		else
		  {mf=true;}	
		System.out.println("Calculating dictionary");
		SimpleLexicon lex=new SimpleLexicon(tc,eosTagInt,table.getTagCode(numberTag),table.getTagCode(ordNumberTag),ot,ct,table.getNTags(),tagSyn,dm,morphPath,plen,slen,prefG,suffG,prefGa,suffGa,mf);
		
		HMMParams p=new HMMParams(lex,parameters,table);
		
		//serialize results
		ObjectOutputStream objS=new ObjectOutputStream(new FileOutputStream(taggerOutputFile));
		objS.writeObject(p);
		objS.flush();
		objS.close();
		//construct Postprocessor
		if (PPTrainFile != null)
		{
	
			int n=table.getNTags();
			//templates
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
			
			//read tag subsets
			int[][] tss=new int[tssFiles.length][];
			ArrayList al=new ArrayList();
			String l=null;
			for (int i=0; i<tssFiles.length; i++)
			{
				if (tssFiles[i].equals("*"))
				{
					tss[i]=null;
					continue;
				}
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(tssFiles[i]))));
				while ((l=br.readLine()) != null)
				{
					al.add(l);
				}
				tss[i] = new int[al.size()];
				for (int j=0; j<al.size(); j++)
				{
					tss[i][j]=table.getTagCode( (String) al.get(j) );
				}
			}
			RuleTemplate[][] temp2=new RuleTemplate[tss.length][];
			for (int i=0; i<temp2.length; i++)
			{
				temp2[i]=temp;
			}
			//get parameters
			int ec=Integer.parseInt(getParam("ppErrCountTH",params));
			int sTh=Integer.parseInt(getParam("ppRuleScoreTH",params));
			int[] eca=new int[tss.length];
			int[] sTha=new int[tss.length];
			for (int i=0; i< eca.length; i++)
			{
				eca[i]=ec;
				sTha[i]=sTh;
			}
			//get HMM parameters
			int ambs = Integer.parseInt(getParam("argmaxBufSize",params));
			float beamWin=Float.parseFloat(getParam("beamWindow",params));
			String wd=getParam("wDir",params);
			//�nstantiate HMM tagger
			PosTagger t=new PosTagger(2,p,ambs,wd,beamWin);
			TaggedCorpus tc2=new OneWordPerLineCorpus(new File(PPTrainFile),table);
			File rFile=new File(PPOutputFile);
			String del=getParam("delimiters",params);
			String[] delimit=split(del,'$');
			//calculate rules
			System.out.println("calculating postprocessing rules...this might take a while");
			RuleBasedPostProcessor pp=new RuleBasedPostProcessor(delimit,temp2,t,tc2,table.getNTags(),eca,sTha,tss,true,rFile);	
		}
		System.out.println("done");
		}
		catch (Exception e) {System.out.println("Error: " + e.getMessage() + "(" + e.getClass() +")"); e.printStackTrace();}
	}
	
	/**installation routine*/
	private final static void install(String jvmStackSize,String installDir)
	{
		try
		{
			System.out.println("Installing...");
			String quote=new Character('"').toString();
			//generate settings.ini
			PrintWriter pw=new PrintWriter(new FileOutputStream(new File(installDir+File.separator+"settings.ini")));
			for (int i=0; i<progParams.length; i++)
			{
				if (defaultValues[i] != null) 
				{
					String line=progParams[i] + "=" + defaultValues[i];
					pw.println(line);
				}
			}
			pw.println("morphPath="+installDir+File.separator+"data"+File.separator);
			pw.println("wDir="+installDir+File.separator+"workingDir");

			pw.flush();
			pw.close();
			//generate train.bat
			pw=new PrintWriter(new FileOutputStream(new File(installDir+File.separator+"train.bat")));
			pw.println("java -DTAGGER_PATH=" + quote + installDir + quote + " -Xmx" + jvmStackSize + " -jar " + quote + installDir + File.separator + "bib" + File.separator + "tagger.jar"+ quote + " train %*");
			pw.flush();
			pw.close();
			//generate tag.bat
			pw=new PrintWriter(new FileOutputStream(new File(installDir+File.separator+"tag.bat")));
			pw.println("java -DTAGGER_PATH=" + quote + installDir + quote + " -Xmx" + jvmStackSize + " -jar " + quote + installDir + File.separator + "bib" + File.separator + "tagger.jar" + quote + " tag %*");
			pw.flush();
			pw.close();
			System.out.println("Installation successfull");
			
		}
		catch (Exception e) {System.out.println("Error: " + e.getMessage() + "(" + e.getClass() +")"); e.printStackTrace();}
		
	}
	
	/**installation routine for Linux*/
	private final static void installLinux(String jvmStackSize,String installDir)
	{
		try
		{
			System.out.println("Installing...");
			String quote=new Character('"').toString();
			//generate settings.ini
			PrintWriter pw=new PrintWriter(new FileOutputStream(new File(installDir+File.separator+"settings.ini")));
			for (int i=0; i<progParams.length; i++)
			{
				if (defaultValues[i] != null) 
				{
					String line=progParams[i] + "=" + defaultValues[i];
					pw.println(line);
				}
			}
			pw.println("morphPath="+installDir+File.separator+"data"+File.separator);
			pw.println("wDir="+installDir+File.separator+"workingDir");

			pw.flush();
			pw.close();
			//generate train.sh
			pw=new PrintWriter(new FileOutputStream(new File(installDir+File.separator+"train.sh")));
			pw.println("java -DTAGGER_PATH=" + quote + installDir + quote + " -Xmx" + jvmStackSize + " -jar " + quote + installDir + File.separator + "bib" + File.separator + "tagger.jar"+ quote + " train $*");
			pw.flush();
			pw.close();
			//generate tag.sh
			pw=new PrintWriter(new FileOutputStream(new File(installDir+File.separator+"tag.sh")));
			pw.println("java -DTAGGER_PATH=" + quote + installDir + quote + " -Xmx" + jvmStackSize + " -jar " + quote + installDir + File.separator + "bib" + File.separator + "tagger.jar" + quote + " tag $*");
			pw.flush();
			pw.close();
			System.out.println("Installation successfull");
			
		}
		catch (Exception e) {System.out.println("Error: " + e.getMessage() + "(" + e.getClass() +")"); e.printStackTrace();}
		
	}
	
	/**reads command line parameters*/
	private final static void parseParameters(String[] args,Hashtable tab) throws Exception
	{
		for (int i=0; i<args.length; i++)
		{
			String[] pair=args[i].split("=");
			if (pair.length != 2) throw new Exception("arguments must be of form <name>=<value>");
			tab.put(pair[0],pair[1]);
		}	
	}
	
	/**reads parameters from setting.ini*/
	private final static void loadParameters(Hashtable tab)
	{
		for (int i=0; i<progParams.length; i++)
		{
			try
			{
			  String paramVal= IniFile.getIniFile().getStringVal(progParams[i]);
			  tab.put(progParams[i],paramVal);
			} catch (Exception e) {}
		}
		
	}
	
	/**returns parameter from hashtable*/
	private final static String getParam(String name,Hashtable t) throws Exception
	{
		if (t.containsKey(name))
		  {return (String) t.get(name);}
		else
		  {throw new Exception("missing param: " + name);}	
	}
	
	/**
	 * splits a given string at all occurences of a given character
	 * @param s the string
	 * @param c the char
	 * @return
	 */
	private final static String[] split(String s, char c)
	{
		ArrayList l=new ArrayList();
		int start=0;
		int i;
		for (i=0; i<s.length(); i++)
		{
			if (s.charAt(i) == c)
			{
				if (i-start > 0)
				{
					l.add(s.substring(start,i));
					start=i+1;
				}
			}
		}
		if (start < s.length())
		{
			l.add(s.substring(start,s.length()));
		}
		String[] res = new String[l.size()];
		l.toArray(res);
		return res;
	}
}
