package converter;

class FrameNetWordComponent extends FrameNetComponentImpl implements FrameNetComponent {

	private String word;
	private int position;
	
	FrameNetWordComponent(String word, int position) {
		this.word = word;
		this.position = position;
	}
	
	@Override
	public String getObject() {
		return word;
	}
	
	@Override
	public int getStart() {
		return position;
	}

	@Override
	public int getEnd() {
		return position + word.length() - 1;
	}

}
