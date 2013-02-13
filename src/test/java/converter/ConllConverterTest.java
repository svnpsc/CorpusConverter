package converter;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import org.junit.Test;
import conll.*;
import tigerxml.TigerXmlMapper;
import java.io.IOException;
import javax.xml.bind.JAXBException;

public class ConllConverterTest {

	private static final String SOURCE_PATH = "src/test/data/CoNLL/danish_ddt_test_blind.conll";
	private static final String TARGET_PATH = "src/test/data/TigerXML/fromCoNLL.xml";

	@Test
	public void convertAndSaveToTigerXml() {
		ConllConverter converter = new ConllConverter(loadSource());

		tigerxml.Corpus target = converter.toTigerXml();

		assertThat(target, notNullValue());
		assertThat(target.getId(), is(converter.getName()));
		writeToFile(target);
	}

	private Corpus loadSource() {
		Corpus result = null;
		try {
			result = ConllMapper.load(SOURCE_PATH);
		} catch (Exception e) {
			fail("Loading failed. Exception was thrown.\n" + e.getMessage());
		}
		return result;
	}

	private void writeToFile(tigerxml.Corpus target) {
		try {
			TigerXmlMapper.save(target, TARGET_PATH);
		} catch (IOException e) {
			fail("Saving failed. IOException was thrown: " + e.getMessage());
		} catch (JAXBException e) {
			fail("Saving failed. JAXBException was thrown: " + e.getMessage());
		}		
	}

}
