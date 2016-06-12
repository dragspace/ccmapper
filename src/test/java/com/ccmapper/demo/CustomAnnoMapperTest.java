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
import com.ccmapper.demo.utils.CompareUtils;
import com.ccmapper.demo.utils.GenerateUtils;
import com.demo.annobean.UserAnno;


/**
 * @Description: CustomMapperTest
 * @author xiaoruihu 2016年6月12日 下午2:19:31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class CustomAnnoMapperTest  extends AbstractTransactionalJUnit4SpringContextTests{

	protected Logger logger = LoggerFactory.getLogger(CustomAnnoMapperTest.class);
	
	@Autowired
	private CustomAnnoMapper<UserAnno> userAnnoCustomAnnoMapper;
	
//	@Autowired
//	private CustomAnnoMapper<OrgAnno> orgAnnoCustomAnnoMapper;
	
	@Before
	public void setup() {
		deleteFromTables("USER");
		deleteFromTables("ORG");
	}

	@After
	public void teardown() {
	}
	
	
	@Test
	public void testInsert(){
		logger.info("insert and getByPrimaryKey test start");
		UserAnno beforeUser = GenerateUtils.generateUserAnno();
		userAnnoCustomAnnoMapper.insert(beforeUser);
		
		UserAnno afterUser = userAnnoCustomAnnoMapper.getByPrimaryKey(beforeUser.getId());
		Assert.assertTrue(beforeUser.equals(afterUser));
		logger.info("insert and getByPrimaryKey test is success");
	}

	@Test
	public void testUpdate(){
		logger.info("update test start");
		UserAnno beforeUser = GenerateUtils.generateUserAnno();
		userAnnoCustomAnnoMapper.insert(beforeUser);
		
		Long primaryKey = beforeUser.getId();
		beforeUser.setName("xxxxA");
		beforeUser.setOrgId(10000);
		userAnnoCustomAnnoMapper.update(beforeUser);
		
		UserAnno afterUser = userAnnoCustomAnnoMapper.getByPrimaryKey(primaryKey);
		Assert.assertTrue(beforeUser.equals(afterUser));
		logger.info("update test is success");
	}
	
	@Test
	public void testList(){
		logger.info("select return list start");
		List<UserAnno> beforeUserList = GenerateUtils.generateUserAnnoList(10);
		for(UserAnno user : beforeUserList){
			userAnnoCustomAnnoMapper.insert(user);
		}
		
		List<UserAnno> afterUserList = userAnnoCustomAnnoMapper.getAll();
		Assert.assertTrue(CompareUtils.isListEqual(beforeUserList, afterUserList));
		logger.info("select return list is cuccess");
	}
	
	
}
