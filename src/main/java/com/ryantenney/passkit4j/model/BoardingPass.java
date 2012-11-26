package com.ryantenney.passkit4j.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper=true)
@Accessors(chain=true, fluent=true)
public class BoardingPass extends PassInformation {

	@NonNull private TransitType transitType;

	public BoardingPass() {
		super("boardingPass");
	}

	public BoardingPass(final TransitType transitType) {
		this();
		this.transitType = transitType;
	}

}
