package com.ccmapper.demo;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
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
import com.ccmapper.demo.utils.GenerateUtils;
import com.demo.annobean.OrgAnno;
import com.demo.annobean.UserAndOrg;
import com.demo.annobean.UserAnno;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class CustomerAnnoMapperLeftJoinTest extends AbstractTransactionalJUnit4SpringContextTests {

	protected Logger logger = LoggerFactory.getLogger(CustomerAnnoMapperLeftJoinTest.class);

	@Autowired
	private CustomAnnoMapper<UserAndOrg> userAndOrgCustomAnnoMapper;

	@Autowired
	private CustomAnnoMapper<OrgAnno> orgAnnoCustomAnnoMapper;

	@Autowired
	private CustomAnnoMapper<UserAnno> userAnnoCustomAnnoMapper;

	@Before
	public void setup() {
		deleteFromTables("USER");
		deleteFromTables("ORG");
	}

	@After
	public void teardown() {

	}

	@Test
	public void testLeftJoin() {

		logger.info(" start test left join ");
		logger.info(" create test data start ");
		int userSize = 10;
		int orgSize = 5;

		List<UserAnno> listUser = GenerateUtils.generateUserAnnoList(userSize);
		long id = 0;
		for (UserAnno u : listUser) {
			u.setId(id++);
		}

		List<OrgAnno> listOrg = GenerateUtils.generateOrgAnnoList(orgSize);
		for (OrgAnno o : listOrg) {
			o.setId(id++);
		}

		for (UserAnno u : listUser) {
			u.setOrgId(listOrg.get(GenerateUtils.RANDOM.nextInt(listOrg.size())).getId());
		}

		for (UserAnno u : listUser) {
			this.userAnnoCustomAnnoMapper.insert(u);
		}

		for (OrgAnno o : listOrg) {
			this.orgAnnoCustomAnnoMapper.insert(o);
		}
		logger.info(" create test data end");
		logger.info(" selectAll compare with userList size");
		List<UserAndOrg> all = this.userAndOrgCustomAnnoMapper.getAll();
		Assert.assertTrue(all.size() == listUser.size());
		logger.info(" test testLeftJoin is success ");

		logger.info("test data : ");

		printList(listUser);
		printList(listOrg);
		printList(all);

	}

	private void printList(List<?> list) {
		for (Object o : list) {
			logger.info(o.toString());
		}
	}
}
