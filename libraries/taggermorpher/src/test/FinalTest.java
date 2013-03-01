package test;

import java.io.*;

/**Performs the test reported in the thesis*/
public class FinalTest 
{
    private final static int[] nTrainS = {38000,30000,20000,15000,10000,5000,1000,800,600,400,200,100,50};
    private final static int[] bWin = {2,5,10,20,30,40,50,60,70,90,110,150};
	private final static String baseDir = "C:" + File.separator+"kurzer" +File.separator + "finalTest2" + File.separator;
	private final static String orderVar_tree_fixtree = baseDir + File.separator+"orderVar_tree_fixtree" + File.separator;
	private final static String orderVar_simple_fixtree = baseDir + File.separator+"orderVar_simple_fixtree" + File.separator;
	private final static String orderVar_simple_morph = baseDir + File.separator + "orderVar_tree_morph" + File.separator;
    private final static String beamWin_acc = baseDir + File.separator + "beamWin_acc" + File.separator;
    private final static String th_acc = baseDir + File.separator + "th_acc" + File.separator;
    private final static float[] tth={5,15,25,35,45,55,65,75,500};
    
	public static void main(String[] args) 
	{
		File bDir=new File(baseDir.substring(0,baseDir.length()-1));
		bDir.mkdirs();
		try
		{
			System.out.println("orderVar_tree_fixtree");
			bDir=new File(orderVar_tree_fixtree.substring(0,orderVar_tree_fixtree.length()-1));
			bDir.mkdirs();
			for (int order=1; order<=3; order++)
			{	
			   System.out.println("Order="+order);
			   bDir=new File(orderVar_tree_fixtree+File.separator+order);
			   bDir.mkdirs();
			   int as;	
			   if (order==3) {as=40;} else {as=120;}	
			   generateQuantPlotFile(new File(orderVar_tree_fixtree+"plot"+order+".dat"),
			   		                 order,
									 false,
									 false,
									 50,
									 as,
									 orderVar_tree_fixtree+File.separator+order+File.separator+"params",
									 orderVar_tree_fixtree+File.separator+order+File.separator+"results",0);
			}   
			System.out.println("orderVar_simple_fixtree");
			bDir=new File(orderVar_simple_fixtree.substring(0,orderVar_simple_fixtree.length()-1));
			bDir.mkdirs();			
			for (int order=1; order<=3; order++)
			{	
			   System.out.println("Order="+order);	
			   bDir=new File(orderVar_simple_fixtree+File.separator+order);
			   bDir.mkdirs();	
			   int as;	
			   if (order==3) {as=40;} else {as=120;}	
			   generateQuantPlotFile(new File(orderVar_simple_fixtree+"plot"+order+".dat"),
			   		                 order,
									 true,
									 false,
									 50,
									 as,
									 orderVar_simple_fixtree+File.separator+order+File.separator+"params",
									 orderVar_simple_fixtree+File.separator+order+File.separator+"results",0);
			}   
			System.out.println("orderVar_simple_morph");
			bDir=new File(orderVar_simple_morph.substring(0,orderVar_simple_morph.length()-1));
			bDir.mkdirs();				
			for (int order=1; order<=3; order++)
			{	
			   System.out.println("Order="+order);	
			   bDir=new File(orderVar_simple_morph+File.separator+order);
			   bDir.mkdirs();			   
			   int as;	
			   if (order==3) {as=40;} else {as=120;}	
			   generateQuantPlotFile(new File(orderVar_simple_morph+"plot"+order+".dat"),
			   		                 order,
									 true,
									 true,
									 50,
									 as,
									 orderVar_simple_morph+File.separator+order+File.separator+"params",
									 orderVar_simple_morph+File.separator+order+File.separator+"results",0);
			}   			
			System.out.println("beamWin_acc");
			bDir=new File(beamWin_acc.substring(0,beamWin_acc.length()-1));
			bDir.mkdirs();				
			for (int order=1; order<=3; order++)
			{	
			   System.out.println("Order="+order);	
			   bDir=new File(beamWin_acc+File.separator+order);
			   bDir.mkdirs();				   
			   int as;	
			   if (order==3) {as=40;} else {as=120;}	
			   generateBWPlotFile(new File(beamWin_acc+"plot"+order+".dat"),
			   		                 order,
									 true,
									 false,
									 38000,
									 as,
									 beamWin_acc+File.separator+order+File.separator+"params",
									 beamWin_acc+File.separator+order+File.separator+"results",0);
			}  
			System.out.println("th_acc");
			bDir=new File(th_acc.substring(0,th_acc.length()-1));
			bDir.mkdirs();				
			for (int order=3; order<=3; order++)
			{	
			   System.out.println("Order="+order);	
			   bDir=new File(th_acc+File.separator+order);
			   bDir.mkdirs();				   
			   int as;	
			   if (order==3) {as=40;} else {as=120;}	
			   generateTHPlotFile(new File(th_acc+"plot"+order+".dat"),
			   		                 order,
									 false,
									 38000,
									 as,
									 th_acc+File.separator+order+File.separator+"params",
									 th_acc+File.separator+order+File.separator+"results",2);
			}  
			
		}
		catch (Exception e) {System.out.println(e.getMessage() +"("+e.getClass().getName()+")");e.printStackTrace();} 
		
		
	}
	
	private static void generateQuantPlotFile(File plotFile,int order,boolean quick,boolean morphFirst,int beamWindow,int argmaxSize,String parameterFile,String resFile,int begin)throws Exception	
	{
		PrintWriter pw=new PrintWriter(new BufferedOutputStream(new FileOutputStream(plotFile)));
		float acc;
		for (int i=begin; i<nTrainS.length; i++)
		{
			acc=TestRun.test(nTrainS[i],order,quick,morphFirst,beamWindow,argmaxSize,new File(parameterFile+nTrainS[i]+".prm"),new File(resFile+nTrainS[i]+".res"),50);
			pw.println(nTrainS[i]+" "+acc);
		}
		pw.flush();
		pw.close();
	}
	
	private static void generateBWPlotFile(File plotFile,int order,boolean quick,boolean morphFirst,int nSent,int argmaxSize,String parameterFile,String resFile,int begin)throws Exception	
	{
		PrintWriter pw=new PrintWriter(new BufferedOutputStream(new FileOutputStream(plotFile)));
		float acc;
		for (int i=begin; i<bWin.length; i++)
		{
			acc=TestRun.test(nSent,order,quick,morphFirst,bWin[i],argmaxSize,new File(parameterFile+bWin[i]+".prm"),new File(resFile+bWin[i]+".res"),50);
			pw.println(bWin[i]+" "+acc);
		}
		pw.flush();
		pw.close();
	}
	private static void generateTHPlotFile(File plotFile,int order,boolean morphFirst,int nSent,int argmaxSize,String parameterFile,String resFile,int begin)throws Exception	
	{
		PrintWriter pw=new PrintWriter(new BufferedOutputStream(new FileOutputStream(plotFile)));
		float acc;
		for (int i=begin; i<tth.length; i++)
		{
			acc=TestRun.test(nSent,order,false,morphFirst,100,argmaxSize,new File(parameterFile+tth[i]+".prm"),new File(resFile+tth[i]+".res"),tth[i]);
			pw.println(tth[i]+" "+acc);
		}
		pw.flush();
		pw.close();
	}
	
}
