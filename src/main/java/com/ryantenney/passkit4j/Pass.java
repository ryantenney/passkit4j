package com.ryantenney.passkit4j;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryantenney.passkit4j.model.Barcode;
import com.ryantenney.passkit4j.model.Color;
import com.ryantenney.passkit4j.model.Location;
import com.ryantenney.passkit4j.model.PassInformation;

@Data
@NoArgsConstructor
@Accessors(chain=true, fluent=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pass {

	@JsonProperty @NonNull private String teamIdentifier;
	@JsonProperty @NonNull private String passTypeIdentifier;
	@JsonProperty @NonNull private String organizationName;
	@JsonProperty @NonNull private String serialNumber;
	@JsonProperty @NonNull private String description;

	@JsonProperty private final int formatVersion = 1;

	@JsonProperty private Color backgroundColor;
	@JsonProperty private Color foregroundColor;
	@JsonProperty private Color labelColor;
	@JsonProperty private String logoText;
	@JsonProperty private Barcode barcode;
	@JsonProperty private boolean suppressStripShine = false;

	@JsonProperty private String webServiceURL;
	@JsonProperty private String authenticationToken;

	@JsonProperty private List<Location> locations;
	@JsonProperty private Date relevantDate;

	@JsonProperty private List<String> associatedStoreIdentifiers;

	@JsonIgnore private PassInformation passInformation;
	@JsonIgnore private List<PassResource> files = new ArrayList<>();

	public List<PassResource> files() {
		return this.files;
	}

	public Pass files(List<PassResource> values) {
		this.files = values;
		return this;
	}

	public Pass files(PassResource... values) {
		this.files = Arrays.asList(values);
		return this;
	}

	public List<Location> locations() {
		return this.locations;
	}

	public Pass locations(List<Location> values) {
		this.locations = values;
		return this;
	}

	public Pass locations(Location... values) {
		this.locations = Arrays.asList(values);
		return this;
	}

	public List<String> associatedStoreIdentifiers() {
		return this.associatedStoreIdentifiers;
	}

	public Pass associatedStoreIdentifiers(List<String> values) {
		this.associatedStoreIdentifiers = values;
		return this;
	}

	public Pass associatedStoreIdentifiers(String... values) {
		this.associatedStoreIdentifiers = Arrays.asList(values);
		return this;
	}

}
