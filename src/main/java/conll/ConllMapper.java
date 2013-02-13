//
// Reference: http://ilk.uvt.nl/conll/index.html
//


package conll;

import java.io.*;

public class ConllMapper {

	private static final String ENCODING = "UTF8";

	public static Corpus load(String path) throws FileNotFoundException, IOException {
	    return parseFile(path);
	}

	static Corpus parseFile(String path) throws IOException {
	    DataInputStream in = new DataInputStream(new FileInputStream(new File(path)));
	    BufferedReader br = getReaderWithCorrectEncoding(in);
	    Corpus result = new Corpus(path);
		Sentence sentence = new Sentence();
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			if (isNoBlankLine(line)) {
				if (sentence == null) {
					sentence = new Sentence();
				}
				sentence.addToken(new Token(line));
			} else {
				result.addSentence(sentence);
				sentence = null;
			}
	    }
	    in.close();
		return result;
	}
	
	private static BufferedReader getReaderWithCorrectEncoding(InputStream in) throws IOException {
	    return new BufferedReader(new InputStreamReader(in, ENCODING));
	}

	private static boolean isNoBlankLine(String line) {
		return line.length() != 0;
	}

	public static void save(Corpus corpus, String path) throws IOException {
		FileOutputStream fos = new FileOutputStream(path);
		Writer out = new OutputStreamWriter(fos, ENCODING);
		try {
			out.write(corpus.toString());
		} finally {
			out.close();
		}
	}

}
