package com.ka.cert.transparency.loader.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Description: CTLog entity
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.model
 * Author: kakyurek
 * Date: 2018.01.07
 */
public class Log {

    private String description;
    private String url;

    @SerializedName("operated_by")
    private List<Integer> operators;

    private Operator operator;

    private long treeSize;

    private long offset;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Integer> getOperators() {
        return operators;
    }

    public void setOperators(List<Integer> operators) {
        this.operators = operators;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public long getTreeSize() {
        return treeSize;
    }

    public void setTreeSize(long treeSize) {
        this.treeSize = treeSize;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

}
