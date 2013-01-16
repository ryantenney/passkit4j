package com.ryantenney.passkit4j;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lombok.Delegate;
import lombok.Getter;
import lombok.experimental.Accessors;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.ryantenney.passkit4j.io.NamedInputStreamSupplier;
import com.ryantenney.passkit4j.model.PassInformation;
import com.ryantenney.passkit4j.sign.PassSigner;
import com.ryantenney.passkit4j.sign.PassSigningException;

public class PassSerializer {

	public static final String PKPASS_TYPE = "application/vnd.apple.pkpass";

	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new ISO8601DateFormat());
		objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
		objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	public static void writePkPassArchive(Pass pass, PassSigner signer, OutputStream out) throws PassSigningException, PassSerializationException {
		ZipOutputStream zip = new ZipOutputStream(out);

		try {

			Map<String, String> manifest = writeAndHashFiles(pass.files(), zip);
			manifest.put("pass.json", write(generatePass(pass), hasher(zipEntry("pass.json", zip))).hash());

			byte[] manifestData = write(generateManifest(manifest), new ByteArrayOutputStream()).toByteArray();
			write(manifestData, zipEntry("manifest.json", zip));

			byte[] signatureData = signer.generateSignature(manifestData);
			write(signatureData, zipEntry("signature", zip));

		} catch (NoSuchAlgorithmException e) {
			throw new PassSerializationException(e);
		} catch (DigestException e) {
			throw new PassSerializationException(e);
		} catch (NoSuchProviderException e) {
			throw new PassSerializationException(e);
		} catch (IOException e) {
			throw new PassSerializationException(e);
		} finally {
			try {
				if (zip != null) zip.close();
			} catch (IOException e) {
				throw new PassSerializationException("Error closing output stream", e);
			}
		}
	}

	protected static ObjectNode generatePass(Pass pass) {
		ObjectNode tree = objectMapper.valueToTree(pass);

		PassInformation info = pass.passInformation();
		tree.put(info.typeName(), objectMapper.valueToTree(info));

		return tree;
	}

	protected static ObjectNode generateManifest(Map<String, String> files) {
		ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
		for (Map.Entry<String, String> file : files.entrySet()) {
			node.put(file.getKey(), file.getValue());
		}
		return node;
	}

	protected static Map<String, String> writeAndHashFiles(List<NamedInputStreamSupplier> files, ZipOutputStream output) throws IOException, NoSuchAlgorithmException, DigestException, NoSuchProviderException {
		Map<String, String> hashes = new HashMap<String, String>();
		for (NamedInputStreamSupplier file : files) {
			OutputStreamHasher hasher = hasher(zipEntry(file.getName(), output));
			copy(file.getInputStream(), hasher);
			hashes.put(file.getName(), hasher.hash());
		}
		return hashes;
	}

	private static <T extends OutputStream> T write(JsonNode node, T output) throws IOException {
		objectMapper
			.getFactory()
			.createGenerator(output)
			.useDefaultPrettyPrinter()
			.writeTree(node);
		output.close();
		return output;
	}

	private static <T extends OutputStream> T write(byte[] data, T output) throws IOException {
		output.write(data);
		output.close();
		return output;
	}

	private static void copy(InputStream input, OutputStream output) throws IOException, NoSuchAlgorithmException, DigestException {
		try {
			final int EOF = -1;
	        int n = 0;
			byte[] buffer = new byte[4096];
	        while (EOF != (n = input.read(buffer))) {
	            output.write(buffer, 0, n);
	        }
		} finally {
			input.close();
			output.close();
		}
	}

	private static OutputStreamHasher hasher(OutputStream output) throws NoSuchAlgorithmException, NoSuchProviderException {
		return new OutputStreamHasher(output);
	}

	@Accessors(fluent=true)
	private static class OutputStreamHasher extends OutputStream {

		@Delegate(types=OutputStream.class, excludes=Closeable.class)
		private final DigestOutputStream output;

		private final MessageDigest digest;

		@Getter private final OutputStream inner;
		@Getter private String hash;

		public OutputStreamHasher(final OutputStream inner) throws NoSuchAlgorithmException, NoSuchProviderException {
			this.inner = inner;
			this.digest = MessageDigest.getInstance("SHA1", BouncyCastleProvider.PROVIDER_NAME);
			this.output = new DigestOutputStream(this.inner, this.digest);
		}

		@Override
		public void close() throws IOException {
			this.hash = new String(Hex.encode(this.output.getMessageDigest().digest()));
			this.output.close();
		}

	}

	private static OutputStream zipEntry(final String name, final ZipOutputStream output) throws IOException {
		return new ZipEntryOutputStream(name, output);
	}

	private static class ZipEntryOutputStream extends OutputStream {

		@Delegate(types=OutputStream.class, excludes=Closeable.class)
		private final ZipOutputStream output;

		public ZipEntryOutputStream(String name, ZipOutputStream output) throws IOException {
			this.output = output;
			this.output.putNextEntry(new ZipEntry(name));
		}

		@Override
		public void close() throws IOException {
			output.closeEntry();
		}

	}

}
