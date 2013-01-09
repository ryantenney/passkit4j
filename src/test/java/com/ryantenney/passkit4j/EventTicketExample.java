package com.ryantenney.passkit4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.ryantenney.passkit4j.Pass;
import com.ryantenney.passkit4j.PassResource;
import com.ryantenney.passkit4j.PassSerializer;
import com.ryantenney.passkit4j.model.*;
import com.ryantenney.passkit4j.sign.PassSigner;
import com.ryantenney.passkit4j.sign.PassSignerImpl;

public class EventTicketExample {

	public static void main(String[] args) throws Throwable {
		Pass pass = new Pass()
			.teamIdentifier("asdfasdfasdf")
			.passTypeIdentifier("pass.com.apple.event")
			.organizationName("Apple Inc.")
			.description("Apple Event Ticket")
			.serialNumber("nmyuxofgnb")
			.relevantDate("2011-12-08T13:00:00-08:00")
			.locations(
				new Location(37.6189722, -122.3748889),
				new Location(37.33182, -122.03118)
			)
			.barcode(new Barcode(BarcodeFormat.PDF417, "123456789"))
			.foregroundColor(Color.WHITE)
			.backgroundColor(new Color(60, 65, 76))
			.files(
				new PassResource("src/test/resources/eventticket/icon.png"),
				new PassResource("src/test/resources/eventticket/icon@2x.png"),
				new PassResource("src/test/resources/eventticket/logo.png"),
				new PassResource("src/test/resources/eventticket/logo@2x.png"),
				new PassResource("src/test/resources/eventticket/background.png"),
				new PassResource("src/test/resources/eventticket/background@2x.png")
			)
			.passInformation(
				new EventTicket()
					.primaryFields(
						new TextField("event", "EVENT", "The Beat Goes On")
					)
					.secondaryFields(
						new TextField("loc", "LOCATION", "Moscone West")
					)
					.backFields(
						new TextField("terms", "TERMS AND CONDITIONS", "Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licnsors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. Generico reserves the right to change or discontinue at any time any aspect or feature of the pass.")
					)
			);

		PassSigner signer = PassSignerImpl.builder()
			.keystore(new FileInputStream("certificates/Certificates.p12"), null)
			.intermediateCertificate(new FileInputStream("certificates/AppleWWDRCA.cer"))
			.build();

		PassSerializer.writePkPassArchive(pass, signer, new FileOutputStream("EventTicket.pkpass"));
	}

}
