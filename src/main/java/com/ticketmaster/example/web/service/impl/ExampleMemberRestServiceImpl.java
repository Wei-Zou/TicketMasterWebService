package com.ticketmaster.example.web.service.impl;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ticketmaster.example.model.MemberEntity;
import com.ticketmaster.example.persistence.service.ExampleMemberPersistenceService;
import com.ticketmaster.example.web.model.Member;
import com.ticketmaster.example.web.service.ExampleMemberRestService;

public class ExampleMemberRestServiceImpl implements ExampleMemberRestService {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ExampleMemberRestServiceImpl.class);
	
	
	private ExampleMemberPersistenceService exampleMemberPersistenceService;

	public void setExampleMemberPersistenceService(ExampleMemberPersistenceService exampleMemberPersistenceService) {
		this.exampleMemberPersistenceService = exampleMemberPersistenceService;
	}

	public ExampleMemberPersistenceService getExampleMemberPersistenceService() {
		return exampleMemberPersistenceService;
	}
	
	private DozerBeanMapper dozerBeanMapper;

	public DozerBeanMapper getDozerBeanMapper() {
		return dozerBeanMapper;
	}

	public void setDozerBeanMapper(final DozerBeanMapper dozerBeanMapper) {
		this.dozerBeanMapper = dozerBeanMapper;
	}	

	// TODO: Keep this commented out for production!
	// This is only needed to generate the JSON schema
	/*
	public static void generateAddressSchema(Class<?> clazz) throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper(); JsonSchema jsonSchema =
			mapper.generateJsonSchema(clazz);
		LOG.debug("JSON:{}",jsonSchema.toString());
	}
	*/
	
	/* (non-Javadoc)
	 * @see com.ticketmaster.example.web.service.ExampleRestService#getMember(long)
	 */
	@Override
	public Member getMember(final long memberId) throws Exception {

    	LOG.debug("getMember() > Arguments: memberId={}", memberId );
		
		//try { generateAddressSchema(Member.class); } catch (JsonMappingException e) {e.printStackTrace();}
    	
    	
    	final MemberEntity memberEntity = exampleMemberPersistenceService.getMember(memberId);

    	final Member returnMember = dozerBeanMapper.map(memberEntity, Member.class);

    	return returnMember;

	}

	/* (non-Javadoc)
	 * @see com.ticketmaster.example.web.service.ExampleRestService#createMember(com.ticketmaster.example.web.model.Member)
	 */
	@Override
	public Member createMember(final Member inMember) throws Exception {
		
    	LOG.debug("createMember() > Arguments: inMember={}", inMember);
		
    	final MemberEntity createMemberEntity = dozerBeanMapper.map(inMember, MemberEntity.class);

    	LOG.debug("createMember() > createMemberEntity={}", createMemberEntity);
    	
    	final MemberEntity createdMemberEntity = exampleMemberPersistenceService.createMember(createMemberEntity);

    	LOG.debug("createMember() > createdMemberEntity={}", createdMemberEntity);
    	
    	final Member returnMember = dozerBeanMapper.map(createdMemberEntity, Member.class);

    	return returnMember;		
	}

	/* (non-Javadoc)
	 * @see com.ticketmaster.example.web.service.ExampleRestService#deleteMember(long)
	 */
	@Override
	public void deleteMember(final long memberId) throws Exception {

    	LOG.debug("getMember() > Arguments: memberId={}", memberId );
    	
		exampleMemberPersistenceService.deleteMember(memberId);
	}

	/* (non-Javadoc)
	 * @see com.ticketmaster.example.web.service.ExampleRestService#updateMember(long, com.ticketmaster.example.web.model.Member)
	 */
	@Override
	public Member updateMember(final long memberId, final Member inMember)
			throws Exception {

		LOG.debug("updateMember() > Arguments: memberId={}, inMember={}", memberId, inMember );
		
    	final MemberEntity updateMemberEntity = dozerBeanMapper.map(inMember, MemberEntity.class);

    	final MemberEntity updatedMemberEntity = exampleMemberPersistenceService.updateMember(memberId, updateMemberEntity);

    	final Member returnMember = dozerBeanMapper.map(updatedMemberEntity, Member.class);
		
		return returnMember;
	}

	/* (non-Javadoc)
	 * @see com.ticketmaster.example.web.service.ExampleRestService#getMemberCount()
	 */
	@Override
	public long getMemberCount() throws Exception {

		LOG.debug("getMemberCount() >" );
		
		return exampleMemberPersistenceService.getMemberCount();
	}



}
