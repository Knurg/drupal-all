package postprocessor.transformer.templates;

import postprocessor.transformer.RuleInstance;
import postprocessor.transformer.RuleTemplate;
import postprocessor.transformer.instances.OneTagRangeConstraintInst;

/**
template class for rule class <pre>OneTagRangeConstraintInst</pre>
 */
public class OneTagRangeConstraint extends RuleTemplate 
{
   /**frame range*/	
   protected int range;
   /**buffer of rule instances*/
   protected OneTagRangeConstraintInst[] inst=null;
   /**buffer of rule instances for tag subset*/
   protected OneTagRangeConstraintInst[] subView=null;
   /**tag subset*/
   protected int[] tagSubSet=null;
   /**flag indicating if all instances have been returned*/
   protected boolean hasNext=false;
   
   /**constructor
    * 
    * @param nTags number of tags
    * @param r frame range
    * @param p flag indicating if the rule condition is to be logically inverted
    */
   public OneTagRangeConstraint(int nTags,int r,boolean p)
   {  
   	  super(nTags,p);
   	  range=r;
  	  inst=new OneTagRangeConstraintInst[nTags];
   	  for (int i=0; i<inst.length; i++)
   	  {	
   	     inst[i]=new OneTagRangeConstraintInst(0,0,range,i,p);
   	  }   
   }
 
   /** 
	 * @see postprocessor.transformer.RuleTemplate#getNextInstances()
	 */
   public RuleInstance[] getNextInstances()
   {  
   	 if (hasNext)
   	 {
   	   hasNext=false;
   	   if (subView==null)
   	     {return inst;}
   	   else
   	     {return subView;}
   	 }
   	 return null;
   }
   
   /**
	 * @see postprocessor.transformer.RuleTemplate#setTagSubSet(int[])
	 */
   public void setTagSubSet(int[] tss)
   {
   	   tagSubSet=tss;
   	   if (tss!=null)
   	   {
   	   	  subView=new OneTagRangeConstraintInst[tss.length];
   	   	  for (int i=0; i<subView.length; i++)
   	   	  {
   	   	  	subView[i]=inst[i];
   	   	  	subView[i].setTag(tss[i]);
   	   	  }
   	   }
   	   else
   	   {
   	   	 subView=null;
   	   	 for (int i=0; i<inst.length; i++)
   	   	 {
   	   	 	inst[i].setTag(i);
   	   	 }
   	   }
   }
   
   /** 
	 * @see postprocessor.transformer.RuleTemplate#reset(int, int)
	 */
   public void reset(int tag, int refTag)
   {
   	   if (subView!=null)
	   {
	   	   for (int i=0; i<subView.length; i++)
	   	     {subView[i].setSourceTag(tag); subView[i].setTargetTag(refTag);}	
	   }
	   else
	   {
	       for (int i=0; i<inst.length; i++)
	           {inst[i].setSourceTag(tag); inst[i].setTargetTag(refTag);}	
	   }
   	   hasNext=true;
   }
   
   
  
   
}
