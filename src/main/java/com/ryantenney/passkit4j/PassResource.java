package com.ryantenney.passkit4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PassResource implements NamedInputStreamSupplier {

	private final String name;
	private final InputStream data;

	public PassResource(final String filename) throws FileNotFoundException {
		this(new File(filename));
	}

	public PassResource(final File file) throws FileNotFoundException {
		this(file.getName(), new FileInputStream(file));
	}

	public PassResource(final String name, final File file) throws FileNotFoundException {
		this(name, new FileInputStream(file));
	}

	public PassResource(final String name, final byte[] data) throws FileNotFoundException {
		this(name, new ByteArrayInputStream(data));
	}

	public PassResource(final String name, final InputStream data) {
		this.name = name;
		this.data = data;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public InputStream getInputStream() {
		return data;
	}

}
