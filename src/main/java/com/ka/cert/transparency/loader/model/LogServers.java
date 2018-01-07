package com.ka.cert.transparency.loader.model;

import java.util.List;

/**
 * Description: LogServers entity that structurally matches https://www.gstatic.com/ct/log_list/all_logs_list.json
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.model
 * Author: kakyurek
 * Date: 2018.01.07
 */
public class LogServers {

    private List<Log> logs;
    private List<Operator> operators;

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public void setOperators(List<Operator> operators) {
        this.operators = operators;
    }

}
