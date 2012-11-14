package com.ryantenney.passkit4j.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class StoreCard extends PassInformation {

	public StoreCard() {
		super("storeCard");
	}

}
