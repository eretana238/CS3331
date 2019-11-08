// Author: Esteban Retana
// Second latest commit: Modified setConstantSpeedLocation for considering max driver speed
// Latest commit: Added 2nd method setDistanceInIntervals for car upfront
// Date: 10/13

// Description: Created a program that finds the location of a car in a track composed of different segments for every 30 sec intervals.
// The program uses an xml file and stores each segment's data to compute the location
// The program uses an integral based approach to figure out the distance for every 30 seconds and finds the location regardless
// of driver type (driving style) and speed.

import java.util.Scanner;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

public class Simulation {
    private static Document doc;
    private static Course course;
    private static Car[] cars;
    private static double timeIncrement = 0.01;
     // user input and prompt
    private static String cmdInterface(Scanner in){
        System.out.println("Please input xml file name:");
        String fileName = in.nextLine();
        return fileName;
    }
    // sets the document file form xml
    private static void setDoc(File inputFile){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void runCars(double timeIncrement){
        while(true){
            // run each car for timeIncrement
            for(int i = 0; i < cars.length; i++){
                Car car = cars[i];
                Segment currentSegment = course.getSegments().get(course.getCarLocationSegment(car));

                // if(course.getSegments().size())
                // makes sure if car has finished course, doesn't run but lets the rest of the cars to run
                if(car.getLocation() >= course.getTotalCourseLength()) 
                    continue;
                // i*60 <= car.getElapsedTime() makes sure that each car starts 1 minute apart from each other
                if(needsAccelerating(car, currentSegment) && car.getState().getClass().getName() != "Accelerating" && car.getElapsedTime() >= i*60){
                    car.accel();
                } 
                else if(needsConstant(car, currentSegment) && car.getState().getClass().getName() != "Coasting" && car.getElapsedTime() >= i*60){
                    car.coast();
                    // System.out.println("coasting");
                }
                else if(needsDecelerating(currentSegment.getSegmentNumber()) && course.isSpeedLimitInRange(i,course.getSegments().get(currentSegment.getSegmentNumber() + 1)) && car.getElapsedTime() >= i*60){
                    car.getState().decelForSegment(course.getSegments().get(currentSegment.getSegmentNumber() + 1));
                }

                else if(needsDecelerating(car, currentSegment) && course.isCarAhead(i) && car.getElapsedTime() >= i*60){
                    car.getState().decelForCarAheacd(cars[i-1]);
                }
                // if(course.getCarLocationSegment(car) != -1)
                car.run(timeIncrement);            

            }
            if(cars[cars.length-1].getElapsedTime() % 30.0 <= 0.1)
                System.out.println();

            if(allCarsFinished()){
                break;
            }
        }
    }
    private static boolean allCarsFinished(){
        for(int i = 0; i < cars.length; i++){
            if(cars[i].getLocation() < course.getTotalCourseLength()) return false;
        }
        return true;
    }
    private static boolean needsDecelerating(int segmentNumber){
        if(course.getSegments().get(segmentNumber+1) != null){
            Segment nextSegment = course.getSegments().get(segmentNumber+1);
            return nextSegment.getSpeedLimit() < course.getSegments().get(segmentNumber).getSpeedLimit();
        }
        return false;
    }
    private static boolean needsAccelerating(Car car, Segment segment){
        return car.getCurrentSpeed() != car.getMaxSpeed()/3600 && car.getCurrentSpeed() != (double)segment.getSpeedLimit()/3600;
    }
    private static boolean needsConstant(Car car, Segment segment){
        return car.getCurrentSpeed() == car.getMaxSpeed()/3600 || car.getCurrentSpeed() == (double)segment.getSpeedLimit()/3600;
    }

    private static void setAmountOfCars(){
        int amountOfCars = doc.getElementsByTagName("DRIVER").getLength();
        cars = new Car[amountOfCars];
    }

    private static void startSIM(Car[] cars){
        runCars(0.1);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        File inputFile = new File(cmdInterface(in));

        while(!inputFile.exists()) {
            System.err.println("File not found!");
        	inputFile = new File(cmdInterface(in));	
        }

        setDoc(inputFile);
        setAmountOfCars();

        course = new Course(cars);

        course.setSegments(doc);
        course.setTotalCourseLength();

        // create car instances 
        for(int i = 0; i < cars.length; i++){
            cars[i] = new Car(doc);
        }

        startSIM(cars);
    }
}