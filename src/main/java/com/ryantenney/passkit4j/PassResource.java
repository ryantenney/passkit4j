package com.ryantenney.passkit4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PassResource implements NamedInputStreamSupplier {

	private final String name;
	private final InputStreamSupplier dataSupplier;

	public PassResource(final String filename) throws FileNotFoundException {
		this(new File(filename));
	}

	public PassResource(final File file) throws FileNotFoundException {
		this(file.getName(), file);
	}

	public PassResource(final String name, final File file) {
		this(name, new InputStreamSupplier() {
			@Override
			public InputStream getInputStream() throws IOException {
				return new FileInputStream(file);
			}
		});
	}

	public PassResource(final String name, final byte[] data) {
		this(name, new InputStreamSupplier() {
			@Override
			public InputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(data);
			}
		});
	}

	public PassResource(final String name, final InputStreamSupplier dataSupplier) {
		this.name = name;
		this.dataSupplier = dataSupplier;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return dataSupplier.getInputStream();
	}

}
