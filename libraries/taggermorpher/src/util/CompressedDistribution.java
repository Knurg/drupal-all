package util;




import hmmtagger.tagger.LexProb;

import java.util.ArrayList;


/**
Compressed prob. vector
 */
public class CompressedDistribution implements java.io.Serializable,Distribution 
{
   /**list of entries*/
   private ArrayList lpAl=null;
   /**default entry*/
   private float nullValue=0;
   
   /**constructor
    * 
    * @param l list of entries
    * @param nl default entry
    */
   public CompressedDistribution(ArrayList l,float nl)
   {
   	  this.lpAl=l;
   	  nullValue=nl;
   }
   
   /**constructor
    */
   public CompressedDistribution()
   {
   	  lpAl=new ArrayList();
   }
   
   /**constructor
    * 
    * @param f array of prob.
    */
   public CompressedDistribution(float[] f)
   {
   	  this();
   	  for (int i=0; i<f.length; i++)
   	  {
   	  	setProb(i,f[i]);
   	  }
   }
   
   /**
	 * @see util.Distribution#getProb(int)
	 */
   public final float getProb(int t)
   {
   	  for (int i=0; i<lpAl.size(); i++)
   	  {
   	  	LexProb l=(LexProb) lpAl.get(i);
   	  	if (l.s == t) {return l.p;}
   	  }
   	  return nullValue;
   }
   
   
   /** 
	 * @see util.Distribution#incr(int)
	 */
   public final void incr(int t)
   {
   	 for (int i=0; i<lpAl.size(); i++)
 	 {
 	  	LexProb l=(LexProb) lpAl.get(i);
 	  	if (l.s == t) {l.p++; return;}
 	 }
   	 LexProb l=new LexProb(t,(float) 1.0);
   	 lpAl.add(l);
   }
   
   /** 
	 * @see util.Distribution#setProb(int, float)
	 */
   public final void setProb(int t,float value)
   {
   	 for (int i=0; i<lpAl.size(); i++)
	 {
	  	LexProb l=(LexProb) lpAl.get(i);
	  	if (l.s == t) 
	  	{
	  		if (value==(float) 0.0)
	  		{	
	  		   lpAl.remove(i);
	  		   return;
	  		}
	  		else
	  		{	
	  		   l.p=value; 
	  		   return;
	  		}   
	  	}
	 }
   	 if (value==(float) 0.0) return;
  	 LexProb l=new LexProb(t,(float) value);
  	 lpAl.add(l);
   }
   
   /** 
	 * @see util.Distribution#addOneEstimation(float, int)
	 */
   public final void addOneEstimation(float gamma,int NBins)
   {
   	    float total=0;
   	    for (int i=0; i<lpAl.size(); i++)
   	    {
   	   	 total += ((LexProb) lpAl.get(i)).p;
   	    }
	   	if (total != 0)
		{	
			for (int i=0; i<lpAl.size(); i++)
			{
				LexProb l = (LexProb) lpAl.get(i);
				l.p = (float) Math.log( (l.p+gamma) /( (float) total + gamma * ((float) NBins) ) );
			}	
			nullValue=(float) Math.log( (0.0+gamma) /( (float) total + gamma * ((float) NBins) ) );
		}
		else
		{
			nullValue = (float) Math.log( ((float) 1) / ((float) NBins) );
		}  
   }
   
   /**@see util.Distribution#Mle(int)*/
   public final void Mle(int n)
   {
   	   addOneEstimation((float) 0.0,n);
	    
   }
   
}
