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


    public synchronized void setPathName(String pathName){
        model.setPathName(pathName);
        model.initSIM();
    }

    public synchronized void updateView(){
        view.update(model.getCarLocations(), model.getCourse().getSegmentLocations(), model.getCourse().getCircumference());
    }
}
