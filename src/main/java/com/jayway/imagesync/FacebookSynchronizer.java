package com.jayway.imagesync;

import com.restfb.FacebookClient;

public interface FacebookSynchronizer {
	void synchronize(FacebookClient facebookClient) throws Exception;
}