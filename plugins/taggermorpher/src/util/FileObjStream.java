package util;

import java.io.*;
import java.util.Vector;

/**
* stream of objects, with an underlying file
 */
public class FileObjStream implements ObjStream 
{	
	/**the file*/
	private File file=null;
	/**stream to access the file*/
	private ObjectInputStream in=null;
	
	/**
	 *constructor 
	 * @param f the file
	 * @throws IOException
	 */
	public FileObjStream(File f) throws IOException
	{
		file=f;
		in=new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
	}
	
	/**
	 * @see util.ObjStream#next()
	 */
	public final Object next() throws IOException
	{
		Object res=null;
		if (in != null)
		{   
			try
			   {res = in.readObject();}
			catch (ClassNotFoundException e) {}
			catch (EOFException e) {res=null;}
			if (res == null)
			{
				in.close();
				in=null;
				file.delete();
				return null;
			}
			return res;
		}
		return null;
	}

	
	/**
	 * @see util.ObjStream#toArray()
	 */
	public final Object[] toArray() throws IOException
	{
		if (in==null) return null;  
		ObjectInputStream instr=new ObjectInputStream(new FileInputStream(file));
		Vector res=new Vector();
		Object[] resArray=null;
		Object tw=null;
		try
		{  
		   try
		   {
		     while (true)
		     {
		   	    tw = instr.readObject();
			    res.add(tw);
		     }
		   }
		   catch (EOFException e) {}
		   resArray = new Object[res.size()];
		   res.toArray(resArray);
		}
		catch (ClassNotFoundException e) {}
		in.close(); 
		instr.close(); 
		instr=null; 
		in=null;   
		file.delete();
		return resArray;
	}

}
