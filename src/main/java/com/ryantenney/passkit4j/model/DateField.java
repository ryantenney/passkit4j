package com.ryantenney.passkit4j.model;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Accessors(chain=true, fluent=true)
@NoArgsConstructor
@RequiredArgsConstructor
public class DateField implements Field<Date> {

	@NonNull private String key;
	private String label;
	private String changeMessage;
	private TextAlignment textAlignment;
	private Set<DataDetectorType> dataDetectorTypes;
	private String attributedValue;

	@NonNull private Date value;
	private DateStyle dateStyle;
	private DateStyle timeStyle;
	@JsonProperty("isRelative") private boolean relative = false;
	private boolean ignoresTimeZone = false;

	public DateField(String key, String label, Date value) {
		this(key, value);
		this.label = label;
	}

}
