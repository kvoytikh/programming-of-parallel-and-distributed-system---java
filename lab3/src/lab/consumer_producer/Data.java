package lab.consumer_producer;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Data {
    private final int[] data;
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore semProducer, semConsumer;
    private int max;
    private int repeat;

    public Data(int max, int repeat) throws IOException {
        this.repeat = repeat;
        this.max = max;
        data = new int[max];
        semProducer = new Semaphore(max);
        semConsumer = new Semaphore(0);
    }

    public int getRepeat() {
        return max;
    }

    public int getLength() {
        return data.length;
    }

    public void add(int number) throws InterruptedException {

        semProducer.acquire();

        mutex.acquire();
        System.out.println("trying to add a product");
        int i = 0;
        while (data[i] != '\0') {
            i++;
        }
        data[i] = number;

        int permits = semConsumer.availablePermits() + 1;

        System.out.println("data added in " + i + " " + Arrays.toString(data)
                + " Resources consumer " + permits
                + " Resources producer  " + semProducer.availablePermits());
        mutex.release();

        semConsumer.release();
    }

    public void remove(int numberElements) throws InterruptedException {
        semConsumer.acquire(numberElements);
        mutex.acquire();
        System.out.println("remove: num-elem=" + numberElements);

        int consumed=0;
        for (int i = 0; consumed < numberElements; i++) {
            if (data[i] != 0) {
                data[i] = 0;
                consumed++;
            }
        }
        System.out.println(
                " Retired " + numberElements + " " + Arrays.toString(data)  );
        mutex.release();
        semProducer.release(numberElements);
    }
}