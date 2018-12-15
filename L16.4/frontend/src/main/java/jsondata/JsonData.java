package jsondata;

import java.io.Serializable;

public interface JsonData<T> extends Serializable {
    CommandEnum getCommand();

    T getUserData();

}
