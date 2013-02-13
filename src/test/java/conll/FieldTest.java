package conll;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import org.junit.*;
import conll.Field.Type;
import java.util.List;

public class FieldTest {

	private static final Type TEST_TYPE = Type.FORM;
	private static final String FORM_TEST_VALUE = "apple";
	private static final Integer DEFAULT_ID_NULL_VALUE = Integer.valueOf(0);
	private static final Integer ID_TEST_VALUE = Integer.valueOf(42);
	private static final String FEATURE_TEST_VALUE = "num=sg|gen=masc";

	private Field field;

	@Test
	(expected=IllegalArgumentException.class)
	public void callConstructorWithNullArguments() {
		field = new Field(null, null);
	}
	
	@Test
	public void idValueIsInstanceOfInteger() {
		field = new Field(Type.ID, ID_TEST_VALUE);
		assertThat(field.getValue(), instanceOf(Integer.class));
	}
	
	@Test
	public void nullId() {
		field = new Field(Type.ID, null);
		assertNotNull("Field value should not be NULL.", field.getValue());
		assertEquals("Wrong ID.", DEFAULT_ID_NULL_VALUE, field.getValue());
	}

	@Test
	public void idFieldAccess() {
		field = new Field(Type.ID, ID_TEST_VALUE);
		assertNotNull("Field value should not be NULL.", field.getValue());
		assertEquals("Wrong value.", ID_TEST_VALUE, field.getValue());
	}

	@Test
	public void idFieldAccessWithStringConstruction() {
		field = new Field(Type.ID, ID_TEST_VALUE.toString());
		assertNotNull("Field value should not be NULL.", field.getValue());
		assertEquals("Wrong ID.", ID_TEST_VALUE, field.getValue());
	}

	@Test
	public void idFieldAccessWithIllegalStringInstance() {
		field = new Field(Type.ID, FORM_TEST_VALUE);
		assertNotNull("Field value should not be NULL.", field.getValue());
		assertEquals("Wrong ID.", DEFAULT_ID_NULL_VALUE, field.getValue());
	}

	@Test
	public void idFieldAccessWithIllegalInstance() {
		field = new Field(Type.ID, new Object());
		assertNotNull("Field value should not be NULL.", field.getValue());
		assertEquals("Wrong ID.", DEFAULT_ID_NULL_VALUE, field.getValue());
	}

	@Test
	public void stringFieldAccess() {
		field = new Field(TEST_TYPE, FORM_TEST_VALUE);
		assertNotNull("Field value should not be NULL.", field.getValue());
		assertEquals("Wrong value.", FORM_TEST_VALUE, field.toString());
	}
	
	@Test
	public void featsValueIsInstanceOfList() {
		field = new Field(Type.FEATS, FEATURE_TEST_VALUE);
		assertThat(field.getValue(), instanceOf(List.class));
	}
	
	@Test
	@SuppressWarnings("unchecked")	// Tested in fieldValueIsInstanceOfList
	public void setFeatures() {
		field = new Field(Type.FEATS, FEATURE_TEST_VALUE);
		assertNotNull("Field value should not be NULL.", field.getValue());
		assertEquals("Wrong number of features.", 2, ((List<String>) field.getValue()).size());
		assertEquals("Wrong string representation.", FEATURE_TEST_VALUE, field.toString());
	}
	
	@Test
	@SuppressWarnings("unchecked")	// Tested in fieldValueIsInstanceOfList
	public void addFeatureToFeatureField() {
		field = new Field(Type.FEATS, null);
		String feature = "kas=nom";
		field.addFeature(feature);
		assertNotNull("Field value should not be NULL.", field.getValue());
		assertEquals("Wrong number of features.", 1, ((List<String>) field.getValue()).size());
		assertEquals("Wrong string representation.", feature, field.toString());
	}

	@Test
	public void addFeatureToWrongField() {
		field = new Field(TEST_TYPE, null);
		field.addFeature("kas=nom");
		assertNull("Field value should be NULL.", field.getValue());
	}
}
