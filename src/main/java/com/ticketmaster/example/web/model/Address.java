package com.ticketmaster.example.web.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * Address -
 * Represents a household associated with an order
 * It could be a billing or shipping address
 */

public class Address implements Serializable {

	private static final long serialVersionUID = -7453068235552003298L;

	private Long id;

	private String street1;

	private String street2;

	private String city;

	private String state;

	private String zip;



	private Date dateTimeCreated;

	private Date dateTimeUpdated;

	public String getStreet1() {
		return this.street1;
	}

	public void setStreet1(final String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return this.street2;
	}

	public void setStreet2(final String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getState() {
		return this.state;
	}

	public void setState(final String state) {
		this.state = state;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(final String zip) {
		this.zip = zip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * <code>setDateTimeCreated</code>
	 *
	 * Sets the date and time created
	 *
	 * @param dateTimeCreated
	 *            represents created date
	 *
	 */
	public void setDateTimeCreated(Date dateTimeCreated) {

		this.dateTimeCreated = dateTimeCreated;

	}

	/**
	 * <code>getDateTimeCreated</code>
	 *
	 * @return the date and time of creation
	 */
	public Date getDateTimeCreated() {
		return this.dateTimeCreated;
	}

	/**
	 * <code>setDateTimeUpdated</code>-
	 * Setting last updated date time information.
	 *
	 * @param dateTimeUpdated
	 *            Represents the last updated date
	 */
	public void setDateTimeUpdated(Date dateTimeUpdated) {
		this.dateTimeUpdated = dateTimeUpdated;
	}

	/**
	 * <code>getDateTimeUpdated</code>
	 *
	 * @return the date and time last updated
	 */
	public Date getDateTimeUpdated(){
		return this.dateTimeUpdated;
	}


	/**
	 * <code>equals</code>-
	 * Checks if input object is the same as this instance.
	 *
	 * Looks at the natural key and uses street1+street2+state+city+zip to determine equality.
	 *
	 * @param obj
	 *            <code>Object</code> to compare against
	 * @return true if equal
	 *         false if not equal
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean returnValue;

		if (this == obj) {
			returnValue = true;
		} else if ((obj == null) || (obj.getClass() != super.getClass())) {
			returnValue = false;
		} else {
			final Address address = (Address) obj;
			final EqualsBuilder builder = new EqualsBuilder();
			returnValue = builder
						.appendSuper(super.equals(obj))
						.append(address.street1, this.street1)
						.append(address.street2, this.street2)
						.append(address.state, this.state)
						.append(address.city, this.city)
						.append(address.zip, this.zip)
						.isEquals();
		}

		return returnValue;
	}

	/**
	 * <code>hashCode</code>-
	 * Determines hash value for equality determination.
	 *
	 * Uses street1+street2+state+city+zip to determine hash value.
	 *
	 * @return hash value of this object instance
	 */
	@Override
	public int hashCode() {

		final HashCodeBuilder builder = new HashCodeBuilder();
		builder
			.append(this.street1)
			.append(this.street2)
			.append(this.state)
			.append(this.city)
			.append(this.zip)
			.toHashCode();

		return builder.toHashCode();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder returnString = new StringBuilder("Address [");
		returnString.append("street1=");
		returnString.append(street1);
		returnString.append(", street2=");
		returnString.append(street2);
		returnString.append(", city=");
		returnString.append(city);
		returnString.append(", state=");
		returnString.append(state);
		returnString.append(", zip=");
		returnString.append(zip);
		returnString.append("]");

		return returnString.toString();
	}

}
