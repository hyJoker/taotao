package com.taotao.common.pojo;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable {
    private long id;
    private String text;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
