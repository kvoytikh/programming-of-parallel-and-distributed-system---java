package lab.consumer_producer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer extends Thread{
    private final Data data;
    private int max;
    private int start;

    public Consumer(Data data) {
        this.data = data;
        max = data.getRepeat();
        start = 0;
    }

    @Override
    public void run() {
        start++;
        while (start < data.getRepeat()) {
            try {
                data.remove((int) (Math.random() * data.getLength()) + 1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
