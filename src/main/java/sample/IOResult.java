package sample;

/**
 * Created by timurg
 * on 31.08.2021
 * project name TGText
 */

public class IOResult<T> {
    private T data;
    private boolean ok;

    public IOResult(boolean ok, T data) {
        this.ok = ok;
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public boolean hasData() {
        return data != null;
    }

    public T getData() {
        return data;
    }
}
