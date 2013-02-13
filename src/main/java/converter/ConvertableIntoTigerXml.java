package converter;

/**
 * 
 * This interface is implemented by converter classes which convert into the TigerXML format.
 *
 */

public interface ConvertableIntoTigerXml {

	public abstract tigerxml.Corpus toTigerXml();

}