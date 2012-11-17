package com.ryantenney.passkit4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class NumberField implements Field<Number> {

	@JsonProperty @NonNull private String key;
	@JsonProperty @NonNull private String label;
	@JsonProperty private String changeMessage;
	@JsonProperty private TextAlignment textAlignment = TextAlignment.LEFT;

	@JsonProperty @NonNull private Number value;
	@JsonProperty private NumberStyle numberStyle = NumberStyle.DECIMAL;
	@JsonProperty private String currencyCode = null;

}
