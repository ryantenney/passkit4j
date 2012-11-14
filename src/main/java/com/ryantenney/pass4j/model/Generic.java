package com.ryantenney.pass4j.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class Generic extends PassInformation {

	public Generic() {
		super("generic");
	}

}
