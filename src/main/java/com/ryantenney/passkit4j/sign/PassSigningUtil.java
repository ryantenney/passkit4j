package com.ryantenney.passkit4j.sign;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

class PassSigningUtil {

	static KeyStore loadPKCS12File(InputStream inputStream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, NoSuchProviderException {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}

		KeyStore keystore = KeyStore.getInstance("PKCS12", BouncyCastleProvider.PROVIDER_NAME);
		keystore.load(inputStream, password);
		return keystore;
	}

	static X509Certificate loadDERCertificate(InputStream inputStream) throws IOException, CertificateException, NoSuchProviderException {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
			Certificate certificate = certificateFactory.generateCertificate(inputStream);
			if (certificate instanceof X509Certificate) {
				return (X509Certificate) certificate;
			} else {
				throw new IllegalArgumentException("Certificate is not a X509Certificate");
			}
		} finally {
			inputStream.close();
		}
	}

}
