package com.ryantenney.pass4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.ryantenney.pass4j.model.*;

public class StoreCardExample {

	public static void main(String[] args) throws Throwable {
		Pass pass = new Pass()
		.passTypeIdentifier("pass.com.bayroast.storeCard")
		.serialNumber("p69f2J")
		.teamIdentifier("asdfasdfasdf")
		.webServiceURL("https://example.com/passes/")
		.authenticationToken("vxwxd7J8AlNNFPS8k0a0FfUFtq0ewzFdc")
		.locations(
			new Location(37.6189722, -122.3748889)
		)
		.barcode(
			new Barcode()
				.message("123456789")
				.format(BarcodeFormat.PDF417)
		)
		.organizationName("Bayroast Coffee")
		.logoText("Bayroast Coffee")
		.foregroundColor(Color.WHITE)
		.backgroundColor(new Color(118, 74, 50))
		.files(
			new PassResource("src/test/resources/storecard/icon.png"),
			new PassResource("src/test/resources/storecard/icon@2x.png"),
			new PassResource("src/test/resources/storecard/logo.png"),
			new PassResource("src/test/resources/storecard/logo@2x.png"),
			new PassResource("src/test/resources/storecard/background.png"),
			new PassResource("src/test/resources/storecard/background@2x.png")
		)
		.passInformation(
			new StoreCard()
				.primaryFields(
					new NumberField()
						.key("balance")
						.label("remaining balance")
						.value(25)
						.currencyCode("USD")
				)
				.auxiliaryFields(
					new TextField()
						.key("level")
						.label("LEVEL")
						.value("Gold"),
					new TextField()
						.key("usual")
						.label("THE USUAL")
						.value("Iced Mocha")
				)
				.backFields(
					new TextField()
						.key("terms")
						.label("TERMS AND CONDITIONS")
						.value("Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. Generico reserves the right to change or discontinue at any time any aspect or feature of the pass.")
				)
		);

		PassSigningInformation signing = PassSigningInformation.builder()
			.keystore(new FileInputStream("certificates/Certificates.p12"), null)
			.intermediateCertificate(new FileInputStream("certificates/wwdr.pem"))
			.build();

		PassSerializer.writePkPassArchive(pass, signing, new FileOutputStream("StoreCard.pkpass"));
	}

}
