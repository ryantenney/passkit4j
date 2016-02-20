package com.ryantenney.passkit4j.model;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Barcode {

	@NonNull private BarcodeFormat format;

	@NonNull private String message;

	private String messageEncoding = "iso-8859-1";

	private String altText = null;

}
