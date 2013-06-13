package com.ryantenney.passkit4j.model;

import java.util.Set;

import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@NoArgsConstructor
@RequiredArgsConstructor
public class NumberField implements Field<Number> {

	@NonNull private String key;
	private String label;
	private String changeMessage;
	private TextAlignment textAlignment;
	private Set<DataDetectorType> dataDetectorTypes;
	private String attributedValue;

	@NonNull private Number value;
	private NumberStyle numberStyle;
	private String currencyCode;

	public NumberField(String key, String label, Number value) {
		this(key, value);
		this.label = label;
	}

}
