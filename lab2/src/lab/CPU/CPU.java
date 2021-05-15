package lab.CPU;

import lab.MVC.View;

public class CPU implements Runnable {
    static View view = new View();
    private String name;
    private int operationTime;
    private ProcessFlow flow;
    private int workedProcesses;

    public int getWorkedProcesses() {
        return workedProcesses;
    }

    public CPU(String name, ProcessFlow flow) {
        this.name = name;
        this.flow = flow;
        workedProcesses = 0;
        operationTime = generateRandOperTime(1, 10);//generates initial random operation time of CPU
    }

    public int generateRandOperTime(int min, int max) {
        int number = (int) (min + Math.random() * (max - min + 1)) * 100;
        return number;
    }

    @Override
    public void run() {
        operateFlow();
    }

    public void operateFlow() {
        ProcessFlow processFlow = flow;
        int index = 0;
        Process listo = processFlow.getList();

        while (!processFlow.isFlag() || listo.getSize() != 0) {
            if (listo.getSize() == 0) {
                waiting();
            } else {
                operating(listo, index);
                //index++;
            }
            if(listo.getSize() == 0) {
                index++;
            }
        }
    }

    /**
     * removes the process from the queue and increments the number of served processes
     */
    public void operating(Process list, int index) {
        try {
            view.printMessage(getName() + "\t" + View.HANDLING_PROCESS + operationTime
                    + "\t" + list.removeProcessFromQueue(index).toString());
            workedProcesses++;
            Thread.sleep(generateRandOperTime(1, 5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * waits during 10 ms
     */
    public void waiting() {
        try {
            view.printMessage(getName() + View.WAITING_FOR_PROCESS);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getName() {
        return name;
    }
}
