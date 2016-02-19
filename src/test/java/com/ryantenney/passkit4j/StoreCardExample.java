package com.ryantenney.passkit4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.ryantenney.passkit4j.Pass;
import com.ryantenney.passkit4j.PassResource;
import com.ryantenney.passkit4j.PassSerializer;
import com.ryantenney.passkit4j.model.*;
import com.ryantenney.passkit4j.sign.PassSigner;
import com.ryantenney.passkit4j.sign.PassSignerImpl;

public class StoreCardExample {

	public static void main(String[] args) throws Throwable {
		Pass pass = new Pass()
			.teamIdentifier("asdfasdfasdf")
			.passTypeIdentifier("pass.com.bouldercoffeeco.storeCard")
			.organizationName("Boulder Coffee Co.")
			.description("Boulder Coffee Rewards Card")
			.serialNumber("p69f2J")
			.locations(
				new Location(43.145863, -77.602690).relevantText("South Wedge"),
				new Location(43.131063, -77.636425).relevantText("Brooks Landing"),
				new Location(43.147528, -77.576051).relevantText("Park Avenue"),
				new Location(43.155763, -77.612724).relevantText("State Street"),
				new Location(43.165389, -77.589655).relevantText("Public Market")
			)
			.barcode(new Barcode(BarcodeFormat.PDF417, "12345678"))
			.barcodes(
					new Barcode(BarcodeFormat.CODE128, "12345678"),
					new Barcode(BarcodeFormat.PDF417, "12345678"))
			.logoText("Boulder Coffee")
			.foregroundColor(Color.WHITE)
			.backgroundColor(new Color(118, 74, 50))
			.files(
				new PassResource("en.lproj/pass.strings", new File("src/test/resources/storecard/en.lproj/pass.strings")),
				new PassResource("src/test/resources/storecard/icon.png"),
				new PassResource("src/test/resources/storecard/icon@2x.png"),
				new PassResource("src/test/resources/storecard/logo.png"),
				new PassResource("src/test/resources/storecard/logo@2x.png"),
				new PassResource("src/test/resources/storecard/strip.png"),
				new PassResource("src/test/resources/storecard/strip@2x.png")
			)
			.nfc(new NFC("test"))
			.passInformation(
				new StoreCard()
					.headerFields(
						new NumberField("balance", "balance_label", 25)
							.textAlignment(TextAlignment.RIGHT)
							.currencyCode("USD")
					)
					.auxiliaryFields(
						new TextField("level", "level_label", "level_gold"),
						new TextField("usual", "usual_label", "Iced Mocha")
					)
					.backFields(
						new TextField("terms", "terms_label", "terms_value")
					)
			);

		PassSigner signer = PassSignerImpl.builder()
			.keystore(new FileInputStream("certificates/Certificates.p12"), null)
			.intermediateCertificate(new FileInputStream("certificates/AppleWWDRCA.cer"))
			.build();

		PassSerializer.writePkPassArchive(pass, signer, new FileOutputStream("StoreCard.pkpass"));
	}

}
