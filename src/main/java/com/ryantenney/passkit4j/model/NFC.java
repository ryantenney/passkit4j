package com.ryantenney.passkit4j.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true, fluent=true)
@RequiredArgsConstructor
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class NFC {

	@NonNull private String message;

	private String encryptionPublicKey;

}
