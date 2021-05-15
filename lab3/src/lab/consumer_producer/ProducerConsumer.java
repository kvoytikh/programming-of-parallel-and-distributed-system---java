package lab.consumer_producer;

import java.io.IOException;

import static java.lang.System.exit;

public class ProducerConsumer {
    public static void main(String[] args) throws IOException {
        final int MAX = 4;
        final int REPEAT = 1000000 * MAX;

        Data data = new Data(MAX, REPEAT);
        Consumer consumer = new Consumer(data);
        Producer producer = new Producer(data);

        consumer.start();
        producer.start();

    }
}
