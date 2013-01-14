package com.ryantenney.passkit4j;

import java.io.IOException;
import java.io.InputStream;

public interface InputStreamSupplier {

	public InputStream getInputStream() throws IOException;

}
