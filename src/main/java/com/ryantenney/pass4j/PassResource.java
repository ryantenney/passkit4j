package com.ryantenney.pass4j;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Getter;
import lombok.experimental.Accessors;

import org.bouncycastle.util.encoders.Hex;

@Accessors(fluent=true)
public class PassResource implements Closeable {

	private @Getter final String name;
	private @Getter final InputStream data;

	private @Getter String hash = null;
	private @Getter boolean consumed = false;

	public PassResource(final String filename) throws IOException, NoSuchAlgorithmException {
		this(new File(filename));
	}

	public PassResource(final File file) throws IOException, NoSuchAlgorithmException {
		this(file.getName(), new FileInputStream(file));
	}

	public PassResource(final String name, final File file) throws IOException, NoSuchAlgorithmException {
		this(name, new FileInputStream(file));
	}

	public PassResource(final String name, final InputStream data) throws NoSuchAlgorithmException {
		this.name = name;
		this.data = data;
	}

	public String copy(OutputStream output) throws IOException, NoSuchAlgorithmException, DigestException {
		if (consumed) {
			throw new IllegalStateException("Asset " + name + " is used up");
		}

		try {
			final int EOF = -1;
	        int n = 0;
			byte[] buffer = new byte[4096];
			MessageDigest digest = MessageDigest.getInstance("SHA1");
	        while (EOF != (n = data.read(buffer))) {
	            output.write(buffer, 0, n);
	            digest.digest(buffer, 0, n);
	        }
	        return new String(Hex.encode(digest.digest()));
		} finally {
			consumed = true;
			close();
		}
	}

	@Override
	public void close() throws IOException {
		data.close();
	}

}
