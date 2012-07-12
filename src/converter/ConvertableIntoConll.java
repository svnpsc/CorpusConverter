package converter;

/**
 * 
 * This interface is implemented by converter classes which convert into the CoNLL format.
 *
 */

public interface ConvertableIntoConll {

	public abstract conll.Corpus toConll();

}