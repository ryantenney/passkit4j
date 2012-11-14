package com.ryantenney.passkit4j.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NumberStyle {

	DECIMAL("PKNumberStyleDecimal"),
	PERCENT("PKNumberStylePercent"),
	SCIENTIFIC("PKNumberStyleScientific"),
	SPELLOUT("PKNumberStyleSpellOut");

	private static final Map<String, NumberStyle> lookup;

	static {
		final Map<String, NumberStyle> _lookup = new HashMap<String, NumberStyle>();
		for (NumberStyle entry : NumberStyle.values()) {
			_lookup.put(entry.value(), entry);
		}
		lookup = Collections.unmodifiableMap(_lookup);
	}

	private final String value;

	private NumberStyle(final String value) {
		this.value = value;
	}

	@JsonValue
	public String value() {
		return this.value;
	}

	@JsonValue
	public static NumberStyle forValue(String value) {
		return lookup.get(value);
	}

}
