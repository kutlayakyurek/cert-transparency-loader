package com.ka.cert.transparency.loader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.*;

/**
 * Description: Certificate utility class
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.util
 * Author: kakyurek
 * Date: 2018.01.07
 */
public class CertificateUtils {

    private static final Logger logger = LoggerFactory.getLogger(CertificateUtils.class);
    private static final String X_509 = "X.509";
    private static final String EXTENSION = ".cf";
    public static final String FOLDER_VALID = "/valid";
    public static final String FOLDER_INVALID = "/invalid";

    private CertificateUtils() {
    }

    /**
     * Saves certificate byte arrays into corresponding operator directory with timestamp as filename
     *
     * @param bytes         X509 certificate as byte array
     * @param timestamp     Timestamp value of MerkleTree leaf
     * @param directoryPath Target directory to save certificate
     * @throws CertificateException Thrown if certificate is corrupted
     */
    public static void saveCertificateFileFromBytes(byte[] bytes, long timestamp, String directoryPath) throws CertificateException {
        InputStream in = new ByteArrayInputStream(bytes);
        CertificateFactory certFactory = CertificateFactory.getInstance(X_509);
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
        String principal = cert.getIssuerX500Principal().getName();
        try {
            cert.checkValidity();
            FileUtils.byteArrayToFile(bytes, directoryPath + FOLDER_VALID + "/" + timestamp + EXTENSION);
        } catch (CertificateExpiredException | CertificateNotYetValidException e) {
            logger.debug(principal + " is not valid");
            FileUtils.byteArrayToFile(bytes, directoryPath + FOLDER_INVALID + "/" + timestamp + EXTENSION);
        }
    }

}
