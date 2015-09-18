package com.ryantenney.passkit4j.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BarcodeFormat {

	PDF417("PKBarcodeFormatPDF417"),
	QR("PKBarcodeFormatQR"),
	AZTEC("PKBarcodeFormatAztec"),
	CODE128("PKBarcodeFormatCode128");

	private static final Map<String, BarcodeFormat> lookup;

	static {
		final Map<String, BarcodeFormat> _lookup = new HashMap<String, BarcodeFormat>();
		for (BarcodeFormat entry : BarcodeFormat.values()) {
			_lookup.put(entry.value(), entry);
		}
		lookup = Collections.unmodifiableMap(_lookup);
	}

	private final String value;

	private BarcodeFormat(final String value) {
		this.value = value;
	}

	@JsonValue
	public String value() {
		return this.value;
	}

	@JsonValue
	public static BarcodeFormat forValue(String value) {
		return lookup.get(value);
	}

}
