package com.jayway.imagesync;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Settings {
	@XmlElement
	private final List<TargetUser> users = new ArrayList<TargetUser>();
	
	public List<TargetUser> getUsers() {
		return users;
	}

	public void add(TargetUser targetUser) {
		users.add(targetUser);
	}
	
	public void synchronize() throws Exception {
		for (TargetUser user : users) {
			user.synchronize();
		}
	}
}
