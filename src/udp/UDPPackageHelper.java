package udp;

import com.sun.istack.internal.Nullable;
import util.ArrayUtils;
import util.BitUtil;

import java.util.ArrayList;
import java.util.Arrays;

import static util.Const.PACKAGE_LEN;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class UDPPackageHelper {

    public byte[] composeDataUDPPackage(ArrayList<UDPPackage> packsArray) {
        ArrayList<byte[]> bytes = new ArrayList<>();
        for (UDPPackage pack : packsArray) {
            bytes.add(pack.getData());
        }
        return ArrayUtils.concatAll(bytes);
    }

    public ArrayList<UDPPackage> cutDataUDPPackage(byte[] data) {
        ArrayList<UDPPackage> packsArray = new ArrayList<>();
        int nums = (data.length - 1) / PACKAGE_LEN;
        for (int i = 0; i < nums + 1; i++) {
            int length = Math.min(PACKAGE_LEN, data.length - i * PACKAGE_LEN);
            byte[] curBytes = new byte[length];
            System.arraycopy(data, i * PACKAGE_LEN, curBytes, 0, length);
            packsArray.add(createDataUDPPackage(i, curBytes));
        }
        return packsArray;
    }

    public ArrayList<UDPPackage> getAckPackage(int seqnum) {
        ArrayList<UDPPackage> packsArray = new ArrayList<>();
        packsArray.add(createAckUDPPackage(seqnum));
        return packsArray;
    }

    public ArrayList<UDPPackage> getHelloPackage(int seqnum, int myport) {
        ArrayList<UDPPackage> packsArray = new ArrayList<>();
        packsArray.add(createHelloUDPPackage(seqnum, myport));
        return packsArray;
    }

    public ArrayList<UDPPackage> getGoodbyePackage(int seqnum) {
        ArrayList<UDPPackage> packsArray = new ArrayList<>();
        packsArray.add(createGoodbyeUDPPackage(seqnum));
        return packsArray;
    }

    public boolean checkUDPPackage(UDPPackage pack) {
        int hashCode = calHashcode(pack);
        boolean cor = calCor(pack);
        return (hashCode == pack.getHashcode()) && (cor == pack.isCor());
    }

    private UDPPackage createDataUDPPackage(int seqNum, byte[] data) {
        UDPPackage pack = new UDPPackage();
        pack.setSeqNum(seqNum);
        pack.setData(data);
        pack.setAck(false);
        pack.setHello(false);
        pack.setGoodbye(false);
        pack.setHashcode(calHashcode(pack));
        pack.setCor(calCor(pack));
        return pack;
    }

    private UDPPackage createAckUDPPackage(int seqNum) {
        UDPPackage pack = new UDPPackage();
        pack.setSeqNum(seqNum);
        pack.setData(new byte[]{});
        pack.setAck(true);
        pack.setHello(false);
        pack.setGoodbye(false);
        pack.setHashcode(calHashcode(pack));
        pack.setCor(calCor(pack));
        return pack;
    }

    private UDPPackage createHelloUDPPackage(int seqNum, int myPort) {
        UDPPackage pack = new UDPPackage();
        pack.setSeqNum(seqNum);
        pack.setData(BitUtil.toByteArray(myPort));
        pack.setAck(false);
        pack.setHello(true);
        pack.setGoodbye(false);
        pack.setHashcode(calHashcode(pack));
        pack.setCor(calCor(pack));
        return pack;
    }

    private UDPPackage createGoodbyeUDPPackage(int seqNum) {
        UDPPackage pack = new UDPPackage();
        pack.setSeqNum(seqNum);
        pack.setData(new byte[]{});
        pack.setAck(false);
        pack.setHello(false);
        pack.setGoodbye(true);
        pack.setHashcode(calHashcode(pack));
        pack.setCor(calCor(pack));
        return pack;
    }

    private int calHashcode(UDPPackage udpPackage) {
        return udpPackage.getSeqNum() + Arrays.hashCode(udpPackage.getData()) + (udpPackage.isAck() ? 1 : 0)
                + (udpPackage.isHello() ? 1 : 0) + (udpPackage.isGoodbye() ? 1 : 0);
    }

    private boolean calCor(UDPPackage udpPackage) {
        int cor = udpPackage.getSeqNum();
        for (byte b : udpPackage.getData()) {
            cor &= b;
        }
        cor &= udpPackage.getHashcode();
        return (cor & 1) == 1;
    }
    public static void main(String[] args) {
        UDPPackageHelper helper = new UDPPackageHelper();
        byte[] test = "wtgrwegeijgojfdsobjrtelkglfdsakewff".getBytes();
        ArrayList<UDPPackage> packages = helper.cutDataUDPPackage(Integer.toString(Integer.MAX_VALUE).getBytes());
        System.out.println(helper.checkUDPPackage(packages.get(0)));
        String string = new String(helper.composeDataUDPPackage(packages));
        System.out.println(string);
    }
}
