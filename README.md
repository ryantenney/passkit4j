# passkit4j [![Build Status](https://secure.travis-ci.org/ryantenney/passkit4j.png?branch=master)](https://travis-ci.org/ryantenney/passkit4j)

## Apple Passbook library for Java

 - Fluent API
 - Stream-oriented

### Usage

Create a Pass Type ID in the [iOS Provisioning Portal](https://developer.apple.com/ios/manage/passtypeids/index.action) (if you haven't done so already), import the resulting `pass.cer` file into Keychain, then export it as a `.p12` file. Download the [Apple WWDR certificate](http://developer.apple.com/certificationauthority/AppleWWDRCA.cer).

In the code, create a `PassSignerImpl` object with these certificates:

```java
PassSigner signer = PassSignerImpl.builder()
	.keystore(new FileInputStream("/path/to/certificate.p12"), "password")
	.intermediateCertificate(new FileInputStream("/path/to/AppleWWDRCA.cer"))
	.build();

Pass pass = new Pass()
	.passTypeIdentifier("pass.com.bouldercoffeeco.storeCard")
	.serialNumber("1a2b3c")
	.teamIdentifier("cafed00d");
	// ... for a full example see src/test/com/ryantenney/passkit4j/StoreCardExample.java

PassSerializer.writePkPassArchive(pass, signer, outputStream);
```

### Maven

Current stable version is 1.0.1

```xml
<dependency>
	<groupId>com.ryantenney.passkit4j</groupId>
	<artifactId>passkit4j</artifactId>
	<version>1.0.1</version>
</dependency>
```

Development has begun on 2.0.0 to add support for iOS 7 Passbook features.
2.0.0-SNAPSHOT is available from the Sonatype Nexus Snapshots repository.

```xml
<repository>
	<id>sonatype-nexus-snapshots</id>
	<name>Sonatype Nexus Snapshots</name>
	<url>http://oss.sonatype.org/content/repositories/snapshots</url>
</repository>

<dependency>
	<groupId>com.ryantenney.passkit4j</groupId>
	<artifactId>passkit4j</artifactId>
	<version>2.0.0-SNAPSHOT</version>
</dependency>
```

---

### License

Copyright (c) 2012-2013 Ryan Tenney

Published under Apache Software License 2.0, see LICENSE

[![Rochester Made](http://rochestermade.com/media/images/rochester-made-dark-on-light.png)](http://rochestermade.com)
