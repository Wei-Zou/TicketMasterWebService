package com.ticketmaster.example.dao.hibernate;

import com.ticketmaster.example.dao.MemberDao;
import com.ticketmaster.example.model.MemberEntity;
import com.ticketmaster.example.commons.persistence.dao.NamedQueryParameter;
import com.ticketmaster.example.commons.persistence.dao.hibernate.ModelBaseDaoHibernateImpl;

/**
 * MemberHibernateDao -
 * Hibernate implementation of MemberDao
 */
public class MemberHibernateDao extends ModelBaseDaoHibernateImpl<MemberEntity> implements MemberDao {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ticketmaster.example.dao.MemberDao#createMember(com.ticketmaster.example.model.Member)
	 */
	public void createMember(MemberEntity inMember) {
		this.create(inMember);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ticketmaster.example.dao.MemberDao#deleteMember(com.ticketmaster.example.model.Member)
	 */
	public void deleteMember(MemberEntity member) {
		this.delete(member);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ticketmaster.example.dao.MemberDao#deleteMemberById(java.lang.Long)
	 */
	public void deleteMemberById(Long id) {
		deleteMember(getMemberById(id));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ticketmaster.example.dao.MemberDao#updateMember(com.ticketmaster.example.model.Member)
	 */
	public MemberEntity updateMember(MemberEntity member) {
		return this.update(member);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ticketmaster.example.dao.MemberDao#getMemberByFirstOrLast(java.lang.String, java.lang.String)
	 */
	public MemberEntity getMemberByFirstOrLast(String first, String last) {
		return this.findInstanceByNamedQueryNamedParameter("getMembersByFirstOrLast", new NamedQueryParameter("first",
				first), new NamedQueryParameter("last", last));
	}


	/* (non-Javadoc)
	 * @see com.ticketmaster.example.dao.MemberDao#getMemberById(long)
	 */
	public MemberEntity getMemberById(long id) {
		return this.findByPk(Long.valueOf(id));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ticketmaster.example.dao.MemberDao#getMemberByEmail(java.lang.String)
	 */
	public MemberEntity getMemberByEmail(String email) {
	    return this.findInstanceByNamedQueryNamedParameter("getMembersByEmail", new NamedQueryParameter("email", email));
	}

	/*
     * (non-Javadoc)
     *
     * @see com.ticketmaster.example.dao.MemberDao#getMemberCount()
     */
	public Number getMemberCount() {
	    return this.countAll();
	}
}