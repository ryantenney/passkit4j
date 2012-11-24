package com.ryantenney.passkit4j.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
public class Barcode {

	@NonNull private BarcodeFormat format;

	@NonNull private String message;

	private String messageEncoding = "iso-8859-1";

	private String altText = null;

}
