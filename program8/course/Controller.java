package course;

import course.core.Simulation;
import course.view.CourseView;

public class Controller {
    private Simulation model;
    private CourseView view;

    public Controller(Simulation model, CourseView view){
        this.model = model;
        this.view = view;
    }

    // sets pathname to simulation
    public synchronized void setPathName(String pathName){
        model.setPathName(pathName);
        model.initSIM();
    }
    // updates view for CourseView
    public synchronized void updateView(){
        view.update(model.getCarLocations(), model.getCourse().getSegmentLocations(), model.getCourse().getCircumference());
    }
    // gets running state from simulation
    public synchronized boolean getRunningState(){
        return model.getRunning();
    }
    // sets running state from simulation
    public synchronized void setRunningState(boolean running){
        model.setRunning(running);
    }
}

