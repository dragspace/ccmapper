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
 * @Description: UserAnno
 * @author xiaoruihu 2016年6月12日 下午3:00:19
 */
@Table(name="USER")
public class UserAnno {
	
	@Id
	private Long id;
	
    @Column(name = "NAME")
    private String name;

    @Column(name = "SEX")
    private Integer sex;

    @Column(name = "AGE")
    private Integer age;
    
    @Column(name = "ORGID")
    private Long orgId;
    

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
    
    
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	@Override
	public String toString() {
		return "UserAnno [id=" + id + ", name=" + name + ", sex=" + sex + ", age=" + age + ", orgId=" + orgId + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAnno other = (UserAnno) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sex == null) {
			if (other.sex != null)
				return false;
		} else if (!sex.equals(other.sex))
			return false;
		return true;
	}
	
	
}
