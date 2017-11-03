package message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class Message implements Serializable {

    private int kind;
    private byte[] data;
    private ArrayList<String> signs;
    private String fromWho;
    private String toWho;

    public Message() {
        signs = null;
    }

    public Message(int kind, String data) {
        setKind(kind);
        setData(data);
        signs = null;
    }

    public int getKind() {
        return kind;
    }

    public String getFromWho() {
        return fromWho;
    }

    public void setFromWho(String fromWho) {
        this.fromWho = fromWho;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setData(String data) {
        this.data = data.getBytes();
    }

    public ArrayList<String> getSigns() {
        return signs;
    }

    public void setSigns(ArrayList<String> signs) {
        this.signs = signs;
    }
}
