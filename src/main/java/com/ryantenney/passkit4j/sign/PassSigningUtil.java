package com.ryantenney.passkit4j.sign;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

class PassSigningUtil {

	static KeyStore loadPKCS12File(InputStream inputStream, String password) throws PassSigningException {
		ensureBCProvider();

		try {

			KeyStore keystore = KeyStore.getInstance("PKCS12", BouncyCastleProvider.PROVIDER_NAME);
			keystore.load(inputStream, chars(password));
			return keystore;

		} catch (Exception e) {
			throw propagateAsPassSigningException("Error loading PKCS12 KeyStore", e);
		} finally {
			try {
				if (inputStream != null) inputStream.close();
			} catch (IOException e) {}
		}
	}

	static X509Certificate loadDERCertificate(InputStream inputStream) throws PassSigningException {
		ensureBCProvider();

		Certificate cert;
		try {

			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
			cert = certificateFactory.generateCertificate(inputStream);

		} catch (Exception e) {
			throw propagateAsPassSigningException("Error loading certificate", e);
		} finally {
			try {
				if (inputStream != null) inputStream.close();
			} catch (IOException e) {}
		}

		if (!(cert instanceof X509Certificate)) {
			throw new PassSigningException("Certificate is not of type X509Certificate");
		}

		return (X509Certificate) cert;
	}

	/**
	 * 
	 * @param keyStore
	 * @param alias
	 * @param password
	 * @return a PrivateKey
	 * @throws PassSigningException if there are problems reading from the KeyStore,
	 * 			there is no private key with the provided alias, or the certificate
	 * 			is not of type PrivateKey
	 */
	static PrivateKey getPrivateKey(KeyStore keyStore, String alias, String password) throws PassSigningException {
		if (alias == null) alias = firstAlias(keyStore);

		Key key;
		try {
			key = keyStore.getKey(alias, chars(password));
		} catch (Exception ex) {
			throw propagateAsPassSigningException("Error retrieving PrivateKey from KeyStore", ex);
		}

		if (key == null) {
			throw new PassSigningException("KeyStore does not contain a PrivateKey with alias '" + alias + "'");
		} else if (!(key instanceof PrivateKey)) {
			throw new PassSigningException("KeyStore entry with alias '" + alias + "' is not of type PrivateKey");
		}

		return (PrivateKey) key;
	}

	/**
	 * 
	 * @param keyStore
	 * @param alias
	 * @return an X509Certificate
	 * @throws PassSigningException if there are problems reading from the KeyStore,
	 * 			there is no certificate with the provided alias, or the certificate
	 * 			is not of type X509Certificate
	 */
	static X509Certificate getCertificate(KeyStore keyStore, String alias) throws PassSigningException {
		if (alias == null) alias = firstAlias(keyStore);

		Certificate cert;
		try {
			cert = keyStore.getCertificate(alias);
		} catch (Exception ex) {
			throw propagateAsPassSigningException("Error retrieving certificate from KeyStore", ex);
		}

		if (cert == null) {
			throw new PassSigningException("KeyStore does not contain an X509Certificate with alias '" + alias + "'");
		} else if (!(cert instanceof X509Certificate)) {
			throw new PassSigningException("KeyStore entry with alias '" + alias + "' is not of type X509Certificate");
		}

		return (X509Certificate) cert;
	}

	/**
	 * 
	 * @param keyStore
	 * @return a valid alias from the provided KeyStore
	 * @throws PassSigningException if there are problems reading from the KeyStore or
	 * 			the KeyStore doesn't contain <em>exactly</em> one alias.
	 */
	static String firstAlias(KeyStore keyStore) throws PassSigningException {
		Enumeration<String> aliases;
		try {
			aliases = keyStore.aliases();
		} catch (KeyStoreException ex) {
			throw propagateAsPassSigningException("Error reading aliases from KeyStore", ex);
		}

		if (aliases.hasMoreElements()) {
			String alias = aliases.nextElement();
			if (aliases.hasMoreElements()) {
				throw new PassSigningException("Provided KeyStore contains multiple aliases, please specify an alias");
			}
			return alias;
		}
		throw new PassSigningException("Provided KeyStore is empty");
	}

	static char[] chars(String password) {
		if (password == null) return new char[0];
		return password.toCharArray();
	}

	static void ensureBCProvider() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	static PassSigningException propagateAsPassSigningException(String message, Exception ex) throws PassSigningException {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		} else if (ex instanceof PassSigningException) {
			throw (PassSigningException) ex;
		}

		throw new PassSigningException(message, ex);
	}

}
