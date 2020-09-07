package com.kpi;


class SumThread implements Runnable  {
    private int[] array;
    private  int startId;
    private int endId;



    private  int result = 0;

    SumThread(int[] array, int startId, int endId) {
        this.array = array;
        this.startId = startId;
        this.endId = endId;
    }

    @Override
    public void run() {
        for (int i = startId; i < endId; i++)
        {
            result += array[i];
        }
    }

    public int getResult() {
        return result;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }
}

public class Main {
    public static final int numCores = 10;
    public static final int[] arr  = new int[10000];

    public static void main(String[] args) throws InterruptedException {
        Arrays.fill(arr, 2);
        int step = arr.length / numCores;

        {
            //time start
            SumThread test = new SumThread(arr, 0, arr.length);
            test.run();
        }
        //time end


        //time start
        int sum = 0;

        {
            SumThread[] sumThreads = new SumThread[numCores];

            for (int i = 0; i < numCores; i++)
            {
                sumThreads[i] = new SumThread(arr, step *i, step*i + step);
            }
            sumThreads[sumThreads.length - 1].setEndId(arr.length);
            Thread[] threads = new Thread[numCores];
            for (int i = 0; i < numCores; i++) {
                threads[i] = new Thread(sumThreads[i]);
                threads[i].start();
            }

            for (int i = 0; i < numCores; i++) {
                threads[i].join();
                sum += sumThreads[i].getResult();
            }
        }

        //time end

        System.out.println("Sum " + sum);
    }
}