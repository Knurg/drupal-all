package postprocessor.transformer.templates;

import postprocessor.transformer.RuleInstance;
import postprocessor.transformer.RuleTemplate;
import postprocessor.transformer.instances.OnePositionConstraintInst;

/**
template class for rule class <pre>OnePositionConstraintInst</pre>
 */
public class OnePositionConstraint extends RuleTemplate 
{
   /**offset*/	
   protected int position;
   /**buffer of rule instances*/
   protected OnePositionConstraintInst inst[]=null;
   /**buffer of rule instances for tag subset*/
   protected OnePositionConstraintInst subView[]=null;
   /**tag subset*/
   protected int[] tagSubSet=null;
   /**flag indicating if all instances have been returned*/
   protected boolean hasNext=false;
   	
   /**constructor
    * 
    * @param nTags number of tags
    * @param pos offset
    * @param p flag indicating if the rule condition is to be logically inverted
    */
   public OnePositionConstraint(int nTags,int pos,boolean p)
   {
   	  super(nTags,p);
   	  position=pos;
   	  inst=new OnePositionConstraintInst[nTags];
   	  for (int i=0; i<inst.length; i++)
   	  {	
   	     inst[i]=new OnePositionConstraintInst(0,0,position,i,p);
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
   	   	  subView=new OnePositionConstraintInst[tss.length];
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
