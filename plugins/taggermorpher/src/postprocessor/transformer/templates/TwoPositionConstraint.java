package postprocessor.transformer.templates;


import postprocessor.transformer.RuleInstance;
import postprocessor.transformer.RuleTemplate;
import postprocessor.transformer.instances.TwoPositionConstraintInst;


/**
template class for rule class <pre>TwoPositionConstraintInst</pre>
 */
public class TwoPositionConstraint extends RuleTemplate 
{
	/**tag subset*/
	protected int[] tagSubSet=null;
	/**first offset*/
    protected int position;
    /**second offset*/
	protected int position2;
	/**buffer of rule instances*/
	protected TwoPositionConstraintInst[] inst=null;
	/**buffer of rule instances for tag subset*/
	protected TwoPositionConstraintInst[] subView=null;
	/**flag indicating if all instances have been returned*/
	protected boolean hasNext=false;
	
	/**
	 * 
	 * @param nTags number of tags
	 * @param pos first offset
	 * @param pos2 second offset
	 * @param p flag indicating if the rule condition is to be logically inverted
	 */
	public TwoPositionConstraint(int nTags, int pos, int pos2,boolean p) 
	{
		super(nTags,p);
		position = pos;
		position2=pos2;
	   	inst=new TwoPositionConstraintInst[nTags*nTags];
	   	for (int i1=0; i1<nTags; i1++)
	   	{	
	   	  for (int i2=0; i2<nTags; i2++)
	   	  {	
	   	     inst[i1*nTags + i2]=new TwoPositionConstraintInst(0,0,pos,i1,pos2,i2, p);
	   	  }
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
	   	   	  subView=new TwoPositionConstraintInst[tss.length*tss.length];
	   	   	  for (int i1=0; i1<tss.length; i1++)
	   	   	  {
	   	   	  	for (int i2=0; i2<tss.length; i2++)
	   	   	  	{  
	   	   	  		int x=i1*tss.length + i2;
	   	   	  		subView[x]=inst[x];
	   	   	  		subView[x].setTag(tss[i1]);
	   	   	  		subView[x].setTag2(tss[i2]);
	   	   	  	}
	   	   	  	     
	   	   	  }
	   	   }
	   	   else
	   	   {
	   	   	 subView=null;
	   	   	 for (int i1=0; i1<nTags; i1++)
	   	   	 {
	   	   	     for (int i2=0; i2<nTags; i2++)
	   	   	     {
	   	   	     	int x=i1*nTags + i2;
	   	   	     	inst[x].setTag(i1);
	   	   	     	inst[x].setTag2(i2);
	   	   	     }
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
