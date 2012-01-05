package com.jayway.imagesync;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

public class TargetUser {
	@XmlAttribute
	private final String userId;
	@XmlAttribute
	private final String accessToken;
	@XmlElement
	private final List<FeedToSynchronize> synchronizers = new ArrayList<FeedToSynchronize>();
	
	private TargetUser() {
		accessToken = null;
		userId = null;
	}

	public TargetUser(String accessToken, String userId) {
		this.accessToken = accessToken;
		this.userId = userId;
	}
	
	public void addSynchronizer(FeedToSynchronize synchronizer) {
		synchronizers.add(synchronizer);
	}
	
	public void synchronize() throws Exception {
		System.out.println("Synchronizing " + userId);
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
		for (FacebookSynchronizer synchronizer : synchronizers) {
			synchronizer.synchronize(facebookClient);
		}
	}
}
