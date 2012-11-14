package com.ryantenney.pass4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
public class TextField implements Field<String> {

	@JsonProperty private String key;
	@JsonProperty private String label;
	@JsonProperty private String changeMessage;
	@JsonProperty private TextAlignment textAlignment = TextAlignment.LEFT;

	@JsonProperty private String value;

}
