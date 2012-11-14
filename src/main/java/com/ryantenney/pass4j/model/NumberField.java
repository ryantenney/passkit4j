package com.ryantenney.pass4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper=true)
@Accessors(chain=true, fluent=true)
public class NumberField extends Field<Number> {

	@JsonProperty private NumberStyle numberStyle = NumberStyle.DECIMAL;

}
