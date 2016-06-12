/**
* @Title: DemoEntity.java
* @Package com.ssd.demo.module.demo.entity
* @Description: TODOo
* @author xiaoruihu 2016年4月22日 下午4:22:40
* @version V1.0
*/
package com.demo.bean;

/**
 * @Description: Org
 * @author xiaoruihu 2016年6月12日 下午3:00:05
 */
public class Org {

	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Org [id=" + id + ", name=" + name + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Org other = (Org) obj;
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
		return true;
	}
}
