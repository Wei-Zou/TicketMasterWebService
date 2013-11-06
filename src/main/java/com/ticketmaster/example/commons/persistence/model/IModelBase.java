package com.ticketmaster.example.commons.persistence.model;

import java.util.Date;

public interface IModelBase {
	
	/**
	 * Getting creation date when model is created
	 * 
	 * @return dateTimeCreated
	 */
	public Date getDateTimeCreated();

	/**
	 * setting created date time information
	 * 
	 * @param dateTimeCreated
	 *            represents created date
	 * @see ModelListener
	 */
	public void setDateTimeCreated(Date dateTimeCreated);

	/**
	 * Getting last updated date when model is last updated
	 * 
	 * @return dateTimeUpdated
	 */
	public Date getDateTimeUpdated();

	/**
	 * setting last updated date time information
	 * 
	 * @param dateTimeUpdated
	 *            represents last updated date
	 * @see ModelListener
	 */
	public void setDateTimeUpdated(Date dateTimeUpdated);

	/**
	 * Getting Identification information
	 * 
	 * @return id represents unique identification to model
	 */
	public Long getId();

	/**
	 * Setting Identification information
	 * 
	 * @param id
	 *            represents unique identification to model
	 */
	public void setId(Long id);

	/**
	 * Getting persistence version information
	 * 
	 * @return persistenceVersion
	 */
	public Integer getPersistenceVersion() ;

	/**
	 * Setting persistence version information
	 * 
	 * @param persistenceVersion
	 */
	public void setPersistenceVersion(Integer persistenceVersion);

}
