package message;

import java.util.ArrayList;

/**
 * Created by tangyifeng on 2017/10/30.
 * Email: yifengtang_hust@outlook.com
 */
public class MessageCoder {

    public static String encode(ArrayList<String> strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
            builder.append(";");
        }
        return builder.toString();
    }

    public static ArrayList<String> decode(String string) {
        String strings[] = string.split(";");
        ArrayList<String> result = new ArrayList<>();
        for (String s : strings) {
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }

}
