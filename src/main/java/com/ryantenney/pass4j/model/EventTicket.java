package com.ryantenney.pass4j.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
public class EventTicket extends PassInformation {

	public EventTicket() {
		super("eventTicket");
	}

}
