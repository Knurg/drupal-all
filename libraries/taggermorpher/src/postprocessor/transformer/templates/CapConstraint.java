package postprocessor.transformer.templates;

import postprocessor.transformer.RuleInstance;
import postprocessor.transformer.RuleTemplate;
import postprocessor.transformer.instances.CapConstraintInst;

/**
template class for rule class <pre>CapConstraintInst</pre>
 */
public class CapConstraint extends RuleTemplate 
{
	/**buffer of rule instances*/
    protected CapConstraintInst inst[]=null;
    /**flag indicating if all instances have been returned*/
    protected boolean hasNext=false;
	
    /**
     * 
     * @param nT number of tags
     * @param off offset
     * @param p flag indicating if the rule condition is to be logically inverted
     */
	public CapConstraint(int nT,int off,boolean p) 
	{
		super(nT,p);
		inst=new CapConstraintInst[1];
		inst[0]=new CapConstraintInst(0,0,off,p);    
	}
	
	/**
	 * @see postprocessor.transformer.RuleTemplate#setTagSubSet(int[])
	 */
	public void setTagSubSet(int[] tss)
	 {}

	/** 
	 * @see postprocessor.transformer.RuleTemplate#getNextInstances()
	 */
	public RuleInstance[] getNextInstances()
	{  
		if (hasNext)
	   	  {hasNext=false; return inst;}
		return null;	
	 }
	
	/** 
	 * @see postprocessor.transformer.RuleTemplate#reset(int, int)
	 */
	public void reset(int tag, int refTag) 
	{
		inst[0].setSourceTag(tag);
		inst[0].setTargetTag(refTag);
        hasNext=true;

	}

}
