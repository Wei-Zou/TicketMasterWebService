package com.ticketmaster.example.commons.persistence.dao;

import java.io.Serializable;
import java.util.List;

import com.ticketmaster.example.commons.persistence.dao.QueryParameter.TemporalType;

/**
 * A generic DAO interface that defines the basic CRUD (Create, Retrieve,
 * Update, and Delete) operations used for most domain model objects.
 */

public interface GenericDao<T, PK extends Serializable> {
	/**
	 * Finds the instance of <code>T</code> identified by <code>pk</code>
	 *
	 * @param pk
	 *         represents unique identification to model
	 * @return T
	 *         returns object from database if match found by pk other wise null
	 */
	T findByPk(PK pk);

	/**
	 * Find all instances of <code>T</code> in the database
	 *
	 * @return a list <code>T</code> objects
	 */
	List<T> findAll();

	/**
	 * Find all instances of <code>T</code> in the database and sorts the
	 * returned list by the field specified by the orderBy parameter.
	 *
	 * @param orderBy
	 *            the field to sort fields by
	 *
	 * @return a list <code>T</code> objects
	 */
	List<T> findAll(String orderBy);

	/**
	 * Find all instances of <code>T</code> in the database, but restrict return
	 * count by limit, and offset by offset count.
	 *
	 * @param offset
	 *            number of rows to offset query.
	 * @param limit
	 *            number of rows to limit result to.
	 *
	 * @return a list <code>T</code> objects
	 */
	List<T> findAll(int offset, int limit);

	/**
	 * Find all instances of <code>T</code> in the database and sorts the
	 * returned list by the field specified by the orderBy parameter, but
	 * restrict return count by limit, and offset by offset count.
	 *
	 * @param orderBy
	 *            the field to sort fields by
	 * @param offset
	 *            number of rows to offset query.
	 * @param limit
	 *            number of rows to limit result to.
	 *
	 * @return a list <code>T</code> objects
	 */
	List<T> findAll(int offset, int limit, String orderBy);

	/**
	 * Returns the total count of records for this entity.
	 *
	 * @return total count.
	 */
	Number countAll();

	/**
	 * Finds all the instances of T from database by query
	 * @param query
	 *           represents JPA query
	 * @return List<T>
	 *           list of model entity from database
	 */
	List<T> find(String query);

	/**
	 * Finds all the instances of T from database by query,but restrict return
	 * count by limit and offset.
	 * @param query
	 *          represents JPA query to find instances of T
	 * @param offset
	 *          number of rows to offset query.
	 * @param limit
	 *          limits the result
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> find(String query, int offset, int limit);

	/**
	 * Finds the instance of T from database
	 * @param query
	 *           represents database query to find instances of T
	 * @param value
	 *           represents database query parameter value
	 * @return List<T>
	 *           returns instance of T from database
	 */
	List<T> find(String query, Object value);

	/**
	 * Finds the instance of T from database,but restrict return
	 * count by limit and offset.
	 * @param query
	 *           represents database query to find instances of T
	 * @param value
	 *           represents database query parameter value
	 * @param offset
	 *           number of rows to offset query.
	 * @param limit
	 *           limits the result
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> find(String query, Object value, int offset, int limit);

	/**
	 * Finds the instance of T from database.
	 * @param query
	 *           represents query to be executed against database
	 * @param name
	 *           represents query parameter name
	 * @param value
	 *          represents database query parameter value
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> find(String query, String name, Object value);

	/**
	 * Finds the instance of T from database but restricts the
	 * result by offset and limit.
	 * @param query
	 *           represents query to be executed against database
	 * @param name
	 *           represents query parameter name
	 * @param value
	 *           represents database query parameter value
	 * @param offset
	 *           number of rows to offset query.
	 * @param limit
	 *          limits the result
	 * @return List<T>
	 *           Returns instances of T from database
	 *
	 */
	List<T> find(String query, String name, Object value, int offset, int limit);

	/**
	 * Finds instances of T having positional parameter in query
	 * @param query
	 *          represents query to be executed against database
	 * @param args
	 *          represents one OR many QueryParameter having value and type
	 * @return List<T>
	 *           Returns instances of T from database
	 * @see QueryParameter
	 */
	List<T> findPositionalParameter(String query, QueryParameter... args);

	/**
	 * Finds instances of T having positional parameter in query but restricts result
	 * by offset and limit
	 * @param query
	 *           represents query to be executed against database
	 * @param offset
	 *           number of rows to offset query.
	 * @param limit
	 *           limits the result
	 * @param args
	 *          represents one OR many QueryParameter having value and type
	 * @return List<T>
	 *           Returns instances of T from database
	 * @see QueryParameter
	 */
	List<T> findPositionalParameter(String query, int offset, int limit,
			QueryParameter... args);

	/**
	 * Finds the instance of T by named queries
	 * @param query
	 *           represents query to be executed against database
	 * @param args
	 *           represents one OR many NamedQueryParameter having parameter name,value and type
	 * @return List<T>
	 *           Returns instances of T from database
	 * @see NamedQueryParameter
	 */
	List<T> findNamedParameter(String query, NamedQueryParameter... args);

	/**
	 * Finds the instance of T by named queries but restrict result by offset and limit
	 * @param query
	 *           represents query to be executed against database
	 * @param offset
	 *           number of rows to offset query.
	 * @param limit
	 *           limits the result
	 * @param args
	 *           represents one OR many NamedQueryParameter having parameter name,value and type
	 * @return List<T>
	 *           Returns instances of T from database
	 * @see NamedQueryParameter
	 */
	List<T> findNamedParameter(String query, int offset, int limit,
			NamedQueryParameter... args);

	/**
	 * Finds the instance of T by named queries
	 * @param queryName
	 *           represents name of NameQuery
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> findByNamedQuery(String queryName);

	/**
	 * Finds the instance of T by named queries but restricts result by offset and limit.
	 * @param queryName
	 *          represents name of NameQuery
	 * @param offset
	 *          number of rows to offset
	 * @param limit
	 *          limits the result
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> findByNamedQuery(String queryName, int offset, int limit);

	/**
	 * Finds the instance of T by named query having parameter name and value
	 * @param queryName
	 *          represents name of NameQuery
	 * @param name
	 *          represents parameter name
	 * @param value
	 *          represents parameter value
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> findByNamedQueryNamedParameter(String queryName, String name,
			Object value);

	/**
	 * Finds the instance of T by named query having parameter name and value.
	 * Restricts the result by offset and limit.
	 * @param queryName
	 *          represents name of NameQuery
	 * @param name
	 *          represents parameter name
	 * @param value
	 *          represents parameter value
	 * @param offset
	 *          number of rows to offset
	 * @param limit
	 *          limits the result
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> findByNamedQueryNamedParameter(String queryName, String name,
			Object value, int offset, int limit);

	/**
	 * Finds the instance of T by named query having parameter name,value and type.
	 * @param queryName
	 *          represents name of NameQuery
	 * @param name
	 *          represents parameter name
	 * @param value
	 *          represents parameter value
	 * @param type
	 *          represents parameter type If any otherwise NONE
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> findByNamedQueryNamedParameter(String queryName, String name,
			Object value, TemporalType type);

	/**
	 * Finds the instance of T by named query having parameter name,value and type,But restricts the result
	 * by limit and offset.
	 * @param queryName
	 *          represents name of NameQuery
	 * @param name
	 *          represents parameter name
	 * @param value
	 *          represents parameter value
	 * @param type
	 *          represents parameter type If any otherwise NONE
	 * @param offset
	 *          number of rows to offset
	 * @param limit
	 *          limits the result
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	List<T> findByNamedQueryNamedParameter(String queryName, String name,
			Object value, TemporalType type, int offset, int limit);

	/**
	 * Find instances of <code>T</code> that match the criteria defined by query
	 * <code>queryName</code>. <code>args</code> provide the values for any
	 * named parameters in the query identified by <code>queryName</code>.
	 *
	 * @param queryName
	 *            the named query to execute
	 * @param args
	 *            the values used by the query
	 * @return a list of <code>T</codE> objects
	 */
	List<T> findByNamedQueryNamedParameter(String queryName,
			NamedQueryParameter... args);

	/**
	 * Find instances of <code>T</code> that match the criteria defined by query
	 * <code>queryName</code>. <code>args</code> provide the values for any
	 * named parameters in the query identified by <code>queryName</code>.
	 * It restricts the result by offset and limit.
	 * @param queryName
	 *            the named query to execute
	 * @param args
	 *            the values used by the query
	 * @param offset
	 *          number of rows to offset
	 * @param limit
	 *          limits the result
	 * @return a list of <code>T</codE> objects
	 */
	List<T> findByNamedQueryNamedParameter(String queryName, int offset,
			int limit, NamedQueryParameter... args);

	/**
	 * Find Primary Keys of instances of <code>T</code> that match the criteria
	 * defined by query <code>queryName</code>. <code>args</code> provide the
	 * values for any named parameters in the query identified by
	 * <code>queryName</code>.
	 *
	 * Assumes that the PK is an Integer, obviously
	 *
	 * @param queryName
	 *            the named query to execute
	 * @param args
	 *            the values used by the query
	 * @return a list of <code>T</code> objects
	 */
	List<PK> findIntegerPKsByNamedQueryNamedParameter(String queryName,
			NamedQueryParameter... args);

	/**
	 * Find Primary Keys of instances of <code>T</code> that match the criteria
	 * defined by query <code>queryName</code>. <code>args</code> provide the
	 * values for any named parameters in the query identified by
	 * <code>queryName</code>.It also restricts result by offset and limit.
	 *
	 * @param queryName
	 *           the named query to execute
	 * @param offset
	 *          number of rows to offset
	 * @param limit
	 *          limits the result	 *
	 * @param args
	 *          the values used by the query
	 * @return a list of <code>T</code> objects
	 */
	List<PK> findIntegerPKsByNamedQueryNamedParameter(String queryName,
			int offset, int limit, NamedQueryParameter... args);

	/**
	 *
	 * Find instances of <code>T</code> that match the criteria defined by query
	 * <code>queryName</code>. <code>args</code> provide values for positional
	 * arguments in the query identified by <code>queryName</code>.
	 *
	 * @param queryName
	 *            the named query to execute
	 * @param args
	 *            the positional values used in the query
	 * @return a list of <code>T</code> objects
	 */
	List<T> findByNamedQueryPositionalParameter(String queryName,
			QueryParameter... args);

	/**
	 * Find instances of <code>T</code> that match the criteria defined by query
	 * <code>queryName</code>. <code>args</code> provide values for positional
	 * arguments in the query identified by <code>queryName</code>.
	 * It also restricts result by limit and offset.
	 * @param queryName
	 *          the named query to execute
	 * @param offset
	 *          number of rows to offset
	 * @param limit
	 *          limits the result
	 * @param args
	 *          the positional values used in the query
	 * @return List<T>
	 *          a list of <code>T</code> objects
	 */
	List<T> findByNamedQueryPositionalParameter(String queryName, int offset,
			int limit, QueryParameter... args);

	/**
	 * Find a single instance of <code>T</code> using the query named
	 * <code>queryName</code>.
	 *
	 * @param queryName
	 *            the name of the query to use
	 * @return T or null if no objects match the criteria
	 * @throws javax.persistence.NonUniqueResultException
	 *             if more than one instance is returned.
	 */
	T findInstanceByNamedQuery(String queryName);

	/**
	 *
	 * Find a single instance of <code>T</code> using the query named
	 * <code>queryName</code> and the arguments identified by <code>args</code>
	 *
	 * @param queryName
	 * @param args
	 *            the arguments for the named query
	 * @return T or null if no objects match the criteria

	 */
	T findInstanceByNamedQueryNamedParameter(String queryName,
			NamedQueryParameter... args);

	/**
	 * Find a single instance of <code>T</code> using the query named
	 * <code>queryName</code> and the arguments identified by <code>args</code>
	 * @param queryName
	 *          the name of the query to use
	 * @param args
	 *          the positional values used in the query
	 * @return T or null if no objects match the criteria
	 */
	T findInstanceByNamedQueryPositionalParameter(String queryName,
			QueryParameter... args);

	/**
	 * Store <code>object</code> in the database.
	 *
	 * @param object
	 *            the instance to save in the database
	 */
	void persist(T object);

	/**
	 * Merges given entity with entity currently persisted.
	 *
	 * @param object
	 *            the object to merge
	 *
	 * @return returns the merged object.
	 */
	T merge(T object);


	/**
	 * Remove <code>object</code> from the database.
	 *
	 * @param object
	 *            the object to be removed from the database
	 */
	void remove(T object);

	// JPA support methods
	/**
	 * Taken from the EntityManager documentation, Synchronize the persistence
	 * context to the underlying database.
	 *
	 */
	void flush();

	/**
	 * Taken from the EntityManager documentation: Clear the persistence
	 * context, causing all managed entities to become detached. Changes made to
	 * entities that have not been flushed to the database will not be
	 * persisted.
	 *
	 */
	void clear();

	/**
	 * Just so direct SQL can be executed from any of these.
	 *
	 * @param query
	 *            native update query to perform
	 *
	 * @return number of rows updated.
	 */
	Number executeUpdateNativeQuery(String query);

	/**
	 * <code>evictFromSecondLevelCache</code>-
	 * Evicts specific entry from second level cache.
	 *
	 * @param primaryKey primary key of entry to evict.
	 */
	void evictFromSecondLevelCache(PK primaryKey);

	/**
	 * <code>evictAllFromSecondLevelCache</code>-
	 * Evicts all entries of a particular entity type from second level cache.
	 */
	void evictAllFromSecondLevelCache();


	/**
	 * <code>evictFromAllCaches</code>-
	 * Evicts specific entry from all caches.  This may be implementation specific as certain persistence
	 * implementations may only have a second level cache.
	 *
	 * @param primaryKey primary key of entry to evict.
	 */
	public void evictFromAllCaches(PK primaryKey);
	
	/**
	 * <code>executeUpdateByNamedQuery</code>-
	 * Execute the update query that match the criteria defined by query
	 * <code>queryName</code>. 
	 * 
	 * @param queryName 
	 *            the named query to execute
	 * @param args
	 *            the values used by the query
	 *            
	 * @return number of rows updated.
	 */
	Number executeUpdateByNamedQuery(String queryName);
	
	/**
	 * <code>executeUpdateByNamedQueryNamedParameter</code>-
	 * Execute the update query that match the criteria defined by query
	 * <code>queryName</code>. <code>args</code> provide the values for any
	 * named parameters in the query identified by <code>queryName</code>.
	 * 
	 * @param queryName 
	 *            the named query to execute
	 * @param args
	 *            the values used by the query
	 *            
	 * @return number of rows updated.
	 */
	Number executeUpdateByNamedQueryNamedParameter(String queryName,
			NamedQueryParameter... args);
	
	

}
