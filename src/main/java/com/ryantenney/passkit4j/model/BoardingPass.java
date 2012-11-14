package com.ryantenney.passkit4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper=true)
@Accessors(chain=true, fluent=true)
public class BoardingPass extends PassInformation {

	@JsonProperty private TransitType transitType = TransitType.AIR;

	public BoardingPass() {
		super("boardingPass");
	}

}
