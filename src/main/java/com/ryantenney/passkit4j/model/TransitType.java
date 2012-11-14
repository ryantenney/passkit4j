package com.ryantenney.passkit4j.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransitType {

	AIR("PKTransitTypeAir"),
	TRAIN("PKTransitTypeTrain"),
	BUS("PKTransitTypeBus"),
	BOAT("PKTransitTypeBoat"),
	GENERIC("PKTransitTypeGeneric");

	private static final Map<String, TransitType> lookup;

	static {
		final Map<String, TransitType> _lookup = new HashMap<String, TransitType>();
		for (TransitType entry : TransitType.values()) {
			_lookup.put(entry.value(), entry);
		}
		lookup = Collections.unmodifiableMap(_lookup);
	}

	private final String value;

	private TransitType(final String value) {
		this.value = value;
	}

	@JsonValue
	public String value() {
		return this.value;
	}

	@JsonValue
	public static TransitType forValue(String value) {
		return lookup.get(value);
	}

}
