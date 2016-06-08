package com.ccmapper.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ccmapper.core.example.Example;
import com.ccmapper.custom.CommonMapper;
import com.ccmapper.customanno.CustomerAnnoMapper;
import com.demo.annobean.Demo4Entity;
import com.demo.bean.Demo;
import com.demo.bean.Demo4;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class CommonMapperExampleTest {

	@Autowired
	private CommonMapper<Demo4> demo4CommonMapper;
	

	@Test
	public void testCommonMapper() {
		try {
			//这个还处于试验阶段不可靠
			Example e = new Example(false);
			e.createAndCriteria().andEqualTo("age", 99);
			
			System.out.println(demo4CommonMapper.getListByExample(e));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
