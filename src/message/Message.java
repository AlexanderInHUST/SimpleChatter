package message;

import java.io.Serializable;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class Message implements Serializable {

    private int kind;
    private String data;

    public Message() {}

    public Message(int kind, String data) {
        setKind(kind);
        setData(data);
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
