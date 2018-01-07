package com.ka.cert.transparency.loader.core;

import com.ka.cert.transparency.loader.application.CertApplication;
import com.ka.cert.transparency.loader.model.Log;
import org.certificatetransparency.ctlog.comm.HttpLogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Description: Manages ctlog scheduled processes
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.util
 * Author: kakyurek
 * Date: 2018.01.07
 */
@Component
public class JobScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CertApplication.class);
    private static final ForkJoinPool processor = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    private LogManager logManager;

    @Autowired
    public JobScheduler(LogManager logManager) {
        this.logManager = logManager;
    }

    /**
     * Executes new jobs regularly if there is not active job processed
     */
    @Scheduled(cron = "${schedule.execute.jobs}", zone = "${schedule.zone}")
    public void executeJobs() {
        List<Log> logList = LogCache.getLogs();
        List<CertDownloadTask> taskList = new ArrayList<>();

        // Executing each logs processor sequentially
        logList.forEach((Log l) -> {
            HttpLogClient client = new HttpLogClient(l.getUrl());
            long previousTreeSize = l.getOffset();
            long refreshedTreeSize = client.getLogSTH().treeSize;
            long lastIndex = refreshedTreeSize - 1;
            if (previousTreeSize != lastIndex) {
                l.setOffset(lastIndex);
                long result = processor.invoke(new CertDownloadTask(previousTreeSize, lastIndex, l.getUrl(), l.getOperator().getDirectory(), true));
            }
        });
    }

    /**
     * Refreshes operator and their log list regularly
     */
    @Scheduled(cron = "${schedule.refresh.servers}", zone = "${schedule.zone}")
    public void updateLogServers() {
        logger.info("Updating log server cache..");
        logManager.updateLogServers();
        logger.info("Log servers cache updated..");
    }

}
