package com.ccmapper.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ccmapper.simplecustom.SimpleCommonMapper;
import com.demo.bean.Demo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class SimpleCommonMapperTest {
	
	@Autowired
	private SimpleCommonMapper<Demo> simpleCommonMapper;
	
	@Test
	public void test(){
		System.out.println(simpleCommonMapper.getAgeById(1));
	}

}
