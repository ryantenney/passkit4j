package com.ryantenney.pass4j.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper=true)
@Accessors(chain=true, fluent=true)
public class Coupon extends PassInformation {

	public Coupon() {
		super("coupon");
	}

}
