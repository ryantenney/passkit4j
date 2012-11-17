package com.ryantenney.passkit4j.signing;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

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

public class CertificatePassSigner implements PassSigner {

	private final PassSigningInformation certs;

	public CertificatePassSigner(PassSigningInformation certs) {
		this.certs = certs;
	}

	public byte[] generateSignature(byte[] data) throws PassSigningException {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}

		X509Certificate certificate = certs.signingCertificate();
		PrivateKey privateKey = certs.privateKey();
		X509Certificate wwdrCertificate = certs.intermediateCertificate();

		try {

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
