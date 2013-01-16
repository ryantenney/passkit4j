package com.ryantenney.passkit4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ryantenney.passkit4j.io.InputStreamSupplier;
import com.ryantenney.passkit4j.io.NamedInputStreamSupplier;

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

	public PassResource(final String name, final InputStream data) {
		this(name, new InputStreamSupplier() {
			private final AtomicBoolean avail = new AtomicBoolean();

			@Override
			public InputStream getInputStream() throws IOException {
				if (!avail.compareAndSet(false, true)) {
					throw new IOException("PassResource '" + name + "' has been consumed");
				}
				return data;
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
