package hmmtagger.ngramtree;

/**ngram decision tree criterium*/
public class SplitCriterium 
{
  /**index of the addressed ngram element*/	
  protected int pos;
  /**value of the addressed element*/
  protected int val;
	
  /**constructor
   * 
   * @param p index
   * @param v value
   */
  public SplitCriterium(int p, int v)
  {
  	pos=p;
  	val=v;
  }
	
  public boolean equals(Object o)
  {
  	try
	{
  		SplitCriterium c=(SplitCriterium) o;
  		return ( (c.pos == pos) && (c.val == val) );
	} catch (Exception e) {return false;}
	
	
  }
  
}
