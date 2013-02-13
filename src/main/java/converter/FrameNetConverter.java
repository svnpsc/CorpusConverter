package converter;

import framenet.corpus.*;
import tigerxml.*;

import java.util.*;

/**
 * 
 * <p>This class is used to convert a code>framenet.corpus.Corpus</code> object into a <code>tigerxml.Corpus</code> object.</p>
 * 
 * <p>
 * The conversion rules belong to the following table:
 * <table border="1">
 * 	<tr>
 * 		<th>FrameNet</th>
 * 		<th colspan="2">TigerXML</th>
 * 	</tr>
 * 	<tr>
 * 		<th>layer</th>
 * 		<th>element</th>
 * 		<th>attribute</th>
 * 	</tr>
 * 	<tr>
 * 		<td><code>GF</code></td>
 * 		<td><code>edge</code></td>
 * 		<td><code>label</code></td>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>PT</code></td>
 * 		<td><code>nt</code></td>
 * 		<td><i>used feature</i></td>
 * 	</tr> 
 * 	<tr>
 * 		<td><code>Other</code></td>
 * 		<td><code>t</code></td>
 * 		<td><i>for each used feature (except <code>word</code>)</i></td>
 * 	</tr> 
 * </table>
 * </p> 
 *
 */
public class FrameNetConverter extends framenet.corpus.Corpus implements ConvertableIntoTigerXml {

	private static final String WORD_FEATURE_NAME = "word";
	private static final String NOT_BOUND_FEATURE_VALUE = "--";
	
	private tigerxml.Corpus targetCorpus = null;
	private tigerxml.Sentence targetSentence;
	private FrameNetComponentsCollection components;
	private FrameNetComponent nextWord = null;
	
	public FrameNetConverter(framenet.corpus.Corpus sourceCorpus) {
		super.setID(sourceCorpus.getID());
		super.setName(sourceCorpus.getName());
		super.setDocuments(sourceCorpus.getDocuments());
	}
	
	@Override
	public tigerxml.Corpus toTigerXml() {
		targetCorpus = new tigerxml.Corpus();
		setTargetCorpusId();
		setTargetCorpusSentences();
		return targetCorpus;
	}

	private void setTargetCorpusId() {
		targetCorpus.setId(super.getID());
	}

	private void setTargetCorpusSentences() {
		for (Document document : super.getDocuments().getDocument()) {
			for (Paragraph paragraph : document.getParagraphs().getParagraph()) {
				for (framenet.corpus.Sentence sentence : paragraph.getSentences().getSentence()) {
					convertSentence(sentence);
					targetCorpus.addSentence(targetSentence);
					registerAnnotations();
				}
			}
		}
	}
	
	private void convertSentence(framenet.corpus.Sentence sourceSentence) {
		targetSentence = new tigerxml.Sentence();
		targetSentence.setId(sourceSentence.getID());
		prepareComponentCollection(sourceSentence);
		buildSyntaxTree(sourceSentence);
	}
	
	private void prepareComponentCollection(framenet.corpus.Sentence sourceSentence) {
		components = new FrameNetComponentsCollection();
		collectWordsWithPositions(sourceSentence);
		// Only first one is taken!
		AnnotationSet annotationSet = sourceSentence.getAnnotationSets().getAnnotationSet().get(0);
		collectLabels(annotationSet);
		addRootLabelIfNecessary(annotationSet.getId(), sourceSentence.getText().length());
	}

	private void collectWordsWithPositions(framenet.corpus.Sentence sourceSentence) {
		String[] words = sourceSentence.getText().trim().split(" ");
		int startPos = 0;
		for (int i = 0; i < words.length; ++i) {
			components.putWord(words[i], startPos);
			startPos += words[i].length() + 1;
		}
	}

	private void collectLabels(AnnotationSet annotationSet) {
		for (Layer layer : annotationSet.getLayers().getLayer()) {
			FrameNetLayerName layerName = layerNameToWorkWith(layer);
			if (layerName == null) continue;
			if (layer.getLabels() == null) continue;

			for (Label label : layer.getLabels().getLabel()) {
				components.putLabel(label, layerName);
			}
		}
	}
	
	private FrameNetLayerName layerNameToWorkWith(Layer layer) {
		if (layer.getName().equals(FrameNetLayerName.PT.toString())) return FrameNetLayerName.PT;
		if (layer.getName().equals(FrameNetLayerName.GF.toString())) return FrameNetLayerName.GF;
		if (layer.getName().equals(FrameNetLayerName.Other.toString())) return FrameNetLayerName.Other;
		return null;
	}

	private void addRootLabelIfNecessary(String rootNodeId, int sentenceLength) {
		if (components.containsPtLabel(0, sentenceLength - 1)) return;

		Label rootPtLabel = new Label();
		rootPtLabel.setName("S");
		rootPtLabel.setID(rootNodeId);
		rootPtLabel.setStart("0");
		rootPtLabel.setEnd(String.valueOf(sentenceLength - 1));
		components.putLabel(rootPtLabel, FrameNetLayerName.PT);
	}

	private void buildSyntaxTree(framenet.corpus.Sentence sourceSentence) {
		Iterator<FrameNetComponent> ptLabelIterator = components.ptLabelIterator();
		FrameNetComponent root = (FrameNetComponent) ptLabelIterator.next();
		targetSentence.getGraph().setRoot(buildSubTree(root, ptLabelIterator, components.wordIterator()));
	}
	
	private Nt buildSubTree(FrameNetComponent subroot, Iterator<FrameNetComponent> ptLabelIterator, Iterator<FrameNetComponent> wordIterator) {
		Nt result = createNonTerminalFromPtLabel(subroot);
		
		FrameNetComponent nextPtLabel = null;
		if (ptLabelIterator.hasNext()) {
			nextPtLabel = (FrameNetComponent) ptLabelIterator.next();
		}
		
		while (wordIterator.hasNext()) {
			if (nextPtLabel != null) {
				if (nextPtLabel.getStart() == nextWordStart(nextWord)) {
					result.addEdge(edgeToNode(nextPtLabel, ptLabelIterator, wordIterator));
					if (subroot.getEnd() == nextPtLabel.getEnd()) break;
					continue;
				}
			}
			nextWord = (FrameNetComponent) wordIterator.next();
			result.addEdge(edgeToLeave(nextWord));
			
			if (subroot.getEnd() == nextWord.getEnd()) break;
		}
		targetSentence.addNonterminal(result);
		return result;
	}
	
	private Nt createNonTerminalFromPtLabel(FrameNetComponent ptLabel) {
		Label label = (Label) ptLabel.getObject();
		String value = label.getName();
		if (value.equals("")) {
			value = NOT_BOUND_FEATURE_VALUE;
		}
		return createNonterminal(label.getID(), FrameNetLayerName.PT.toString(), value);
	}

	private Nt createNonterminal(String id, String featureAttributeName, String featureAttributeValue) {
		Nt result = new Nt();
		result.setId(id);
		result.setFeature(featureAttributeName, featureAttributeValue);
		return result;
	}
	
	private int nextWordStart(FrameNetComponent word) {
		if (word == null) return 0;
		return word.getEnd() + 2;
	}
	
	private Edge edgeToNode(FrameNetComponent node, Iterator<FrameNetComponent> ptLabelIterator, Iterator<FrameNetComponent> wordIterator) {
		Edge result = createEdge(node.getKey());
		result.setIdref(buildSubTree(node, ptLabelIterator, wordIterator));
		return result;
	}
	
	private Edge edgeToLeave(FrameNetComponent word) {
		Edge result = createEdge(word.getKey());
		result.setIdref(createTerminalFromWord(word));
		return result;
	}

	private T createTerminalFromWord(FrameNetComponent word) {
		T result = new T();
		result.setId("t_" + word.getStart());
		result.setFeature(WORD_FEATURE_NAME, (String) word.getObject());
		if (components.containsOtherLabel(word.getKey())) {
			Label other = (Label) components.getOtherLabel(word.getKey());
			result.setFeature(FrameNetLayerName.Other.toString(), other.getName());
		}
		targetSentence.addTerminal(result);
		return result;
	}
	
	private Edge createEdge(int key) {
		Edge result = new Edge();
		if (components.containsGfLabel(key)) {
			Label gf = (Label) components.getGfLabel(key);
			result.setLabel(gf.getName());
		}
		return result;
	}

	private void registerAnnotations() {
		registerWordFeature();
		registerUsedLabels();
	}
	
	private void registerWordFeature() {
		Feature wordFeature = new Feature();
		wordFeature.setDomain(FrameNetLayerName.FeatureDomain.T.toString());
		wordFeature.setName(WORD_FEATURE_NAME);
		targetCorpus.addFeature(wordFeature);
	}

	private void registerUsedLabels() {
		Collection<FrameNetComponent> labels;
		for (FrameNetLayerName layerName : FrameNetLayerName.values()) {
			labels = components.allLabels(layerName);
			if (labels.isEmpty()) continue;

			if (layerName.equals(FrameNetLayerName.GF)) {
				targetCorpus.setEdgelabel(buildEdgelabelToRegister(labels));
			} else {
				targetCorpus.addFeature(buildFeatureToRegister(labels, layerName));
			}
		}
	}

	private Feature buildFeatureToRegister(Collection<FrameNetComponent> labels, FrameNetLayerName layerName) {
		Feature result = new Feature();
		result.setDomain(layerName.correspondingFeatureDomain());
		result.setName(layerName.toString());
		for (Featurevalue featurevalue : usedLabelNamesAsFeatures(labels)) {
			result.addFeaturevalue(featurevalue);
		}
		return result;
	}

	private Edgelabel buildEdgelabelToRegister(Collection<FrameNetComponent> labels) {
		Edgelabel result = new Edgelabel();
		for (Featurevalue featurevalue : usedLabelNamesAsFeatures(labels)) {
			result.addFeaturevalue(featurevalue);
		}
		return result;
	}

	private Set<Featurevalue> usedLabelNamesAsFeatures(Collection<FrameNetComponent> labels) {
		Set<Featurevalue> result = new HashSet<Featurevalue>();
		for (FrameNetComponent label : labels) {
			Featurevalue featurevalue = new Featurevalue();
			featurevalue.setName(((Label) label.getObject()).getName());
			result.add(featurevalue);
		}		
		return result;
	}
}
