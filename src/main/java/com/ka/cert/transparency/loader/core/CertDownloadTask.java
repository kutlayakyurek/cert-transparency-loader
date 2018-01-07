package com.ka.cert.transparency.loader.core;

import com.ka.cert.transparency.loader.util.CertificateUtils;
import org.certificatetransparency.ctlog.ParsedLogEntry;
import org.certificatetransparency.ctlog.TimestampedEntry;
import org.certificatetransparency.ctlog.comm.HttpLogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.cert.CertificateException;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Description: Certificate downloader and processor. Downloads and persists ctlog merkle tree leafs
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.application
 * Author: kakyurek
 * Date: 2018.01.06
 */
public class CertDownloadTask extends RecursiveTask<Long> {

    private static final Logger logger = LoggerFactory.getLogger(CertDownloadTask.class);
    private static final int processLimit = 1000;

    private long offset;
    private long size;
    private String url;
    private String directory;
    private boolean saveCert;

    public CertDownloadTask(long offset, long size, String url, String directory, boolean saveCert) {
        this.offset = offset;
        this.size = size;
        this.url = url;
        this.directory = directory;
        this.saveCert = saveCert;
    }

    /**
     * Fetches and saves certificates with divide and conquer strategy
     *
     * @return Processed certificate amount
     */
    @Override
    protected Long compute() {
        if (size - offset <= processLimit) {
            HttpLogClient client = new HttpLogClient(url);
            logger.info("Downloading logs from " + offset + " to " + size);
            List<ParsedLogEntry> list = client.getLogEntries(offset, size);
            list.forEach(l -> {
                try {
                    TimestampedEntry te = l.getMerkleTreeLeaf().timestampedEntry;
                    CertificateUtils.saveCertificateFileFromBytes(te.signedEntry.x509, te.timestamp, directory);
                } catch (CertificateException e) {
                    logger.error("Error thrown while trying to save certificate file.\n" + e.getMessage());
                }
            });
            return (long) list.size();
        } else {
            long mid = offset + (size - offset) / 2;
            CertDownloadTask left = new CertDownloadTask(offset, mid, url, directory, saveCert);
            CertDownloadTask right = new CertDownloadTask(mid + 1, size, url, directory, saveCert);
            left.fork();
            long rightProcessCount = right.compute();
            long leftProcessCount = left.join();
            return rightProcessCount + leftProcessCount;
        }
    }

}
