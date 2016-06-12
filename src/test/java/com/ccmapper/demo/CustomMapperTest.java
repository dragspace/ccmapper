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

import com.ccmapper.custom.CustomMapper;
import com.ccmapper.demo.utils.CompareUtils;
import com.ccmapper.demo.utils.GenerateUtils;
import com.demo.bean.User;


/**
 * @Description: CustomMapperTest
 * @author xiaoruihu 2016年6月12日 下午2:19:31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class CustomMapperTest  extends AbstractTransactionalJUnit4SpringContextTests{

	protected Logger logger = LoggerFactory.getLogger(CustomMapperTest.class);
	
	@Autowired
	private CustomMapper<User> userCustomMapper;
	
//	@Autowired
//	private CustomMapper<Org> orgCustomMapper;
//	
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
		User beforeUser = GenerateUtils.generateUser();
		userCustomMapper.insert(beforeUser);
		
		User afterUser = userCustomMapper.getByPrimaryKey(beforeUser.getId());
		Assert.assertTrue(beforeUser.equals(afterUser));
		logger.info("insert and getByPrimaryKey test is success");
	}

	@Test
	public void testUpdate(){
		logger.info("update test start");
		User beforeUser = GenerateUtils.generateUser();
		userCustomMapper.insert(beforeUser);
		
		Long primaryKey = beforeUser.getId();
		beforeUser.setName("xxxxA");
		beforeUser.setOrgId(10000);
		userCustomMapper.update(beforeUser);
		
		User afterUser = userCustomMapper.getByPrimaryKey(primaryKey);
		Assert.assertTrue(beforeUser.equals(afterUser));
		logger.info("update test is success");
	}
	
	@Test
	public void testList(){
		logger.info("select return list start");
		List<User> beforeUserList = GenerateUtils.generateUserList(10);
		for(User user : beforeUserList){
			userCustomMapper.insert(user);
		}
		
		List<User> afterUserList = userCustomMapper.getAll();
		Assert.assertTrue(CompareUtils.isListEqual(beforeUserList, afterUserList));
		logger.info("select return list is cuccess");
	}
	
	
}
