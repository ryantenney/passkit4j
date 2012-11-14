package com.ryantenney.passkit4j.model;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Accessors(chain=true, fluent=true)
public class DateField implements Field<Date> {

	@JsonProperty private String key;
	@JsonProperty private String label;
	@JsonProperty private String changeMessage;
	@JsonProperty private TextAlignment textAlignment = TextAlignment.LEFT;

	@JsonProperty private Date value;
	@JsonProperty private DateStyle dateStyle = DateStyle.SHORT;
	@JsonProperty private DateStyle timeStyle = DateStyle.SHORT;
	@JsonProperty("isRelative") private boolean relative = false;

}
