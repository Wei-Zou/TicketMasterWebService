package com.ticketmaster.example.dao;

import com.ticketmaster.example.model.MemberEntity;

/**
 * MemberDao -
 * Provides the data access contract for looking up Members (customers) and their details.
 */
public interface MemberDao {

    /**
     * createMember
     * @param inMember
     */
    void createMember(
    		MemberEntity inMember);

    /**
     * deleteMember
     * deletes a member from the database given the Member object
     * @param member
     */
    void deleteMember(MemberEntity member);

    /**
     * deleteMemberById
     * deletes a member from the database given the id
     * @param id
     */
    void deleteMemberById(Long id);

    /**
     * update Member
     * updates a member in the database
     * @param member
     * @return updated member
     */
    MemberEntity updateMember(MemberEntity member);

    /**
     * getMemberByFirstOrLast
     * Looks up a member by its first or last name
     *
     * @param first
     * @param last
     * @return
     */
    MemberEntity getMemberByFirstOrLast(String first, String last);

    /**
     * getMemberById -
     * Looks up a member by its id
     *
     * @param id - the unique identifier for the Member
     * @return  - Member associated with given id.
     */
    MemberEntity getMemberById(long id);


    /** 
     * getMemberByEmail - 
     * Looks up a member by its email.
     * 
     * @param email - should be unique (if not, implementing class should throw appropriate exception).
     * @return - Member associated with the given email address.
     */
    MemberEntity getMemberByEmail(String email);
    
    /**
     * getMemberCount -
     * Gets the total number of member records.
     * 
     * @return - total number of members in the system.
     */
    Number getMemberCount();

}