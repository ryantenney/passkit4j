package com.ryantenney.passkit4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class Barcode {

	@JsonProperty @NonNull private BarcodeFormat format;

	@JsonProperty @NonNull private String message;

	@JsonProperty private String messageEncoding = "iso-8859-1";

	@JsonProperty private String altText = null;

}
