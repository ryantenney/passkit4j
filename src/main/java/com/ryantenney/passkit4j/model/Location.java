package com.ryantenney.passkit4j.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Accessors(chain=true, fluent=true)
@NoArgsConstructor
public class Location {

	private double latitude;
	private double longitude;

	public Location(double latitude, double longitude) {
	  this.latitude = latitude;
	  this.longitude = longitude;
	}

	@JsonInclude(Include.NON_DEFAULT) private double altitude = Double.NaN;
	private String relevantText;

}
