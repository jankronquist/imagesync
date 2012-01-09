package com.jayway.imagesync;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.MongoURI;

public class SyncServlet extends HttpServlet {
	
	private Datastore ds;

	public SyncServlet() throws Exception {
		MongoURI mongoURI = new MongoURI(System.getenv("MONGOHQ_URL"));
		DB db = mongoURI.connectDB();
		db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
		Morphia morphia = new Morphia();
		this.ds = morphia.createDatastore(db.getMongo(), db.getName());
	}
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.getWriter().println("Hello");
	}

	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			extracted(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void extracted(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Settings.class);
		Settings settings = (Settings) jc.createUnmarshaller().unmarshal(req.getReader());
		for (TargetUser user : settings.getUsers()) {
			ds.save(user);
		}
	}

}
