package com.ticketmaster.example.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ticketmaster.example.commons.persistence.model.ModelBase;
import com.ticketmaster.example.commons.persistence.model.ModelListener;

/**
 * Member -
 * Represents a customer or purchaser that has placed orders in the system
 */
@Entity
@Table(name = "tbl_purchaser")
@Cacheable(true)
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "PurchaserID")),
		@AttributeOverride(name = "dateTimeCreated", column = @Column(name = "Date_Record_Added")),
		@AttributeOverride(name = "dateTimeUpdated", column = @Column(name = "Latest_Record_Update")) })
@NamedQueries({
		@NamedQuery(name = "getMembersByFirstOrLastCount", query = "SELECT count(*) FROM MemberEntity aMember WHERE aMember.first LIKE :first OR aMember.last LIKE :last"),
		@NamedQuery(name = "getMembersByFirstOrLast", query = "SELECT aMember FROM MemberEntity aMember WHERE aMember.first LIKE :first OR aMember.last LIKE :last"),
		@NamedQuery(name = "getMembersByEmail", query = "SELECT aMember FROM MemberEntity aMember WHERE aMember.email = :email") })
public class MemberEntity extends ModelBase {

	private static final long serialVersionUID = -485992108994597544L;

	private static final int HASH = 31;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z", Locale.ROOT);

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "HouseHoldID")
	private AddressEntity address;

	@Column(name = "FirstName", nullable = true)
	private String first;
	
	@Column(name = "LastName", nullable = false)
	private String last;

	@Column(name = "Initial", nullable = true)
	private String initial;

	@Column(name = "Prefix", nullable = true)
	private String prefix;

	@Column(name = "Suffix", nullable = true)
	private String suffix;

	@Column(name = "Email", nullable = true)
	private String email;

	@Column(name = "Phone1", nullable = true)
	private String phone1;

	@Column(name = "Phone2", nullable = true)
	private String phone2;

	@Column(name = "NoEmailFlag", nullable = true)
	private Boolean optOut;

	public AddressEntity getAddress() {
		return this.address;
	}

	public void setAddress(final AddressEntity address) {
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

	/**
	 * setting created date time information.
	 * 
	 * @param dateTimeCreated
	 *            represents created date
	 * @see ModelListener
	 */
	public void setDateTimeCreated(Date dateTimeCreated) {

		if (dateTimeCreated == null) {
			super.setDateTimeCreated(null);
		} else {
			try {
				super.setDateTimeCreated(sdf.parse(sdf.format(dateTimeCreated)));
			} catch (ParseException e) {
				// default to previous behavior
				super.setDateTimeCreated(new Date());
			}
		}
	}

	/**
	 * setting last updated date time information.
	 * 
	 * @param dateTimeUpdated
	 *            represents last updated date
	 * @see ModelListener
	 */
	public void setDateTimeUpdated(Date dateTimeUpdated) {
		if (dateTimeUpdated == null) {
			super.setDateTimeUpdated(null);
			return;
		} else {
			try {
				super.setDateTimeUpdated(sdf.parse(sdf.format(dateTimeUpdated)));
			} catch (ParseException e) {
				// default to previous behavior
				super.setDateTimeUpdated(new Date());
			}
		}
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
			final MemberEntity member = (MemberEntity) obj;
			returnValue = (getId().intValue() == member.getId().intValue());
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
		int returnValue;

		if (getId() == null) {
			returnValue = HASH;
		} else {
			returnValue = HASH * getId().intValue();
		}

		return returnValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		final StringBuilder returnString = new StringBuilder("Member [memberId=");
		returnString.append(getId());
		returnString.append(", first=");
		returnString.append(first);
		returnString.append(", last=");
		returnString.append(last);
		returnString.append(", initial=");
		returnString.append(initial);
		returnString.append(", prefix=");
		returnString.append(prefix);
		returnString.append(", suffix=");
		returnString.append(suffix);
		returnString.append(", email=");
		returnString.append(email);
		returnString.append(", phone1=");
		returnString.append(phone1);
		returnString.append(", phone2=");
		returnString.append(phone2);
		returnString.append(", optOut=");
		returnString.append(optOut);
		returnString.append("]");

		return returnString.toString();
	}
}
