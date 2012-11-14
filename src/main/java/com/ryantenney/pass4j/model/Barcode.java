package com.ryantenney.pass4j.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@JsonInclude(Include.NON_EMPTY)
public class Barcode {

	@JsonProperty private BarcodeFormat format = BarcodeFormat.PDF417;

	@JsonProperty private String message;

	@JsonProperty private String messageEncoding = "iso-8859-1";

	@JsonProperty private String altText;

}
