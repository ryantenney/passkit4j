package com.ryantenney.passkit4j.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
public class Location {

	@NonNull private double latitude;
	@NonNull private double longitude;

	private double altitude = 0.0;
	private String relevantText;

}
