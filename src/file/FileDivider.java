package file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by tangyifeng on 2017/12/20.
 * Email: yifengtang_hust@outlook.com
 */
public class FileDivider {

    private LinkedBlockingDeque<Package> dataDeque;
    private RandomAccessFile file;

    public FileDivider(String fileName) {
        try {
            dataDeque = new LinkedBlockingDeque<>();
            file = new RandomAccessFile(fileName, "r");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private class CuttingFile implements Runnable {

        @Override
        public void run() {

        }
    }

}
