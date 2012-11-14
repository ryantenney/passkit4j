package com.ryantenney.passkit4j.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DateStyle {

	NONE("PKDateStyleNone"),
	SHORT("PKDateStyleShort"),
	MEDIUM("PKDateStyleMedium"),
	LONG("PKDateStyleLong"),
	FULL("PKDateStyleFull");

	private static final Map<String, DateStyle> lookup;

	static {
		final Map<String, DateStyle> _lookup = new HashMap<String, DateStyle>();
		for (DateStyle entry : DateStyle.values()) {
			_lookup.put(entry.value(), entry);
		}
		lookup = Collections.unmodifiableMap(_lookup);
	}

	private final String value;

	private DateStyle(final String value) {
		this.value = value;
	}

	@JsonValue
	public String value() {
		return this.value;
	}

	@JsonValue
	public static DateStyle forValue(String value) {
		return lookup.get(value);
	}

}
