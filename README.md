# passkit4j [![Build Status](https://secure.travis-ci.org/ryantenney/passkit4j.png?branch=master)](https://travis-ci.org/ryantenney/passkit4j)

#### Apple Passbook library for Java

 - Fluent API
 - Stream-oriented

#### Usage

Create a Pass Type ID in the iOS Provisioning Portal (if you haven't done so already), import the resulting `pass.cer` file into Keychain, then export it as a `.p12` file. Download the [Apple WWDR certificate](http://developer.apple.com/certificationauthority/AppleWWDRCA.cer) and export it as a `.pem` file.

In the code, create a `PassSigningInformation` object with these certificates:

    PassSigningInformation signing = PassSigningInformation.builder()
        .keystore(new FileInputStream("certificate.p12"), "password")
        .intermediateCertificate(new FileInputStream("wwdr.pem"))
        .build();

---

#### License

Copyright (c) 2012 Ryan Tenney

Published under Apache Software License 2.0, see LICENSE
