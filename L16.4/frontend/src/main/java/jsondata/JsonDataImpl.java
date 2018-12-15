package jsondata;

import base.DataSet;
import base.UserDataSet;

public class JsonDataImpl<T extends DataSet> implements JsonData {

    private T userData;
    private CommandEnum method;

    public void setData(T data) {
        this.userData = data;
    }

    public void setMethod(String method) {
        this.method = CommandEnum.valueOf(method);
    }
    public void setMethod(CommandEnum method) {
        this.method = method;
    }

    public JsonDataImpl(CommandEnum method, T userData) {
        this.method = method;
        this.userData = userData;
    }

    @Override
    public CommandEnum getCommand() {
        return method;
    }

    @Override
    public T getUserData() {
        return (T) userData;
    }
}
