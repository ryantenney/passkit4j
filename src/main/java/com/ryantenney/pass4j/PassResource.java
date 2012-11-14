package com.ryantenney.pass4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent=true)
public class PassResource {

	private @Getter final String name;
	private @Getter final InputStream data;

	private @Getter String hash = null;
	private @Getter boolean consumed = false;

	public PassResource(final String filename) throws FileNotFoundException {
		this(new File(filename));
	}

	public PassResource(final File file) throws FileNotFoundException {
		this(file.getName(), new FileInputStream(file));
	}

	public PassResource(final String name, final File file) throws FileNotFoundException {
		this(name, new FileInputStream(file));
	}

	public PassResource(final String name, final InputStream data) {
		this.name = name;
		this.data = data;
	}

}
