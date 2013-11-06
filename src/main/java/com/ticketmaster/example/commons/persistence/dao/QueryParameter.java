package com.ticketmaster.example.commons.persistence.dao;

/**
 * Concrete class representation of database query parameter values and type.
 * QueryParameter constructed with query parameter value and type.
 *
 */
public class QueryParameter {
	
	/**
	 * Type used to indicate a specific mapping of parameter value.
	 */
	public enum TemporalType { NONE, DATE, TIME, TIMESTAMP };
	/**
	 * Represents query parameter value
	 */
	private final Object value; // this class should be a generic type
	/**
	 * Represents one of TemporalType
	 */
	private final TemporalType type;  // optional 
	
	/**
	 * Creation of QueryParameter with value and type
	 * @param value represents query parameter value 
	 * @param type represents one of TemporalType
	 */
	public QueryParameter(final Object value, final TemporalType type) {
		super();
		this.value = value;
		this.type = type;
	}
	
	/**
	 * Creation of QueryParameter only with value and assign type to NONE
	 * @param value represents query parameter value
	 */
	public QueryParameter(final Object value) {
		super();
		
		this.value = value;
		this.type = TemporalType.NONE;
	}

	/**
	 * Get the type for query parameter value if any otherwise TemporalType.NONE
	 * @return type represents one of type defined in TemporalType
	 */
	public TemporalType getType() {
		return type;
	}

	/**
	 * Get the value of query parameter
	 * @return value of query parameter
	 */
	public Object getValue() {
		return value;
	}
}
