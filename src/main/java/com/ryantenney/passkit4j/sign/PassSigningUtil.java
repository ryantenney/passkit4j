package com.ryantenney.passkit4j.sign;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

class PassSigningUtil {

	static KeyStore loadPKCS12File(InputStream inputStream, String password) throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException, NoSuchProviderException {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}

		KeyStore keystore = KeyStore.getInstance("PKCS12", BouncyCastleProvider.PROVIDER_NAME);
		keystore.load(inputStream, chars(password));
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

	static PrivateKey getPrivateKey(KeyStore keyStore, String password) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
		return (PrivateKey) keyStore.getKey(firstAlias(keyStore), chars(password));
	}

	static X509Certificate getCertificate(KeyStore keyStore) throws KeyStoreException {
		return (X509Certificate) keyStore.getCertificate(firstAlias(keyStore));
	}

	static String firstAlias(KeyStore keyStore) throws KeyStoreException {
		Enumeration<String> aliases = keyStore.aliases();
		if (aliases.hasMoreElements()) {
			return aliases.nextElement();
		}
		throw new RuntimeException("KeyStore is empty");
	}

	static char[] chars(String password) {
		if (password == null) return new char[0];
		return password.toCharArray();
	}

}
