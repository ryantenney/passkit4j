package com.ryantenney.passkit4j.model;

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
public class TextField implements Field<String> {

	@JsonProperty @NonNull private String key;
	@JsonProperty @NonNull private String label;
	@JsonProperty private String changeMessage;
	@JsonProperty private TextAlignment textAlignment = TextAlignment.LEFT;

	@JsonProperty @NonNull private String value;

}
