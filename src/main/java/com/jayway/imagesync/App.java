package com.jayway.imagesync;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

// get access token https://developers.facebook.com/docs/authentication/

@SuppressWarnings("unchecked")
public class App {
	public static void main(String[] args) throws Exception {
		JAXBContext jc = JAXBContext.newInstance(Settings.class);
		File file = new File("settings.xml");
		Settings settings = (Settings) jc.createUnmarshaller().unmarshal(file);
		try {
			settings.synchronize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
		marshaller.marshal(settings, file);
	}
}
