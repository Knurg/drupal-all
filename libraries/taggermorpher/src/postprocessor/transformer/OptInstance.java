package postprocessor.transformer;

/**container class storing a rule instance and its score*/
public class OptInstance 
{
   /**the instance*/	
   RuleInstance instance;
   /**the score*/
   int score;
   
   /**constructor
    * 
    * @param i rule instance
    * @param s score
    */
   public OptInstance(RuleInstance i,int s)
   {
   	  instance=i;
   	  score=s;
   }
}
