package com.ryantenney.passkit4j.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Beacon {

	@NonNull private String proximityUUID;

	@JsonInclude(Include.NON_DEFAULT) private long major = -1;
	@JsonInclude(Include.NON_DEFAULT) private long minor = -1;

	private String relevantText;

}
