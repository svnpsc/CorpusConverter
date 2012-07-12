package converter;

import framenet.corpus.Label;

class FrameNetLabelComponent extends FrameNetComponentImpl implements FrameNetComponent {

	private Label label;
	
	FrameNetLabelComponent(Label label) {
		this.label = label;
	}

	@Override
	public Object getObject() {
		return label;
	}

	@Override
	public int getStart() {
		return Integer.parseInt(label.getStart());
	}

	@Override
	public int getEnd() {
		return Integer.parseInt(label.getEnd());
	}

}
