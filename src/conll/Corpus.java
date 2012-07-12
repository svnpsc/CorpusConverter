//
// Reference: http://ilk.uvt.nl/conll/index.html
//


package conll;

import java.util.*;

public class Corpus {

	private static final String SENTENCE_DELIMITER = "\n";

	private String name;
	private List<Sentence> sentences;
	
	public Corpus(String name) {
		this.name = name;
		this.sentences = new LinkedList<Sentence>();
	}
	
	/**
	 * 
	 * Copy constructor which sets <code>toCopy</code>'s name and contained sentences.
	 */
	public Corpus(Corpus toCopy) {
		this.name = toCopy.name;
		this.sentences = new LinkedList<Sentence>(toCopy.sentences);
	}

	public void addSentence(Sentence sentence) {
		sentences.add(sentence);
	}

	@Override
	public String toString() {
		if (sentences.isEmpty()) {
			return "";
		}
		return getStringOfSeparatedSentences();
	}

	private String getStringOfSeparatedSentences() {
		StringBuilder result = new StringBuilder(); 
		for (Sentence s : sentences) {
			result.append(s.toString());
			result.append(SENTENCE_DELIMITER);
		}
		return result.toString();
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return Iterator over all contained sentences.
	 */
	public Iterator<Sentence> getSentenceIterator() {
		return sentences.iterator();
	}
}
