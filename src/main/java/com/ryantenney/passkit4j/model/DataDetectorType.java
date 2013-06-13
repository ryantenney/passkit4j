package com.ryantenney.passkit4j.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DataDetectorType {

	PHONE_NUMBER("PKDataDetectorTypePhoneNumber"),
	LINK("PKDataDetectorTypeLink"),
	ADDRESS("PKDataDetectorTypeAddress"),
	CALENDAR_EVENT("PKDataDetectorTypeCalendarEvent");

	private static final Map<String, DataDetectorType> lookup;

	static {
		final Map<String, DataDetectorType> _lookup = new HashMap<String, DataDetectorType>();
		for (DataDetectorType entry : DataDetectorType.values()) {
			_lookup.put(entry.value(), entry);
		}
		lookup = Collections.unmodifiableMap(_lookup);
	}

	private final String value;

	private DataDetectorType(final String value) {
		this.value = value;
	}

	@JsonValue
	public String value() {
		return this.value;
	}

	@JsonValue
	public static DataDetectorType forValue(String value) {
		return lookup.get(value);
	}

}
