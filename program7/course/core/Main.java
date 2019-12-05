package course.core;

import course.Controller;

public class Main {
    public static void main(String[] args) {
        Runnable sim = new Simulation();

        Runnable view = new CourseView();

        Controller controller = new Controller((Simulation) sim, (CourseView) view);
        ((CourseView) view).setController(controller);
        ((Simulation) sim).setController(controller);

        Thread simulationThread = new Thread(sim);
        Thread viewThread = new Thread(view);

        simulationThread.start();
        viewThread.start();

    }
}
