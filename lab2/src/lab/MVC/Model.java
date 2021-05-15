package lab.MVC;

import lab.CPU.CPU;
import lab.CPU.ProcessFlow;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Model {
    ProcessFlow process = new ProcessFlow(20, 20);
    private Queue<Thread> queue = new ConcurrentLinkedQueue<>();

    CPU cpu1, cpu2, cpu3;

    public void createServiceSystem() {
        cpu1 = createCPU("CPU1", process);
        cpu2 = createCPU("CPU2", process);
        cpu3 = createCPU("CPU3", process);
        
        Thread p = new Thread(process);

        Thread c1 = new Thread(cpu1);
        Thread c2 = new Thread(cpu2);
        Thread c3 = new Thread(cpu3);
        queue.add(c1);
        queue.add(c2);
        queue.add(c3);


        Iterator<Thread> iter = queue.iterator();
        while (iter.hasNext()) {
            iter.next().start();
        }
        p.start();

        try {
            p.join();
            c1.join();
            c2.join();
            c3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("CPU1 ->" + cpu1.getWorkedProcesses());
        System.out.println("CPU2 ->" + cpu2.getWorkedProcesses());
        System.out.println("CPU3 ->" + cpu3.getWorkedProcesses());
    }

    public CPU createCPU(String CPU_name, ProcessFlow process) {
        CPU cpu = new CPU(CPU_name, process);
        return cpu;
    }
}
