package security;

/**
 * Created by tangyifeng on 2017/10/29.
 * Email: yifengtang_hust@outlook.com
 */
public class SecurityConst {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    public static final int KEY_LENGTH = 1024;
    public static final int MAX_DATA_SEG = 117;

    public static final int SERVER_RSA = 1;
    public static final int CLIENT_RSA = 2;

}
