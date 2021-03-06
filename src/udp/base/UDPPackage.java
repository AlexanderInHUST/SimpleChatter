package udp.base;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class UDPPackage implements Serializable {

    private int seqNum;
    private byte[] data;
    private boolean ack;
    private boolean hello;
    private boolean goodbye;

    public boolean isHello() {
        return hello;
    }

    public void setHello(boolean hello) {
        this.hello = hello;
    }

    public boolean isGoodbye() {
        return goodbye;
    }

    public void setGoodbye(boolean goodbye) {
        this.goodbye = goodbye;
    }

    private int hashcode;
    private boolean cor;

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(int seqNum) {
        this.seqNum = seqNum;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getHashcode() {
        return hashcode;
    }

    public void setHashcode(int hashcode) {
        this.hashcode = hashcode;
    }

    public boolean isCor() {
        return cor;
    }

    public void setCor(boolean cor) {
        this.cor = cor;
    }
}
