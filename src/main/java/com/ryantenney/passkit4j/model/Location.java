package com.ryantenney.passkit4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Location {

	@NonNull private double latitude;
	@NonNull private double longitude;

	@JsonInclude(Include.NON_DEFAULT) private double altitude = Double.NaN;
	private String relevantText;

}
