package util;

import java.io.Serializable;

/**
Encapsulates a not compressed prob. vector
 */
public class ArrayDistribution implements Distribution,Serializable 
{
	/**prob. vector*/
	private float[] dist=null;

	/**constructor
	 * 
	 * @param n number of elements
	 */
	public ArrayDistribution(int n)
	{
		dist=new float[n];
		for (int i=0; i<n; i++)
		{
			dist[i]=0;
		}
	}
	
	/**
	 * constructor
	 * @param f prob. vector (which is copied)
	 */
	public ArrayDistribution(float[] f)
	{
		dist=new float[f.length];
		for (int i=0; i<f.length; i++)
		{
			dist[i]=f[i];
		}
	}
	
	/**
	 * @see util.Distribution#getProb(int)
	 */
	public final float getProb(int t) throws Exception 
	{
	    try
		{
	    	return dist[t];
		}catch (Exception e) {throw new Exception("ArrayDistribution.getProb: tag number out of range");}
	}

	public final float[] getArray()
	{
		return dist;
	}
	
	/** 
	 * @see util.Distribution#incr(int)
	 */
	public final void incr(int t) throws Exception 
	{
	    try
		{
	    	dist[t] += 1;
		}catch (Exception e) {throw new Exception("ArrayDistribution.incr: tag number out of range");}

	}

	/** 
	 * @see util.Distribution#addOneEstimation(float, int)
	 */
	public final void addOneEstimation(float gamma, int NBins) throws Exception 
	{
		Utility.addOneEstimation(dist,gamma);
	}
    
	/**@see util.Distribution#Mle(int)*/
	public final void Mle(int n)
	{
		Utility.Mle(dist);
	}
	
	/** 
	 * @see util.Distribution#setProb(int, float)
	 */
	public final void setProb(int t, float value) throws Exception 
	{
		try
		{
	    	dist[t] = value;
		}catch (Exception e) {throw new Exception("ArrayDistribution.setProb: tag number out of range");}

	}

}
