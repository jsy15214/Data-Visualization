package edu.cmu.cs.cs214.hw5b.framework.core;

/**
 * Represents the data entry in the set of data extracted from the source.
 * Class invariant : 1. data != null once constructed
 * 		     2. data's specific type is integer, double or string
 * 
 * @author YanningMao
 *
 */
public final class DataEntry {
	
	/* ---------- instance variable ---------- */
	
	private String rawData; 	// stores the raw data in the string format
	private Object data;		// stores the data parsed from raw data
	
	/* ------------- constructor ------------- */

	/**
	 * Takes a a string type raw data, and finds its specific type.
	 * If the raw data is not an integer or a double, then it remains as a string.
	 * The parsed raw data is stored as an object type object after parsing.
	 * 
	 * @param rawData, the string type raw data to be processed
	 */
	public DataEntry(String rawData) {
		// store the raw data in string format
		this.rawData = rawData;
		// find the data's specific type
		if (!parseInteger() && !parseDouble()) {
			data = (Object)rawData;
		}
		
		// postcondition
		assert(rawData != null);
		assert(data != null);
		assert(data instanceof Integer || data instanceof Double
						|| data instanceof String);
	}
	
	/**
	 * Tries to parse the string type raw data into an integer type data.
	 * Stores the parsed integer data and returns true if parse succeeds;
	 * does nothing and returns if parse fails with an exception.
	 * 
	 * @return parseSucceed, true if the string type raw data can be parsed as an integer;
	 * 			 false otherwise
	 */
	private boolean parseInteger() {
		try {
			int value = Integer.parseInt(rawData);
			data = (Object) (new Integer(value));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Tries to parse the string type raw data into a double type data.
	 * Stores the parsed double data and returns true if parse succeeds;
	 * does nothing and returns if parse fails with an exception.
	 * 
	 * @return parseSucceed, true if the string type raw data can be parsed as a double;
	 * 			 false otherwise
	 */
	private boolean parseDouble() {
		try {
			double value = Double.parseDouble(rawData);
			data = (Object) (new Double(value));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/* ----------- instance methods ----------- */

	/**
	 * Checks if the stored data is a string.
	 * Requires : data is not null
	 * 
	 * @return isStr, true if the data is a string; false otherwise
	 */
	public boolean isString() {
		return (data instanceof String);
	}
	
	/**
	 * Checks if the stored data is an integer
	 * Requires : data is not null
	 * 
	 * @return isInt, true if the data is a string; false otherwise
	 */
	public boolean isInteger() {
		return (data instanceof Integer);
	}
	
	/**
	 * Checks if the stored data is a double
	 * Requires : data is not null
	 * 
	 * @return isDouble, true if the data is a double; false otherwise
	 */
	public boolean isDouble() {
		return (data instanceof Double);
	}
	
	/**
	 * Convert and return the stored data as a string.
	 * Requires : data is a string
	 * Ensures : returns exactly the original data as string type
	 * 
	 * @return strData, the stored data as a string type
	 * @throws IllegalStateException, if the stored data is not an instance of string
	 */
	public String convertToString() {
		if (!isString()) {
			throw new IllegalStateException("Data is not a string");
		}
		
		assert(data instanceof String);
		return (String)data;
	}
	
	/**
	 * Convert and return the stored data as an integer.
	 * Requires : data is an integer
	 * Ensures : returns exactly the original data as Integer type
	 * 
	 * @return intData, the stored data as a string type
	 * @throws IllegalStateException, if the stored data is not an instance of integer
	 */
	public Integer convertToInteger() {
		if (!isInteger()) {
			throw new IllegalStateException("Data is not an integer");
		}
		return (Integer)data;
	}
	
	/**
	 * Convert and return the stored data as a double.
	 * Requires : data is a double
	 * Ensures : returns exactly the original data as double type
	 * 
	 * @return doubleData, the stored data as a string type
	 * @throws IllegalStateException, if the stored data is not an instance of double
	 */
	public Double convertToDouble() {
		if ((!isDouble()) && (!isInteger())) {
			throw new IllegalStateException("Data is not an integer");
		}
		// convert integer to data
		if (isInteger()) {
			return new Double(convertToInteger().intValue());
		}
		return (Double)data;
	}
	
	/**
	 * Compare two data entries. Returns 0 if they are equal; returns negative value if the
	 * data entry is smaller than the argument data entry; returns positive value if the data
	 * entry is larger than the argument data entry.
	 * Convention : If at least one of the entry's specific type is string, both entries are
	 * compared as stings.
	 * 
	 * @param otherData, the other data entry to be compared with
	 * @return relationship, a integer value indicating comparison result
	 */
	public int compareTo(DataEntry otherData) {
		
		// if both are integers
		if (isInteger() && otherData.isInteger()) {
			Integer int1 = convertToInteger();
			Integer int2 = otherData.convertToInteger();
			return int1.compareTo(int2);
		}
		// if both are doubles
		if (isDouble() && otherData.isDouble()) {
			Double d1 = convertToDouble();
			Double d2 = otherData.convertToDouble();
			return d1.compareTo(d2);
		}
		// if one is double and one is integer
		if (isInteger() && otherData.isDouble()) {
			Double d1 = Double.valueOf(convertToInteger().intValue());
			Double d2 = otherData.convertToDouble();
			return d1.compareTo(d2);
		} else if (isDouble() && otherData.isInteger()) {
			Double d1 = convertToDouble();
			Double d2 = Double.valueOf(otherData.convertToInteger().intValue());
			return d1.compareTo(d2);
		}
		// if not both are numeric values, compare their string format
		else {
			return toString().compareTo(otherData.toString());
		}
	}
	
	/**
	 * Creates a copy of the data entry.
	 * 
	 * @return dataEntry, the self copy of the data entry.
	 */
	public final DataEntry copy() {
		return new DataEntry(rawData);
	}
	
	/* --------------- override object methods --------------- */
	
	/**
	 * Returns the string representation of the data entry.
	 * 
	 * @return strRepr, the string representation of the data entry
	 */
	@Override
	public String toString() {
		return rawData;
	}
	
	/**
	 * Checks equality between two data entries.
	 * Two data sets are considered equal if they have the same string representation.
	 * 
	 * @param other, the object to be compared
	 * @return equality, true if equal; false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		
		// check type
		if (!(obj instanceof DataEntry)) {
			return false;
		}
		
		// convert type
		DataEntry data = (DataEntry) obj;
		
		// check string format equal
		return toString().equals(data.toString());
	}
	
	/**
	 * Returns the hash code of the data entry.
	 * 
	 * @return hashCode, the hash code of the data entry
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
}
