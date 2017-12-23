package file;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by tangyifeng on 2017/12/19.
 * Email: yifengtang_hust@outlook.com
 */
public class Package {

    private long pid;
    private int length;
    private long hashcode;
    private byte[] data;

    Package() {}

    Package(long pid, byte[] data) {
        this.pid = pid;
        this.data = data;
        this.length = data.length;
        this.hashcode = generateHashcode(this);
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public static Package buildPackage(byte[] bytes) throws PackageBrokenException{
        if (bytes == null) {
            throw new PackageBrokenException("Package found broken because of null input bytes");
        }
        Package pack = new Package();
        ByteBuffer buffer = ByteBuffer.allocate(20);
        if (bytes.length < 20) {
            throw new PackageBrokenException("Package found broken because of missing basic information.");
        }
        buffer.put(Arrays.copyOfRange(bytes, 0, 20));
        buffer.flip();
        pack.pid = buffer.getLong();
        pack.length = buffer.getInt();
        pack.hashcode = buffer.getLong();
        if (pack.pid < 0 || pack.length < 0) {
            throw new PackageBrokenException("Package found broken because of basic information.");
        }
        if (bytes.length != 20 + pack.length) {
            throw new PackageBrokenException("Package found broken because of missing data.");
        }
        pack.data = Arrays.copyOfRange(bytes, 20, bytes.length);
        if (!checkPackage(pack)) {
            throw new PackageBrokenException("Package found broken because of bad data.");
        }
        return pack;
    }

    public byte[] toByte() {
        ByteBuffer buffer = ByteBuffer.allocate(20 + length);
        buffer.putLong(pid);
        buffer.putInt(length);
        buffer.putLong(hashcode);
        buffer.put(data);
        return buffer.array();
    }

    private static boolean checkPackage(Package pack) {
        return pack.hashcode == generateHashcode(pack);
    }

    private static long generateHashcode(Package pack) {
        return Arrays.hashCode(pack.data) + pack.pid + pack.length;
    }
}
