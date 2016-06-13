package com.demo.annobean;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "(select u.id as userId, u.name as userName, u.sex as userSex, g.id as orgId, g.name as orgName from user u left join org g on u.orgId = g.Id) mm")
public class UserAndOrg {

	@Column(name = "userId")
	private Long userId;
	
	@Column
	private String userName;
	
	@Column
	private Integer userSex;
	
	@Column
	private Long orgId;
	
	@Column
	private String orgName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getUserSex() {
		return userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	@Override
	public String toString() {
		return "UserAndOrg [userId=" + userId + ", userName=" + userName + ", userSex=" + userSex + ", orgId=" + orgId + ", orgName=" + orgName + "]";
	}
	
}
