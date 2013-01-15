package com.ryantenney.passkit4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferedPassResource extends PassResource {

	public BufferedPassResource(final String filename) throws IOException {
		this(new File(filename));
	}

	public BufferedPassResource(final File file) throws IOException {
		this(file.getName(), new FileInputStream(file));
	}

	public BufferedPassResource(final String name, final File file) throws IOException {
		this(name, read(new FileInputStream(file), (int) file.length()));
	}

	public BufferedPassResource(final String name, final InputStreamSupplier dataSupplier) throws IOException {
		this(name, dataSupplier.getInputStream());
	}

	public BufferedPassResource(final String name, final InputStream data) throws IOException {
		this(name, read(data, -1));
	}

	public BufferedPassResource(final String name, final byte[] data) {
		super(name, new InputStreamSupplier() {
			@Override
			public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(data);
			}
		});
	}

	private static byte[] read(InputStream input, int length) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(length);
		int read = 0;
		byte[] buf = new byte[4096];
		while ((read = input.read(buf)) != -1) {
			out.write(buf, 0, read);
		}
		return out.toByteArray();
	}

}
