package converter;

/**
 * 
 * This interface is implemented by converter classes which convert into the FrameNet format.
 *
 */

public interface ConvertableIntoFrameNet {

	public abstract framenet.corpus.Corpus toFrameNet(String name);

}