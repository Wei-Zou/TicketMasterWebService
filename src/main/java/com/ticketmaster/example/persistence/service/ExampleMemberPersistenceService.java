package com.ticketmaster.example.persistence.service;

import com.ticketmaster.example.model.MemberEntity;

public interface ExampleMemberPersistenceService {

	/**
	 * Looks up a member by Id
	 *
	 * @param id
	 *            the unique identifier of the member to be looked up
	 * @return Member
	 * @throws Exception
	 *             an error that occurred during retrieving the member
	 */
	public abstract MemberEntity getMember(long memberId) throws Exception;

	/**
	 * Creates a permanent record of the given Member object
	 *
	 * @param inMember
	 *            the Member object to be stored/created
	 * @return the Member object that was successfully stored/created
	 * @throws Exception
	 *             an error that occurred during the creation of the member
	 */
	public abstract MemberEntity createMember(MemberEntity inMember) throws Exception;

	/**
	 * Deletes the record associated with the given Member unique identifier
	 *
	 * @param memberId
	 *            the id for the Member to be deleted
	 * @return a status code of "SUCCESS" when the operation completes successfully
	 * @throws BusinessException
	 *             an error that occurred during the deletion of the member
	 */
	public abstract void deleteMember(long memberId) throws Exception;

	/**
	 * Updates the given Member
	 *
	 * @param memberId
	 *          the id for the Member to be updaetd
	 * @param inMember
	 *          the Member object to be updated
	 * @return
	 * @throws Exception
	 *             an error that occured during the updating of the member
	 */
	public abstract MemberEntity updateMember(long memberId, MemberEntity inMember)
			throws Exception;

	/**
	 * Looks up the total number of Member records in the system.
	 *
	 * @return Member count
	 */
	public abstract long getMemberCount() throws Exception;

}