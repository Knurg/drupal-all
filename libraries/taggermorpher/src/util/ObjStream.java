package util;

import java.io.IOException;

/**
*stream of objects
 */
public interface ObjStream 
{
	/**
	 * 
	 * @return the next object
	 * @throws IOException
	 */
	public Object next() throws IOException;
	
	/**
	 * 
	 * @return an array of all objects of this stream
	 * @throws IOException
	 */
	public Object[] toArray() throws IOException;
}
