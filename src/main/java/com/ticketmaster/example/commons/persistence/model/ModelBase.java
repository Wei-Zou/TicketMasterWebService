package com.ticketmaster.example.commons.persistence.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@MappedSuperclass
@EntityListeners({ ModelListener.class })
/**
 * Acting as a base class for any model entity being persisted.
 * It provides basic model operation which are quite essential to any model such as 
 * Identification,versioning,Auditing such as last updated and created. 
 * @see #ModelListener
 */
public class ModelBase implements Serializable, IModelBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ENTITY_HASH_BASE = 217;
	/**
	 * Represents Identification to model
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	/**
	 * Represents Version information
	 */
	@Version
	@Column(name = "persistence_version")
	private Integer persistenceVersion;

	/**
	 * Represents when model is being created
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "date_time_created", nullable = false)
	private Date dateTimeCreated;

	/**
	 * Represents when model is last updated
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "date_time_updated", nullable = false)
	private Date dateTimeUpdated;

	/**
	 * Getting creation date when model is created
	 * 
	 * @return dateTimeCreated
	 */
	public Date getDateTimeCreated() {
		return dateTimeCreated;
	}

	/**
	 * setting created date time information
	 * 
	 * @param dateTimeCreated
	 *            represents created date
	 * @see ModelListener
	 */
	public void setDateTimeCreated(Date dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}

	/**
	 * Getting last updated date when model is last updated
	 * 
	 * @return dateTimeUpdated
	 */
	public Date getDateTimeUpdated() {
		return dateTimeUpdated;
	}

	/**
	 * setting last updated date time information
	 * 
	 * @param dateTimeUpdated
	 *            represents last updated date
	 * @see ModelListener
	 */
	public void setDateTimeUpdated(Date dateTimeUpdated) {
		this.dateTimeUpdated = dateTimeUpdated;
	}

	/**
	 * Getting Identification information
	 * 
	 * @return id represents unique identification to model
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setting Identification information
	 * 
	 * @param id
	 *            represents unique identification to model
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getting persistence version information
	 * 
	 * @return persistenceVersion
	 */
	public Integer getPersistenceVersion() {
		return persistenceVersion;
	}

	/**
	 * Setting persistence version information
	 * 
	 * @param persistenceVersion
	 */
	public void setPersistenceVersion(Integer persistenceVersion) {
		this.persistenceVersion = persistenceVersion;
	}

	/**
	 * equality check for model entity by comparing id
	 * 
	 * If your code requirements better equality logic then the logic
	 * represented in this equals method we highly recommend that you override
	 * the the equals and hashcode method in your own extended class.
	 * 
	 * @param obj
	 *            represents model object
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		ModelBase m = (ModelBase) obj;
		if (m == null || m.getId() == null || getId() == null) {
			return false;
		}

		return getId().longValue() == m.getId().longValue();
	}

	/**
	 * Generated hash code for each model entity If model has id then hash code
	 * will be assigned from id
	 */
	public int hashCode() {
		int hash = ModelBase.ENTITY_HASH_BASE;
		if (getId() != null) {
			hash = getId().hashCode();
		}
		return hash;
	}

}
