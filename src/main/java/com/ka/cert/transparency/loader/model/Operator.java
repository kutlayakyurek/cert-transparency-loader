package com.ka.cert.transparency.loader.model;

/**
 * Description: CTLog operator entity that structurally matches https://www.gstatic.com/ct/log_list/all_logs_list.json
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.model
 * Author: kakyurek
 * Date: 2018.01.07
 */
public class Operator {

    private int id;
    private String name;
    private String directory;

    public Operator(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

}
