package conll;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsNull.*;
import org.junit.*;
import java.io.*;


public class ConllMapperTest {

	static final String TEST_FILE = "src/test/data/CoNLL/danish_ddt_test_blind.conll";
	static final String OUTPUT_FILE = "src/test/data/CoNLL/output.conll";

	@Test
	public void createFromTestFile() {
		Corpus corpus = null;
		try {
			corpus = ConllMapper.load(TEST_FILE);
		} catch (FileNotFoundException e) {
			fail("Creation threw FileNotFoundException: '" + e.getMessage() + "'");
		} catch (IOException e) {
			fail("Creation threw IOException: '" + e.getMessage() + "'");
		}
		assertThat(corpus, notNullValue());
	}

	@Test
	public void saveToFile() {
		Corpus corpus = new Corpus("");
		Sentence s1 = new Sentence();
		Sentence s2 = new Sentence();
		s1.addToken(new Token("1\tDaarvoor\tdaarvoor\tAdv\tAdv\tpron|aanw\t_\t_\t_\t_"));
		s1.addToken(new Token("2\tis\tben\tV\tV\thulpofkopp|ott|3|ev\t_\t_\t_\t_"));
		s2.addToken(new Token("1\tOok\took\tAdv\tAdv\tgew|geenfunc|stell|onverv\t_\t_\t_\t_"));
		corpus.addSentence(s1);
		corpus.addSentence(s2);
		try {
			ConllMapper.save(corpus, OUTPUT_FILE);
		} catch (IOException e) {
			fail("Saving threw IOException: '" + e.getMessage() + "'");
		}
	}
}
