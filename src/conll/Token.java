//
// Reference: http://ilk.uvt.nl/conll/index.html
//


package conll;

import java.util.*;

import conll.Field.Type;

public class Token {

	private static final String SPLITTER_REGEX = "\\s+";	// One or more whitespace characters: [ \t\n\x0B\f\r]+
	private static final String FIELD_DELIMITER = "\t";
	private static final String NULL_ID = "0";
	
	private Map<Type, Field> fields = new EnumMap<Type, Field>(Type.class);
	
	/**
	 * 
	 * @param line One text line of a .conll corpus file.
	 */
	public Token(String line) {
		if (line.length() == 0) {
			throw new IllegalArgumentException("Token line was empty.");
		}
		initialize(line.split(SPLITTER_REGEX));
	}

	private void initialize(String[] values) {
		if (hasTooLessEntries(values)) {
			values = fillMissingFields(values);
		}
		for (Type t : Type.values()) {
			setField(t, values[t.ordinal()]);
		}
	}

	private boolean hasTooLessEntries(String[] values) {
		return values.length < numberOfFields();
	}

	private String[] fillMissingFields(String[] givenValues) {
		return Arrays.copyOf(givenValues, numberOfFields());
	}

	private static int numberOfFields() {
		return Type.values().length;
	}

	/**
	 * 
	 * @return <code>Token</code> object without ID.
	 */
	public static Token createEmptyToken() {
		return new Token(NULL_ID);
	}

	public void setId(int id) {
		setField(Type.ID, Integer.valueOf(id));
	}
	public int getId() {
		return ((Integer) getField(Type.ID)).intValue();
	}
	
	public void setForm(String form) {
		setField(Type.FORM, form);
	}
	public String getForm() {
		Object result = getField(Type.FORM);
		if (result == null) return "";
		return result.toString();
	}

	public void setLemma(String lemma) {
		setField(Type.LEMMA, lemma);
	}
	public String getLemma() {
		Object result = getField(Type.LEMMA);
		if (result == null) return "";
		return result.toString();
	}

	public void setCoarseGrainedPos(String cgPos) {
		setField(Type.CPOSTAG, cgPos);
	}
	public String getCoarseGrainedPos() {
		Object result = getField(Type.CPOSTAG);
		if (result == null) return "";
		return result.toString();
	}

	public void setPos(String pos) {
		setField(Type.POSTAG, pos);
	}
	public String getPos() {
		Object result = getField(Type.POSTAG);
		if (result == null) return "";
		return result.toString();
	}

	public void addFeature(String feature) {
		fields.get(Type.FEATS).addFeature(feature);
	}
	
	/**
	 * 
	 * @return List of all features listed in the token's <i>FEATS</i> field.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getFeatures() {
		return (List<String>) fields.get(Type.FEATS).getValue();
	}
	
	/**
	 * 
	 * @param type Named index that specifies the field to set.
	 * @param value Value to set.
	 */
	public void setField(Type type, Object value) {
		if (fields.containsKey(type)) {
			fields.get(type).setValue(value);
		} else {
			fields.put(type, new Field(type, value));
		}
	}
	/**
	 * 
	 * @param type Named field index.
	 * @return The specified field's value.
	 */
	private Object getField(Type type) {
		return fields.get(type).getValue();
	}
	
	/**
	 * 
	 * @return A token's CoNLL string representation like in .conll files. That means fields separated by tabstops.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (Field f : fields.values()) {
			result.append(f.toString());
			result.append(FIELD_DELIMITER);
		}
		return result.toString().trim();
	}
}
