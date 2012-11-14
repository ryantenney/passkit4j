package com.ryantenney.passkit4j.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class EventTicket extends PassInformation {

	public EventTicket() {
		super("eventTicket");
	}

}
