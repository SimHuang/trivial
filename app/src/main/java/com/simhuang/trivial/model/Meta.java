package com.simhuang.trivial.model;

import java.util.List;

public class Meta {

    private String type;
    private String code;
    private List<String> messages;

    public Meta(String type, String code, List<String> messages) {
        this.type = type;
        this.code = code;
        this.messages = messages;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
