package com.ryantenney.passkit4j.model;

import java.util.Date;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
public class DateField implements Field<Date> {

	@NonNull private String key;
	@NonNull private String label;
	private String changeMessage;
	private TextAlignment textAlignment = TextAlignment.LEFT;

	@NonNull private Date value;
	private DateStyle dateStyle = DateStyle.SHORT;
	private DateStyle timeStyle = DateStyle.SHORT;
	@JsonProperty("isRelative") private boolean relative = false;

}
