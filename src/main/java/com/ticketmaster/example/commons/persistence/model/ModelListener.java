package com.ticketmaster.example.commons.persistence.model;


import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 
 * Acting as a entity listener to any type of model entity.
 * Annotated with PrePersist and PreUpdate.
 * It always being called before persisting and updating any model entity.
 * @see @EntityListeners,@PrePersist,@PreUpdate
 */
public class ModelListener {

	/**
	 * Responsible for setting dates such as last update,created
	 * @param modelBase represents model entity being persisted OR updated
	 */
	@PrePersist
	@PreUpdate
	public void setDates(IModelBase modelBase) {
		Date now = new Date();
		if (modelBase.getDateTimeCreated() == null) {
			modelBase.setDateTimeCreated(now);
		}
		modelBase.setDateTimeUpdated(now);
	}

}
