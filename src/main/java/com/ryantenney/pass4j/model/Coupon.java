package com.ryantenney.pass4j.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class Coupon extends PassInformation {

	public Coupon() {
		super("coupon");
	}

}
