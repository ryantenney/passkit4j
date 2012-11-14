package com.ryantenney.pass4j.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public abstract class PassInformation {

	@JsonIgnore private final String typeName;

	@JsonProperty private List<Field<?>> headerFields = new ArrayList<>();

	@JsonProperty private List<Field<?>> primaryFields = new ArrayList<>();

	@JsonProperty private List<Field<?>> secondaryFields = new ArrayList<>();

	@JsonProperty private List<Field<?>> backFields = new ArrayList<>();

	@JsonProperty private List<Field<?>> auxiliaryFields = new ArrayList<>();

	protected PassInformation(final String typeName) {
		this.typeName = typeName;
	}

	public String typeName() {
		return this.typeName;
	}

	public List<Field<?>> headerFields() {
		return Collections.unmodifiableList(headerFields);
	}

	public PassInformation headerFields(Field<?>... fields) {
		headerFields(Arrays.asList(fields));
		return this;
	}

	public PassInformation headerFields(List<Field<?>> fields) {
		headerFields.addAll(fields);
		return this;
	}

	public List<Field<?>> primaryFields() {
		return Collections.unmodifiableList(primaryFields);
	}

	public PassInformation primaryFields(Field<?>... fields) {
		primaryFields(Arrays.asList(fields));
		return this;
	}

	public PassInformation primaryFields(List<Field<?>> fields) {
		primaryFields.addAll(fields);
		return this;
	}

	public List<Field<?>> secondaryFields() {
		return Collections.unmodifiableList(secondaryFields);
	}

	public PassInformation secondaryFields(Field<?>... fields) {
		secondaryFields(Arrays.asList(fields));
		return this;
	}

	public PassInformation secondaryFields(List<Field<?>> fields) {
		secondaryFields.addAll(fields);
		return this;
	}

	public List<Field<?>> backFields() {
		return Collections.unmodifiableList(backFields);
	}

	public PassInformation backFields(Field<?>... fields) {
		backFields(Arrays.asList(fields));
		return this;
	}

	public PassInformation backFields(List<Field<?>> fields) {
		backFields.addAll(fields);
		return this;
	}

	public List<Field<?>> auxiliaryFields() {
		return Collections.unmodifiableList(auxiliaryFields);
	}

	public PassInformation auxiliaryFields(Field<?>... fields) {
		auxiliaryFields(Arrays.asList(fields));
		return this;
	}

	public PassInformation auxiliaryFields(List<Field<?>> fields) {
		auxiliaryFields.addAll(fields);
		return this;
	}

}
