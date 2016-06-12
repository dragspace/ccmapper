package com.ccmapper.demo;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ccmapper.core.example.Example;
import com.ccmapper.core.example.Example.Criteria;
import com.ccmapper.custom.CustomMapper;
import com.demo.bean.Demo4;

/**
 * @Description: CommonMapperExampleTest  动态查询测试  
 * @author xiaoruihu 2016年6月10日 上午10:12:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring-jdbc.xml" })
public class CommonMapperExampleTest {

	@Autowired
	private CustomMapper<Demo4> demo4CommonMapper;
	

	@Test
	public void testCommonMapper() {
		try {
			Example e = new Example(false);
			e.selectProperties("id", "name");
			Criteria and = e.andCriteria();
			and.equalTo("name", "我是谁");
//			//c.notIn("age", Arrays.asList(20, 30));
//			and.lessThanOrEqualTo("age", 30);
			
			//and.andIsNotNull("sex");
			
			Criteria or = e.orCriteria();
			or.equalTo("name", "abc");
			or.equalTo("age", "150");
			
			Criteria or2 = e.orCriteria();
			or2.equalTo("name", "abc");
			or2.equalTo("age", "120");
			
			e.orderBy("id").desc();
			
			printList(demo4CommonMapper.getListByExample(e));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void printList(List<?> list){
		for(Object o : list){
			System.out.println(o);
		}
	}
	
}
