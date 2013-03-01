package util;


import hmmtagger.api.TagTable;
import hmmtagger.api.TaggedResultWord;
import hmmtagger.ngramtree.NgramItem;
import hmmtagger.tagger.LexProb;
import hmmtagger.tagger.TaggedCorpus;
import hmmtagger.tagger.TaggedWord;

import java.util.*;
import java.io.*;


/**
 *Utility
 */
public class Utility 
{
   /**
    * generates a one word per line training corpus file from a stream of tagged words
    * @param c th the stream
    * @param t tag table
    * @param out the file to be generated
    * @throws Exception
    */	
   public static void generateTrainingsFile(TaggedCorpus c,TagTable t,File out) throws Exception
   {
   	  TaggedWord tw=null;
   	  PrintWriter pw=new PrintWriter(new FileOutputStream(out));
   	  while ( (tw=c.nextToken()) != null )
   	  {
   	  	pw.println(tw.getWord() + (new Character((char)9)).toString() + t.getTagName(tw.getIntTag()) );
   	  }
   	  pw.flush();
   	  pw.close();
   }
   
   /**
    * generates a lexicon file from a given dictionary object
    * @param dic dictionary object
    * @param table tag table
    * @param lexicFile file to be generated
    * @throws Exception
    */
   public static void generateLexicon(hmmtagger.tagger.Dictionary dic,TagTable table,File lexicFile) throws Exception
   {
   	Enumeration en = dic.getEntries();
    PrintWriter pw = new PrintWriter(new FileOutputStream(lexicFile));
    while (en.hasMoreElements())
    {
    	String w = (String) en.nextElement();
    	if ( ! (Character.isDigit(w.charAt(0))) )
    	{	
    	  LexProb[] lp= dic.getDistribution(w);
    	  for (int i=0; i<lp.length; i++)
    	  {
    		  w = w + new Character((char) 9).toString() + table.getTagName(lp[i].s) +" -";
    	  }
    	pw.println(w);
    	}
    }
    pw.flush();
    pw.close();
   }
   
   /**performs Lidstone estimation for a given array of counts
    * 
    * @param c the array
    * @param gamma estimation parameter
    */
   public final static void addOneEstimation(float[] c,float gamma)
   {
		float total = 0;
		for (int i=0; i<c.length; i++)
		{
			total += c[i]; 
		}
		if (total != 0)
		{	
			for (int i=0; i<c.length; i++)
			{
					c[i] = (float) Math.log( (c[i]+gamma) /( (float) total + gamma * ((float) c.length) ) );
		
			}	
		}
		else
		{
			float x = (float) Math.log( ((float) 1) / ((float) c.length) );
			for (int i=0; i<c.length; i++)
			{
				c[i] = x;
			}
		}
   }
   
   /**
    * performs MLE for a given array of counts
    * @param c the array
    */
   public final static void Mle(float[] c)
   {
   	   float total=0;
   	   for (int i=0; i<c.length; i++)
   	   {
   	   	   total += c[i];
   	   }
   	   for (int i=0; i<c.length; i++)
   	   {
   	   	 c[i] = (float) Math.log(c[i]/total);
   	   }
   }
   
   /**calculates entropy for a given array of items
    * 
    * @param items the array
    * @param nClasses number of classes
    * @return the entropy 
    */ 
   public final static float calcEntropy(ClassifiedItem[] items, int nClasses)
   {
   	int nItem= items.length;
   	int[] counts = new int[nClasses];
   	for (int i=0; i<nClasses; i++)  
 	  {counts[i]=0;}                
   	for (int i=0; i<nItem; i++)
   	{
   		counts[items[i].getClassId()]++;
   	}
   	float res=0;
   	for (int i=0; i<nClasses; i++)
   	{
   		 float p= ( (float) counts[i] / (float) nItem );
   		 if (p!=0.0)
   		    {res += p *  ( Math.log( (double) p) / Math.log(2.0) );}
   	}
    return res;
   }
   
   
   //very unelegant...specialized version for ngrams, necessary for performance reasons
   public final static float calcEntropy(NgramItem[] items, int nClasses)
   {
   	int nItem= items.length;
   	int[] counts = new int[nClasses];
   	for (int i=0; i<nClasses; i++)  //
   	  {counts[i]=0;}                //
   	for (int i=0; i<nItem; i++)
   	{
   		counts[items[i].getClassId()]++;
   	}
   	float res=0;
   	for (int i=0; i<nClasses; i++)
   	{
   		 float p= ( (float) counts[i] / (float) nItem );
   		 if (p!=0.0)
   		    {res += p *  ( Math.log( (double) p) / Math.log(2.0) );}
   	}
    return res;
   }
   
// very unelegant...specialized version for ngrams, necessary for performance reasons
   public final static float calcEntropy(float[] counts)
   {
   	int nClasses = counts.length;
   	int nItem = 0;
   	for (int i=0; i<nClasses; i++)
   	  {nItem += counts[i];}
   	float res=0;
   	for (int i=0; i<nClasses; i++)
   	{
   		 float p= ( (float) counts[i] / (float) nItem );
   		 if (p!=0.0)
   		    {res += p *  ( Math.log( (double) p) / Math.log(2.0) );}
   	}
       return res;
   }
   
   
   /**
    * restricts a sequence of tagged words to the ones tagged with tags contained in a given subset of tags
    * @param sentence the sequence
    * @param tags the subset of tags
    * @return the restricted sequence
    */
   public static TaggedResultWord[] project(TaggedResultWord[] sentence, int[] tags)
   {
   	  ArrayList a=new ArrayList();
   	  for (int i=0; i<sentence.length; i++)
   	  {
   		 for (int j=0; j<tags.length; j++)
   		 {
   		 	if (tags[j]==sentence[i].getBestIntTag() )
   		 	  {a.add(sentence[i]);}
   		 }
   	  }
   	  TaggedResultWord[] res=new TaggedResultWord[a.size()];
   	  a.toArray(res);
   	  return res;
   } 

   /**
    * checks if a given string is an element of a given string array
    * @param a the array
    * @param s the string
    * @return true, if it is
    */ 
   public static boolean elementOf(String[] a,String s)
   {
   	 for (int i=0; i<a.length; i++)
   	   {if (a[i].equals(s)) return true;}
   	 return false;
   } 
   
   /**
    * checks if a given integer is an element of a given integer array
    * @param a the array
    * @param s the integer
    * @return true, if it is
    */ 
   public static boolean elementOf(int[] a,int s)
   {
   	 for (int i=0; i<a.length; i++)
   	   {if (a[i]==s) return true;}
   	 return false;
   }  
}
