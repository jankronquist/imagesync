package com.jayway.imagesync;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.MongoURI;

public abstract class MongoUtil {
	private static Datastore datastore;

	private MongoUtil() {}
	
	public static Datastore getDatastore() throws Exception {
		if (datastore == null) {
			MongoURI mongoURI = new MongoURI(System.getenv("MONGOHQ_URL"));
			DB db = mongoURI.connectDB();
			db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
			Morphia morphia = new Morphia();
			datastore = morphia.createDatastore(db.getMongo(), db.getName());
		}
		return datastore;
	}

}
