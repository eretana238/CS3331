/*
*
* 
Author: Esteban Retana
Second latest commit: added controller
Latest commit: added CourseView and modified controller
Date: 12/05

Description: Programthat finds the location of a car in a track composed of different segments for every 30 sec intervals.
The program uses an xml file and stores each segment's data to compute the location
The program uses an integral based approach to figure out the distance for every 30 seconds and finds the location regardless
of driver type (driving style) and speed. The course can have multiple laps and cars slow down if a car from the same lane is
in front of it.

GUI: Simple enough to display one lane of cars(elipses) going around the track. The segments are defined by elipses as well to
not focus on creating lines based on course circumference
*
*
*/
package course.core;

import course.Controller;
import course.view.CourseView;

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
