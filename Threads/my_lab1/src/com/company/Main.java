package com.company;

import java.util.Date;
import java.util.Vector;

class ThreadCalc extends  Thread {
    private Vector<Double> vect;
    private int startId;
    private int endId;

    private double minValue;
    private double maxValue;

    public double getMinValue() {
        return minValue;
    }
    public double getMaxValue() {
        return maxValue;
    }

    ThreadCalc(Vector<Double> vect, int startId, int endId) {
        this.vect = vect;
        this.startId = startId;
        this.endId = endId;
        this.minValue = vect.get(startId);
        this.maxValue = vect.get(startId);
    }

    @Override
    public void run() {
        for(int i = startId; i < endId; i++) {
            if(vect.get(i) > maxValue) maxValue = vect.get(i);
            if(vect.get(i) < minValue) { minValue = vect.get(i); }
        }
    }
}

public class Main {
    public static int SIZE = 1000;
    public static final int numCores = 10;

    public static void main(String[] args) throws InterruptedException{
        Vector<Double> vect = new Vector(SIZE);
        int step = SIZE / numCores;

        Date date = new Date();

        for(int i = 0; i < SIZE; i++) {
            vect.add(i, Math.random() * 1000);
            //System.out.print(" " + vect.get(i));
        }
        double max = vect.get(0);
        double min = vect.get(0);

        for(int i = 0; i < SIZE; i++) {
            if(vect.get(i) > max) max = vect.get(i);
            if(vect.get(i) < min) min = vect.get(i);
        }


        System.out.println("\nПослідовний: ");
        System.out.println("max value: " + max);
        System.out.println("min value: " + min);


        ThreadCalc[] threadCalcs = new ThreadCalc[numCores];

        for (int i = 0; i < numCores; i++) {
            threadCalcs[i] = new ThreadCalc(vect, step * i, step*i + step);
            threadCalcs[i].start();
        }
       for(int i = 0; i < numCores; i++) {
            threadCalcs[i].join();

        }
        double parallelMinRes = threadCalcs[0].getMaxValue();
        double parallelMaxRes = threadCalcs[0].getMinValue();

        for(int i = 1; i < numCores; i++) {
             if (threadCalcs[i].getMaxValue() > parallelMaxRes) {
                 parallelMaxRes = threadCalcs[i].getMaxValue();
             }
             if (threadCalcs[i].getMinValue() < parallelMinRes) {
                 parallelMinRes = threadCalcs[i].getMinValue();
             }

        }

        System.out.println("Послідовний: ");
        System.out.println(parallelMaxRes);
        System.out.println(parallelMinRes);
    }
}
