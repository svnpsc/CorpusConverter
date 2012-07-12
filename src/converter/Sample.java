package converter;

import java.io.IOException;
import javax.xml.bind.JAXBException;

public class Sample {

	public void convertFromTigerXmlToFramenet() {
		// Step 1: Create a Java Object from a corpus file 
		tigerxml.Corpus tigerCorpus = null;
		try {
			tigerxml.TigerXmlMapper.load("<Path to TigerXML corpus file>");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		//	You may make some changes to the corpus object here.
		
		// Step 2: Create a converter from the source corpus object
		converter.TigerXmlConverter converter = new converter.TigerXmlConverter(tigerCorpus);
		
		// Step 3: Export it to the source format (FrameNet here)
		framenet.corpus.Corpus framenetCorpus = converter.toFrameNet("<corpus name>");
		//	Additional changes to the FrameNet corpus object can be made
		
		// Step 4: Save the object to a file
		try {
			framenet.FrameNetMapper.save(framenetCorpus, "<Path to FrameNet corpus file>");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
