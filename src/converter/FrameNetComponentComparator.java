package converter;

import java.util.Comparator;

/**
 * Used by FrameNetConverter.
 *
 */
public class FrameNetComponentComparator implements Comparator<Integer> {

	@Override
	public int compare(Integer key1, Integer key2) {
		int start1 = Cantor.computeX(key1);
		int start2 = Cantor.computeX(key2);
		if (start1 != start2) {
			return start1 - start2;
		}
		return Cantor.computeY(key2) -  Cantor.computeY(key1);
	}

}
