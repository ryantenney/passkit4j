package com.ryantenney.pass4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
public class Location {

	@JsonProperty private double latitude;

	@JsonProperty private double longitude;

	@JsonProperty private double altitude = 0.0;

	@JsonProperty private String relevantText;

}
