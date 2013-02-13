package converter;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.CoreMatchers.*;
import org.junit.experimental.theories.*;
import org.junit.runner.RunWith;
import org.junit.*;
import framenet.corpus.*;
import framenet.FrameNetMapper;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@RunWith(Theories.class)
public class FrameNetConverterTest {

	private static final String SAMPLE_CORPUS_PATH = "src/test/data/FrameNet/NTICorpus.xml";
	private static final String SAMPLE_CORPUS_TWO_PATH = "src/test/data/FrameNet/SmallTest.xml";
	
	@DataPoints
	public static String[] layerNamesToRemove = { "Target", "FE", "GF", "PT", "Other", "Verb", "Sent", "BNC" }; 
	
	private FrameNetConverter converter = null;
	private Corpus source = null;

	@Test
	public void initConverter() {
		initConverterWithSource(SAMPLE_CORPUS_PATH);

		assertThat(converter.getID(), is(source.getID()));
	}
	
	@Test
	public void convertToTigerXml() throws IOException, JAXBException {
		initConverterWithSource(SAMPLE_CORPUS_TWO_PATH);
		
		tigerxml.Corpus target = converter.toTigerXml();
		tigerxml.TigerXmlMapper.save(target, "src/test/data/FrameNet/SmallTestAsTigerXML.xml");
		
		assertThat(target, notNullValue());
		assertThat(target.getId(), is(source.getID()));
		assertThat(target.getBody().getSentences().size(), is(1));
	}

	@Theory
	public void convertWithNonExistingLayers(String layerName)
	{
		initConverterWithSource(SAMPLE_CORPUS_TWO_PATH);
		
		removeLayer(layerName);
		tigerxml.Corpus target = converter.toTigerXml();
		
		assertThat(target, notNullValue());
	}

	private void removeLayer(String layerName) {
		List<Layer> layers = source.getDocuments().getDocument().get(0).getParagraphs().getParagraph().get(0).getSentences().getSentence().get(0).getAnnotationSets().getAnnotationSet().get(0).getLayers().getLayer();
		for (Layer layer : layers) {
			if (layer.getName().equals(layerName)) {
				layers.remove(layer);
				return;
			}
		}
	}
	
	private void initConverterWithSource(String corpusPath) {
		try {
			loadCorpusAndInitializeConverter(corpusPath);
		} catch (JAXBException e) {
			fail("JAXBException was thrown: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException was thrown: " + e.getMessage());
		}
	}

	private void loadCorpusAndInitializeConverter(String corpusPath) throws IOException, JAXBException {
		source = FrameNetMapper.load(corpusPath);
		converter = new FrameNetConverter(source);
	}
}
