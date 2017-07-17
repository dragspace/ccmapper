package com.ccmapper.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ccmapper.core.example.Example;
import com.ccmapper.core.example.Example.Criteria;
import com.ccmapper.customanno.CustomAnnoMapper;
import com.ccmapper.demo.utils.GenerateUtils;
import com.demo.annobean.UserAnno;

/**
 * @Description: CommonMapperExampleTest 动态查询测试
 * @author xiaoruihu 2016年6月10日 上午10:12:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class CustomAnnoMapperExampleTest extends AbstractTransactionalJUnit4SpringContextTests {

	protected Logger logger = LoggerFactory.getLogger(CustomAnnoMapperExampleTest.class);

	@Autowired
	private CustomAnnoMapper<UserAnno> userAnnoCustomMapper;

	@Before
	public void setup() {
		deleteFromTables("USER");
		deleteFromTables("ORG");

		// init data
		List<UserAnno> list = GenerateUtils.generateUserAnnoList(10);
		for (UserAnno ua : list) {
			userAnnoCustomMapper.insert(ua);
		}
		logger.info("init data : ");
		printList(list);
		logger.info("init data end ");
	}

	@Test
	public void testSetProperties() {
		Example e = new Example();
		e.selectProperties("id", "name");
		printList(userAnnoCustomMapper.getListByExample(e));
	}

	@Test
	public void testCrieria22() {
		Example e = new Example();

		Criteria c = e.createAndCriteria();
		c.equalTo("sex", 1);
		
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
	//	e.createOrCriteria().equalTo("name" , "abc").equalTo("age", 5).equalTo("name", "yyyy").in("age", "1" , "3");
		
		e.createOrCriteria().in("age", "1" , "3");

		
		e.createAndCriteria().equalTo("sex", 2);
//		Criteria or = e.orCriteria();
//		or.greaterThan("age", 50);
//		
//		//or.greaterThan("age", 50);
//
//		Criteria or2 = e.orCriteria();
//		//or2.greaterThan("age", 30);
//		or2.greaterThan("sex", 0);

		e.orderBy("age").desc();
		e.orderBy("id").desc();

		printList(userAnnoCustomMapper.getListByExample(e));
	}
	
	private void printList(List<?> list) {
		for (Object o : list) {
			logger.info(o.toString());
		}
	}

}
