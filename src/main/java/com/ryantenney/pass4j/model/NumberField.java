package com.ryantenney.pass4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@JsonInclude(Include.NON_EMPTY)
public class NumberField implements Field<Number> {

	@JsonProperty private String key;
	@JsonProperty private String label;
	@JsonProperty private String changeMessage;
	@JsonProperty private TextAlignment textAlignment = TextAlignment.LEFT;

	@JsonProperty private Number value;
	@JsonProperty private NumberStyle numberStyle = NumberStyle.DECIMAL;
	@JsonProperty private String currencyCode = null;

}
