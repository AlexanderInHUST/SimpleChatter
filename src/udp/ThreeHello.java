package udp;

import util.Timer;

/**
 * Created by tangyifeng on 2017/10/12.
 * Email: yifengtang_hust@outlook.com
 */
public class ThreeHello {

    private int whoIAm, state;
    private int sendCount;
    private Timer timer;

    public ThreeHello(int whoIAm) {
        this.whoIAm = whoIAm;
    }

    public void start() {

    }

    private boolean startAsSender() {
        while (true) {
            switch (state) {
                case 0: {
                    sendCount = 0;
                    state = 1;
                    break;
                }
                case 1: {

                }
            }
        }
    }

}
