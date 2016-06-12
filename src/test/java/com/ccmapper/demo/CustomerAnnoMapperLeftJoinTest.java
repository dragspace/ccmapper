package com.ccmapper.demo;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ccmapper.customanno.CustomAnnoMapper;
import com.demo.annobean.UserAndOrg;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class CustomerAnnoMapperLeftJoinTest extends AbstractTransactionalJUnit4SpringContextTests {

	protected Logger logger = LoggerFactory.getLogger(CustomerAnnoMapperLeftJoinTest.class);

	@Autowired
	private CustomAnnoMapper<UserAndOrg> userAndOrgCustomAnnoMapper;

	// @Autowired
	// private CustomAnnoMapper<OrgAnno> orgAnnoCustomAnnoMapper;

	@Before
	public void setup() {
//		deleteFromTables("USER");
//		deleteFromTables("ORG");
	}

	@After
	public void teardown() {
		
	}
	
	@Test
	public void testSelect(){
		
		List<UserAndOrg> dd = this.userAndOrgCustomAnnoMapper.getAll();
		
		System.out.println(dd);
	}

}
