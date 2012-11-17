package com.ryantenney.passkit4j.signing;

public interface PassSigner {

	public byte[] generateSignature(byte[] data) throws PassSigningException;

}
