package com.ticketmaster.example.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ticketmaster.example.model.AddressEntity;
import com.ticketmaster.example.model.MemberEntity;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/testApplicationContext.xml"})
@Transactional
public class MemberDaoTest {
  
  private MemberDao memberDao;

  @Autowired
  public void setMemberDao(MemberDao inMemberDao){
    memberDao = inMemberDao;
  }
  
  @Test
  public void testGetMemberById() throws Exception {
    MemberEntity member = memberDao.getMemberById(1);
    assertNotNull(member);
  }
    
  @Test
  public void testGetMemberCount() throws Exception {
      Number count = memberDao.getMemberCount();
      assertNotNull(count);
      assertTrue( count.longValue() > 0);
  }
  
  @Test
  public void testCreateMember() throws Exception {
    MemberEntity member = null;
    try{
      member = memberDao.getMemberByFirstOrLast("Jason", "Wu");
      fail( "member with firstname \"Jason\" or \"Wu\" already exists in database" );
    } catch (javax.persistence.NoResultException e){
      //expect this exception to be thrown
    }
    assertNull(member);
    member = new MemberEntity();
    member.setAddress(new AddressEntity());
    member.setFirst("Jason1");
    member.setLast("Wu1");
    member.setInitial("K");
    member.setPrefix("Mr");
    member.setSuffix("");
    member.setEmail("a@b.c");
    member.setPhone1("1112223333");
    member.setPhone2("4445556666");
    member.setOptOut(true);
    member.getAddress().setStreet1("street1");
    member.getAddress().setStreet2("street2");
    member.getAddress().setCity("LA");
    member.getAddress().setState("CA");
    member.getAddress().setZip("90909");
    memberDao.createMember(member); 
    member = memberDao.getMemberByFirstOrLast("Jason1", "Wu1");
    
    assertNotNull(member);
    assertEquals(member.getFirst(), "Jason1");
    assertEquals(member.getLast(), "Wu1");
  }
  
  @Test
  public void testDeleteMember() throws Exception {
    MemberEntity member = memberDao.getMemberById(2);
    assertNotNull(member);
    memberDao.deleteMember(member);
    
    member = memberDao.getMemberById(2);
    assertNull(member);
  }
  
  @Test
  public void testDeleteMemberById() throws Exception {
    MemberEntity member = memberDao.getMemberById(4);
    assertNotNull(member);
    memberDao.deleteMember(member);
    
    member = memberDao.getMemberById(4);
    assertNull(member);
  }
  
  @Test
  public void testUpdateMember() throws Exception {
    MemberEntity member = memberDao.getMemberById(3);
    assertNotNull(member);
    assertEquals(member.getFirst(), "Clark");
    assertEquals(member.getLast(), "Kent");
    member.setFirst("Super");
    member.setLast("Man");
    member.setEmail("red@cape.com");
    member = memberDao.updateMember(member);
    member = memberDao.getMemberById(3);
    assertEquals(member.getFirst(), "Super");
    assertEquals(member.getLast(), "Man");
    assertEquals(member.getEmail(), "red@cape.com");   
  }
  
  @Test
  public void testGetMemberByEmail() {
      MemberEntity member = memberDao.getMemberById(3);
      assertNotNull(member);
      assertFalse("red@cape.com".equalsIgnoreCase( member.getEmail() ) );
      member.setEmail("red@cape.com");
      member = memberDao.updateMember(member);
      member = memberDao.getMemberById(3);
      assertEquals(member.getEmail(), "red@cape.com");   

      member = memberDao.getMemberByEmail("red@cape.com");
      assertNotNull(member);
      assertEquals(member.getId(), Long.valueOf(3));
      
  }

}
