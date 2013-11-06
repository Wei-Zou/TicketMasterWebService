package com.ticketmaster.example.persistence.service.hibernate;

import static com.ticketmaster.example.constants.ConstantDataManager.OP_CREATE;
import static com.ticketmaster.example.constants.ConstantDataManager.OP_UPDATE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ticketmaster.example.dao.MemberDao;
import com.ticketmaster.example.model.AddressEntity;
import com.ticketmaster.example.model.MemberEntity;
import com.ticketmaster.example.persistence.service.ExampleMemberPersistenceService;
import com.ticketmaster.example.util.MemberValidator;

public class ExampleMemberPersistenceServiceHibernateImpl implements ExampleMemberPersistenceService {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ExampleMemberPersistenceServiceHibernateImpl.class);


    //
    // data sources, these will be injected
    //
    private MemberDao memberDao;

    public MemberDao getMemberDao() {
    	return memberDao;
    }

    public void setMemberDao(final MemberDao memberDao) {
    	this.memberDao = memberDao;
    }



    /* (non-Javadoc)
	 * @see com.ticketmaster.example.persistence.service.hibernate.ExampleMemberPersistenceService#getMember(long)
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
	public MemberEntity getMember(final long memberId) throws Exception {

    	LOG.debug("getMember() > Arguments: memberId={}", memberId );

    	//
    	// validate input
    	//
    	// Id valid range
    	if (memberId < 1) {

    	    throw new Exception("Member Id check failed - Could not retrieve member (" + memberId + ")");
    	}

    	//
    	// input is good
    	//
    	// Member exists
    	final MemberEntity returnMember = memberDao.getMemberById(memberId);
    	
    	if (returnMember == null) {
    	    LOG.info("No matching Member found for id=({})", memberId);

    	    throw new Exception("Member check failed - Could not retrieve member (" + memberId + ")");
    	}

    	return returnMember;
    }

    /* (non-Javadoc)
	 * @see com.ticketmaster.example.persistence.service.hibernate.ExampleMemberPersistenceService#createMember(com.ticketmaster.example.model.Member)
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)    
    @Override
	public MemberEntity createMember(final MemberEntity inMember) throws Exception {

    	LOG.debug("createMember()");

    	MemberEntity returnMember = null;

    	//
    	// validate
    	//
    	MemberValidator.validateMember(inMember, OP_CREATE);

    	try {

    	    // make sure new id is assigned and add defaults
    	    inMember.setId(null);
    	    if (inMember.getOptOut() == null) {
	    		inMember.setOptOut(Boolean.FALSE);
	    		inMember.setPersistenceVersion(1);
    	    }

    	    // add an address if one is not given
    	    if (inMember.getAddress() != null) {
	    		inMember.getAddress().setId(null);
	    		inMember.getAddress().setPersistenceVersion(1);
    	    }

    	    // create it
    	    memberDao.createMember(inMember);

    	    // save was successful, return
    	    returnMember = inMember;

    	    return returnMember;

    	} catch (Exception e) {
    	    LOG.info("Unable to create Member: {}", e);
    	    
    	    // Re-throw exception after logging
    	    throw e;
    	}
    }

    /* (non-Javadoc)
	 * @see com.ticketmaster.example.persistence.service.hibernate.ExampleMemberPersistenceService#deleteMember(long)
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)    
    @Override
	public void deleteMember(final long memberId) throws Exception{
    	LOG.debug("getMember() > Arguments: memberId=({})", memberId );
    	// validate a good id was passed in
    	if (memberId < 1) {

    	    LOG.debug("invalid id for deletion");
    	    // throw new Fault(FaultCode.VALIDATION_ERROR,
    	    // "cannot remove a member that has an invalid MemberId");
    	    throw new Exception("Member Id check failed - Could not delete member with memberId=(" + memberId + ")");

    	} else {

    	    try {

	    		// first check that the member exists
	    		final MemberEntity aMember = memberDao.getMemberById(memberId);
	    		if (aMember == null) {
	
	    		    LOG.debug("No matching Member found id: {},", memberId);
	
	    		    throw new Exception("Member check failed - Could not delete member with memberId=(" + memberId + ")");
	
	    		} else {
	
	    		    // we verified it exists, now try to remove
	    		    memberDao.deleteMemberById(memberId);
	    		}

    	    } catch (Exception e) {
	    		LOG.info("Unable to delete Member: {}", e);
	    		throw e;
    	    }
    	}

    }

    /* (non-Javadoc)
	 * @see com.ticketmaster.example.persistence.service.hibernate.ExampleMemberPersistenceService#updateMember(long, com.ticketmaster.example.model.Member)
	 */
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)    
    @Override
	public MemberEntity updateMember(final long memberId, final MemberEntity inMember)
            throws Exception {

    	MemberEntity returnMember = null;
    	
    	//
    	// validate
    	//
    	MemberValidator.validateMember(inMember, OP_UPDATE);


    	final AddressEntity inAddress = inMember.getAddress();

    	// find the member by id first, no point in running update if it doesn't
    	// exist
    	final MemberEntity member = memberDao.getMemberById(memberId);
    	if (member == null) {

    	    LOG.debug("No matching Member found id: {},", memberId);

    	    throw new Exception("Member id check failed - Could not update member with memberId=(" + memberId + ")");

    	}

    	// update fields from object passed in

    	try {

    	    member.setFirst(inMember.getFirst());
    	    member.setLast(inMember.getLast());
    	    member.setInitial(inMember.getInitial());
    	    member.setPrefix(inMember.getPrefix());
    	    member.setSuffix(inMember.getSuffix());
    	    member.setEmail(inMember.getEmail());
    	    member.setPhone1(inMember.getPhone1());
    	    member.setPhone2(inMember.getPhone2());
    	    member.setOptOut(inMember.getOptOut());

    	    // not sure why this is not happening automatically
    	    Integer perVersion = member.getPersistenceVersion();
    	    if (perVersion == null) {
    		perVersion = 0;
    	    }
    	    member.setPersistenceVersion(perVersion + 1);

    	    // update the address
    	    if (inAddress != null) {
    		member.getAddress().setStreet1(inAddress.getStreet1());
    		member.getAddress().setStreet2(inAddress.getStreet2());
    		member.getAddress().setCity(inAddress.getCity());
    		member.getAddress().setState(inAddress.getState());
    		member.getAddress().setZip(inAddress.getZip());

    		// not sure why this is not happening automatically
    		perVersion = member.getAddress().getPersistenceVersion();
    		if (perVersion == null) {
    		    perVersion = 0;
    		}
    		member.getAddress().setPersistenceVersion(perVersion + 1);
    	    }

    	    // run the update
    	    returnMember = memberDao.updateMember(member);

    	} catch (Exception e) {
    	    LOG.info("Unable to update member: {}", e);
    	    throw e;
    	}

    	return returnMember;
    	
    }

    /* (non-Javadoc)
	 * @see com.ticketmaster.example.persistence.service.hibernate.ExampleMemberPersistenceService#getMemberCount()
	 */
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)    
    @Override
	public long getMemberCount() throws Exception {

    	long returnValue = 0;

    	Number numberValue = memberDao.getMemberCount();

    	if (numberValue != null) {
    	    returnValue = numberValue.longValue();
    	}

    	return returnValue;

    }

}
