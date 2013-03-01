package util;


/**
*stream of objects, using a simple array
 */
public class ArrayObjStream implements ObjStream 
{

	/**the array*/
	private Object[] obj=null;
	/**pointer to current element*/
	private int current=-1;
	
	/**
	 * constructor
	 * @param o the array
	 */
	public ArrayObjStream(Object[] o)
	{
		obj=o;
	}
	
	/**
	 * @see util.ObjStream#next()
	 */
	public final Object next() 
	{
		current++;
		if (current >=obj.length) {return null;}
		return obj[current];
	}

	/**
	 * @see util.ObjStream#toArray()
	 */
	public final Object[] toArray() 
	{
		return obj;
	}

}
