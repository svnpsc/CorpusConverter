package converter;

abstract class FrameNetComponentImpl implements FrameNetComponent {

	public int getKey() {
		return Cantor.compute(getStart(), getEnd());
	}

}