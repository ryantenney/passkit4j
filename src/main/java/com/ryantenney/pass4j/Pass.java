package com.ryantenney.pass4j;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryantenney.pass4j.model.Barcode;
import com.ryantenney.pass4j.model.Color;
import com.ryantenney.pass4j.model.Location;
import com.ryantenney.pass4j.model.PassInformation;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain=true, fluent=true)
public class Pass {

	@JsonIgnore @Getter @Setter private PassInformation passInformation;

	@JsonProperty @Getter @Setter @NonNull private String teamIdentifier;
	@JsonProperty @Getter @Setter @NonNull private String passTypeIdentifier;
	@JsonProperty @Getter @Setter @NonNull private String organizationName;
	@JsonProperty @Getter @Setter @NonNull private String serialNumber;
	@JsonProperty @Getter @Setter @NonNull private String description;

	@JsonProperty private final int formatVersion = 1;

	@JsonProperty @Getter @Setter private Color backgroundColor;
	@JsonProperty @Getter @Setter private Color foregroundColor;
	@JsonProperty @Getter @Setter private Color labelColor;
	@JsonProperty @Getter @Setter private String logoText;
	@JsonProperty @Getter @Setter private Barcode barcode;
	@JsonProperty @Getter @Setter private boolean suppressStripShine = false;

	@JsonProperty @Getter @Setter private String webServiceURL;
	@JsonProperty @Getter @Setter private String authenticationToken;

	@JsonProperty @Getter @Setter private List<Location> locations;
	@JsonProperty @Getter @Setter private Date relevantDate;

	@JsonProperty @Getter @Setter private List<String> associatedStoreIdentifiers;

	@JsonIgnore private List<PassResource> files = new ArrayList<>();

	public List<PassResource> files() {
		return Collections.unmodifiableList(files);
	}

	public Pass files(PassResource... files) {
		this.files.addAll(Arrays.asList(files));
		return this;
	}

	public Pass files(List<PassResource> files) {
		this.files.addAll(files);
		return this;
	}

	public Pass file(final PassResource file) {
		this.files.add(file);
		return this;
	}

}
