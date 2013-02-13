//
// Reference: http://ilk.uvt.nl/conll/index.html
//


package conll;

import java.util.*;

public class Field {

	public enum Type {
		ID {
			@Override
			public Object convertValue(Object value) {
				if (value == null) {
					return DEFAULT_ID_VALUE;
				}
				if (value instanceof Integer) {
					return value;
				}
				return convertToInteger(value);
			}

			private Integer convertToInteger(Object value) {
				Integer result;
				try {
					result = new Integer((String) value);
				} catch (ClassCastException e) {
					result = DEFAULT_ID_VALUE;
				} catch (NumberFormatException e) {
					result = DEFAULT_ID_VALUE;
				}
				return result;
			}
		},
		FORM,
		LEMMA,
		CPOSTAG,
		POSTAG,
		FEATS {
			@Override
			public Object convertValue(Object value) {
				if (value == null) {
					return null;
				}
				List<String> result = new LinkedList<String>();
				for (String feature : ((String) value).split(REGEX_ESCAPER + FEATURE_DELIMITER)) {
					if (! feature.equals(EMPTY_FIELD_VALUE)) {
						result.add(feature);
					}
				}
				return result;
			}
			
			@Override
			public Object concatFeatures(Object currentValue, String newFeature) {
				List<String> result = currentFeatures(currentValue);
				result.add(newFeature);
				return result;
			}
			
			@SuppressWarnings("unchecked")
			private List<String> currentFeatures(Object value) {
				if (value == null) {
					return new LinkedList<String>();
				}
				return (LinkedList<String>) value;
			}
			
			@Override
			public String toString(Object value) {
				if (value == null) {
					return EMPTY_FIELD_VALUE;
				}
				return barSeparatedFeatures(value);
			}

			@SuppressWarnings("unchecked")
			private String barSeparatedFeatures(Object value) {
				ListIterator<String> iterator = ((LinkedList<String>) value).listIterator();
				StringBuilder result = new StringBuilder(iterator.next());
				while (iterator.hasNext()) {
					result.append(FEATURE_DELIMITER).append(iterator.next());
				}
				return result.toString();
			}
		},
		HEAD,
		DEPREL,
		PHEAD,
		PDEPREL;

		private static final String FEATURE_DELIMITER = "|";
		private static final String REGEX_ESCAPER = "\\";
		private static final String EMPTY_FIELD_VALUE = "_";
		private static final Integer DEFAULT_ID_VALUE = Integer.valueOf(0);

		public Object convertValue(Object value) {
			return value;
		}
		
		public Object concatFeatures(Object currentValue, String newFeature) {
			return currentValue;
		}

		public String toString(Object value) {
			if (value == null) {
				return EMPTY_FIELD_VALUE;
			}
			return value.toString();
		}
	}

	private Type type;
	private Object value;

	public Field(Type type, Object value) {
		if (type == null) {
			throw new IllegalArgumentException("Field type must be specified.");
		}
		this.type = type;
		setValue(value);
	}

	public void setValue(Object value) {
		this.value = type.convertValue(value);
	}
	public Object getValue() {
		return value;
	}

	/**
	 * Adds feature if field holds FEATS type. Else without function.
	 * @param feature
	 */
	public void addFeature(String feature) {
		value = type.concatFeatures(value, feature);
	}

	@Override
	public String toString() {
		return type.toString(value);
	}
}

