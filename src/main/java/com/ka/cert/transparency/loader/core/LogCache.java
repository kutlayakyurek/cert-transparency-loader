package com.ka.cert.transparency.loader.core;

import com.ka.cert.transparency.loader.model.Log;
import com.ka.cert.transparency.loader.model.Operator;
import com.ka.cert.transparency.loader.util.CertificateUtils;
import com.ka.cert.transparency.loader.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: CTLog in memory cache for log servers and operators
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.application
 * Author: kakyurek
 * Date: 2018.01.07
 */
public class LogCache {

    private static final Logger logger = LoggerFactory.getLogger(LogCache.class);
    private static final HashMap<String, Log> logCache = new HashMap<>();

    private LogCache() {
    }

    public static Log getLog(String url) {
        return logCache.get(url);
    }

    /**
     * Get all logs in cache as List
     *
     * @return List of logs
     */
    static List<Log> getLogs() {
        List<Log> logList = new ArrayList<>();
        for (Map.Entry<String, Log> l : logCache.entrySet()) {
            logList.add(l.getValue());
        }
        return logList;
    }

    /**
     * Updates cache regularly according to scheduler settings in properties file
     *
     * @param logs      Certificate transparency logs
     * @param operators Log operators
     */
    static void updateCache(List<Log> logs, List<Operator> operators) {
        Map<Integer, Operator> operatorMap = new HashMap<>();
        operators.forEach(o -> {
            String directoryPath = String.valueOf(o.getId());
            FileUtils.createDirectoryIfNotExists(directoryPath);
            FileUtils.createDirectoryIfNotExists(directoryPath.concat(CertificateUtils.FOLDER_VALID));
            FileUtils.createDirectoryIfNotExists(directoryPath.concat(CertificateUtils.FOLDER_INVALID));
            o.setDirectory(directoryPath);
            operatorMap.put(o.getId(), o);
        });
        logs.forEach(l -> {
            if (!logCache.containsKey(l.getUrl())) {
                assert l.getOperators().size() > 0;
                l.setOperator(operatorMap.get(l.getOperators().get(0)));
                logCache.put(l.getUrl(), l);
            }
        });
    }

}
