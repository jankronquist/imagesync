package com.jayway.imagesync;

import static com.jayway.imagesync.MongoUtil.getDatastore;

public class PerformSync {
	public static void main(String[] args) throws Exception {
		for (TargetUser user : getDatastore().find(TargetUser.class)) {
			user.synchronize();
			getDatastore().merge(user);
		}
	}
}
