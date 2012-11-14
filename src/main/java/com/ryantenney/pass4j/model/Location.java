package com.ryantenney.pass4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@NoArgsConstructor
@RequiredArgsConstructor
public class Location {

	@JsonProperty @NonNull private double latitude;
	@JsonProperty @NonNull private double longitude;

	@JsonProperty private double altitude = 0.0;
	@JsonProperty private String relevantText;

}
