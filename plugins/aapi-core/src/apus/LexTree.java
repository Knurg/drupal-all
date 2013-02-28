package apus;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

public abstract class LexTree {
	
	public abstract LexTree get(char c);
	public abstract LexTree ensure(char c);
	public abstract void remove(char c);
	public abstract char[] getAllChars();
	
	public Object info;
	
	/**
	 * 
	 * @param cs
	 * @return the info object or <code>null</code> if not contained
	 */
	public LexTree get (CharSequence cs) {
		if (cs == null) throw new NullPointerException("cs may not be null");
		LexTree l = this;
		for (int i = 0; i < cs.length(); i++) {
			l = l.get(cs.charAt(i));
			if (l == null) return null;
		}
		return l;
	}
	
	
	/**
	 * 
	 * @param cs
	 * @return Object[]{prefix as CharSeq, remaining chars as CharSeq, LexTree of prefix}
	 */
	public Object[] getLongestContained(CharSequence cs) {
		if (cs == null) throw new NullPointerException("cs may not be null");
		LexTree l = this;
		for (int i = 0; i < cs.length(); i++) {
			LexTree ln = l.get(cs.charAt(i));
			if (ln == null) {
				return new Object[]{
						cs.subSequence(0, i) ,
						cs.subSequence(i, cs.length()) ,
						l
				};
			}
			l = ln;
		}
		return new Object[]{cs, "", l};
	}
	
	public LexTree ensure(CharSequence cs) {
		if (cs == null) throw new NullPointerException("cs may not be null");
		LexTree l = this;
		for (int i = 0; i < cs.length(); i++) {
			l = l.ensure(cs.charAt(i));
		}
		return l;
	}
	
	
	public String toString() {
		return toString("");
	}
	
	private final String toString(String p) {
		StringBuilder b = new StringBuilder(p + "[");
		char[] ca = getAllChars();
		if (ca.length == 0) b.append("]");
		else {
			b.append("\n");
			for (char c: getAllChars()) {
				b.append(" ").append(p).append(c).append(": ").append(get(c).info).append("\n");
				if (get(c) != null) {
					b.append(get(c).toString(p + "    "));
					b.append("\n");
				}
			}
			b.append(p + "]");
		}
		return b.toString();
	}
	
	
	public static class HashMapImpl extends LexTree {

		HashMap<Character, HashMapImpl> l = new HashMap<Character, HashMapImpl>();

		@Override
		public LexTree ensure(char c) {
			if (! l.containsKey(c)) l.put(c, new HashMapImpl());
			return l.get(c);
		}

		@Override
		public char[] getAllChars() {
			Set<Character> cs = l.keySet();
			char[] ca = new char[cs.size()];
			int i = 0;
			for (Character c: cs) {
				ca[i++] = c;
			}
			return ca;
		}

		@Override
		public LexTree get(char c) {
			return l.get(c);
		}

		@Override
		public void remove(char c) {
			l.remove(c);
		}
		
	}
	
	public static class TreeMapImpl extends LexTree {

		TreeMap<Character, TreeMapImpl> l = new TreeMap<Character, TreeMapImpl>();

		@Override
		public LexTree ensure(char c) {
			if (! l.containsKey(c)) l.put(c, new TreeMapImpl());
			return l.get(c);
		}

		@Override
		public char[] getAllChars() {
			Set<Character> cs = l.keySet();
			char[] ca = new char[cs.size()];
			int i = 0;
			for (Character c: cs) {
				ca[i++] = c;
			}
			return ca;
		}

		@Override
		public LexTree get(char c) {
			return l.get(c);
		}

		@Override
		public void remove(char c) {
			l.remove(c);
		}
		
	}
	
}
