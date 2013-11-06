package com.ticketmaster.example.commons.persistence.dao.hibernate;


import java.util.List;
import com.ticketmaster.example.commons.persistence.dao.ModelBaseDao;
import com.ticketmaster.example.commons.persistence.model.IModelBase;

/**
 * ModelBaseDaoHibernateImpl implementation of ModelBaseDao responsible for managing model CRUD (Create, Retrieve,
 * Update, and Delete). Implementation of CRUD functionality has been provided by extending GenericDao Hibernate implementation.
 * @param <T> represents model entity which extends IModelBase
 * @see GenericDaoHibernateImpl
 * @see ModelBaseDao
 */
public class ModelBaseDaoHibernateImpl<T extends IModelBase> extends GenericDaoHibernateImpl<T,Long> implements ModelBaseDao<T>{

	/**
	 * Default constructor
	 */
	public ModelBaseDaoHibernateImpl() {
		super();
	}

	/**
	 * Creation of ModelBaseDaoHibernateImpl with type
	 * @param type represents domain model
	 */
	public ModelBaseDaoHibernateImpl(Class<T> type) {
		super(type);
	}

	/**
	 * Finds the instance of identified by <code>id</code>
	 *
	 * @param id
	 *         represents unique identification to model
	 * @return object from database If found otherwise null
	 */
	@Override
	public T getById(Long id) {
		return super.findByPk(id);
	}

	/**
	 * Store <code>object</code> in the database.
	 *
	 * @param object
	 *            the instance of model to be saved in the database
	 */
	@Override
	public void create(T object) {
		this.persist(object);
	}

	/**
	 * Delete <code>object</code> from the database.
	 *
	 * @param object
	 *            the object to be deleted from the database
	 */
	@Override
	public void delete(T object) {
		if(object == null) {
			return;
		}
		this.remove(object);

	}

	/**
	 * Update <code>object</code> in the database.
	 *
	 * @param object
	 *            the instance of model to be updated in the database
	 *  @return returns the merged object.
	 *
	 */
	@Override
	public T update(T object) {
		return this.merge(object);
	}

	/**
	 * Delete <code>list of object</code> from the database.
	 *
	 * @param list
	 *            list to be deleted from the database
	 */
	@Override
	public void delete(List<T> list) {
		if(list != null) {
    		for(T t : list) {
    			delete(t);
    		}
		}
	}

}