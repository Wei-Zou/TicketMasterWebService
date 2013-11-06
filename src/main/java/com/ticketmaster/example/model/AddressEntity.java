package com.ticketmaster.example.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ticketmaster.example.commons.persistence.model.ModelBase;
import com.ticketmaster.example.commons.persistence.model.ModelListener;

/**
 * Address -
 * Represents a household associated with an order
 * It could be a billing or shipping address
 */
@Entity
@Table(name = "tbl_household")
@Cacheable(true)
@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "HouseHoldID", nullable = false)),
		@AttributeOverride(name = "dateTimeCreated", column = @Column(name = "Date_Record_Added")),
		@AttributeOverride(name = "dateTimeUpdated", column = @Column(name = "Latest_Record_Update")) })
public class AddressEntity extends ModelBase {

	private static final long serialVersionUID = -7453068235552003298L;

	private static final int HASH = 31;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.000Z", Locale.ROOT);

	@Column(name = "Address1", nullable = true)
	private String street1;

	@Column(name = "Address2", nullable = true)
	private String street2;

	@Column(name = "City", nullable = true)
	private String city;

	@Column(name = "State", nullable = true)
	private String state;

	@Column(name = "Zip", nullable = true)
	private String zip;

	@Column(name = "PurchaserID", nullable = true)
	private transient Long memberId; // shard key if database is sharded

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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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
			final AddressEntity address = (AddressEntity) obj;
			returnValue = (getId().intValue() == address.getId().intValue());
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
		final StringBuilder returnString = new StringBuilder("Address [addressId=");
		returnString.append(getId());
		returnString.append(", street1=");
		returnString.append(street1);
		returnString.append(", street2=");
		returnString.append(street2);
		returnString.append(", city=");
		returnString.append(city);
		returnString.append(", state=");
		returnString.append(state);
		returnString.append(", zip=");
		returnString.append(zip);
		returnString.append(", dateCreated=");
		returnString.append(getDateTimeCreated());
		returnString.append(", dateUpdated=");
		returnString.append(getDateTimeUpdated());
		returnString.append("]");

		return returnString.toString();
	}

}
