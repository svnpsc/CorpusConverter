package converter;

import conll.*;
import tigerxml.CorpusUnit;
import tigerxml.Feature;
import tigerxml.Featurevalue;
import tigerxml.T;
import tigerxml.Nt;
import tigerxml.Edge;

import java.util.*;

/**
 * 
 * <p>This class is used to convert a <code>conll.Corpus</code> object into a <code>tigerxml.Corpus</code> object.</p>
 * 
 * <p>
 * Every <code>conll.Token</code> gets transformed into a <code>tigerxml.T</code>-<code>tigerxml.Nt</code> pair with 
 * the <code>Token</code>'s <code>POS</code> field value written into the <code>Nt</code>'s <code>pos</code> attribute. The <code>Token</code>'s 
 * <code>FORM</code> field value becomes the <code>T</code>'s <code>word</code> attribute value. For each <code>FEATS</code> field's entry there will 
 * be a new <code>T</code>'s attribute. All non set attributes will get the constant value <code>"--"</code>.
 * </p> 
 *
 */
public class ConllConverter extends Corpus implements ConvertableIntoTigerXml {

	private static final String TERMINAL_FEATURE_DOMAIN = "T";
	private static final String NONTERMINAL_FEATURE_DOMAIN = "NT";
	private static final String FEATS_PART_SPLIT_EXPRESSION = "\\=";
	private static final String WORD_FEATURE_NAME = "word";
	private static final String POS_FIELD_FEATURE_NAME = "pos";
	private static final String ROOT_FEATURE_VALUE = "S";
	private static final String NOT_BOUND_FEATURE_VALUE = "--";
	
	private tigerxml.Corpus target = null;
	private Map<String, Feature> usedFeatures = new HashMap<String, Feature>();

	public ConllConverter(Corpus source) {
		super(source);
	}

	@Override
	public tigerxml.Corpus toTigerXml() {
		target = new tigerxml.Corpus();
		copyName();
		convertAllSentences();
		completeTerminalsFeatures();
		registerUsedFeatures();
		return target;
	}

	private void copyName() {
		target.setId(super.getName());
	}
	
	private void convertAllSentences() {
		int sentenceCounter = 1;
		Iterator<Sentence> sentenceIterator = super.getSentenceIterator();
		while (sentenceIterator.hasNext()) {
			target.addSentence(convertSentence((Sentence) sentenceIterator.next(), sentenceCounter++));
		}
	}

	private tigerxml.Sentence convertSentence(Sentence sentence, int sentenceNumber) {
		tigerxml.Sentence result = new tigerxml.Sentence();
		result.setId("s" + sentenceNumber);
		
		Iterator<Token> tokenIterator = sentence.getTokenIterator();
		Token token;
		T terminal;
		Nt nonterminal;
		Nt root = createRoot(sentenceNumber);
		while (tokenIterator.hasNext()) {
			token = (Token) tokenIterator.next();
			terminal = extractTerminal(token, sentenceNumber);
			nonterminal = extractNonterminal(token, terminal, sentenceNumber);
			root.addEdge(createEdge(nonterminal));
			result.addTerminal(terminal);
			result.addNonterminal(nonterminal);
		}
		result.addNonterminal(root);
		result.setRoot(root);
		return result;
	}
	
	private Nt createRoot(int sentenceNumber) {
		Nt result = new Nt();
		result.setId("nt" + sentenceNumber + "_0");
		result.setFeature(POS_FIELD_FEATURE_NAME, ROOT_FEATURE_VALUE);
		registerFeaturevalueToFeature(ROOT_FEATURE_VALUE, POS_FIELD_FEATURE_NAME, NONTERMINAL_FEATURE_DOMAIN);
		return result;
	}

	private T extractTerminal(Token token, int sentenceNumber) {
		T result = createTerminalWithFeatures(token.getFeatures());
		result.setId("t" + sentenceNumber + "_" + token.getId());
		result.setFeature(WORD_FEATURE_NAME, token.getForm());
		return result;
	}
	
	private T createTerminalWithFeatures(List<String> featureList) {
		T result = new T();
		if (featureList == null) return result;
		
		String featureName, featureValue;
		int namelessFeatureCounter = 1;
		String[] featureParts;
		for (String item : featureList) {
			if (item.equals("")) continue;
			featureParts = item.split(FEATS_PART_SPLIT_EXPRESSION);
			if (featureParts.length > 1) {
				featureName = featureParts[0];
				featureValue = featureParts[1];
			} else {
				featureName = "other" + (namelessFeatureCounter++ > 1 ? "_" + namelessFeatureCounter : "");
				featureValue = item;
			}
			result.setFeature(featureName, featureValue);
			registerFeaturevalueToFeature(featureValue, featureName, TERMINAL_FEATURE_DOMAIN);
		}
		return result;
	}
	
	private Nt extractNonterminal(Token token, T terminal, int sentenceNumber) {
		String featureName = POS_FIELD_FEATURE_NAME;
		String featureValue = token.getPos();
		Nt result = new Nt();
		result.setId("nt" + sentenceNumber + "_" + token.getId());
		result.setFeature(featureName, featureValue);
		registerFeaturevalueToFeature(featureValue, featureName, NONTERMINAL_FEATURE_DOMAIN);
		result.addEdge(createEdge(terminal));
		return result;
	}
	
	private void registerFeaturevalueToFeature(String value, String featureName, String featureDomain) {
		Feature targetFeature = getTargetFeature(featureName, featureDomain);
		targetFeature = extendFeatureByFeaturevalue(targetFeature, value);
		usedFeatures.put(featureName, targetFeature);
	}
	
	private Feature getTargetFeature(String featureName, String featureDomain) {
		if (usedFeatures.containsKey(featureName)) {
			return usedFeatures.get(featureName);
		}
		return createFeature(featureName, featureDomain);
	}

	private Feature createFeature(String featureName, String featureDomain) {
		Feature result = new Feature();
		result.setName(featureName);
		result.setDomain(featureDomain);
		return result;
	}
	
	private Feature extendFeatureByFeaturevalue(Feature feature, String valueName) {
		for (Featurevalue fv : feature.getValue()) {
			if (fv.getName().equals(valueName)) return feature;
		}
		Featurevalue newValue = new Featurevalue();
		newValue.setName(valueName);
		feature.addFeaturevalue(newValue);
		return feature;
	}
	
	private Edge createEdge(Object target) {
		Edge result = new Edge();
		result.setIdref(target);
		return result;
	}
	
	private void completeTerminalsFeatures() {
		for (CorpusUnit sentence : target.getBody().getSentences()) {
			for (T terminal : ((tigerxml.Sentence) sentence).getTerminals()) {
				for (Feature feature : usedFeatures.values()) {
					if (! feature.getDomain().equals(TERMINAL_FEATURE_DOMAIN)) continue;
					if (terminal.getAttributeValue(feature.getName()) != null) continue;
					terminal.setFeature(feature.getName(), NOT_BOUND_FEATURE_VALUE);
				}
			}
		}
	}

	private void registerUsedFeatures() {
		target.addFeature(createWordFeature());
		for (Feature feature : usedFeatures.values()) {
			feature.addFeaturevalue(createNotBoundFeaturevalue());
			target.addFeature(feature);
		}
	}

	private Featurevalue createNotBoundFeaturevalue() {
		Featurevalue result = new Featurevalue();
		result.setName(NOT_BOUND_FEATURE_VALUE);
		result.setValue("not bound");
		return result;
	}
	
	private Feature createWordFeature() {
		Feature result = new Feature();
		result.setName(WORD_FEATURE_NAME);
		result.setDomain(TERMINAL_FEATURE_DOMAIN);
		return result;
	}
}
