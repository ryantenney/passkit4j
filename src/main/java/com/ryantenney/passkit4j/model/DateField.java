package com.ryantenney.passkit4j.model;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
@NoArgsConstructor
public class DateField implements Field<Date> {

	@JsonProperty @NonNull private String key;
	@JsonProperty @NonNull private String label;
	@JsonProperty private String changeMessage;
	@JsonProperty private TextAlignment textAlignment = TextAlignment.LEFT;

	@JsonProperty @NonNull private Date value;
	@JsonProperty private DateStyle dateStyle = DateStyle.SHORT;
	@JsonProperty private DateStyle timeStyle = DateStyle.SHORT;
	@JsonProperty("isRelative") private boolean relative = false;

}
