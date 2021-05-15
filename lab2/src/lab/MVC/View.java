package lab.MVC;

public class View {
    public static final String GENERATE_FLOW = "Generating flow, consisting of processes= ";
    public static final String TIME_BETWEEN_PROCESSES = " Sleeping..The next process will be generated  in= ";
    public static final String WAITING_FOR_PROCESS=" is waiting until any process will be added to the queue";
    public static final String HANDLING_PROCESS=" Process has been handled in CPU for a period= ";

    public void printMessage(String message) {
        System.out.println(message);
    }
}
