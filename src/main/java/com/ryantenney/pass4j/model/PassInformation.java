package com.ryantenney.pass4j.model;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonInclude(Include.NON_EMPTY)
public abstract class PassInformation {

	@JsonIgnore private final String typeName;

	@JsonProperty private List<Field<?>> headerFields;
	@JsonProperty private List<Field<?>> primaryFields;
	@JsonProperty private List<Field<?>> secondaryFields;
	@JsonProperty private List<Field<?>> backFields;
	@JsonProperty private List<Field<?>> auxiliaryFields;

	protected PassInformation(final String typeName) {
		this.typeName = typeName;
	}

	public String typeName() {
		return this.typeName;
	}

	public PassInformation headerFields(Field<?>... fields) {
		this.headerFields = Arrays.asList(fields);
		return this;
	}

	public PassInformation headerFields(List<Field<?>> fields) {
		this.headerFields = fields;
		return this;
	}

	public PassInformation primaryFields(Field<?>... fields) {
		this.primaryFields = Arrays.asList(fields);
		return this;
	}

	public PassInformation primaryFields(List<Field<?>> fields) {
		this.primaryFields = fields;
		return this;
	}

	public PassInformation secondaryFields(Field<?>... fields) {
		this.secondaryFields = Arrays.asList(fields);
		return this;
	}

	public PassInformation secondaryFields(List<Field<?>> fields) {
		this.secondaryFields = fields;
		return this;
	}

	public PassInformation backFields(Field<?>... fields) {
		this.backFields = Arrays.asList(fields);
		return this;
	}

	public PassInformation backFields(List<Field<?>> fields) {
		this.backFields = fields;
		return this;
	}

	public PassInformation auxiliaryFields(Field<?>... fields) {
		this.auxiliaryFields = Arrays.asList(fields);
		return this;
	}

	public PassInformation auxiliaryFields(List<Field<?>> fields) {
		this.auxiliaryFields = fields;
		return this;
	}

}
