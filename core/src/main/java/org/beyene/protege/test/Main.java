package org.beyene.protege.test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.beyene.protege.core.Protocol;

public class Main {

	public static void main(String[] args) throws JAXBException {
		Protocol p = Units.getProtocol();
		
		JAXBContext context = JAXBContext.newInstance(Protocol.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(p, System.out);
	}
}