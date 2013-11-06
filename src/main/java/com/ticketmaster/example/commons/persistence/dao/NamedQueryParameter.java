package com.ticketmaster.example.commons.persistence.dao;


/**
 * NamedQueryParameter constructed only for named queries with parameter name,value and type.
 * @see #QueryParameter
 */
public class NamedQueryParameter extends QueryParameter {
	
	/**
	 * represents parameter named defined in NameQuery
	 */
	private final String parameterName;


	/**
	 * Creation of NamedQueryParameter with parameterName,value and type
	 * @param parameterName declared in NameQuery
	 * @param value represents parameter value
	 * @param type represents parameter type if any other wise NONE
	 */
	public NamedQueryParameter(String parameterName, Object value, TemporalType type) {
		super(value, type);
		this.parameterName = parameterName;
	}


	/**
	 * Creation of NamedQueryParameter with parameterName and value.
	 * It assigns type as NONE
	 * @param parameterName declared in NameQuery
	 * @param value represents parameter value
	 */
	public NamedQueryParameter(String parameterName, Object value) {
		super(value);
		this.parameterName = parameterName;
	}


	/**
	 * Getting parameter name
	 * @return parameterName
	 */
	public String getParameterName() {
		return parameterName;
	}

}
