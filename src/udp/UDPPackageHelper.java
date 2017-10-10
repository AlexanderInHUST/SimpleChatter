package udp;

import com.sun.istack.internal.Nullable;
import util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static util.Const.PACKAGE_LEN;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class UDPPackageHelper {

    public byte[] composeUDPPackage(ArrayList<UDPPackage> packsArray) {
        ArrayList<byte[]> bytes = new ArrayList<>();
        for (UDPPackage pack : packsArray) {
            bytes.add(pack.getData());
        }
        return ArrayUtils.concatAll(bytes);
    }

    public ArrayList<UDPPackage> cutUDPPackage(byte[] data, boolean isAck) {
        ArrayList<UDPPackage> packsArray = new ArrayList<>();
        if (!isAck) {
            int nums = data.length / PACKAGE_LEN;
            for (int i = 0; i < nums + 1; i++) {
                int length = Math.min((i + 1) * PACKAGE_LEN, data.length);
                byte[] curBytes = new byte[length];
                System.arraycopy(data, i * PACKAGE_LEN, curBytes, 0, length);
                packsArray.add(createUDPPackage(i, curBytes, false));
            }
        } else {
            packsArray.add(createUDPPackage(0, data,true)); // Data is ack_seq in String
        }
        return packsArray;
    }

    public boolean checkUDPPackage(UDPPackage pack) {
        int hashCode = calHashcode(pack);
        boolean cor = calCor(pack);
        return (hashCode == pack.getHashcode()) && (cor == pack.isCor());
    }

    private UDPPackage createUDPPackage(int seqNum, byte[] data, boolean isAck) {
        UDPPackage pack = new UDPPackage();
        pack.setSeqNum(seqNum);
        pack.setData(data);
        pack.setAck(isAck);
        pack.setHashcode(calHashcode(pack));
        pack.setCor(calCor(pack));
        return pack;
    }

    private int calHashcode(UDPPackage udpPackage) {
        return udpPackage.getSeqNum() + Arrays.hashCode(udpPackage.getData()) + (udpPackage.isAck() ? 1 : 0);
    }

    private boolean calCor(UDPPackage udpPackage) {
        int cor = udpPackage.getSeqNum();
        for (byte b : udpPackage.getData()) {
            cor &= b;
        }
        cor &= udpPackage.getHashcode();
        return (cor & 1) == 1;
    }
//    public static void main(String[] args) {
//        UDPPackageHelper helper = new UDPPackageHelper();
//        byte[] test = "wtgrwegeijgojfdsobjrtelkglfdsakewff".getBytes();
//        ArrayList<UDPPackage> packages = helper.cutUDPPackage(Integer.toString(Integer.MAX_VALUE).getBytes(), true);
//        System.out.println(helper.checkUDPPackage(packages.get(0)));
//        String string = new String(helper.composeUDPPackage(packages));
//        System.out.println(string);
//    }
}
