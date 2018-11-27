package ws.jsondata;

import base.UserDataSet;

import java.io.Serializable;

public class CommandInfo implements Serializable {
    private String method;
    private UserDataSet userData;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public UserDataSet getUserData() {
        return userData;
    }

    public void setUserData(UserDataSet userData) {
        this.userData = userData;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    private long userId;
}
