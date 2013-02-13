package framenet;

import framenet.corpus.Corpus;
import javax.xml.bind.*;

import java.io.*;

public class FrameNetMapper {

	/**
	 * 
	 * @param path Corpus file's path.
	 * @return Corpus Java object to work with.
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static Corpus load(String path) throws IOException, JAXBException {
	    return JAXB.unmarshal(new File(path), Corpus.class);
	}

	/**
	 * 
	 * @param corpus Corpus object to be saved to disk.
	 * @param path Saving path.
	 * @throws IOException
	 * @throws JAXBException
	 */
	public static void save(Corpus corpus, String path) throws IOException, JAXBException {
		JAXBContext context = JAXBContext.newInstance(Corpus.class);
		Marshaller m = context.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		m.marshal(corpus, new FileOutputStream(path));
	}
}
