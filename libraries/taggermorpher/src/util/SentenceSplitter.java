package util;


import hmmtagger.tagger.TaggedCorpus;
import hmmtagger.tagger.TaggedWord;

import java.util.ArrayList;

/**
* splits a given training corpus into sentences
 */
public class SentenceSplitter 
{
   /**the corpus*/
   private TaggedCorpus corpus=null;
   /**delimiter tag*/
   private int delimiter;
	
   /**constructor
    * 
    * @param c training corpus
    * @param delimit delimiter tag
    */
   public SentenceSplitter(TaggedCorpus c,int delimit)
   {
   	corpus = c; delimiter=delimit;
   }
   
   /**
    * 
    * @return the next sentence
    * @throws Exception
    */
   public final TaggedWord[] nextSentence() throws Exception
   {
   	  ArrayList l=new ArrayList();
   	  TaggedWord tw=null;
   	  do 
   	  {
   	    tw=corpus.nextToken();
   	    int t=delimiter+1;
   	    if (tw != null)
   	    {	
   	  	   l.add(tw);
   	  	   t=tw.getIntTag();
   	    }   
   	  	if ((t == delimiter) || ((tw==null) && (l.size()>0)))
   	  	{
   	  		TaggedWord[] res=new TaggedWord[l.size()];
   	  		l.toArray(res);
   	  		return res;
   	  	}
   	  }   while (tw != null);
   	  
   	  return null;
   }
   
}
