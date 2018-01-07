package com.ka.cert.transparency.loader.application;

import com.ka.cert.transparency.loader.core.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description: Certification downloader application
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.application
 * Author: kakyurek
 * Date: 2018.01.06
 */
@SpringBootApplication(scanBasePackages = {"com.ka.cert.transparency.loader"})
public class CertApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CertApplication.class);

    private LogManager logManager;

    @Autowired
    public CertApplication(LogManager logManager) {
        this.logManager = logManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(CertApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("Fetching log servers..");
        logManager.updateLogServers();
        logger.info("Cached log servers..");
    }

}
