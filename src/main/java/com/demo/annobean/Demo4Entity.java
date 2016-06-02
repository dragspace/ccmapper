/**
* @Title: DemoEntity.java
* @Package com.ssd.demo.module.demo.entity
* @Description: TODOo
* @author xiaoruihu 2016年4月22日 下午4:22:40
* @version V1.0
*/
package com.demo.annobean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @Description: DemoEntity
 * @author xiaoruihu 2016年4月22日 下午4:22:40
 */
@Table(name="DEMO4")
public class Demo4Entity  {
	@Id
	private Long id;
	
    @Column(name = "NAME")
    private String name;

    @Column(name = "SEX")
    private Integer sex;

    @Column(name = "AGE")
    private Integer age;

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Integer getSex(){
        return this.sex;
    }
    public void setSex(Integer sex){
        this.sex = sex;
    }
    public Integer getAge(){
        return this.age;
    }
    public void setAge(Integer age){
        this.age = age;
    }
	@Override
	public String toString() {
		return "Demo4Entity [id=" + id + ", name=" + name + ", sex=" + sex + ", age=" + age + "]";
	}
    
}
