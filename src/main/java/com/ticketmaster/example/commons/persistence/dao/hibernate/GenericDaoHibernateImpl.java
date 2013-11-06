package com.ticketmaster.example.commons.persistence.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import com.ticketmaster.example.commons.persistence.dao.GenericDao;
import com.ticketmaster.example.commons.persistence.dao.NamedQueryParameter;
import com.ticketmaster.example.commons.persistence.dao.QueryParameter;
import com.ticketmaster.example.commons.persistence.dao.QueryParameter.TemporalType;
import com.ticketmaster.example.commons.persistence.exceptions.StaleObjectException;

/**
 * A generic DAO interface implementation that defines the basic CRUD (Create, Retrieve,
 * Update, and Delete) operations used for most domain model objects.
 * It also provides helper method for executing finder queries.
 * @param <T> represents entity model instance
 * @param <PK> represents unique identification of entity.
 * @see GenericDao
 */
public class GenericDaoHibernateImpl<T, PK extends Serializable> implements
		GenericDao<T, PK> {

	/**
	 * represents logger instance
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(GenericDaoHibernateImpl.class);
	/**
	 * represents if query cache property is enabled
	 */
	private boolean queryCacheEnabled = false;
	/**
	 * Represents instance of entity manager
	 */
	private EntityManagerFactory entityManagerFactory;
	/**
	 * Represents type of entity model
	 */
	private Class<T> type;

	/**
	 * This is a convenience constructor, if you don't want to declare a
	 * specific DAO class, that is to say the methods defined in GenericDao are
	 * sufficient, then you this constructor and you'll have a type safe,
	 * generic DAO implementation.
	 *
	 * @param type
	 *            the model type you are providing DAO services for
	 */
	public GenericDaoHibernateImpl(final Class<T> type) {
		this.type = type;
	}

	/**
	 * This is the default constructor, if you subclass this class, all you need
	 * to do is define the type of the subclass and GenericDaoHibernateImpl will
	 * have the correct type. Here is how you would subclass
	 * GenericDaoHibernateImpl:
	 *
	 * <pre>
	 * <code>
	 * public class ForumDaoHibernate extends GenericDaoHibernateImpl&lt;Forum, Long&gt; implements ForumDao
	 * </code>
	 * </pre>
	 */
	public GenericDaoHibernateImpl() {
		@SuppressWarnings("unchecked")
		Class<T> aType = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.type = aType;
	}

	/**
	 * set the type of model instance to be managed by Dao.
	 * @param type
	 */
	public void setType(Class<T> type) {
		this.type = type;
	}

	/**
	 * Get the type of the model entity that is associated with this Dao
	 * @return the type of the model instance to be managed by this Dao
	 */
	protected Class<T> getType() {
		return type;
	}

	/**
	 * Find all instances of <code>T</code> in the database
	 *
	 * @return a list <code>T</code> objects
	 */
	@Override
	public List<T> findAll() {

		return findAll(0, 0, null);
	}

	/**
	 * Find all instances of <code>T</code> in the database and sorts the
	 * returned list by the field specified by the orderBy parameter.
	 *
	 * @param orderBy
	 *            the field to sort fields by
	 *
	 * @return a list <code>T</code> objects
	 */
	@Override
	public List<T> findAll(String orderBy) {

		return findAll(0, 0, orderBy);
	}

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
	@Override
	public List<T> findAll(int offset, int limit) {
		return findAll(offset, limit, null);
	}

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
	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(int offset, int limit, String orderBy) {

		StringBuilder buffer = new StringBuilder();
		buffer.append("select o from ").append(type.getSimpleName()).append(" o");
		if (orderBy != null) {
			buffer.append(" order by ").append(orderBy);
		}

		Query q = getTransactionalEntityManager()
				.createQuery(buffer.toString());
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		return q.getResultList();
	}

	/**
	 * Returns the total count of records for this entity.
	 *
	 * @return total count.
	 */
	@Override
	public Number countAll() {
		Query q = getTransactionalEntityManager().createQuery(
				"select count(o) from " + type.getSimpleName() + " o");
		return (Number) q.getSingleResult();
	}

	/**
	 * Store <code>object</code> in the database.
	 *
	 * @param object
	 *            the instance to save in the database
	 */
	@Override
	public void persist(T object) {
		LOG.info("Attempting to persist object");
		getTransactionalEntityManager().persist(object);
	}

	/**
	 * Taken from the EntityManager documentation, Synchronize the persistence
	 * context to the underlying database.
	 *
	 */
	@Override
	public void flush() {
		getTransactionalEntityManager().flush();

	}

	/**
	 * Taken from the EntityManager documentation: Clear the persistence
	 * context, causing all managed entities to become detached. Changes made to
	 * entities that have not been flushed to the database will not be
	 * persisted.
	 *
	 */
	@Override
	public void clear() {
		getTransactionalEntityManager().clear();
	}

	/**
	 * Remove <code>object</code> from the database.
	 *
	 * @param object
	 *            the object to be removed from the database
	 */
	@Override
	public void remove(T object) {
		if (!getTransactionalEntityManager().contains(object)) {
			// if object isn't managed by EM, load it into EM
			object = getTransactionalEntityManager().merge(object);
		}
		// object is now a managed object so it can be removed.
		getTransactionalEntityManager().remove(object);
	}

	/**
	 * Merges given entity with entity currently persisted.
	 *
	 * @param object
	 *            the object to merge
	 *
	 * @return returns the merged object.
	 */
	@Override
	public T merge(T object) {
		try {
			return getTransactionalEntityManager().merge(object);
		} catch (org.hibernate.StaleObjectStateException exception) {
			throw new StaleObjectException(object, exception);
		}
	}


	/**
	 * Execute JPA Query
	 * @param query
	 *         represents JPA Query
	 * @return List
	 *         instance of T from database
	 */
	@SuppressWarnings("rawtypes")
	public List executeJpql(String query) {
		Query q = getTransactionalEntityManager().createQuery(query);
		return q.getResultList();
	}

	/**
	 * Create Native Query with positional parameters
	 * @param query
	 *           represents native query
	 * @param args
	 *           the positional values used in the query
	 * @return Query
	 */
	private Query getAndCreateNativeQueryPositionalParameters(String query,
			QueryParameter... args) {

		Query q = getTransactionalEntityManager().createNativeQuery(query);
		// ((QueryImpl<?>) q).setCacheable(true);
		populateQueryPositionalParameters(q, args);

		return q;
	}

	/**
	 * Just so direct SQL can be executed from any of these.
	 *
	 * @param query
	 *            native update query to perform
	 *
	 * @return number of rows updated.
	 */
	@Override
	public Number executeUpdateNativeQuery(String query) {
		Query q = getTransactionalEntityManager().createNativeQuery(query);
		return q.executeUpdate();
	}

	/**
	 * Execute update operation using a literal query.
	 *
	 * @param query
	 *            literal update query to perform.
	 *
	 * @return number of rows updated/removed.
	 */
	public Number executeUpdateLiteralQuery(String query) {
		Query q = getTransactionalEntityManager().createQuery(query);
		return q.executeUpdate();
	}


	/**
	 * Execute aggregate function native query
	 * @param query
	 *         represents native query
	 * @return Number
	 *         return result from query
	 */
	public Number executeAggregateFunctionByNativeQuery(String query) {
		try {
			Query q = getTransactionalEntityManager().createNativeQuery(query);
			return (Number) q.getSingleResult();
		} catch (NoResultException e) {
			return Integer.valueOf(0);
		}
	}

	/**
	 * Execute aggregate function native query having positional parameters
	 * @param query
	 *          represents native query
	 * @param args
	 *          the positional values used in the query
	 * @return Number
	 *          return result from query
	 */
	public Number executeAggregateFunctionByNativeQueryPositionalParameters(
			String query, QueryParameter... args) {
		try {
			Query q = getAndCreateNativeQueryPositionalParameters(query, args);
			return (Number) q.getSingleResult();
		} catch (NoResultException e) {
			return Integer.valueOf(0);
		}
	}

	/**
	 * Find objects from data base by native query having positional parameters
	 * @param query
	 *         represents native query
	 * @param args
	 *         the positional values used in the query
	 * @return List<Object>
	 *          return result from query
	 */
	public List<Object> findByNativeQueryPositionalParameters(String query,
			QueryParameter... args) {
		return findByNativeQueryPositionalParameters(query, 0, 0, args);
	}

	/**
	 * Find objects from data base by native query having positional parameters
	 * but restricts result by offset and limit.
	 * @param query
	 *         represents native query
	 * @param args
	 *         the positional values used in the query
	 * @param offset
	 *          number of rows to offset
	 * @param limit
	 *          limits the result
	 * @return List<Object>
	 *          return result from query
	 */
	public List<Object> findByNativeQueryPositionalParameters(String query,
			int offset, int limit, QueryParameter... args) {
		Query q = getAndCreateNativeQueryPositionalParameters(query, args);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}

		@SuppressWarnings("unchecked")
		List<Object> results = q.getResultList();

		return results;
	}

	/**
	 * Create Literal query
	 * @param query
	 *           represents query
	 * @return Query
	 */
	protected Query getAndCreateLitteralQueryNamedParameters(String query) {
		Query q = getTransactionalEntityManager().createQuery(query);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		return q;
	}

	/**
	 * Create literal query by named parameters
	 * @param query
	 *          represents literal query
	 * @param args
	 *          the arguments for the named query
	 * @return Query
	 */
	private Query getAndCreateLitteralQueryNamedParameters(String query,
			NamedQueryParameter... args) {

		Query q = getTransactionalEntityManager().createQuery(query);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		populateQueryNamedParameter(q, args);

		return q;
	}

	/**
	 * Execute aggregate function literal query
	 * @param query
	 *          represents literal query
	 * @return Number
	 *          return result from query
	 */
	public Number executeAggregateFunctionByLitteralQuery(String query) {
		try {
			Query q = getTransactionalEntityManager().createQuery(query);
			return (Number) q.getSingleResult();
		} catch (NoResultException e) {
			return Integer.valueOf(0);
		}
	}

	/**
	 * Execute aggregate function native query having named parameters
	 * @param query
	 *          represents native query
	 * @param list
	 *          contains the positional values used in the query
	 * @return Number
	 *          return result from query
	 */
	public Number executeAggregateFunctionByLitteralQueryNamedParameter(
			String query, List<NamedQueryParameter> list) {
		NamedQueryParameter[] args = new NamedQueryParameter[list.size()];
		for (int i = 0; i < list.size(); i++) {
			args[i] = list.get(i);
		}
		return executeAggregateFunctionByLitteralQueryNamedParameter(query,
				args);
	}

	/**
	 * Execute aggregate function native query having named parameters
	 * @param query
	 *          represents literal query
	 * @param args
	 *          the positional values used in the query
	 * @return Number
	 *          return result from query
	 */
	public Number executeAggregateFunctionByLitteralQueryNamedParameter(
			String query, NamedQueryParameter... args) {
		try {
			Query q = getAndCreateLitteralQueryNamedParameters(query, args);
			return (Number) q.getSingleResult();
		} catch (NoResultException e) {
			return Integer.valueOf(0);
		}
	}

	/**
	 * Find all instances of <code>T</code> in the database by literal query
	 * @param query
	 *          represents literal query
	 * @return a list <code>T</code> objects
	 */
	public List<T> findByLitteralQuery(String query) {
		return findByLitteralQuery(query, 0, 0);
	}

	/**
	 * Finds all the instances of T from database by literal query,but restrict return
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
	public List<T> findByLitteralQuery(String query, int offset, int limit) {
		Query q = getAndCreateLitteralQueryNamedParameters(query);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();

		return result;
	}

	/**
	 * Finds the instance of T by named queries
	 * @param query
	 *           represents literal query to be executed against database
	 * @param args
	 *           represents one OR many NamedQueryParameter having parameter name,value and type
	 * @return List<T>
	 *           Returns instances of T from database
	 * @see NamedQueryParameter
	 */
	public List<T> findByLitteralQueryNamedParameter(String query,
			NamedQueryParameter... args) {
		return findByLitteralQueryNamedParameter(query, 0, 0, args);
	}

	/**
	 * Finds the instance of T by named literal query but restrict result by offset and limit
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
	public List<T> findByLitteralQueryNamedParameter(String query, int offset,
			int limit, NamedQueryParameter... args) {
		Query q = getAndCreateLitteralQueryNamedParameters(query, args);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();

		return result;
	}

	/**
	 * Finds the instance of T by literal query having named parameters
	 * @param query
	 *           represents literal query to be executed against database
	 * @param list
	 *           contains NamedQueryParameter having parameter name,value and type
	 * @return List<T>
	 *           Returns instances of T from database
	 * @see NamedQueryParameter
	 */
	public List<T> findByLitteralQueryNamedParameter(String query,
			List<NamedQueryParameter> list) {
		return findByLitteralQueryNamedParameter(query, 0, 0, list);
	}

	/**
	 * Finds the instance of T by literal query having named parameters
	 * but restricts result by offset and limit
	 * @param query
	 *           represents literal query to be executed against database
	 * @param offset
	 *           number of rows to offset query.
	 * @param limit
	 *           limits the result
	 * @param list
	 *           contains NamedQueryParameter having parameter name,value and type
	 * @return List<T>
	 *           Returns instances of T from database
	 * @see NamedQueryParameter
	 */
	public List<T> findByLitteralQueryNamedParameter(String query, int offset,
			int limit, List<NamedQueryParameter> list) {

		NamedQueryParameter[] args = new NamedQueryParameter[list.size()];
		for (int i = 0; i < list.size(); i++) {
			args[i] = list.get(i);
		}
		return findByLitteralQueryNamedParameter(query, offset, limit, args);
	}

	/**
	 * Find Primary Keys of instances of <code>T</code> that match the criteria
	 * defined by query <code>query</code>. <code>list</code> provide the
	 * values for any named parameters in the query identified by
	 * <code>queryName</code>.
	 *
	 * @param query
	 *            the named query to execute
	 * @param list
	 *            the values used by the query
	 * @return a list of <code>PK</code> primary keys for objects
	 */
	public List<PK> findPKsByLitteralQueryNamedParameter(
			String query, List<NamedQueryParameter> list) {
		return findPKsByLitteralQueryNamedParameter(query, 0, 0, list);
	}

	/**
	 * Find Primary Keys of instances of <code>T</code> that match the criteria
	 * defined by query <code>query</code>. <code>list</code> provide the
	 * values for any named parameters in the query identified by
	 * <code>queryName</code>.It restricts the result by offset and limit.
	 *
	 * @param query
	 *            the named query to execute
	 * @param list
	 *            the values used by the query
	 * @param offset
	 *          number of rows to offset
	 * @param limit
	 *          limits the result
	 * @return a list of <code>PK</code> primary keys for objects
	 */
	public List<PK> findPKsByLitteralQueryNamedParameter(
			String query, int offset, int limit, List<NamedQueryParameter> list) {

		NamedQueryParameter[] args = new NamedQueryParameter[list.size()];
		for (int i = 0; i < list.size(); i++) {
			args[i] = list.get(i);
		}
		Query q = getAndCreateLitteralQueryNamedParameters(query, args);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<PK> result = q.getResultList();

		return result;
	}

	/**
	 * Find a single instance of <code>T</code> using the query
	 * <code>query</code> having named parameter <code>list</code>.
	 *
	 * @param query
	 *            the query to use to find instance
	 * @param list
	 *            the values used by the query
	 * @return T or null if no objects match the criteria
	 *
	 */
	public T findInstanceByLitteralQueryNamedParameter(String query,
			List<NamedQueryParameter> list) {
		NamedQueryParameter[] args = new NamedQueryParameter[list.size()];
		for (int i = 0; i < list.size(); i++) {
			args[i] = list.get(i);
		}
		return findInstanceByLitteralQueryNamedParameter(query, args);
	}

	/**
	 *
	 * Find a single instance of <code>T</code> using the literal query
	 * <code>query</code> and the arguments identified by <code>args</code>
	 *
	 * @param query
	 * @param args
	 *            the arguments for the named query
	 * @return T or null if no objects match the criteria

	 */
	public T findInstanceByLitteralQueryNamedParameter(String query,
			NamedQueryParameter... args) {
		Query q = getAndCreateLitteralQueryNamedParameters(query, args);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		@SuppressWarnings("unchecked")
		T result = (T) q.getSingleResult();

		return result;
	}

	/**
	 * Create Named query with positional parameters
	 * @param queryName
	 *           represents named query
	 * @param args
	 *           represents one OR many NamedQueryParameter having parameter name,value and type
	 * @return Query
	 */
	private Query getAndCreateQueryNamedParameters(String queryName,
			NamedQueryParameter... args) {

		Query q = getTransactionalEntityManager().createNamedQuery(queryName);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		populateQueryNamedParameter(q, args);

		return q;
	}

	/**
	 * Create Named query with positional parameters
	 * @param queryName
	 *           represents named query
	 * @param args
	 *           represents one OR many QueryParameter having parameter value and type
	 * @return Query
	 */
	private Query getAndCreateNamedQueryPositionalParameters(String queryName,
			QueryParameter... args) {

		Query q = getTransactionalEntityManager().createNamedQuery(queryName);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		populateQueryPositionalParameters(q, args);

		return q;
	}

	/**
	 * Execute aggregate function by named query
	 * @param queryName
	 *            represents named query
	 * @return Number
	 *            return result from query
	 */
	public Number executeAggregateFunctionByNamedQuery(String queryName) {
		try {
			Query q = getTransactionalEntityManager().createNamedQuery(
					queryName);
			return (Number) q.getSingleResult();
		} catch (NoResultException e) {
			return Integer.valueOf(0);
		}
	}

	/**
	 * Execute aggregate function by named query having named parameters
	 * @param queryName
	 *            represents named query
	 * @param args
	 *           represents one OR many NamedQueryParameter having parameter name,value and type
	 * @return Number
	 *            return result from query
	 */
	public Number executeAggregateFunctionByNamedQueryNamedParameter(
			String queryName, NamedQueryParameter... args) {
		try {
			Query q = getAndCreateQueryNamedParameters(queryName, args);
			return (Number) q.getSingleResult();
		} catch (NoResultException e) {
			return Integer.valueOf(0);
		}
	}

	/**
	 * Finds the instance of T by named queries
	 * @param queryName
	 *           represents name of NameQuery
	 * @return List<T>
	 *           Returns instances of T from database
	 */
	@Override
	public List<T> findByNamedQuery(String queryName) {
		return findByNamedQuery(queryName, 0, 0);
	}

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
	@Override
	public List<T> findByNamedQuery(String queryName, int offset, int limit) {

		Query q = getTransactionalEntityManager().createNamedQuery(queryName);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);

		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> results = q.getResultList();

		return results;
	}

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
	@Override
	public List<T> findByNamedQueryNamedParameter(String queryName,
			String name, Object value) {
		return findByNamedQueryNamedParameter(queryName, 0, 0,
				new NamedQueryParameter(name, value));
	}

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
	@Override
	public List<T> findByNamedQueryNamedParameter(String queryName,
			String name, Object value, int offset, int limit) {
		return findByNamedQueryNamedParameter(queryName, offset, limit,
				new NamedQueryParameter(name, value));
	}

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
	@Override
	public List<T> findByNamedQueryNamedParameter(String queryName,
			String name, Object value, TemporalType type) {
		return findByNamedQueryNamedParameter(queryName, 0, 0,
				new NamedQueryParameter(name, value, type));
	}

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
	@Override
	public List<T> findByNamedQueryNamedParameter(String queryName,
			String name, Object value, TemporalType type, int offset, int limit) {
		return findByNamedQueryNamedParameter(queryName, offset, limit,
				new NamedQueryParameter(name, value, type));
	}

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
	@Override
	public List<T> findByNamedQueryNamedParameter(String queryName,
			NamedQueryParameter... args) {
		return findByNamedQueryNamedParameter(queryName, 0, 0, args);
	}

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
	@Override
	public List<T> findByNamedQueryNamedParameter(String queryName, int offset,
			int limit, NamedQueryParameter... args) {
		Query q = getAndCreateQueryNamedParameters(queryName, args);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();

		return result;
	}

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
	@Override
	public List<PK> findIntegerPKsByNamedQueryNamedParameter(
			String queryName, NamedQueryParameter... args) {
		return findIntegerPKsByNamedQueryNamedParameter(queryName, 0, 0, args);
	}

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
	@Override
	public List<PK> findIntegerPKsByNamedQueryNamedParameter(
			String queryName, int offset, int limit,
			NamedQueryParameter... args) {
		Query q = getAndCreateQueryNamedParameters(queryName, args);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<PK> result = q.getResultList();

		return result;
	}

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
	@Override
	public List<T> findByNamedQueryPositionalParameter(String queryName,
			QueryParameter... args) {
		return findByNamedQueryPositionalParameter(queryName, 0, 0, args);
	}

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
	@Override
	public List<T> findByNamedQueryPositionalParameter(String queryName,
			int offset, int limit, QueryParameter... args) {
		Query q = getAndCreateNamedQueryPositionalParameters(queryName, args);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}

		@SuppressWarnings("unchecked")
		List<T> results = q.getResultList();

		return results;
	}

	/**
	 *
	 * Find a single instance of <code>T</code> using the query named
	 * <code>queryName</code> and the arguments identified by <code>args</code>
	 *
	 * @param queryName
	 * @return T or null if no objects match the criteria

	 */
	@Override
	public T findInstanceByNamedQuery(String queryName) {
		Query q = getTransactionalEntityManager().createNamedQuery(queryName);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		@SuppressWarnings("unchecked")
		T result = (T) q.getSingleResult();

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ticketmaster.platform.commons.persistence.dao.GenericDao#
	 * findInstanceByNamedQueryNamedParameter(String, NamedQueryParameter[])
	 */
	@Override
	public T findInstanceByNamedQueryNamedParameter(String queryName,
			NamedQueryParameter... args) {
		Query q = getAndCreateQueryNamedParameters(queryName, args);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		@SuppressWarnings("unchecked")
		T result = (T) q.getSingleResult();

		return result;
	}

	/**
	 * Find a single instance of <code>T</code> using the query named
	 * <code>queryName</code> and the arguments identified by <code>args</code>
	 * @param queryName
	 *          the name of the query to use
	 * @param args
	 *          the positional values used in the query
	 * @return T or null if no objects match the criteria
	 */
	@Override
	public T findInstanceByNamedQueryPositionalParameter(String queryName,
			QueryParameter... args) {
		Query q = getAndCreateNamedQueryPositionalParameters(queryName, args);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		@SuppressWarnings("unchecked")
		T result = (T) q.getSingleResult();

		return result;
	}

	/**
	 * Populating query parameters for named query
	 * @param q
	 *        represents query object
	 * @param args
	 *        represents one OR many NamedQueryParameter having name ,value and type as args
	 */
	private void populateQueryNamedParameter(Query q,
			NamedQueryParameter... args) {

		if (args != null) {

			for (NamedQueryParameter qp : args) {
				if (qp.getType() == null || qp.getType() == TemporalType.NONE) {
					q.setParameter(qp.getParameterName(), qp.getValue());
				} else {
					switch (qp.getType()) {
					case DATE:
						if (qp.getValue() instanceof Date) {
							q.setParameter(qp.getParameterName(),
									(Date) qp.getValue(),
									javax.persistence.TemporalType.DATE);
						} else if (qp.getValue() instanceof Calendar) {
							q.setParameter(qp.getParameterName(),
									(Calendar) qp.getValue(),
									javax.persistence.TemporalType.DATE);
						} else {
							q.setParameter(qp.getParameterName(), qp.getValue());
						}
						break;
					case TIME:
						if (qp.getValue() instanceof Date) {
							q.setParameter(qp.getParameterName(),
									(Date) qp.getValue(),
									javax.persistence.TemporalType.TIME);
						} else if (qp.getValue() instanceof Calendar) {
							q.setParameter(qp.getParameterName(),
									(Calendar) qp.getValue(),
									javax.persistence.TemporalType.TIME);
						} else {
							q.setParameter(qp.getParameterName(), qp.getValue());
						}
						break;
					case TIMESTAMP:
						if (qp.getValue() instanceof Date) {
							q.setParameter(qp.getParameterName(),
									(Date) qp.getValue(),
									javax.persistence.TemporalType.TIMESTAMP);
						} else if (qp.getValue() instanceof Calendar) {
							q.setParameter(qp.getParameterName(),
									(Calendar) qp.getValue(),
									javax.persistence.TemporalType.TIMESTAMP);
						} else {
							q.setParameter(qp.getParameterName(), qp.getValue());
						}
						break;
					default:
						throw new java.lang.IllegalArgumentException("Invalid TemporalType provided: " + qp.getType());
					}
				}
			}
		}
	}

	/**
	 * Populating query positional parameters
	 * @param q
	 *        represents query object
	 * @param args
	 *        represents one OR many QueryParameter having name and value as args
	 */
	private void populateQueryPositionalParameters(Query q,
			QueryParameter... args) {
		if (args != null) {

			int position = 1;

			for (QueryParameter qp : args) {
				if (qp.getType() == null || qp.getType() == TemporalType.NONE) {
					q.setParameter(position++, qp.getValue());
				} else {
					switch (qp.getType()) {
					case DATE:
						if (qp.getValue() instanceof Date) {
							q.setParameter(position++, (Date) qp.getValue(),
									javax.persistence.TemporalType.DATE);
						} else if (qp.getValue() instanceof Calendar) {
							q.setParameter(position++,
									(Calendar) qp.getValue(),
									javax.persistence.TemporalType.DATE);
						} else {
							q.setParameter(position++, qp.getValue());
						}
						break;
					case TIME:
						if (qp.getValue() instanceof Date) {
							q.setParameter(position++, (Date) qp.getValue(),
									javax.persistence.TemporalType.TIME);
						} else if (qp.getValue() instanceof Calendar) {
							q.setParameter(position++,
									(Calendar) qp.getValue(),
									javax.persistence.TemporalType.TIME);
						} else {
							q.setParameter(position++, qp.getValue());
						}
						break;
					case TIMESTAMP:
						if (qp.getValue() instanceof Date) {
							q.setParameter(position++, (Date) qp.getValue(),
									javax.persistence.TemporalType.TIMESTAMP);
						} else if (qp.getValue() instanceof Calendar) {
							q.setParameter(position++,
									(Calendar) qp.getValue(),
									javax.persistence.TemporalType.TIMESTAMP);
						} else {
							q.setParameter(position++, qp.getValue());
						}
						break;
					default:
						throw new java.lang.IllegalArgumentException("Invalid TemporalType provided: " + qp.getType());
					}
				}
			}
		}
	}

	/**
	 * Finds all the instances of T from database by query
	 * @param query
	 *           represents JPA query
	 * @return List<T>
	 *           list of model entity from database
	 */
	@Override
	public List<T> find(String query) {
		return find(query, 0, 0);
	}

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
	@Override
	public List<T> find(String query, int offset, int limit) {
		Query q = getTransactionalEntityManager().createQuery(query);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();

		return result;
	}

	/**
	 * Finds the instance of T from database
	 * @param query
	 *           represents database query to find instances of T
	 * @param value
	 *           represents database query parameter value
	 * @return List<T>
	 *           returns instance of T from database
	 */
	@Override
	public List<T> find(String query, Object value) {
		return find(query, value, 0, 0);
	}

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
	@Override
	public List<T> find(String query, Object value, int offset, int limit) {
		Query q = getTransactionalEntityManager().createQuery(query);
		q.setParameter(1, value);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();

		return result;
	}

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
	@Override
	public List<T> find(String query, String name, Object value) {
		return find(query, name, value, 0, 0);
	}

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
	@Override
	public List<T> find(String query, String name, Object value, int offset,
			int limit) {
		Query q = getTransactionalEntityManager().createQuery(query);
		q.setParameter(name, value);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();

		return result;
	}

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
	@Override
	public List<T> findNamedParameter(String query, NamedQueryParameter... args) {
		return findNamedParameter(query, 0, 0, args);
	}

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
	@Override
	public List<T> findNamedParameter(String query, int offset, int limit,
			NamedQueryParameter... args) {
		Query q = getTransactionalEntityManager().createQuery(query);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		populateQueryNamedParameter(q, args);

		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();

		return result;

	}

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
	@Override
	public List<T> findPositionalParameter(String query, QueryParameter... args) {
		return findPositionalParameter(query, 0, 0, args);
	}

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
	@Override
	public List<T> findPositionalParameter(String query, int offset, int limit,
			QueryParameter... args) {
		Query q = getTransactionalEntityManager().createQuery(query);
		((QueryImpl<?>) q).getHibernateQuery().setCacheable(queryCacheEnabled);
		populateQueryPositionalParameters(q, args);

		if (offset > 0) {
			q.setFirstResult(offset);
		}
		if (limit > 0) {
			q.setMaxResults(limit);
		}
		@SuppressWarnings("unchecked")
		List<T> result = q.getResultList();

		return result;
	}

	/**
	 * Finds the instance of <code>T</code> identified by <code>pk</code>
	 *
	 * @param pk
	 *         represents unique identification to model
	 * @return T
	 *         returns object from database if match found by pk other wise null
	 */
	@Override
	public T findByPk(PK pk) {
		if (pk == null) {
			return null;
		}
		return getTransactionalEntityManager().find(type, pk);
	}



	/**
	 * Getting transactional manager
	 * @return EntityManager transaction aware entity manager
	 * @throws IllegalStateException if entity manager returns null
	 */
	protected EntityManager getTransactionalEntityManager() {
		LOG.info("Attempting to obtain transactional entity manager");
		EntityManagerFactory emf = getEntityManagerFactory();
		// Assert.state(emf != null, "No EntityManagerFactory specified");
		if (emf == null) {
			throw new IllegalStateException(
					"No EntityManagerFactory specified.");
		}
		EntityManager em = EntityManagerFactoryUtils
				.getTransactionalEntityManager(emf, null);
		if (em == null) {
			//em = emf.createEntityManager();
			LOG.error("Attempting to obtain transactional entity manager failed!");
			throw new IllegalStateException("There is no current transaction.");
		}
		return em;
	}


	/**
	 * Getting entity manager factory
	 * @return the entityManagerFactory
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	/**
	 * setting entity manager factory
	 * @param entityManagerFactory
	 *            the entityManagerFactory to set
	 */
	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	/**
	 * setting property for query cache
	 * @param queryCacheEnabled
	 */
	public void setQueryCacheEnabled(boolean queryCacheEnabled) {
		this.queryCacheEnabled = queryCacheEnabled;
	}

	/**
	 * check for query cache enabled
	 * @return true if query cache is enabled otherwise false
	 */
	public boolean isQueryCacheEnabled() {
		return queryCacheEnabled;
	}


	/* (non-Javadoc)
	 * @see com.ticketmaster.platform.commons.persistence.dao.GenericDao#evictFromSecondLevelCache(java.io.Serializable)
	 */
	public void evictFromSecondLevelCache(PK primaryKey){

		LOG.debug("evictFromSecondLevelCache() > primaryKey={}",primaryKey);
		EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

		if (entityManagerFactory != null)
		{
			Cache cache = entityManagerFactory.getCache();

			if (cache != null)
			{
				LOG.debug("evictFromSecondLevelCache() > Evicting type={} and object with primaryKey={}", this.type, primaryKey);
				cache.evict(this.type, primaryKey);
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.ticketmaster.platform.commons.persistence.dao.GenericDao#evictAllFromSecondLevelCache()
	 */
	public void evictAllFromSecondLevelCache(){

		LOG.debug("evictAllFromSecondLevelCache()");

		EntityManagerFactory entityManagerFactory = getEntityManagerFactory();

		if (entityManagerFactory != null)
		{
			Cache cache = entityManagerFactory.getCache();

			if (cache != null)
			{
				cache.evict(this.type);
			}
		}

	}


	/* (non-Javadoc)
	 * @see com.ticketmaster.platform.commons.persistence.dao.GenericDao#evictFromAllCaches(java.io.Serializable)
	 */
	public void evictFromAllCaches(PK primaryKey){

		LOG.debug("evictFromAllCaches() > primaryKey={}",primaryKey);

		// Evict from second level cache
		this.evictFromSecondLevelCache(primaryKey);

		// Evict from first level cache
		Session session = getTransactionalEntityManager().unwrap(Session.class);

		T object = this.findByPk(primaryKey);

		if (session != null && object != null)
		{
			LOG.debug("evictFromAllCaches() > Found session and object, evicting object: ", object);
			session.evict(object);
		}

	}

	/* (non-Javadoc)
	 * @see com.ticketmaster.platform.commons.persistence.dao.GenericDao#executeUpdateByNamedQuery(java.lang.String)
	 */
	@Override
	public Number executeUpdateByNamedQuery(String queryName) {

		Query q = getTransactionalEntityManager().createNamedQuery(queryName);
		return q.executeUpdate();

	}

	/* (non-Javadoc)
	 * @see com.ticketmaster.platform.commons.persistence.dao.GenericDao#executeUpdateByNamedQueryNamedParameter(java.lang.String, com.ticketmaster.platform.commons.persistence.dao.NamedQueryParameter[])
	 */
	@Override
	public Number executeUpdateByNamedQueryNamedParameter(String queryName,
			NamedQueryParameter... args) {

		Query q = getTransactionalEntityManager().createNamedQuery(queryName);
		populateQueryNamedParameter(q, args);
		return q.executeUpdate();

	}



}
