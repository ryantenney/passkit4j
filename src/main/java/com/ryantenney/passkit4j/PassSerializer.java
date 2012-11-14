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
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lombok.Delegate;
import lombok.Getter;
import lombok.experimental.Accessors;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.encoders.Hex;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.ryantenney.passkit4j.model.PassInformation;

public class PassSerializer {

	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new ISO8601DateFormat());
		objectMapper.setVisibilityChecker(objectMapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
	}

	public static void writePkPassArchive(Pass pass, PassSigningInformation signingInformation, OutputStream out) throws IOException, NoSuchAlgorithmException, DigestException, CertificateEncodingException, OperatorCreationException, CMSException, NoSuchProviderException {
		ZipOutputStream zip = new ZipOutputStream(out);

		Map<String, String> manifest = writeAndHashFiles(pass.files(), zip);
		manifest.put("pass.json", write(generatePass(pass), hasher(zipEntry("pass.json", zip))).hash());

		byte[] manifestData = write(generateManifest(manifest), new ByteArrayOutputStream()).toByteArray();
		write(manifestData, zipEntry("manifest.json", zip));

		byte[] signatureData = generateSignature(manifestData, signingInformation);
		write(signatureData, zipEntry("signature", zip));

		zip.close();
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

	protected static byte[] generateSignature(byte[] data, PassSigningInformation signingInformation) throws OperatorCreationException, CertificateEncodingException, CMSException, IOException {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}

		X509Certificate certificate = signingInformation.signingCertificate();
		PrivateKey privateKey = signingInformation.privateKey();
		X509Certificate wwdrCertificate = signingInformation.intermediateCertificate();

		ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA")
				.setProvider(BouncyCastleProvider.PROVIDER_NAME)
				.build(privateKey);

		DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder()
				.setProvider(BouncyCastleProvider.PROVIDER_NAME)
				.build();

		SignerInfoGenerator signerInfoGenerator = new JcaSignerInfoGeneratorBuilder(digestCalculatorProvider)
				.build(sha1Signer, certificate);

		CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
		generator.addSignerInfoGenerator(signerInfoGenerator);
		generator.addCertificates(new JcaCertStore(Arrays.asList(wwdrCertificate, certificate)));

		CMSProcessableByteArray processableData = new CMSProcessableByteArray(data);
		CMSSignedData signedData = generator.generate(processableData);
		return signedData.getEncoded();
	}

	protected static Map<String, String> writeAndHashFiles(List<PassResource> files, ZipOutputStream output) throws IOException, NoSuchAlgorithmException, DigestException, NoSuchProviderException {
		Map<String, String> hashes = new HashMap<String, String>();
		for (PassResource file : files) {
			OutputStreamHasher hasher = hasher(zipEntry(file.name(), output));
			copy(file.data(), hasher);
			hashes.put(file.name(), hasher.hash());
		}
		return hashes;
	}

	private static <T extends OutputStream> T write(JsonNode node, T output) throws IOException {
		objectMapper.getFactory().createGenerator(output).writeTree(node);
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
