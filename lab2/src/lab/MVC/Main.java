package lab.MVC;

public class Main {

    public static void main(String[] args) {
        /*--Initilization--*/
        Model model = new Model();
        View view = new View();

        Controller controller = new Controller(model, view);
        controller.ourProcess();
    }
}
