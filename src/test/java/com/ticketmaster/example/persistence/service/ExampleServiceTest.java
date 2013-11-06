package com.ticketmaster.example.persistence.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ticketmaster.example.model.AddressEntity;
import com.ticketmaster.example.model.MemberEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/testApplicationContext.xml" })
@Transactional
public class ExampleServiceTest {

	private ExampleMemberPersistenceService exampleMemberPersistenceService;

	@Autowired
	public void setExampleMemberPersistenceService(ExampleMemberPersistenceService exampleMemberPersistenceService) {
		this.exampleMemberPersistenceService = exampleMemberPersistenceService;
	}

	@Test
	public void testGetMember() throws Exception {

		// Test retrieving valid member
		MemberEntity member = exampleMemberPersistenceService.getMember(5);
		assertNotNull(member);
		assertEquals(5, member.getId().intValue());
		assertNotNull(member.getAddress());

		// Test retrieving invalid member
		// Cannot find member
		try {
			member = exampleMemberPersistenceService.getMember(25);
		} catch (Exception ex) {
				// Try another member
				member = exampleMemberPersistenceService.getMember(5);
		}

		// Test retrieving invalid member
		// Member Id invalid value
		try {
			member = exampleMemberPersistenceService.getMember(-1);
		} catch (Exception ex) {
				// Try another member
				member = exampleMemberPersistenceService.getMember(5);
		}

	}

	@Test
    public void testGetMemberCount() throws Exception {
	    long count = exampleMemberPersistenceService.getMemberCount();
	    assertNotNull(count);
	    assertTrue( count > 0 );
	}
	
	@Test
	public void testCreateMember() throws Exception {
		MemberEntity member = new MemberEntity();
		member.setAddress(new AddressEntity());
		member.setId(null);
		member.setDateTimeCreated(null);
		member.setDateTimeUpdated(null);
		member.setFirst("Jason");
		member.setLast("Wu");
		member.setInitial("");
		member.setPrefix("Mr");
		member.setSuffix("");
		member.setEmail("a@b.c");
		member.setPhone1("1112223333");
		member.setPhone2("4445556666");
		member.setOptOut(false);
		member.getAddress().setStreet1("1 one way");
		member.getAddress().setStreet2("apt 1");
		member.getAddress().setCity("Hollywood");
		member.getAddress().setState("CA");
		member.getAddress().setZip("90006");
		member.getAddress().setDateTimeCreated(null);
		member.getAddress().setDateTimeUpdated(null);
		MemberEntity member1 = exampleMemberPersistenceService.createMember(member);
		assertNotNull(member1);
		assertEquals(member1.getFirst(), "Jason");
		assertEquals(member1.getLast(), "Wu");

		// Test with invalid input
		member = new MemberEntity();
		member.setAddress(new AddressEntity());
		member.setId(null);

		Date dateCreated = new Date();
		// DateTimeCreated should not be passed
		member.setDateTimeCreated(dateCreated);
		member.setDateTimeUpdated(null);
		member.setFirst("David");
		member.setLast("Klosowski");
		member.setInitial("");
		member.setPrefix("Mr");
		member.setSuffix("");
		member.setEmail("a@b.c");
		member.setPhone1("1112223333");
		member.setPhone2("4445556666");
		member.setOptOut(false);
		member.getAddress().setStreet1("1 one way");
		member.getAddress().setStreet2("apt 1");
		member.getAddress().setCity("Hollywood");
		member.getAddress().setState("CA");
		member.getAddress().setZip("90001");
		member.getAddress().setDateTimeCreated(null);
		member.getAddress().setDateTimeUpdated(null);

		MemberEntity member2 = null;
		try {
			member2 = exampleMemberPersistenceService.createMember(member);
		} catch (Exception ex) {
			// Act on the exception
				member.setDateTimeCreated(null);
				member2 = exampleMemberPersistenceService.createMember(member);
		}

		assertNotNull(member2);
		assertEquals(member2.getFirst(), "David");
		assertEquals(member2.getLast(), "Klosowski");

	}

	@Test
	public void testDeleteMember() throws Exception {
		exampleMemberPersistenceService.deleteMember(7);

		MemberEntity member = null;
		try {
			member = exampleMemberPersistenceService.getMember(7);
			fail("member was not deleted from the database.");
		} catch (Exception e) {
			// do nothing
		}
		assertNull(member);
	}

	@Test
	public void testUpdateMember() throws Exception {
		MemberEntity aMember = exampleMemberPersistenceService.getMember(6);
		aMember.setFirst("Jane");
		aMember.setDateTimeCreated(null);
		aMember.setDateTimeUpdated(null);
		aMember.setPersistenceVersion(null);
		aMember.getAddress().setDateTimeCreated(null);
		aMember.getAddress().setDateTimeUpdated(null);
		aMember.getAddress().setPersistenceVersion(null);
		
		// Update member to first name = "Jane"
		exampleMemberPersistenceService.updateMember(6, aMember);

		
		MemberEntity member1 = exampleMemberPersistenceService.getMember(6);
		assertNotNull(member1);
		
		// Confirm that the returned member has a first name of "Jane"
		assertEquals(member1.getFirst(), "Jane");
	}

}
