package com.ryantenney.passkit4j.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
public class TextField implements Field<String> {

	@NonNull private String key;
	@NonNull private String label;
	private String changeMessage;
	private TextAlignment textAlignment = TextAlignment.LEFT;

	@NonNull private String value;

}
