package converter;


import tigerxml.*;
import conll.Token;
import conll.Field.Type;
import java.util.*;
import javax.xml.namespace.QName;

import framenet.corpus.AnnotationSet;


/**
 * 
 * <p>This class is used to convert a <code>tigerxml.Corpus</code> object into a <code>framenet.corpus.Corpus</code> object or
 * a <code>conll.Corpus</code> object.</p>
 * 
 * <p>
 * <u>From TigerXML to CoNLL:</u><br>
 * For each <code>T</code> a <code>Token</code> gets created. <code>T</code>'s (and their parent <code>Nt</code>s') features will be 
 * mapped as follows:<br>
 * <table border="1">
 * 	<tr>
 * 		<th><code>T</code>'s feature attribute name</th>
 * 		<th><code>Token</code>'s <code>Field</code> name</th>
 * 	</tr>
 * 	<tr>
 * 		<td><code>word</code></td>
 * 		<td><code>FORM</code></td>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>lemma</code></td>
 * 		<td><code>LEMMA</code></td>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>pos</code></td>
 * 		<td><code>CPOSTAG</code></td>
 * 	</tr>
 * 	<tr>
 * 		<td><i>else</i></td>
 * 		<td><code>FEATS</code></td>
 * 	</tr>
 * </table>
 * </p> 
 *
 * <p>
 * <u>From TigerXML to FrameNet:</u><br>
 * In FrameNet there are more elements with IDs than in TigerXML. So during the conversion they are generated sequentially according to
 * the following scheme of prefixes where the asterisk is replaced by the sequential number:
 * <br>
 * <table border="1">
 * 	<tr>
 * 		<th>Element</th>
 * 		<th><code>id</code> attribute value</th>
 * 	</tr>
 * 	<tr>
 * 		<td><code>Document</code></td>
 * 		<td><i><code>d1</code></i></td>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>Paragraph</code></td>
 * 		<td><i><code>p1</code></i></td>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>AnnotationSet</code></td>
 * 		<td><i><code>as_*</code></i></td>
 * 	</tr>
 * 	<tr>
 * 		<td><code>Layer</code></td>
 * 		<td><i><code>lr_*</code></i></td>
 * 	</tr>
 * 	<tr>
 * 		<td><code>Label</code></td>
 * 		<td><i><code>lb_*</code></i></td>
 * 	</tr>
 * </table>
 * Additionally, embedded elements inherit the complete ID prefix of their parent elements, e.g. the <code>AnnotationSet</code>s' IDs of the third
 * <code>Sentence</code> are <code>as_3_1</code>, <code>as_3_1</code>, <code>as_3_1</code>, ... .<br>
 * The <code>T</code>s' word values get concatenated to FrameNet's <code>text</code> element.<br>
 * All other conversion rules are listed in the following table:
 * <table border="1">
 * 	<tr>
 * 		<th colspan="2">TigerXML</th>
 * 		<th colspan="3">FrameNet</th>
 * 	</tr>
 * 	<tr valign="top">
 * 		<th rowspan="2">element</th>
 * 		<th rowspan="2">attribute</th>
 * 		<th rowspan="2">layer</th>
 * 		<th colspan="2">label</th>
 * 	</tr>
 * 	<tr>
 * 		<th>start</th>
 * 		<th>end</th>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>edge</code></td>
 * 		<td><code>label</code></td>
 * 		<td><code>GF</code></td>
 * 		<td><i>first character below <code>edge</code></i></td>
 * 		<td><i>last character below <code>edge</code></i></td>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>nt</code></td>
 * 		<td><i>used feature</i></td>
 * 		<td><code>PT</code></td>
 * 		<td><i>first character below <code>nt</code></i></td>
 * 		<td><i>last character below <code>nt</code></i></td>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>t</code></td>
 * 		<td><i>for each used feature (except <code>word</code>)</i></td>
 * 		<td><code>Other</code></td>
 * 		<td><i><code>t</code>'s start index in <code>text</code></i></td>
 * 		<td><i><code>t</code>'s end index in <code>text</code></i></td>
 * 	</tr> 
 * </table>
 * </p> 
 *
 */
public class TigerXmlConverter implements ConvertableIntoConll, ConvertableIntoFrameNet {

	private static final String NONTERMINAL_FEATURE_DOMAIN = "NT";

	private static final String DEFAULT_DOCUMENT_ID = "d1";
	private static final String DEFAULT_PARAGRAPH_ID = "p1";
	private static final String DEFAULT_PARAGRAPH_DOCUMENT_ORDER = "1";
	private static final String DEFAULT_ANNOTATIONSET_ID_PREFIX = "as";
	private static final String DEFAULT_ANNOTATIONSET_STATUS = "";
	private static final String DEFAULT_LAYER_ID_PREFIX = "lr";
	private static final String DEFAULT_LABEL_ID_PREFIX = "lb";

	private static final String DEFAULT_WORD_FEATURE_NAME = "word";
	private static final String DEFAULT_LEMMA_FEATURE_NAME = "lemma";
	private static final String DEFAULT_POS_FEATURE_NAME = "pos";
	
	private tigerxml.Corpus source;
	
	private Map<String, Type> conllFieldTypeMappings;
	
	private Map<String, Integer> framenetWordIndexes;

	
	public TigerXmlConverter(Corpus source) {
		this.source = source;
	}

	
	@Override
	public framenet.corpus.Corpus toFrameNet(String name) {
		framenetWordIndexes = new HashMap<String, Integer>();
		return convertToFramenet(name);
	}
	
	private framenet.corpus.Corpus convertToFramenet(String name) {
		framenet.corpus.Corpus result = new framenet.corpus.Corpus();
		result.setID(source.getId());
		result.setName(name);
		result.addDocument(createFramenetDocument(name));
		return result;
	}

	private framenet.corpus.Document createFramenetDocument(String name) {
		framenet.corpus.Document result = new framenet.corpus.Document();
		result.setID(DEFAULT_DOCUMENT_ID);
		result.setDescription(name + "'s content");
		result.addParagraph(createFramenetParagraph());
		return result;
	}
	
	private framenet.corpus.Paragraph createFramenetParagraph() {
		framenet.corpus.Paragraph result = new framenet.corpus.Paragraph();
		result.setID(DEFAULT_PARAGRAPH_ID);
		result.setDocumentOrder(DEFAULT_PARAGRAPH_DOCUMENT_ORDER);
		int sentenceCounter = 1;
		for (CorpusUnit sentence : getCollectedSentences()) {
			result.addSentence(createFramenetSentence(sentenceCounter++, (Sentence) sentence));
		}
		return result;
	}
	
	private framenet.corpus.Sentence createFramenetSentence(int sentenceNumber, Sentence sourceSentence) {
		framenet.corpus.Sentence result = new framenet.corpus.Sentence();
		result.setID(sourceSentence.getId());
		result.setText(createFramenetText(sourceSentence.getTerminals()));

		int asCounter = 1;
		String asIdSuffix = sentenceNumber + "_" + asCounter++;
		AnnotationSet defaultAnnotationSet = createFramenetAnnotationSet(asIdSuffix);
		
		int layerCounter = 1;
		if (source.getHead().getAnnotation().getEdgelabel() != null) {
			defaultAnnotationSet.addLayer(createGfLayer(asIdSuffix, layerCounter++, sourceSentence));
		}
		
		AnnotationSet remainingTerminalFeatures;
		for (Feature feature : source.getHead().getAnnotation().getFeature()) {
			if (feature.getDomain().equals(NONTERMINAL_FEATURE_DOMAIN)) {
				defaultAnnotationSet.addLayer(createPtLayer(asIdSuffix, layerCounter++, sourceSentence, feature.getName()));
			} else if (feature.getName().equals(DEFAULT_WORD_FEATURE_NAME)) {
				continue;
			} else {
				if (layerCounter > 1) {
					defaultAnnotationSet.addLayer(createOtherLayer(asIdSuffix, layerCounter, sourceSentence, feature.getName()));
					result.addAnnotationSet(defaultAnnotationSet);
					layerCounter = 1;
				} else {
					asIdSuffix = sentenceNumber + "_" + asCounter++;
					remainingTerminalFeatures = createFramenetAnnotationSet(asIdSuffix);
					remainingTerminalFeatures.addLayer(createOtherLayer(asIdSuffix, layerCounter, sourceSentence, feature.getName()));
					result.addAnnotationSet(remainingTerminalFeatures);
				}
			}
		}

		return result;
	}
	
	private String createFramenetText(List<T> terminals) {
		StringBuilder result = new StringBuilder();
		int wordIndex = 0;
		String word;
		for (T terminal : terminals) {
			word = terminal.getAttributeValue(DEFAULT_WORD_FEATURE_NAME);
			framenetWordIndexes.put(word, wordIndex);
			wordIndex += word.length() + 1;
			result.append(word + " ");
		}
		return result.toString().trim();
	}
	
	private AnnotationSet createFramenetAnnotationSet(String idSuffix) {
		AnnotationSet result = new AnnotationSet();
		result.setId(DEFAULT_ANNOTATIONSET_ID_PREFIX + idSuffix);
		result.setStatus(DEFAULT_ANNOTATIONSET_STATUS);
		return result;
	}
	
	private framenet.corpus.Layer createGfLayer(String idPath, int layerNumber, Sentence sourceSentence) {
		framenet.corpus.Layer result = createLayer(idPath, layerNumber, "GF");
		int labelCounter = 1;
		for (Nt nonterminal : sourceSentence.getGraph().getNonterminals().getNt()) {
			for (Edge edge : nonterminal.getEdge()) {
				result.addLabel(createGfLabel(layerIdSuffix(idPath, layerNumber), labelCounter++, edge));
			}
		}
		return result;
	}
	
	private framenet.corpus.Layer createPtLayer(String idPath, int layerNumber, Sentence sourceSentence, String featureName) {
		framenet.corpus.Layer result = createLayer(idPath, layerNumber, "PT");
		int labelCounter = 1;
		for (Nt nt : sourceSentence.getGraph().getNonterminals().getNt()) {
			result.addLabel(createPtLabel(layerIdSuffix(idPath, layerNumber), labelCounter++, nt, featureName));
		}
		return result;
	}

	private framenet.corpus.Layer createOtherLayer(String idPath, int layerNumber, Sentence sourceSentence, String featureName) {
		framenet.corpus.Layer result = createLayer(idPath, layerNumber, "Other");
		int labelCounter = 1;
		for (T terminal : sourceSentence.getTerminals()) {
			result.addLabel(createOtherLabel(layerIdSuffix(idPath, layerNumber), labelCounter++, terminal, featureName));
		}
		return result;
	}
	
	private framenet.corpus.Layer createLayer(String idPath, int layerNumber, String name) {
		framenet.corpus.Layer result = new framenet.corpus.Layer();
		result.setID(DEFAULT_LAYER_ID_PREFIX + layerIdSuffix(idPath, layerNumber));
		result.setName(name);
		return result;
	}

	private String layerIdSuffix(String idPath, int layerNumber) {
		return idPath + "_" + layerNumber;
	}

	private framenet.corpus.Label createGfLabel(String idPath, int labelNumber, Edge edge) {
		return createLabel(idPath, labelNumber, edge.getLabel(), edge);
	}

	private framenet.corpus.Label createPtLabel(String idPath, int labelNumber, Nt nonterminal, String featureName) {
		return createLabel(idPath, labelNumber, nonterminal.getAttributeValue(featureName), nonterminal);
	}
	
	private framenet.corpus.Label createOtherLabel(String idPath, int labelNumber, T terminal, String featureName) {
		return createLabel(idPath, labelNumber, terminal.getAttributeValue(featureName), terminal);
	}

	private framenet.corpus.Label createLabel(String idPath, int labelNumber, String name, Object sourceElement) {
		framenet.corpus.Label result = new framenet.corpus.Label();
		result.setID(DEFAULT_LABEL_ID_PREFIX + idPath + "_" + labelNumber);
		result.setName(name);
		result.setStart(String.valueOf(minStart(sourceElement)));
		result.setEnd(String.valueOf(maxEnd(sourceElement)));
		return result;
	}
	
	private int minStart(Object obj) {
		if (obj instanceof T) return startIndex((T) obj);
		if (obj instanceof Edge) return minStart(((Edge)obj).getIdref());
		
		int result = Integer.MAX_VALUE;
		for (Edge edge : ((Nt) obj).getEdge()) {
			if (minStart(edge) < result) {
				result = minStart(edge);
			}
		}
		return result;
	}
	
	private int maxEnd(Object obj) {
		if (obj instanceof T) return endIndex((T) obj);
		if (obj instanceof Edge) return maxEnd(((Edge) obj).getIdref());
		
		int result = 0;
		for (Edge edge : ((Nt) obj).getEdge()) {
			if (maxEnd(edge) > result) {
				result = maxEnd(edge);
			}
		}
		return result;
	}
	
	private int startIndex(T terminal) {
		return framenetWordIndexes.get((terminal).getAttributeValue(DEFAULT_WORD_FEATURE_NAME));
	}
	
	private int endIndex(T terminal) {
		String word = terminal.getAttributeValue(DEFAULT_WORD_FEATURE_NAME);
		return framenetWordIndexes.get(word) + word.length() - 1;
	}


	@Override
	public conll.Corpus toConll() {
		initConllMappings();
		return convertToConll();
	}

	private void initConllMappings() {
		conllFieldTypeMappings = new HashMap<String, Type>();
		conllFieldTypeMappings.put(DEFAULT_WORD_FEATURE_NAME, Type.FORM);
		conllFieldTypeMappings.put(DEFAULT_LEMMA_FEATURE_NAME, Type.LEMMA);
		conllFieldTypeMappings.put(DEFAULT_POS_FEATURE_NAME, Type.CPOSTAG);
	}

	private conll.Corpus convertToConll() {
		conll.Corpus result = new conll.Corpus(source.getId());
		for (CorpusUnit sentence : getCollectedSentences()) {
			result.addSentence(getSentenceFilledWithTerminals((Sentence) sentence));
		}
		return result;
	}

	private conll.Sentence getSentenceFilledWithTerminals(Sentence s) {
		conll.Sentence result = new conll.Sentence();
		for (T t : getTerminals(s)) {
			result.addToken(convertToToken(t));
		}
		return result;
	}

	private List<T> getTerminals(Sentence s) {
		return ((Sentence) s).getGraph().getTerminals().getT();
	}

	private conll.Token convertToToken(T t) {
		conll.Token result = Token.createEmptyToken();
		for (QName attributeQName : getAttributeNames(t)) {
			String attributeName = attributeQName.toString();
			String attributeValue = t.getAttributeValue(attributeQName);
			if (isMappedToFeatureField(attributeName)) {
				result.addFeature(attributeToString(attributeName, attributeValue));
			} else {
				result.setField(conllFieldTypeMappings.get(attributeName), attributeValue);
			}
		}
		return result;
	}
	
	private Set<QName> getAttributeNames(T t) {
		return t.getOtherAttributes().keySet();
	}

	private boolean isMappedToFeatureField(String attributeName) {
		Type fieldToSet = conllFieldTypeMappings.get(attributeName);
		return fieldToSet == null || fieldToSet == Type.FEATS;
	}

	private String attributeToString(String attributeName, String attributeValue) {
		return attributeName + "=" + attributeValue;
	}


	private List<CorpusUnit> getCollectedSentences() {
		List<CorpusUnit> results = new LinkedList<CorpusUnit>();
		for (CorpusUnit cu : getTopLevelCorpusUnits()) {
			results.addAll(cu.getSentences());
		}
		return results;
	}

	private List<CorpusUnit> getTopLevelCorpusUnits() {
		return source.getBody().getSubcorpusOrS();
	}
}
