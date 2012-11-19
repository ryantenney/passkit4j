package com.ryantenney.passkit4j.signing;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

@Data
@Accessors(fluent=true)
@RequiredArgsConstructor
public class PassSignerImpl implements PassSigner {

	@NonNull private final X509Certificate signingCertificate;
	@NonNull private final PrivateKey privateKey;
	@NonNull private final X509Certificate intermediateCertificate;

	public static Builder builder() {
		return new Builder();
	}

	@Data
	@Accessors(chain=true, fluent=true)
	@NoArgsConstructor
	public static class Builder {

		private X509Certificate signingCertificate;
		private PrivateKey privateKey;
		private X509Certificate intermediateCertificate;

		public Builder keystore(InputStream inputStream, String password) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, NoSuchProviderException, IOException, UnrecoverableKeyException {
			KeyStore keyStore = PassSigningUtil.loadPKCS12File(inputStream, chars(password));
			return this.keystore(keyStore, password);
		}

		public Builder keystore(KeyStore keyStore, String password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
			for (String alias : Collections.list(keyStore.aliases())) {
				Key key = keyStore.getKey(alias, chars(password));
				if (key instanceof PrivateKey) {
					this.privateKey = (PrivateKey) key;
					Object cert = keyStore.getCertificate(alias);
					if (cert instanceof X509Certificate) {
						this.signingCertificate = (X509Certificate) cert;
						break;
					}
				}
			}

			if (this.signingCertificate == null || this.privateKey == null) {
				throw new IllegalStateException("KeyStore must contain a PrivateKey and Certificate");
			}

			return this;
		}

		public Builder intermediateCertificate(InputStream inputStream) throws CertificateException, NoSuchProviderException, IOException {
			this.intermediateCertificate = PassSigningUtil.loadDERCertificate(inputStream);
			return this;
		}

		public Builder intermediateCertificate(X509Certificate intermediateCertificate) {
			this.intermediateCertificate = intermediateCertificate;
			return this;
		}

		public PassSigner build() {
			if (signingCertificate == null || privateKey == null || intermediateCertificate == null) {
				throw new IllegalArgumentException();
			}

			return new PassSignerImpl(signingCertificate, privateKey, intermediateCertificate);
		}

		private static final char[] chars(String password) {
			if (password == null) return new char[0];
			return password.toCharArray();
		}

	}

	public byte[] generateSignature(byte[] data) throws PassSigningException {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}

		try {

			ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA")
					.setProvider(BouncyCastleProvider.PROVIDER_NAME)
					.build(privateKey);
	
			DigestCalculatorProvider digestCalculatorProvider = new JcaDigestCalculatorProviderBuilder()
					.setProvider(BouncyCastleProvider.PROVIDER_NAME)
					.build();
	
			SignerInfoGenerator signerInfoGenerator = new JcaSignerInfoGeneratorBuilder(digestCalculatorProvider)
					.build(sha1Signer, signingCertificate);
	
			CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
			generator.addSignerInfoGenerator(signerInfoGenerator);
			generator.addCertificates(new JcaCertStore(Arrays.asList(intermediateCertificate, signingCertificate)));
	
			CMSProcessableByteArray processableData = new CMSProcessableByteArray(data);
			CMSSignedData signedData = generator.generate(processableData);
			return signedData.getEncoded();

		} catch (IOException e) {
			throw new PassSigningException(e);
		} catch (OperatorCreationException e) {
			throw new PassSigningException(e);
		} catch (CertificateEncodingException e) {
			throw new PassSigningException(e);
		} catch (CMSException e) {
			throw new PassSigningException(e);
		}
	}

}
