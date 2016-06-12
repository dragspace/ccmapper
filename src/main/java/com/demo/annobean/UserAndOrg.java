package com.demo.annobean;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "(select u.id as userId, u.name as userName from user u left join org g on u.orgId = g.Id) mm")
public class UserAndOrg {

	@Column(name = "userId")
	private Long userId;
	
	@Column
	private String userName;

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

	@Override
	public String toString() {
		return "UserAndOrg [userId=" + userId + ", userName=" + userName + "]";
	}
}
