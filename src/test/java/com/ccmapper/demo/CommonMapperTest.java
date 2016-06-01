package com.ccmapper.demo;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ccmapper.custom.CommonMapper;
import com.demo.bean.Demo;
import com.demo.bean.Demo4;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class CommonMapperTest {

	@Autowired
	private CommonMapper<Demo4> demo4CommonMapper;
	
	@Autowired
	private CommonMapper<Demo> demoCommonMapper;

	@Test
	public void testGetByPrimaryKey() {
		try {
			System.out.println(demo4CommonMapper.getListByPropertyEqual("age", 120));
			System.out.println(demo4CommonMapper.getByPropertyEqual("age", 99));
			System.out.println(demo4CommonMapper.getByPrimaryKey(5L));

			System.out.println(demo4CommonMapper.getMapListAll());
			System.out.println(demo4CommonMapper.getMapByPrimaryKey(5L).get("id"));

			System.out.println(demoCommonMapper.getAll());
//			Demo4 demo4 = new Demo4();
//			demo4.setName("我是谁");
//			
//			demo4CommonMapper.insert(demo4);
//			System.out.println(demo4);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
