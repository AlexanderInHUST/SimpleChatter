package security;

import util.ArrayUtils;

import java.util.ArrayList;

import static security.SecurityConst.ENCRYPT_DATA_LENGTH;
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

    public byte[] encryptByPublicKey(byte[] dataBytes) {
        ArrayList<byte[]> encryptData = new ArrayList<>();
        int nums = (dataBytes.length - 1) / MAX_DATA_SEG;
        for (int i = 0; i < nums + 1; i++) {
            int length = Math.min(MAX_DATA_SEG, dataBytes.length - i * MAX_DATA_SEG);
            byte[] curBytes = new byte[length];
            System.arraycopy(dataBytes, i * MAX_DATA_SEG, curBytes, 0, length);
            encryptData.add(helper.encryptByPublicKey(curBytes));
        }
        return ArrayUtils.concatAll(encryptData);
    }

    public byte[] encryptByPrivateKey(byte[] dataBytes) {
        signatures = new ArrayList<>();
        ArrayList<byte[]> encryptData = new ArrayList<>();
        int nums = (dataBytes.length - 1) / MAX_DATA_SEG;
        for (int i = 0; i < nums + 1; i++) {
            int length = Math.min(MAX_DATA_SEG, dataBytes.length - i * MAX_DATA_SEG);
            byte[] curBytes = new byte[length];
            System.arraycopy(dataBytes, i * MAX_DATA_SEG, curBytes, 0, length);
            byte[] curEnBytes = helper.encryptByPrivateKey(curBytes);
            signatures.add(helper.sign(curEnBytes));
            encryptData.add(curEnBytes);
        }
        return ArrayUtils.concatAll(encryptData);
    }

    public byte[] decryptByPublicKey(ArrayList<String> signatures, byte[] encryptData) {
        ArrayList<byte[]> originalData = new ArrayList<>();
        int nums = (encryptData.length - 1) / ENCRYPT_DATA_LENGTH;
        for (int i = 0; i < nums + 1; i++) {
            int length = ENCRYPT_DATA_LENGTH;
            byte[] curBytes = new byte[length];
            System.arraycopy(encryptData, i * ENCRYPT_DATA_LENGTH, curBytes, 0, length);
            if (helper.verify(curBytes, signatures.get(i))) {
                originalData.add(helper.decryptByPublicKey(curBytes));
            } else {
                return null;
            }
        }
        return ArrayUtils.concatAll(originalData);
    }

    public byte[] decryptByPublicKeyWithoutSigns(byte[] encryptData) {
        ArrayList<byte[]> originalData = new ArrayList<>();
        int nums = (encryptData.length - 1) / ENCRYPT_DATA_LENGTH;
        for (int i = 0; i < nums + 1; i++) {
            int length = ENCRYPT_DATA_LENGTH;
            byte[] curBytes = new byte[length];
            System.arraycopy(encryptData, i * ENCRYPT_DATA_LENGTH, curBytes, 0, length);
            originalData.add(helper.decryptByPublicKey(curBytes));
        }
        return ArrayUtils.concatAll(originalData);
    }

    public byte[] decryptByPrivateKey(byte[] encryptData) {
        ArrayList<byte[]> originalData = new ArrayList<>();
        int nums = (encryptData.length - 1) / ENCRYPT_DATA_LENGTH;
        for (int i = 0; i < nums + 1; i++) {
            int length = ENCRYPT_DATA_LENGTH;
            byte[] curBytes = new byte[length];
            System.arraycopy(encryptData, i * ENCRYPT_DATA_LENGTH, curBytes, 0, length);
            originalData.add(helper.decryptByPrivateKey(curBytes));
        }
        return ArrayUtils.concatAll(originalData);
    }

    public static void main(String[] args) {
        SecurityGuard guard = new SecurityGuard();
        String org = "Complete!";
        byte[] encryptData = guard.encryptByPublicKey(org.getBytes());
//        ArrayList<String> signs = guard.getSignatures();
//        byte[] data = guard.decryptByPublicKey(signs, encryptData);
        System.out.println(new String(guard.decryptByPrivateKey(encryptData)));
    }

}
