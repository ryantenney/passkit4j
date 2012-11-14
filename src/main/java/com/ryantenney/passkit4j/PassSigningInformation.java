package com.ryantenney.passkit4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent=true)
@RequiredArgsConstructor
public class PassSigningInformation {

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

		public PassSigningInformation build() {
			if (signingCertificate == null || privateKey == null || intermediateCertificate == null) {
				throw new IllegalArgumentException();
			}

			return new PassSigningInformation(signingCertificate, privateKey, intermediateCertificate);
		}

		private static final char[] chars(String password) {
			if (password == null) return new char[0];
			return password.toCharArray();
		}

	}

}
