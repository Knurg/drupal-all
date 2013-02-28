package apus.tok;


public class Token {
	
	public int type;
	public String surface;
	
	public Token (String surface, int type) {
		this.type = type;
		this.surface = surface;
	}
	
	public boolean isType (int flag) {
		return (type & flag) != 0;
	}
	
	public String toString () {
		return TokenUtil.toString(this);
	}
	
}
