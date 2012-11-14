package com.ryantenney.pass4j.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class StoreCard extends PassInformation {

	public StoreCard() {
		super("storeCard");
	}

}
