package tigerxml;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import tigerxml.Corpus;
import tigerxml.TigerXmlMapper;

public class TigerXmlMapperTest {

	static final String SAMPLE_CORPUS_ID = "sampleTIGER";
	static final String SAMPLE_CORPUS_PATH = "TestData/TigerXML/sampleTIGER.xml";
	static final String XML_OUTPUT_PATH = "TestData/TigerXML/mapper_out.xml";

	@Test
	public void loadingCorpus() {
		Corpus corpus = initCorpus();

		assertThat(corpus, notNullValue());
		assertThat(corpus.getId(), is(SAMPLE_CORPUS_ID));
	}

	private Corpus initCorpus() {
		Corpus result = null;
		try {
			result = TigerXmlMapper.load(SAMPLE_CORPUS_PATH);
		} catch (IOException e) {
			fail("IOException was thrown: " + e.getMessage());
		} catch (JAXBException e) {
			fail("JAXBException was thrown: " + e.getMessage());
		}
		return result;
	}
	
	@Test
	public void savingCorpus() {
		Corpus corpus = buildCorpus();
		saveCorpus(corpus);
	}

	private void saveCorpus(Corpus corpus) {
		try {
			TigerXmlMapper.save(corpus, XML_OUTPUT_PATH);
		} catch (IOException e) {
			fail("Saving threw IOException: '" + e.getMessage() + "'");
		} catch (JAXBException e) {
			fail("Saving threw JAXBException: '" + e.getMessage() + "'");
		}
	}

	private tigerxml.Corpus buildCorpus() {
		tigerxml.Corpus result = new Corpus();
		result.addSentence(buildSentence());
		return result;
	}

	private tigerxml.Sentence buildSentence() {
		tigerxml.Sentence result = new tigerxml.Sentence();
		result.addTerminal(new T());
		return result;
	}
}
