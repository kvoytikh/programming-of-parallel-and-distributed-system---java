package lab.CPU;


import lab.MVC.View;


public class ProcessFlow implements Runnable {
    private int processNumber;
    private boolean flag;
    private Process list = new Process();

    public boolean isFlag() {
        return flag;
    }

    public Process getList() {
        return list;
    }


    static View view = new View();

    public ProcessFlow(int min, int max) {
        processNumber = generateRandomNumber(min, max);
    }

    public int generateRandomNumber(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }

    @Override
    public void run() {
        view.printMessage(Thread.currentThread() + "\t" + View.GENERATE_FLOW + processNumber);

        int rand;
        Process process;
        for (int i = 0; i < processNumber; i++) {
            rand = generateRandomNumber(1, 10);
            process = new Process(rand, 2 * rand);
            
            view.printMessage(Thread.currentThread() + "\t" + process);
            list.addProcessToList(process);
            try {
                view.printMessage(Thread.currentThread() + "\t" + View.TIME_BETWEEN_PROCESSES + process.getTime() + "\n" + "on ");
                Thread.sleep(process.getTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        synchronized (this) {
            notifyAll();
        }
        flag = true;
    }
}
