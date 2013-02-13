package converter;

import java.util.*;

import framenet.corpus.Label;

class FrameNetComponentsCollection {

	private Map<Integer, FrameNetComponent> words = new TreeMap<Integer, FrameNetComponent>(new FrameNetComponentComparator());
	private Map<Integer, FrameNetComponent> ptLabels = new TreeMap<Integer, FrameNetComponent>(new FrameNetComponentComparator());
	private Map<Integer, FrameNetComponent> gfLabels = new TreeMap<Integer, FrameNetComponent>();
	private Map<Integer, FrameNetComponent> otherLabels = new TreeMap<Integer, FrameNetComponent>();

	
	void putWord(String word, int position) {
		FrameNetWordComponent wc = new FrameNetWordComponent(word, position);
		words.put(wc.getKey(), wc);
	}

	Iterator<FrameNetComponent> wordIterator() {
		return words.values().iterator();
	}
	

	void putLabel(Label label, FrameNetLayerName layerName) {
		switch (layerName) {
		case PT : { putPtLabel(new FrameNetLabelComponent(label)); return; }
		case GF : { putGfLabel(new FrameNetLabelComponent(label)); return; }
		case Other : { putOtherLabel(new FrameNetLabelComponent(label)); return; }
		default : return;
		}
	}
	
	private void putPtLabel(FrameNetLabelComponent label) {
		ptLabels.put(label.getKey(), label);
	}
	
	private void putGfLabel(FrameNetLabelComponent label) {
		gfLabels.put(label.getKey(), label);
	}
	
	private void putOtherLabel(FrameNetLabelComponent label) {
		otherLabels.put(label.getKey(), label);
	}
	
	Label getGfLabel(int key) {
		return (Label) gfLabels.get(key).getObject();
	}
	
	Label getOtherLabel(int key) {
		return (Label) otherLabels.get(key).getObject();
	}
	
	boolean containsPtLabel(int start, int end) {
		return ptLabels.containsKey(Cantor.compute(start, end));
	}
	
	boolean containsGfLabel(int key) {
		return gfLabels.containsKey(key);
	}

	boolean containsOtherLabel(int key) {
		return otherLabels.containsKey(key);
	}

	Iterator<FrameNetComponent> ptLabelIterator() {
		return ptLabels.values().iterator();
	}
	
	Collection<FrameNetComponent> allLabels(FrameNetLayerName layerName) {
		switch (layerName) {
		case PT : return ptLabels.values();
		case GF : return gfLabels.values();
		case Other : return otherLabels.values();
		default : return Collections.emptySet();
		}
	}

}
