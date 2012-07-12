package converter;

class Cantor {
	public static int compute(int x, int y) {
		return (x + y) * (x + y + 1) / 2 + y;
	}

	public static int computeX(int z) {
		int i = (int) Math.floor(Math.sqrt(0.25 + 2 * z) - 0.5);
		return i - computeY(z);
	}

	public static int computeY(int z) {
		int i = (int) Math.floor(Math.sqrt(0.25 + 2 * z) - 0.5);
		return z - i * (i + 1) / 2;
	}
}
