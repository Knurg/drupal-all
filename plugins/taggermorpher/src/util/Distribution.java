package util;

/**
Represents a vector of prob./counts
 */
public interface Distribution 
{      
	   /**
	    * returns a prob.
	    * @param t index of the prob.
	    * @return
	    * @throws Exception
	    */
	   public float getProb(int t) throws Exception;
	   
	   /**
	    * increments an element 
	    * @param t index of the element
	    * @throws Exception
	    */
	   public void incr(int t) throws Exception;
	   
	   /**
	    * performs Lidstone estimation
	    * @param gamma parameter
	    * @param NBins number of bins
	    * @throws Exception
	    */
	   public void addOneEstimation(float gamma,int NBins) throws Exception;
       
	   /**
	    * sets one prob.
	    * @param t index of the prob.
	    * @param value the new value
	    * @throws Exception
	    */
	   public void setProb(int t,float value) throws Exception;
	   
	   /**
	    * performs MLE-Estimation
	    * @param n number of classes
	    */
	   public void Mle(int n);
}
