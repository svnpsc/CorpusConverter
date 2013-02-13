package conll;

import static org.junit.Assert.*;
import org.junit.*;

public class TokenTest {

	private static final String INPUT_LINE = "10\ttror\t_\tV\tVA\tmood=indic|tense=present|voice=active\t_\t_\t_\t_";

	private Token token = null;

	@Before
	public void setUp() {
		token = createTestToken();
	}

	static Token createTestToken() {
		return new Token(INPUT_LINE);
	}
	
	@Test
	public void stringRepresantation() {
		assertEquals("Wrong String representation.", INPUT_LINE, token.toString());
	}
	@Test
	public void createTokenFromTooLessFields() {
		String argumentLine = "10\ttror\t_\tV\tVA\tmood=indic|tense=present|voice=active";
		String correctLine = argumentLine + "\t_\t_\t_\t_";
		token = new Token(argumentLine);
		assertEquals("Wrong String representation.", correctLine, token.toString());
	}
	@Test
	(expected=IllegalArgumentException.class)
	public void createTokenFromEmptyArray() {
		token = new Token("");
	}
	@Test
	(expected=NullPointerException.class)
	public void createTokenFromNullArray() {
		token = new Token(null);
	}

	@Test
	public void emptyToken() {
		Token empty = Token.createEmptyToken();
		assertEquals("Empty token has wrong contents.", "0\t_\t_\t_\t_\t_\t_\t_\t_\t_", empty.toString());
	}

	@Test
	public void idAccess() {
		int newId = token.getId() + 1;
		token.setId(newId);
		assertEquals("Got wrong value after change.", newId, token.getId());
	}
	
	@Test
	public void featureAdding() {
		token = Token.createEmptyToken();
		String firstFeature = "word=apple";
		token.addFeature(firstFeature);
		assertEquals("Feature was not added.", "0\t_\t_\t_\t_\t" + firstFeature + "\t_\t_\t_\t_", token.toString());
		String secondFeature = "cat=NN";
		token.addFeature(secondFeature);
		assertEquals("Feature was not added.", "0\t_\t_\t_\t_\t" + firstFeature + "|" + secondFeature + "\t_\t_\t_\t_", token.toString());
	}
	
	@Test
	public void copyToken() {
		Token copy = new Token(token.toString());
		assertEquals("Copying gone wrong.", token.toString(), copy.toString());
	}
}
