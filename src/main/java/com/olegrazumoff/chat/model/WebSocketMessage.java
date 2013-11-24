package com.olegrazumoff.chat.model;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 20.11.13
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketMessage {
    final static public String REGISTER = "register";
    final static public String ADDUSER = "adduser";
    final static public String REMOVEUSER = "removeuser";
    final static public String MESSAGE = "message";
    final static public String ERROR = "error";

    private String type;
    private String data;

    public WebSocketMessage(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
