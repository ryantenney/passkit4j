package com.ryantenney.passkit4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
public class TextField implements Field<String> {

	@NonNull private String key;
	private String label;
	private String changeMessage;
	private TextAlignment textAlignment;

	@NonNull @JsonInclude(Include.ALWAYS) private String value;

	public TextField(String key, String label, String value) {
		this(key, value);
		this.label = label;
	}

}
