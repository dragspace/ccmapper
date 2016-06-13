package com.ccmapper.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.demo.annobean.OrgAnno;
import com.demo.annobean.UserAnno;
import com.demo.bean.User;

public class GenerateUtils {

	public static final Random RANDOM = new Random();

	public static OrgAnno generateOrgAnno() {
		OrgAnno oa = new OrgAnno();
		oa.setName(CommonGenerateUtils.getRandomEnglishLastName());
		return oa;
	}

	public static List<OrgAnno> generateOrgAnnoList(int size) {
		List<OrgAnno> list = new ArrayList<OrgAnno>();
		for (int i = 0; i < size; i++) {
			list.add(generateOrgAnno());
		}
		return list;
	}

	public static UserAnno generateUserAnno() {
		UserAnno ue = new UserAnno();
		ue.setAge(RANDOM.nextInt(100) + 1);
		ue.setSex(RANDOM.nextInt(2));
		ue.setName(CommonGenerateUtils.getRandomEnglishName());

		return ue;
	}

	public static List<UserAnno> generateUserAnnoList(int size) {

		List<UserAnno> list = new ArrayList<UserAnno>();
		for (int i = 0; i < size; i++) {
			list.add(generateUserAnno());
		}

		return list;
	}

	public static User generateUser() {
		User ue = new User();
		ue.setAge(RANDOM.nextInt(100) + 1);
		ue.setSex(RANDOM.nextInt(2));
		ue.setName(CommonGenerateUtils.getRandomEnglishName());

		return ue;
	}

	public static List<User> generateUserList(int size) {

		List<User> list = new ArrayList<User>();
		for (int i = 0; i < size; i++) {
			list.add(generateUser());
		}

		return list;
	}
}
