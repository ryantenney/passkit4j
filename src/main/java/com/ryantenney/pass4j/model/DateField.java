package com.ryantenney.pass4j.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@EqualsAndHashCode(callSuper=true)
@Accessors(chain=true, fluent=true)
public class DateField extends Field<Date> {

	@JsonProperty private DateStyle dateStyle = DateStyle.SHORT;

	@JsonProperty private DateStyle timeStyle = DateStyle.SHORT;

	@JsonProperty("isRelative") private boolean relative = false;

}
