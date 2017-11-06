package util;

import java.net.InetAddress;

/**
 * Created by tangyifeng on 2017/11/6.
 * Email: yifengtang_hust@outlook.com
 */
public class WebUtil {

    public static String getLocalHostIP() {
        String ip;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch(Exception ex) {
            ip = "";
        }
        return ip;
    }
}
