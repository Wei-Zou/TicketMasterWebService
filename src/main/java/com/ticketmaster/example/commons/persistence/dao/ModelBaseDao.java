package com.ticketmaster.example.commons.persistence.dao;

import java.util.List;

import com.ticketmaster.example.commons.persistence.model.IModelBase;

/**
 * ModelBaseDao responsible for managing model CRUD (Create, Retrieve, Update,
 * and Delete).
 *
 * @param <T>
 *            represents model entity which extends ModelBase
 */
public interface ModelBaseDao<T extends IModelBase> {

	/**
	 * Store <code>object</code> in the database.
	 *
	 * @param object
	 *            the instance of model to be saved in the database
	 */
	void create(T object);

	/**
	 * Update <code>object</code> in the database.
	 *
	 * @param object
	 *            the instance of model to be updated in the database
	 * @return returns the merged object.
	 *
	 */
	T update(T object);

	/**
	 * Delete <code>object</code> from the database.
	 *
	 * @param object
	 *            the object to be deleted from the database
	 */
	void delete(T object);

	/**
	 * Finds the instance of identified by <code>id</code>
	 *
	 * @param id
	 *            represents unique identification to model
	 * @return object from database If found otherwise null
	 */
	T getById(Long id);

	/**
	 * Delete <code>list of object</code> from the database.
	 *
	 * @param list
	 *            list to be deleted from the database
	 */
	void delete(List<T> list);

}
