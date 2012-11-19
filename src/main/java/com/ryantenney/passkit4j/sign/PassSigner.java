package com.ryantenney.passkit4j.sign;

public interface PassSigner {

	public byte[] generateSignature(byte[] data) throws PassSigningException;

}
