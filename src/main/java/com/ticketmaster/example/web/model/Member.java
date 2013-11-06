package com.ticketmaster.example.web.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Member -
 * Represents a customer or purchaser that has placed orders in the system
 */
@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.PROPERTY)
@XmlRootElement(name = "Member")
public class Member implements Serializable {

	private static final long serialVersionUID = -485992108994597544L;


	private Long id;

	private Address address;


	private String first;

	private String last;

	private String initial;

	private String prefix;

	private String suffix;

	private String email;

	private String phone1;

	private String phone2;

	private Boolean optOut;

	private Date dateTimeCreated;

	private Date dateTimeUpdated;

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	public String getFirst() {
		return this.first;
	}

	public void setFirst(final String first) {
		this.first = first;
	}

	public String getLast() {
		return this.last;
	}

	public void setLast(final String last) {
		this.last = last;
	}

	public String getInitial() {
		return this.initial;
	}

	public void setInitial(final String initial) {
		this.initial = initial;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(final String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhone1() {
		return this.phone1;
	}

	public void setPhone1(final String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return this.phone2;
	}

	public void setPhone2(final String phone2) {
		this.phone2 = phone2;
	}

	public Boolean getOptOut() {
		return this.optOut;
	}

	public void setOptOut(final Boolean optOut) {
		this.optOut = optOut;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
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
	public void setDateTimeCreated(final Date dateTimeCreated) {

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
	public void setDateTimeUpdated(final Date dateTimeUpdated) {
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
			final Member member = (Member) obj;
			final EqualsBuilder builder = new EqualsBuilder();
			returnValue = builder
						.appendSuper(super.equals(obj))
						.append(member.email, this.email)
						.isEquals();
		}

		return returnValue;
	}

	/**
	 * <code>hashCode</code>-
	 * Determines hash value for equality determination.
	 *
	 * @return hash value of this object instance
	 */
	@Override
	public int hashCode() {

		final HashCodeBuilder builder = new HashCodeBuilder();
		builder
			.append(this.email)
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

    	final ToStringBuilder returnString = new ToStringBuilder(this)
    	.append("id", this.id)
    	.append("prefix", this.prefix)
    	.append("first", this.first)
    	.append("initial", this.initial)
    	.append("last", this.last)
    	.append("suffix", this.suffix)
    	.append("email", this.email)
    	.append("phone1", this.phone1)
    	.append("phone2", this.phone2)
    	.append("optout", this.optOut)
    	.append("dateTimeCreated", this.dateTimeCreated)
    	.append("dateTimeUpdated", this.dateTimeUpdated);

		return returnString.toString();
	}


}
