package lab.CPU;

import java.util.ArrayList;
import java.util.List;

public class Process {
    private int time;
    private int name;
    private List<Process> processList = new ArrayList<>();
    static volatile int counter;
    public Process() {

    }
    public Process(int min, int max) {
        this.time = generateRandomNumber(min, max);
        name = counter++;
    }

    public int generateRandomNumber(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1)) * 10;
    }

    public int getTime() {
        return time;
    }

    public synchronized void addProcessToList(Process process) {
        processList.add(process);
    }
    public int getSize() {
        return processList.size();
    }

    public synchronized Process removeProcessFromQueue(int process) {
        return processList.remove(process);
    }
    @Override
    public String toString() {
        return "Process{" +
                "name=" + name +
                '}';
    }

}
