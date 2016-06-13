package com.ccmapper.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ccmapper.customsimple.SimpleCommonMapper;
import com.demo.simple.SimpleBean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class SimpleCommonMapperTest {
	
	@Autowired
	private SimpleCommonMapper<SimpleBean> simpleCommonMapper;
	
	@Test
	public void test(){
		System.out.println(simpleCommonMapper.getNameById(1));
		System.out.println(simpleCommonMapper.getById(2));
	}

}
