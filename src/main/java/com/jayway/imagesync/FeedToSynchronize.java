package com.jayway.imagesync;

import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Album;
import com.restfb.types.FacebookType;
import com.restfb.types.User;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class FeedToSynchronize implements FacebookSynchronizer {
	@XmlAttribute
	private String albumId;
	@XmlAttribute
	private String feedUrl;
	@XmlAttribute
	private Date lastPublishDate;
	
	private FeedToSynchronize() {
		lastPublishDate = new Date(0);
	}
	
	public FeedToSynchronize(String feedUrl, Date lastPublishedDate) throws Exception {
		this.feedUrl = feedUrl;
		this.lastPublishDate = lastPublishedDate;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void synchronize(FacebookClient facebookClient) throws Exception {
		SyndFeed feed = getFeed();
		if (albumId == null) {
			albumId = findOrCreateAlbum(feed.getTitle(), feed.getLink(), facebookClient);
		}
        
		List<SyndEntry> entries = feed.getEntries();
		int newPhotos = 0;
		for (SyndEntry syndEntry : entries) {
			Date publishedDate = syndEntry.getPublishedDate();
			if (publishedDate.after(lastPublishDate)) {
				List<SyndEnclosure> enclosures = syndEntry.getEnclosures();
				if (enclosures.size() > 0) {
					SyndEnclosure syndEnclosure = enclosures.get(0);
					publishImage(facebookClient, syndEntry.getTitle(), syndEnclosure.getUrl());
					newPhotos++;
				}
				lastPublishDate = publishedDate;
			}
		}
		if (newPhotos > 0) {
			System.out.println(String.format("Published %d new photos to the album '%s'", newPhotos, feed.getTitle()));
		}
	}

	private void publishImage(FacebookClient facebookClient, String title, String url) {
		facebookClient.publish(albumId + "/photos", FacebookType.class, 
				Parameter.with("url", url), 
				Parameter.with("message", title));
	}

	private String findOrCreateAlbum(String title, String description, FacebookClient facebookClient) {
		Connection<Album> albums = facebookClient.fetchConnection("me/albums", Album.class);
		for (Album album : albums.getData()) {
			if (album.getName().equals(title)) {
				return album.getId();
			}
		}
		User user = facebookClient.fetchObject("me", User.class);
		FacebookType createAlbumResponse = facebookClient.publish(user.getId() + "/albums", FacebookType.class, 
				Parameter.with("name", title), 
				Parameter.with("message", description));
		
		return createAlbumResponse.getId();
	}

	private SyndFeed getFeed() throws Exception {
		URL feedSource = new URL(feedUrl);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
		return feed;
	}
}
