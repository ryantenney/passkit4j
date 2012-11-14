package com.ryantenney.passkit4j.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TextAlignment {

	LEFT("PKTextAlignmentLeft"),
	CENTER("PKTextAlignmentCenter"),
	RIGHT("PKTextAlignmentRight"),
	JUSTIFIED("PKTextAlignmentJustified"),
	NATURAL("PKTextAlignmentNatural");

	private static final Map<String, TextAlignment> lookup;

	static {
		final Map<String, TextAlignment> _lookup = new HashMap<String, TextAlignment>();
		for (TextAlignment entry : TextAlignment.values()) {
			_lookup.put(entry.value(), entry);
		}
		lookup = Collections.unmodifiableMap(_lookup);
	}

	private final String value;

	private TextAlignment(final String value) {
		this.value = value;
	}

	@JsonValue
	public String value() {
		return this.value;
	}

	@JsonValue
	public static TextAlignment forValue(String value) {
		return lookup.get(value);
	}

}
