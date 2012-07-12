package converter;

import static org.junit.Assert.*;

import org.junit.*;
import tigerxml.*;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public class TigerXmlConverterTest {

	private static final String SAMPLE_TIGER_XML_CORPUS_PATH = "TestData/TigerXML/Testcorpus.xml";

	private TigerXmlConverter converter = null;
	private Corpus source = null;

	@Before
	public void setUp() {
		try {
			source = TigerXmlMapper.load(SAMPLE_TIGER_XML_CORPUS_PATH);
		} catch (IOException e) {
			fail("Saving threw IOException: '" + e.getMessage() + "'");
		} catch (JAXBException e) {
			fail("JAXBException was thrown.\n" + e.getMessage());
		}
		converter = new TigerXmlConverter(source);
		assertNotNull("Converter object is NULL after loading", converter);
	}

	@Test
	public void convertToFrameNet() {
		framenet.corpus.Corpus target = converter.toFrameNet("Testcorpus");
		try {
			framenet.FrameNetMapper.save(target, "TestData/TigerXML/TestcorpusAsFrameNet.xml");
		} catch (IOException e) {
			fail("Saving failed. IOException was thrown: " + e.getMessage());
		} catch (JAXBException e) {
			fail("Saving failed. JAXBException was thrown: " + e.getMessage());
		}		
	}
	
	@Test
	public void convertToConll() {
		conll.Corpus target = converter.toConll();
		assertNotNull("NULL returned instead of conll.Corpus object.", target);
		assertEquals("Wrong corpus name.", source.getId(), target.getName());
		try {
			conll.ConllMapper.save(target, "TestData/CoNLL/TestcorpusFromTigerXml.xml");
		} catch (IOException e) {
			fail("Saving failed. IOException was thrown: " + e.getMessage());
		}		
	}
}
