package framenet;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsNull.*;
import org.junit.Test;
import framenet.corpus.Corpus;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public class FrameNetMapperTest {

	static final String TEST_FILE = "data/FrameNet/NTICorpus.xml";
	
	@Test
	public void createFromTestFile() {
		Corpus corpus = null;
		try {
			corpus = FrameNetMapper.load(TEST_FILE);
		} catch (IOException e) {
			fail("Loading threw IOException: '" + e.getMessage() + "'");
		} catch (JAXBException e) {
			fail("JAXBException was thrown:\n" + e.getMessage());
		}
		assertThat(corpus, notNullValue());
	}

}
