package nerimd.annotation;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.fau.cs8.mnscholz.util.ArraysUtil;

/**
 * 
 * Uses properties:<p>
 * <table border="1">
 * <tr><th>Name<th>Mandatory<th>Default<th>Description</tr>
 * <tr><td>nerimd.annotation.annotators.TokenAnnotator.posSet<td>no<td><code>null</code><td>Name of the POS set to be used. This must be an implementation of {@code POSSet}. If none is specified invoking the corresponing methods on a {@code Token} will result in a {@code NullPointerException}</tr>
 * </table> 
 * 
 * @author martini
 *
 */
public abstract class TokenAnnotator extends Annotator {
	
	public final class Token {
		
		public final String surface, lemma, pos;
		/**This is a shortcut for <code>(lemma == null) ? surface : lemma</code>.
		 * It is provided as annotators normally make use of the base form of a word, and fall back to the surface form if the former one is not given.
		 */
		public final String token;
		private int idx;
		private Token[] tokens;
		protected final Annotation annotation;
		
		public Token prev () { return (idx == 0) ? null : tokens[idx - 1]; }
		public Token next () { return (idx + 1 == tokens.length) ? null : tokens[idx + 1]; }
		public Token sibling (int offset) {
			int i = idx + offset;
			if (i < 0 || i >= tokens.length) return null;
			return tokens[i];
		}
		
		public Annotation[] getSurroundingAnnotations (AnnotationType... types) {
			Annotation[] a1 = annotation.owner.getAnnotations(annotation.start, types);
			Annotation[] a2 = annotation.owner.getAnnotations(annotation.end - 1, types);
			for (int i = 0; i < a1.length; i++) if (ArraysUtil.indexOf(a2, a1[i]) == -1) a1[i] = null;
			return ArraysUtil.copyOmitNull(a1);
		}
		
		/**Returns the offset to that token such that <code>sibling(getOffset(t)) == t</code>.
		 */
		public int getOffset (Token t) {
			int i = ArraysUtil.indexOf(tokens, t);
			if (i == -1) throw new IllegalArgumentException();
			return i - idx;
		}
		
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isVerb () { return posSet.isVerb(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isNoun () { return posSet.isNoun(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isNamedEntity () { return posSet.isNamedEntity(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isAdjective () { return posSet.isAdjective(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isAdverb () { return posSet.isAdverb(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isPreposition () { return posSet.isPreposition(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isPronoun () { return posSet.isPronoun(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isPossPronoun () { return posSet.isPossPronoun(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isDetArticle () { return posSet.isDetArticle(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isUndetArticle () { return posSet.isUndetArticle(pos); }
		/**Throws a <code>NullPointerException</code> if the POS set of the underlying <code>TokenAnnotator</code> is <code>null</code>.
		 */
		public boolean isNumber () { return posSet.isNumber(pos); }
		
		
		public Token (Annotation anno, int idx, Token[] tokens) {
			this.surface = anno.surface();
			this.lemma = anno.attr("lemma");
			this.pos = anno.attr("POS");
			this.idx = idx;
			this.tokens = tokens;
			this.annotation = anno;
			this.token = (this.lemma == null) ? this.surface : this.lemma;
		}
		
	}
	
	
	public final POSSet posSet;
	
	
	public TokenAnnotator(Properties p) {
		super(p);
		
		POSSet s = null;
		try { s = (POSSet) Class.forName(this.properties.getProperty("nerimd.annotation.annotators.TokenAnnotator.posSet", "")).newInstance();
		} catch (Exception e) {
			throw new AnnotatorInitializationException("loading POS set " + this.properties.getProperty(this.getClass().getCanonicalName() + ".posSet", ""), e);
		}
		posSet = s;
		
	}

	@Override
	public final void annotate(Text text) {
		
		Annotation[] annoto = text.getAnnotations(AnnotationType.TOKEN);
		Token[] tokens = new Token[annoto.length];
		
		for (int i = 0; i < tokens.length; i++) {
			Annotation a = annoto[i];
			tokens[i] = new Token(a, i, tokens);
		}
		
		annotateTokens(tokens);
		
	}
	
	
	protected abstract void annotateTokens (Token[] tokens);
	
	public Annotation makeAnno (AnnotationType type, Map<String, String> attrs, Token firsttoken, Token lasttoken, Set<Annotation> enclosed) {
		if (type == null || firsttoken == null || lasttoken == null) return null;
		if (attrs == null) attrs = Collections.emptyMap();
		
		return firsttoken.annotation.owner.addAnnotation(type, firsttoken.annotation.start, lasttoken.annotation.end, attrs, enclosed);
	}


}
