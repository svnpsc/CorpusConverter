//
// Reference: http://ilk.uvt.nl/conll/index.html
//


package conll;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Sentence {

	private static final String TOKEN_DELIMITER = "\n";

	private List<Token> tokens;

	public Sentence() {
		this.tokens = new LinkedList<Token>();
	}

	public void addToken(Token token) {
		token.setId(tokens.size() + 1);
		tokens.add(token);
	}

	/**
	 * 
	 * @return A sentence's CoNLL string representation like in .conll files. That means tokens separated by line feeds. 
	 */
	@Override
	public String toString() {
		if (tokens.isEmpty()) {
			return "";
		}
		return getStringOfSeparatedTokens();
	}

	private String getStringOfSeparatedTokens() {
		StringBuilder result = new StringBuilder();
		for (Token t : tokens) {
			result.append(t.toString());
			result.append(TOKEN_DELIMITER);
		}
		return result.toString();
	}

	/**
	 * 
	 * @return Iterator over all contained tokens.
	 */
	public Iterator<Token> getTokenIterator() {
		return tokens.iterator();
	}
}
