package com.ticketmaster.example.web.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Prediction - Represents one prediction for a ticket sold on x days before the
 * event. Prediction includes percent chance that the ticket will be able to
 * sell and the price the ticket will sell.
 */
@XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.PROPERTY)
@XmlRootElement(name = "Prediction")
public class Prediction implements Serializable {
	// TODO
	private static final long serialVersionUID = -485992108994597544L;

	// number of days before the event, something like <date of event> - <curr
	// date>
	private int daysToEvent;

	// recommended prices, index corresponds with pSale
	private float[] prices;

	// percent chance of successful sale, index corresponds with prices
	private float[] percentSuccessfulSale;

	public int getDaysToEvent() {
		return this.daysToEvent;
	}

	public void setDaysToEvent(final int daysToEvent) {
		this.daysToEvent = daysToEvent;
	}

	public float[] getPrices() {
		return this.prices;
	}

	public void setPrices(final float[] prices) {
		this.prices = prices;
	}

	public float[] getPercentSuccessfulSale() {
		return this.percentSuccessfulSale;
	}

	public void setPercentSuccessfulSale(final float[] percentSuccessfulSale) {
		this.percentSuccessfulSale = percentSuccessfulSale;
	}

	/**
	 * <code>equals</code>- Checks if input object is the same as this instance.
	 * NOTE: only checks if daysToEvent is the same. Very useless function in
	 * this case.
	 * 
	 * @param obj
	 *            <code>Object</code> to compare against
	 * @return true if equal false if not equal
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean returnValue;

		if (this == obj) {
			returnValue = true;
		} else if ((obj == null) || (obj.getClass() != super.getClass())) {
			returnValue = false;
		} else {
			final Prediction prediction = (Prediction) obj;
			final EqualsBuilder builder = new EqualsBuilder();
			returnValue = builder.appendSuper(super.equals(obj))
					.append(prediction.daysToEvent, this.daysToEvent)
					.isEquals();
		}

		return returnValue;
	}

	/**
	 * <code>hashCode</code>- Determines hash value for equality determination.
	 * 
	 * @return hash value of this object instance
	 */
	@Override
	public int hashCode() {

		final HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(this.daysToEvent).toHashCode();

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
				.append("daysToEvent", this.daysToEvent)
				.append("prices", Arrays.toString(this.prices))
				.append("percentSuccessfulSale",
						Arrays.toString(this.percentSuccessfulSale));

		return returnString.toString();
	}

}
