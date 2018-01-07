package com.ka.cert.transparency.loader.core;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.ka.cert.transparency.loader.model.Log;
import com.ka.cert.transparency.loader.model.LogServers;
import org.certificatetransparency.ctlog.comm.HttpLogClient;
import org.certificatetransparency.ctlog.comm.LogCommunicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.application
 * Author: kakyurek
 * Date: 2018.01.07
 */
@Component
public class LogManager {

    private static final Logger logger = LoggerFactory.getLogger(LogManager.class);
    private static final Gson gson = new Gson();
    private static final String PROFILE_DEV = "dev";

    @Value("${log.servers.file}")
    private String logServersFile;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public void updateLogServers() {
        Gson gson = new Gson();
        JsonReader reader;
        FileReader fileReader;

        //If development profile is activated, log-servers file is read from classpath. Otherwise it is read from external directory jar file is reside on
        try {
            fileReader = activeProfile != null && activeProfile.equals(PROFILE_DEV) ? new FileReader(new ClassPathResource(logServersFile).getFile()) : new FileReader(logServersFile);
            reader = new JsonReader(fileReader);
        } catch (IOException e) {
            logger.info("There is no server list file. Create appropriate file with name 'log-servers.json'");
            return;
        }

        // Fetching log endpoints and operators from file
        LogServers logs = gson.fromJson(reader, LogServers.class);
        List<Log> availableLogs = new ArrayList<>();

        // Caching logs, their operators and creating necessary folders for output
        logs.getLogs().forEach(l -> {
            try {
                HttpLogClient client = new HttpLogClient(l.getUrl());
                l.setTreeSize(client.getLogSTH().treeSize);
                availableLogs.add(l);
            } catch (LogCommunicationException ex) {
                logger.info("Can not fetch information of log at " + l.getUrl());
                logger.debug("Exception Details: " + ex.getMessage());
            }
        });

        // Updating cache
        LogCache.updateCache(availableLogs, logs.getOperators());
    }

}
