package com.ryantenney.pass4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bouncycastle.util.encoders.Hex;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ryantenney.pass4j.model.PassInformation;

public class PassSerializer {

	protected void writePassBundle(Pass pass, OutputStream out) throws IOException, NoSuchAlgorithmException, DigestException {
		JsonFactory factory = new ObjectMapper().getFactory();
		ZipOutputStream zip = new ZipOutputStream(out);

		Map<String, String> manifest = writeFiles(pass.files(), zip);

		OutputStream passOut = new ZipEntryOutputStream("pass.json", zip);
		String passHash = write(generatePass(pass), factory, passOut);
		passOut.close();

		manifest.put("pass.json", passHash);

		JsonNode manifestJson = generateManifest(manifest);
		ByteArrayOutputStream manifestDataOut = new ByteArrayOutputStream();
		write(manifestJson, factory, manifestDataOut);
		byte[] manifestData = manifestDataOut.toByteArray();

		OutputStream manifestOut = new ZipEntryOutputStream("manifest.json", zip);
		manifestOut.write(manifestData);
		manifestOut.close();

		try {
			byte[] signed = new byte[0]; //signature(manifestData);
			OutputStream signatureOut = new ZipEntryOutputStream("signature", zip);
			signatureOut.write(signed);
			signatureOut.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		zip.close();
	}

	protected Map<String, String> writeFiles(List<PassResource> files, ZipOutputStream output) throws IOException, NoSuchAlgorithmException, DigestException {
		Map<String, String> hashes = new HashMap<>();
		for (PassResource file : files) {
			OutputStream entry = new ZipEntryOutputStream(file.name(), output);
			String hash = file.copy(entry);
			entry.close();
			hashes.put(file.name(), hash);
		}
		return hashes;
	}

	protected ObjectNode generatePass(Pass pass) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
		ObjectNode tree = objectMapper.valueToTree(pass);

		PassInformation info = pass.passInformation();
		tree.put(info.typeName(), objectMapper.valueToTree(info));

		return tree;
	}

	protected ObjectNode generateManifest(Map<String, String> files) {
		ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
		for (Map.Entry<String, String> file : files.entrySet()) {
			node.put(file.getKey(), file.getValue());
		}
		return node;
	}

	protected String write(JsonNode node, JsonFactory factory, OutputStream output) throws IOException, NoSuchAlgorithmException {
		DigestOutputStream digest = new DigestOutputStream(output, MessageDigest.getInstance("SHA1"));
		factory.createGenerator(output).writeTree(node);
		return new String(Hex.encode(digest.getMessageDigest().digest()));
	}

	static class ZipEntryOutputStream extends OutputStream {

		private ZipOutputStream output;

		public ZipEntryOutputStream(String name, ZipOutputStream output) throws IOException {
			this.output = output;

			output.putNextEntry(new ZipEntry(name));
		}

		@Override
		public void write(int b) throws IOException {
			output.write(b);
		}

		@Override
		public void write(byte[] b) throws IOException {
			output.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			output.write(b, off, len);
		}

		@Override
		public void close() throws IOException {
			output.closeEntry();
		}

	}

}
