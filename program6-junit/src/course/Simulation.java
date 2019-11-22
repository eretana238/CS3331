package course;
// Author: Esteban Retana
// Second latest commit: Added SimulationTest
// Latest commit: Added TestRunner
// Date: 11/21

// Description: Extended program5 where it displays the location of a car in a track composed of different segments for every
// 30 sec intervals. But changed its overall function to work for small increments of time.
// The program uses an integral based approach to figure out the distance every small increment of time and finds the location regardless
// of driver type (driving style)
// Program used JUNIT 4 to test main functions from each class


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

public class Simulation {
    private static Document doc;
    private static Course course;
    private static List<Car> cars = new ArrayList<Car>();
    private static double timeIncrement = 0.001;
     // user input and prompt
    public static String cmdInterface(Scanner in){
        System.out.println("Please input xml file name:");
        String fileName = in.nextLine();
        return fileName;
    }

    // sets the document file form xml
    public static void setDoc(File inputFile){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // gets max size from results list
    public static int getMaxResultSize(){
        int max = -1;
        for(int i = 0; i < cars.size(); i++){
            if(max < cars.get(i).getResults().size())
                max = cars.get(i).getResults().size();
        }
        return max;
    }
    // prints car result data
    public static void printCarResults(){
        for(int i = 0; i < cars.size(); i++){
            System.out.print("TIME\tSPEED\tDISTANCE\t");
        }
        System.out.println();
        for(int j = 0; j < getMaxResultSize(); j++){
            for(int i = 0; i < cars.size(); i++){
                // check if the j is at the size of the car.results, then repeat last value
                if(j < cars.get(i).getResults().size()){
                    ArrayList<Double> result = cars.get(i).getResults().get(j);
                    System.out.printf("%d\t%.2f\t%.2f\t\t", j*30,result.get(0),result.get(1));
                }
                else{
                    int size = cars.get(i).getResults().size();
                    ArrayList<Double> result = cars.get(i).getResults().get(size-1);
                    System.out.printf("%d\t%.2f\t%.2f\t\t", j*30,result.get(0),result.get(1));
                }
            }
            System.out.println();
        }
    }
    // sets car lane number depending on the number of lanes the current segment has
    public static void setCarLaneNumber(Car car){
        int laneNumber = car.getCarNumber() % course.getSegments().get(course.getCurrentSegment(car)).getLanes();
        if(car.getLaneNumber() != laneNumber){
            car.setLaneNumber(laneNumber);
        }
    }

    public static int getCarLaneNumber(int carNumber){
        return cars.get(carNumber).getLaneNumber();
    }
    // starts the cars to run on the course
    public static void runCars(){
        while(true){
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

                if(car.needsDecelerating(course, course.getCurrentSegment(car))){
                    // car ahead has priority
                    if(course.isCarAhead(i) && car.getElapsedTime() >= i*60){
                        car.getState().decelForCarAhead(cars.get(i-1), timeIncrement);
                    }

                    else if(course.isSpeedLimitInRange(i,currentSegment.getSegmentNumber()) && car.getElapsedTime() >= i*60){
                        car.getState().decelForSegment(course.getSegments().get(currentSegment.getSegmentNumber()), timeIncrement);
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
            // finishes program
            if(allCarsFinished()){
                break;
            }
        }
    }
    // checks if all cars have surpassed the total length of the course
    public static boolean allCarsFinished(){
        for(int i = 0; i < cars.size(); i++){
            if(cars.get(i).getLocation() < course.getTotalCourseLength()) return false;
        }
        return true;
    }
    // gets the total number of cars in the course
    public static int getAmountOfCars(){
        int amountOfCars = doc.getElementsByTagName("DRIVER").getLength();
        return amountOfCars;
    }
    // initiates the simulation
    public static void initSIM(){
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
//    run program and print results
    public static void startSIM(){
        runCars();
        printCarResults();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        File inputFile = new File(cmdInterface(in));
        // user input
        while(!inputFile.exists()) {
            System.err.println("File not found!");
        	inputFile = new File(cmdInterface(in));	
        }

        setDoc(inputFile);

        initSIM();
        startSIM();
    }
}