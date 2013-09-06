package com.ryantenney.passkit4j.sign;

import static com.ryantenney.passkit4j.sign.PassSigningUtil.*;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@RequiredArgsConstructor
public class PassSignerImpl implements PassSigner {

	@NonNull private final CMSSignedDataGenerator generator;

	public static Builder builder() {
		return new Builder();
	}

	@Getter
	@Setter
	@Accessors(chain=true, fluent=true)
	@NoArgsConstructor
	public static class Builder {

		@NonNull private X509Certificate signingCertificate;
		@NonNull private PrivateKey privateKey;
		@NonNull private X509Certificate intermediateCertificate;

		private KeyStore keyStore;
		private String signingCertificateAlias;
		private String privateKeyAlias;
		private String password;

		public Builder keystore(InputStream inputStream, String password) throws PassSigningException {
			return this.keystore(inputStream, null, password);
		}

		/**
		 * Deprecated in favor of calling {@code keystore(InputStream, String)}
		 * followed by setting the alias with {@code alias(String)}
		 * @param keyStore
		 * @param alias
		 * @param password
		 * @return
		 */
		@Deprecated
		public Builder keystore(InputStream inputStream, String alias, String password) throws PassSigningException {
			KeyStore keyStore = loadPKCS12File(inputStream, password);
			return this.keystore(keyStore, alias, password);
		}

		public Builder keystore(KeyStore keyStore, String password) {
			return this.keystore(keyStore, null, password);
		}

		/**
		 * Deprecated in favor of calling {@code keystore(KeyStore, String)}
		 * followed by setting the alias with {@code alias(String)}
		 * @param keyStore
		 * @param alias
		 * @param password
		 * @return
		 */
		@Deprecated
		public Builder keystore(KeyStore keyStore, String alias, String password) {
			this.keyStore = keyStore;
			this.signingCertificateAlias = alias;
			this.privateKeyAlias = alias;
			this.password = password;
			return this;
		}

		/**
		 * Sets both the signing certificate and private key aliases
		 * @param alias
		 * @return this
		 */
		public Builder alias(String alias) {
			this.signingCertificateAlias = alias;
			this.privateKeyAlias = alias;
			return this;
		}

		public Builder intermediateCertificate(InputStream inputStream) throws PassSigningException {
			this.intermediateCertificate = loadDERCertificate(inputStream);
			return this;
		}

		public Builder intermediateCertificate(X509Certificate intermediateCertificate) {
			this.intermediateCertificate = intermediateCertificate;
			return this;
		}

		public PassSigner build() throws PassSigningException {
			if (signingCertificate == null) {
				signingCertificate = getCertificate(keyStore, signingCertificateAlias);
			}

			if (privateKey == null) {
				privateKey = getPrivateKey(keyStore, privateKeyAlias, password);
			}

			if (intermediateCertificate == null) {
				throw new PassSigningException("Must provide an intermediate certificate");
			}

			return new PassSignerImpl(signingCertificate, privateKey, intermediateCertificate);
		}

	}

	public PassSignerImpl(X509Certificate signingCertificate, PrivateKey privateKey, X509Certificate intermediateCertificate) throws PassSigningException {
		this.generator = createGenerator(signingCertificate, privateKey, intermediateCertificate);
	}

	public CMSSignedDataGenerator createGenerator(X509Certificate signingCertificate, PrivateKey privateKey, X509Certificate intermediateCertificate) throws PassSigningException {
		ensureBCProvider();

		try {

			CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
			generator.addSignerInfoGenerator(new JcaSimpleSignerInfoGeneratorBuilder()
				.setProvider(BouncyCastleProvider.PROVIDER_NAME)
				.build("SHA1withRSA", privateKey, signingCertificate));
			generator.addCertificates(new JcaCertStore(Arrays.asList(intermediateCertificate, signingCertificate)));

			return generator;

		} catch (Exception ex) {
			throw propagateAsPassSigningException("Error creating PassSignerImpl instance", ex);
		}
	}

	public byte[] generateSignature(byte[] data) throws PassSigningException {
		ensureBCProvider();

		try {

			CMSProcessableByteArray processableData = new CMSProcessableByteArray(data);
			CMSSignedData signedData = generator.generate(processableData);
			return signedData.getEncoded();

		} catch (Exception ex) {
			throw propagateAsPassSigningException("Error generating signautre", ex);
		}
	}

}
