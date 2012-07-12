package tigerxml;

import javax.xml.bind.*;
import java.io.*;

public class TigerXmlMapper {
	
	private static final String SUBCORPUS_EXTERNAL = "subcorpus.xml";

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
		if (corpus.getBody().getSubcorpusOrS().size() > 1) {
			saveCorpusAndSubcorpusSeparately(corpus, path);
		} else {
			saveCorpus(corpus, path);
		}
	}

	private static void saveCorpusAndSubcorpusSeparately(Corpus corpus, String xmlOutputPath) throws IOException, JAXBException {
		Subcorpus subcorpus = extractSubcorpus(corpus);
		Corpus corpusWithReference = createCorpusWithReference(corpus, subcorpus);
		saveSubcorpus(subcorpus, subcorpusXmlOutputPath(xmlOutputPath));
		saveCorpus(corpusWithReference, xmlOutputPath);
	}
	
	private static Subcorpus extractSubcorpus(Corpus corpus) {
		Subcorpus result = new Subcorpus();
		result.setName("embedded corpus");
		result.subcorpusOrS = corpus.getBody().getSubcorpusOrS();
		return result;
	}
	
	private static Corpus createCorpusWithReference(Corpus completeCorpus, Subcorpus referencedSubcorpus) {
		Corpus result = createBodylessCorpus(completeCorpus);
		Subcorpus subcorpusReferece = createSubcorpusReference(referencedSubcorpus);
		result.setSubcorpus(subcorpusReferece);
		return result;
	}
	
	private static Corpus createBodylessCorpus(Corpus completeCorpus) {
		Corpus result = new Corpus();
		result.setId(completeCorpus.getId());
		result.setVersion(completeCorpus.getVersion());
		result.setHead(completeCorpus.getHead());
		return result;
	}
	
	private static Subcorpus createSubcorpusReference(Subcorpus referencedSubcorpus) {
		Subcorpus result = new Subcorpus();
		result.setName(referencedSubcorpus.getName());
		result.setExternal("file:" + SUBCORPUS_EXTERNAL);
		return result;
	}
	
	private static String subcorpusXmlOutputPath(String corpusXmlOutputPath) {
		return corpusXmlOutputPath.substring(0, corpusXmlOutputPath.lastIndexOf("/") + 1) + SUBCORPUS_EXTERNAL;
	}
	
	private static void saveSubcorpus(Subcorpus subcorpus, String xmlOutputPath) throws IOException, JAXBException {
		JAXBContext context = JAXBContext.newInstance(Subcorpus.class);
		Marshaller m = context.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		m.marshal(subcorpus, new FileOutputStream(xmlOutputPath));
	}
	
	private static void saveCorpus(Corpus corpus, String xmlOutputPath) throws IOException, JAXBException {
		JAXBContext context = JAXBContext.newInstance(Corpus.class);
		Marshaller m = context.createMarshaller();
		m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		m.marshal(corpus, new FileOutputStream(xmlOutputPath));
	}
}
