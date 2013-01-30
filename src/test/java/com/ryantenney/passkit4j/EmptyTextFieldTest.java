package com.ryantenney.passkit4j;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ryantenney.passkit4j.model.TextField;

public class EmptyTextFieldTest {

	ObjectMapper mapper = PassSerializer.objectMapper;

	@Test
	public void nonEmptyValue() {
		ObjectNode node = mapper.valueToTree(new TextField("key", "label", "value"));
		Assert.assertEquals("value", node.get("value").textValue());
	}

	@Test(expected=NullPointerException.class)
	public void nullValue() {
		new TextField("key", "label", null);
	}

	@Test
	public void emptyValue() {
		ObjectNode node = mapper.valueToTree(new TextField("key", "label", ""));
		Assert.assertEquals("", node.get("value").textValue());
	}

}
