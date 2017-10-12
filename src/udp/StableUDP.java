package udp;

import java.io.*;
import java.net.Socket;

/**
 * Created by tangyifeng on 2017/10/10.
 * Email: yifengtang_hust@outlook.com
 */
public class StableUDP {

    public static void main(String[] args) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject("shit");
            stream.flush();
            stream.close();
//            System.out.println(new String(outputStream.toByteArray()));

            ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
            String i = (String) input.readObject();
            input.close();
            System.out.println(i);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // java.io.StreamCorruptedException
        }
    }

}
