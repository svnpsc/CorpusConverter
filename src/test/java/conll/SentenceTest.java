package conll;

import static org.junit.Assert.assertEquals;
import org.junit.*;
import java.util.Iterator;

public class SentenceTest {

	private Sentence sentence = null;
	private static String[] tokenLines = new String[] {	"1\tDaarvoor\tdaarvoor\tAdv\tAdv\tpron|aanw\t_\t_\t_\t_",
														"2\tis\tben\tV\tV\thulpofkopp|ott|3|ev\t_\t_\t_\t_",
														"3\tgekozen\tkies\tV\tV\ttrans|verldw|onverv\t_\t_\t_\t_",
														"4\tomdat\tomdat\tConj\tConj\tonder|metfin\t_\t_\t_\t_",
														"5\tgemeenten\tgemeente\tN\tN\tsoort|mv|neut\t_\t_\t_\t_"};
	
	@Before
	public void setUp() {
		sentence = new Sentence();
	}

	@Test
	public void stringRepresantation() {
		StringBuilder correct = new StringBuilder();
		for (String s : tokenLines) {
			sentence.addToken(new Token(s));
			correct.append(s).append("\n");
		}
		assertEquals("Wrong String representation.", correct.toString(), sentence.toString());
	}
	@Test
	public void stringOfEmptySentence() {
		assertEquals("Wrong String representation.", "", sentence.toString());
	}

	@Test
	public void setTokenIdsIncrementally() {
		sentence.addToken(Token.createEmptyToken());
		Token token = Token.createEmptyToken();
		token.setId(1);
		assertEquals("Wrong token id.", token.toString() + "\n", sentence.toString());
	}
	
	@Test
	public void iterateThroughTokens() {
		for (String s : tokenLines) {
			sentence.addToken(new Token(s));
		}
		Iterator<Token> it = sentence.getTokenIterator();
		int index = 0;
		while (it.hasNext()) {
			assertEquals("Wrong token id.", Integer.parseInt(tokenLines[index++].substring(0, 1)), it.next().getId());
		}
	}
}
