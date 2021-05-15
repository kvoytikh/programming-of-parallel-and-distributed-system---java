package lab.consumer_producer;

import static java.lang.System.exit;

public class Producer extends Thread{
    private final Data data;
    private int max;
    private int start;

    public Producer(Data data) {
        this.data = data;
        max = data.getRepeat();
        start = 0;
    }

    @Override
    public void run() {
        start++;

        while (start < data.getRepeat()) {
            try {
                data.add((int) (Math.random() * data.getLength()) + 1);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            start++;
        }
        //exit(0);
    }
}