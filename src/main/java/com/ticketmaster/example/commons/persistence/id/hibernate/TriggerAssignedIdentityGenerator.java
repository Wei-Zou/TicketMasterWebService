package com.ticketmaster.example.commons.persistence.id.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.exception.JDBCExceptionHelper;
import org.hibernate.id.AbstractPostInsertGenerator;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.id.PostInsertIdentityPersister;
import org.hibernate.id.SequenceIdentityGenerator.NoCommentsInsert;
import org.hibernate.id.insert.AbstractReturningDelegate;
import org.hibernate.id.insert.Binder;
import org.hibernate.id.insert.IdentifierGeneratingInsert;
import org.hibernate.id.insert.InsertGeneratedIdentifierDelegate;
import org.hibernate.pretty.MessageHelper;

/**
 * A generator with immediate retrieval through JDBC3
 * {@link java.sql.Connection#prepareStatement(String, String[])
 * getGeneratedKeys}. The value of the identity column must be set from a
 * "before insert trigger"
 * <p/>
 * This generator only known to work with newer Oracle drivers compiled for JDK
 * 1.4 (JDBC3). The minimum version is 10.2.0.1
 * <p/>
 * Note: Due to a bug in Oracle drivers, sql comments on these insert statements
 * are completely disabled.
 * 
 * @author Jean-Pol Landrain Code from
 *         https://forum.hibernate.org/viewtopic.php?t=973262 updated to skip
 *         trigger generated IDs for HSQL dialects and use the IdentityGenerator
 *         instead.
 */
public class TriggerAssignedIdentityGenerator extends AbstractPostInsertGenerator {

    // The DB dialect for which trigger generation will be skipped; set to
    // HSQLDialect
    public static final String DB_DIALECT_SKIP_TRIGGER_GENERATION = "org.hibernate.dialect.HSQLDialect";

    @Override
    public InsertGeneratedIdentifierDelegate getInsertGeneratedIdentifierDelegate(
	    PostInsertIdentityPersister persister, Dialect dialect, boolean isGetGeneratedKeysEnabled)
	    throws HibernateException {

	// Delegate to the Identity based Delegate for HSQLDB
	if (dialect.toString().equals(DB_DIALECT_SKIP_TRIGGER_GENERATION)) {
	    return new HSQLDelegate(persister, dialect);
	} else {
	    return new Delegate(persister, dialect);
	}
    }

    public static class Delegate extends AbstractReturningDelegate {
	private final Dialect dialect;

	private final String[] keyColumns;

	public Delegate(PostInsertIdentityPersister persister, Dialect dialect) {
	    super(persister);
	    this.dialect = dialect;
	    this.keyColumns = getPersister().getRootTableKeyColumnNames();
	    if (keyColumns.length > 1) {
		throw new HibernateException(
			"trigger assigned identity generator cannot be used with multi-column keys");
	    }
	}

	public IdentifierGeneratingInsert prepareIdentifierGeneratingInsert() {
	    NoCommentsInsert insert = new NoCommentsInsert(dialect);
	    return insert;
	}

	protected PreparedStatement prepare(String insertSQL, SessionImplementor session) throws SQLException {
	    return session.getBatcher().prepareStatement(insertSQL, keyColumns);
	}

	protected Serializable executeAndExtract(PreparedStatement insert) throws SQLException {
	    insert.executeUpdate();
	    return IdentifierGeneratorHelper.getGeneratedIdentity(insert.getGeneratedKeys(), getPersister()
		    .getIdentifierType());
	}
    }

    /**
     * I Implemented this class as a workaround to using the trigger generation
     * solution when using HSQL. Since using triggers requires they be setup via
     * DDL prior to executing unit tests, etc... Since we are very auto DDL
     * happy when using hibernate and unit testing this class will allow us to
     * proceed with using auto ddl and not have to worrry about any type of ID
     * generation strategy since this delagate class will generate id in memory
     * always when using the HSQL dialect
     * 
     * @author Vincent.Furlanetto
     * 
     */
    public class HSQLDelegate implements InsertGeneratedIdentifierDelegate {

	private final Dialect dialect;
	private final PostInsertIdentityPersister persister;
	// private final String sequenceNextValFragment;
	private long GLOBAL_SEQUENCE = 1;
	private final String[] keyColumns;
	private static final String ID_LOCATION_MARKER = ":ID";

	public HSQLDelegate(PostInsertIdentityPersister persister, Dialect dialect) {
	    this.persister = persister;
	    this.dialect = dialect;

	    this.keyColumns = getPersister().getRootTableKeyColumnNames();
	    if (keyColumns.length > 1) {
		throw new HibernateException(
			"trigger assigned identity generator cannot be used with multi-column keys");
	    }

	}

	public final Serializable performInsert(String insertSQL, SessionImplementor session, Binder binder) {
	    try {

		Long generatedId = incrementSequence();
		insertSQL = insertSQL.replaceFirst(ID_LOCATION_MARKER, generatedId.toString());
		PreparedStatement insert = session.connection().prepareStatement(insertSQL);

		try {
		    binder.bindValues(insert);
		    insert.executeUpdate();
		    return generatedId;
		} finally {
		    releaseStatement(insert, session);
		}
	    }

	    catch (SQLException sqle) {
		throw JDBCExceptionHelper.convert(session.getFactory().getSQLExceptionConverter(), sqle,
			"could not insert: " + MessageHelper.infoString(persister), insertSQL);
	    }

	}

	protected PostInsertIdentityPersister getPersister() {
	    return this.persister;
	}

	protected void releaseStatement(PreparedStatement insert, SessionImplementor session) throws SQLException {
	    session.getBatcher().closeStatement(insert);
	}

	@Override
	public IdentifierGeneratingInsert prepareIdentifierGeneratingInsert() {
	    NoCommentsInsert insert = new NoCommentsInsert(dialect);
	    insert.addColumn(keyColumns[0], ID_LOCATION_MARKER);
	    return insert;
	}

	private synchronized Long incrementSequence() {
	    GLOBAL_SEQUENCE++;
	    return GLOBAL_SEQUENCE;
	}
    }
}

