package converter;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.bind.JAXBException;

/**
 * Usage:
 * java -jar ConverterApp.jar -<source format> <source path> -<target format> <target path> [target corpus name]
 *
 * Possible formats:
 * 	cn	CoNLL
 * 	tg	TigerXML
 * 	fn	FrameNet
 * 
 * Target corpus name is only required for FrameNet.
 * 
 * Example: (conversion from a TigerXML corpus into CoNLL format)
 * 	java -jar Converter.jar -tg a_corpus.xml -cn new_corpus.conll
 * 
 */
public class ConverterApp {

	private static final String CONLL_FORMAT_PARAMETER = "-cn";
	private static final String TIGERXML_FORMAT_PARAMETER = "-tg";
	private static final String FRAMENET_FORMAT_PARAMETER = "-fn";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			printUsageInfo();
			return;
		}
		
		String sourceFormat = args[0];
		String sourcePath = args[1];
		String targetFormat = args[2];
		String targetPath = args[3];
		String targetName = args.length == 5 ? args[4] : null;
		
		Object sourceCorpusConverter = null;
		try {
			sourceCorpusConverter = loadSourceCorpus(sourceFormat, sourcePath);
		} catch (Exception e) {
			System.out.println("Error loading source corpus:\n" + e.getMessage());
			return;
		}

		Object targetCorpus = null;
		try {
			targetCorpus = convertCorpus(sourceCorpusConverter, targetFormat, targetName);
		} catch (Exception e) {
			System.out.println("Error converting into specified target corpus format:\n" + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		try {
			saveTargetCorpus(targetCorpus, targetPath, targetFormat);
		} catch (Exception e) {
			System.out.println("Error saving target corpus:\n" + e.getMessage());
			return;
		}
	}
	
	private static void printUsageInfo() {
		System.out.println("Usage:\n" +
							"java -jar ConverterApp.jar -<source format> <source path> -<target format> <target path> [target corpus name]\n" +
							"\n" +
							"Possible formats:\n" +
							"\tcn\tCoNLL\n" +
							"\ttg\tTigerXML\n" +
							"\tfn\tFrameNet\n" +
							"\n" +
							"Target corpus name is only required for FrameNet.\n" +
							"\n" + 
							"Example: (conversion from a TigerXML corpus into CoNLL format)\n" +
							"java -jar Converter.jar -tg a_corpus.xml -cn new_corpus.conll");
	}
	
	private static Object loadSourceCorpus(String format, String path) throws FileNotFoundException, IOException, JAXBException {
		if (format.equals(CONLL_FORMAT_PARAMETER)) return new ConllConverter(conll.ConllMapper.load(path));
		if (format.equals(TIGERXML_FORMAT_PARAMETER)) return new TigerXmlConverter(tigerxml.TigerXmlMapper.load(path));
		if (format.equals(FRAMENET_FORMAT_PARAMETER)) return new FrameNetConverter(framenet.FrameNetMapper.load(path));
		throw new IllegalArgumentException("Illegal source format parameter.");
	}
	
	private static Object convertCorpus(Object sourceCorpusConverter, String format, String name) {
		if (format.equals(CONLL_FORMAT_PARAMETER)) return ((ConvertableIntoConll) sourceCorpusConverter).toConll();
		if (format.equals(TIGERXML_FORMAT_PARAMETER)) return ((ConvertableIntoTigerXml) sourceCorpusConverter).toTigerXml();
		if (format.equals(FRAMENET_FORMAT_PARAMETER)) {
			if (name == null) throw new IllegalArgumentException("FrameNet requires target corpus name as fifth argument.");
			return ((ConvertableIntoFrameNet) sourceCorpusConverter).toFrameNet(name);
		}
		throw new IllegalArgumentException("Illegal target format parameter.");
	}
	
	private static void saveTargetCorpus(Object corpus, String path, String format) throws IOException, JAXBException {
		if (format.equals(CONLL_FORMAT_PARAMETER)) conll.ConllMapper.save((conll.Corpus) corpus, path);
		if (format.equals(TIGERXML_FORMAT_PARAMETER)) tigerxml.TigerXmlMapper.save((tigerxml.Corpus) corpus, path);
		if (format.equals(FRAMENET_FORMAT_PARAMETER)) framenet.FrameNetMapper.save((framenet.corpus.Corpus) corpus, path);
	}
}
