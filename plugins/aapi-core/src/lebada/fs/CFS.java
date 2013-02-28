package lebada.fs;

public interface CFS extends FS {

	public void set(String name, Object value);

	public void unset(String name);

	public void set(String name, int value);

	public void set(String name, long value);

	public void set(String name, boolean value);

	public void set(String name, double value);

}