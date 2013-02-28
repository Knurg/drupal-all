package lebada.fs;

import java.io.Serializable;
import java.util.List;

public interface FS extends Serializable {

	public Object get(String key);

	public String getString(String key);

	public Integer getInt(String key);

	public Boolean getBoolean(String key);

	public Long getLong(String key);

	public Double getDouble(String key);

	public FS[] getFSArray(String key);

	public FS getFS(String key);

	public List<? extends FS> getFSList(String key);

	public String[] getFeatureNames();

	public boolean hasFeatureName(String key);

	public String toString();

}