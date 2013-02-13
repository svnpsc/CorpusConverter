package tigerxml;

import static org.junit.Assert.*;
import org.junit.*;
import java.io.IOException;
import javax.xml.bind.JAXBException;

public class TTest {

	private Corpus corpus = null;
	
	@Before
	public void setUp() {
		corpus = new Corpus();
	}

	@Test
	public void setFeaturevalue() {
		T terminal = new T();
		String featureName = "word";
		terminal.setFeature(featureName, "ein");
		assertEquals("Wrong number of attributes", 1, terminal.getOtherAttributes().size());
		
		try {
			saveCorpusToXml(terminal);
		} catch (Exception e) {
			fail("Saving failed.\n" + e.getMessage());
		}
	}
	private void saveCorpusToXml(T terminal) throws IOException, JAXBException {
		Sentence sentence = new Sentence();
		sentence.addTerminal(terminal);
		corpus.addSentence(sentence);
		TigerXmlMapper.save(corpus, TigerXmlMapperTest.XML_OUTPUT_PATH);
	}
}
