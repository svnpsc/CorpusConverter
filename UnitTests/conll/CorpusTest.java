package conll;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;
import java.util.*;

public class CorpusTest {

	private static final String TEST_CORPUS_NAME = "Testcorpus.conll";
	private static final String[] TOKEN_LINES = {	"1\tDaarvoor\tdaarvoor\tAdv\tAdv\tpron|aanw\t_\t_\t_\t_",
													"1\tOok\took\tAdv\tAdv\tgew|geenfunc|stell|onverv\t_\t_\t_\t_"	};

	private Corpus corpus = null;

	@Before
	public void setUp() {
		corpus = new Corpus(TEST_CORPUS_NAME);
		for (String s : TOKEN_LINES) {
			corpus.addSentence(newSentenceWithOneToken(s));
		}
	}
	
	@Test
	public void copyConstructorCopiesName() {
		Corpus clone = new Corpus(corpus);
		assertThat(clone.getName(), is(corpus.getName()));
	}
	
	private Sentence newSentenceWithOneToken(String tokenLine) {
		Sentence result = new Sentence();
		result.addToken(new Token(tokenLine));
		return result;
	}
	
	@Test
	public void stringRepresantation() {
		StringBuilder correctStringRepresantation = new StringBuilder();
		for (String token : TOKEN_LINES) {
			correctStringRepresantation.append(token + "\n").append("\n");
		}
		assertThat(corpus.toString(), is(correctStringRepresantation.toString()));
	}

	@Test
	public void stringOfEmptyCorpus() {
		corpus = new Corpus(TEST_CORPUS_NAME);
		assertThat(corpus.toString(), is(""));
	}
	
	@Test
	public void iterateThroughTokens() {
		Iterator<Sentence> it = corpus.getSentenceIterator();
		int index = 0;
		while (it.hasNext()) {
			assertThat(it.next().toString(), is(TOKEN_LINES[index++] + "\n"));
		}
	}
}
