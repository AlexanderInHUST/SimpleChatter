package security;

import util.ArrayUtils;

import java.util.ArrayList;

import static security.SecurityConst.MAX_DATA_SEG;
import static security.SecurityConst.SERVER_RSA;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class SecurityGuard {

    private KeyPairHelper helper;
    private ArrayList<String> signatures;

    public ArrayList<String> getSignatures() {
        return signatures;
    }

    public SecurityGuard() {
        helper = new KeyPairHelper();
        helper.createKeys();
    }

    public SecurityGuard(String key, int who) {
        helper = new KeyPairHelper();
        if (who == SERVER_RSA) {
            helper.setPublicKey(key);
        } else {
            helper.setPrivateKey(key);
        }
    }

    public String getPrivateKey() {
        return helper.getPrivateKey();
    }

    public String getPublicKey() {
        return helper.getPublicKey();
    }

    public void setPrivateKey(String privateKey) {
        helper.setPrivateKey(privateKey);
    }

    public void setPublic(String publicKey) {
        helper.setPublicKey(publicKey);
    }

    public ArrayList<byte[]> encryptByPublicKey(String data) {
        ArrayList<byte[]> encryptData = new ArrayList<>();
        byte[] dataBytes = data.getBytes();
        int nums = (dataBytes.length - 1) / MAX_DATA_SEG;
        for (int i = 0; i < nums + 1; i++) {
            int length = Math.min(MAX_DATA_SEG, dataBytes.length - i * MAX_DATA_SEG);
            byte[] curBytes = new byte[length];
            System.arraycopy(dataBytes, i * MAX_DATA_SEG, curBytes, 0, length);
            encryptData.add(helper.encryptByPublicKey(curBytes));
        }
        return encryptData;
    }

    public ArrayList<byte[]> encryptByPrivateKey(String data) {
        signatures = new ArrayList<>();
        ArrayList<byte[]> encryptData = new ArrayList<>();
        byte[] dataBytes = data.getBytes();
        int nums = (dataBytes.length - 1) / MAX_DATA_SEG;
        for (int i = 0; i < nums + 1; i++) {
            int length = Math.min(MAX_DATA_SEG, dataBytes.length - i * MAX_DATA_SEG);
            byte[] curBytes = new byte[length];
            System.arraycopy(dataBytes, i * MAX_DATA_SEG, curBytes, 0, length);
            byte[] curEnBytes = helper.encryptByPrivateKey(curBytes);
            signatures.add(helper.sign(curEnBytes));
            encryptData.add(curEnBytes);
        }
        return encryptData;
    }

    public byte[] decryptByPublicKey(ArrayList<String> signatures, ArrayList<byte[]> encryptData) {
        ArrayList<byte[]> originalData = new ArrayList<>();
        for (int i = 0; i < signatures.size(); i++) {
            if (helper.verify(encryptData.get(i), signatures.get(i))) {
                originalData.add(helper.decryptByPublicKey(encryptData.get(i)));
            } else {
                return null;
            }
        }
        return ArrayUtils.concatAll(originalData);
    }

    public byte[] decryptByPrivateKey(ArrayList<byte[]> encryptData) {
        ArrayList<byte[]> originalData = new ArrayList<>();
        for (int i = 0; i < encryptData.size(); i++) {
            originalData.add(helper.decryptByPrivateKey(encryptData.get(i)));
        }
        return ArrayUtils.concatAll(originalData);
    }

    public static void main(String[] args) {
        SecurityGuard guard = new SecurityGuard();
        String org = "hello world!";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            builder.append(org);
        }
        ArrayList<byte[]> encryptData = guard.encryptByPrivateKey(builder.toString());
        ArrayList<String> signs = guard.getSignatures();
        byte[] data = guard.decryptByPublicKey(signs, encryptData);
        System.out.println(new String(data));
    }

}
