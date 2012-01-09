package com.jayway.imagesync;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;

public class SyncServlet extends HttpServlet {
	
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
			MongoUtil.getDatastore().save(user);
		}
	}

}
