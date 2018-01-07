package com.ka.cert.transparency.service.test.integration;

import com.ka.cert.transparency.loader.core.CertDownloadTask;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ForkJoinPool;

/**
 * Description: Certification client operations integration test set
 * Project: cert-service
 * Package: org.cert.transparency.service.test.integration
 * Author: kakyurek
 * Date: 2018.01.06
 */
@Category(IntegrationTest.class)
public class CertClientTest {

    private static final Logger logger = LoggerFactory.getLogger(CertClientTest.class);
    private static final String logUrl = "http://ct.googleapis.com/aviator/ct/v1/";
    private static final String logDirectory = "0";

    @Test
    public void downloadCertTest() {
        int fetchSize = 5000;
        int nThreads = Runtime.getRuntime().availableProcessors();
        ForkJoinPool pool = new ForkJoinPool(nThreads);
        Long processedLogCount = pool.invoke(new CertDownloadTask(0, fetchSize, logUrl, logDirectory, false));
        logger.info("Processed Log Count: " + processedLogCount);
        Assert.assertEquals(new Long(fetchSize + 1), processedLogCount);
    }

}
