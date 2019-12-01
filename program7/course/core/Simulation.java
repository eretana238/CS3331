package course.core;
// Author: Esteban Retana
// Second latest commit: Added SimulationTest
// Latest commit: Added TestRunner
// Date: 11/21

// Description:

import java.util.ArrayList;
import java.util.List;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import course.Controller;
import org.w3c.dom.Document;

public class Simulation implements Runnable {
    private Document doc;
    private Course course;
    private List<Car> cars = new ArrayList<Car>();
    private double timeIncrement = 0.005;
    private String pathName;
    private Controller controller;
    private boolean running;

    // sets the document file form xml
    public void setDoc(File inputFile){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            running = true;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setController(Controller controller){
        this.controller = controller;
    }
    // gets max size from results list
    public int getMaxResultSize(){
        int max = -1;
        for(int i = 0; i < cars.size(); i++){
            if(max < cars.get(i).getResults().size())
                max = cars.get(i).getResults().size();
        }
        return max;
    }

    public void setPathName(String pathName){
        this.pathName = pathName;
    }
    // sets car lane number depending on the number of lanes the current segment has
    public void setCarLaneNumber(Car car){
        int laneNumber = car.getCarNumber() % course.getSegments().get(course.getCurrentSegment(car)).getLanes();
        if(car.getLaneNumber() != laneNumber){
            car.setLaneNumber(laneNumber);
        }
    }

    public int getCarLaneNumber(int carNumber){
        return cars.get(carNumber).getLaneNumber();
    }
    // starts the cars to run on the course
    public void runCars(){
        // run each car for timeIncrement
        for(int i = 0; i < cars.size(); i++){
            Car car = cars.get(i);
            ArrayList<Segment> segments = course.getSegments();
            // makes sure if car has finished course, doesn't run but lets the rest of the cars to run
            if(car.getLocation() >= course.getTotalCourseLength()){
                car.setLocation(course.getTotalCourseLength()*3);
                continue;
            }
            Segment currentSegment = segments.get(course.getCurrentSegment(car));

            setCarLaneNumber(car);
            // i*60 <= car.getElapsedTime() makes sure that each car starts 1 minute apart from each other
            if(car.needsAccelerating(course, currentSegment.getSegmentNumber()-1, timeIncrement) && car.getElapsedTime() >= i*60)
                car.accel();

            if(car.needsConstant(course, currentSegment, timeIncrement) && car.getState().getClass().getName() != "Coasting" && car.getElapsedTime() >= i*60)
                car.coast();

            if(car.needsDecelerating(course, course.getCurrentSegment(car), timeIncrement)){
                // car ahead has priority
                if(course.isCarAhead(i) && car.getElapsedTime() >= i*60){
                    car.getState().decelForCarAhead(cars.get(i-1), timeIncrement);
                }

                else if(course.isSpeedLimitInRange(i,currentSegment.getSegmentNumber()) && car.getElapsedTime() >= i*60){
                    car.getState().decelForSegment(course.getSegments().get(currentSegment.getSegmentNumber()), timeIncrement);
                }

                else if(car.getCurrentSpeed() > (double)course.getSegments().get(course.getCurrentSegment(car)).getSpeedLimit()/3600){
                    car.getState().needToBrake();
                }
            }
            // sets car to run for the time increment
            car.run(timeIncrement);

            if(car.getElapsedTime() % 30.0 <= timeIncrement){
                ArrayList<Double> result = new ArrayList<Double>();
                result.add(car.getCurrentSpeed()*3600);
                result.add(car.getLocation());
                car.addResult(result);
            }
        }
    }
    // checks if all cars have surpassed the total length of the course
    public boolean allCarsFinished(){
        if(cars.size() == 0) return false;

        for(int i = 0; i < cars.size(); i++){
            if(cars.get(i).getLocation() < course.getTotalCourseLength()) return false;
        }
        return true;
    }
    // gets the total number of cars in the course
    public int getAmountOfCars(){
        int amountOfCars = doc.getElementsByTagName("DRIVER").getLength();
        return amountOfCars;
    }
    public void toggleRunning(){
        this.running = !running;
    }
    // initiates the simulation
    public void initSIM(){
        try{
            File inputFile = new File(pathName);
            setDoc(inputFile);
        } catch(Exception e){
            System.err.println("File not found");
            return;
        }

        // create car instances
        for(int i = 0; i < getAmountOfCars(); i++){
            cars.add(new Car(doc));
        }
        course = Course.getInstance();

        course.setCars(cars);
        course.setSegments(doc);
        // circular course
        int laps = 2;
        course.createCircularCourse(laps);
        course.setTotalCourseLength();
    }
    public Course getCourse(){
        return this.course;
    }
    public List<Car> getCars(){
        return this.cars;
    }
    public String getPathName(){
        return this.pathName;
    }
    public double[] getCarLocations(){
        double[] locations = new double[cars.size()];
        for(int i = 0; i < cars.size(); i++){
            locations[i] = cars.get(i).getLocation();
        }
        return locations;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(20);
                if(running){
                    runCars();
                    controller.updateView();
                }
                if(allCarsFinished()) break;
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}