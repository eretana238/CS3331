// Author: Esteban Retana
// Second latest commit: Modified setConstantSpeedLocation for considering max driver speed
// Latest commit: Added 2nd method setDistanceInIntervals for car upfront
// Date: 10/13

// Description: Created a program that finds the location of a car in a track composed of different segments for every 30 sec intervals.
// The program uses an xml file and stores each segment's data to compute the location
// The program uses an integral based approach to figure out the distance for every 30 seconds and finds the location regardless
// of driver type (driving style) and speed.

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
    public static void runCars(){
        while(true){
            // run each car for timeIncrement
            for(int i = 0; i < cars.size(); i++){
                Car car = cars.get(i);
                Segment currentSegment = course.getSegments().get(course.getCurrentSegment(car));

                // makes sure if car has finished course, doesn't run but lets the rest of the cars to run
                if(car.getLocation() >= course.getTotalCourseLength()){
                    // TODO: timeIncrement elapsed time
                    if(cars.get(cars.size()-1).getElapsedTime() % 30.0 <= timeIncrement)
                        System.out.printf("%.0f\t%.2f\t%.2f\t\t", car.getElapsedTime(),car.getCurrentSpeed()*3600, car.getLocation());
                    continue;
                }
                if(needsDecelerating(currentSegment.getSegmentNumber())){
                    // car ahead has priority
                    if(course.isCarAhead(i) && car.getElapsedTime() >= i*60){
                        car.getState().decelForCarAhead(cars.get(i-1), timeIncrement);
                        continue;
                    }

                    if(course.isSpeedLimitInRange(i,currentSegment.getSegmentNumber() + 1) && car.getElapsedTime() >= i*60){
                        car.getState().decelForSegment(course.getSegments().get(currentSegment.getSegmentNumber() + 1), timeIncrement);
                        continue;
                    }
                    
                }
                // i*60 <= car.getElapsedTime() makes sure that each car starts 1 minute apart from each other
                if(needsAccelerating(car, currentSegment.getSegmentNumber()) && car.getState().getClass().getName() != "Accelerating" && car.getElapsedTime() >= i*60)
                    car.accel();
                
                if(needsConstant(car, currentSegment) && car.getState().getClass().getName() != "Coasting" && car.getElapsedTime() >= i*60)
                    car.coast();
                
                car.run(timeIncrement);           
            }
            // prints new line
            if(cars.get(cars.size()-1).getElapsedTime() % 30.0 <= timeIncrement){
                System.out.println();
            }
            if(allCarsFinished()){
                break;
            }
        }
    }
    private static boolean allCarsFinished(){
        for(int i = 0; i < cars.size(); i++){
            if(cars.get(i).getLocation() < course.getTotalCourseLength()) return false;
        }
        return true;
    }
    private static boolean needsDecelerating(int segmentNumber){
        if(course.getSegments().get(segmentNumber+1) != null){
            Segment nextSegment = course.getSegments().get(segmentNumber+1);
            Segment currentSegment = course.getSegments().get(segmentNumber);
            return nextSegment.getSpeedLimit() < currentSegment.getSpeedLimit();
        }
        return false;
    }
    private static boolean needsAccelerating(Car car, int segmentNumber){
        // checks if speed is the same as next segment and if car is close to ending segment
        // prevents accelerating when segment is about to end
        double threshold = -(timeIncrement/100);
        
        if(segmentNumber < course.getSegments().size()){
            boolean isCarSpeedEqual = (car.getCurrentSpeed() - (double)course.getSegments().get(segmentNumber+1).getSpeedLimit()/3600) < threshold;
            boolean isCarCloseToSegment = course.getRemainingDistanceOfSegment(car) < threshold;

            boolean isSpeedLessThanMaxSpeed = car.getCurrentSpeed() - car.getMaxSpeed()/3600 < threshold;
            boolean isSpeedLessThanSpeedLimit = car.getCurrentSpeed() - (double)course.getSegments().get(segmentNumber).getSpeedLimit()/3600 < threshold;

            return (!isCarSpeedEqual || !isCarCloseToSegment) && isSpeedLessThanMaxSpeed && isSpeedLessThanSpeedLimit;            
        }
        else{
            // checks if car current speed is below current segment speed limit
            boolean isSpeedLessThanMaxSpeed = car.getCurrentSpeed() - car.getMaxSpeed()/3600 < threshold;
            boolean isSpeedLessThanSpeedLimit = car.getCurrentSpeed() - (double)course.getSegments().get(segmentNumber).getSpeedLimit()/3600 < threshold;

            return isSpeedLessThanMaxSpeed && isSpeedLessThanSpeedLimit;
        }
    }
    private static boolean needsConstant(Car car, Segment currentSegment){
        double threshold = timeIncrement/700;
        double speedDiffMax = car.getCurrentSpeed() - car.getMaxSpeed()/3600;
        boolean isSpeedEqualToMaxSpeed =  speedDiffMax < threshold && speedDiffMax > -threshold;
        
        double speedDiffLimit = car.getCurrentSpeed() - (double)currentSegment.getSpeedLimit()/3600;
        boolean isSpeedEqualToSpeedLimit = speedDiffLimit < threshold && speedDiffLimit > -threshold ;

        return isSpeedEqualToMaxSpeed || isSpeedEqualToSpeedLimit;
    }

    private static int getAmountOfCars(){
        int amountOfCars = doc.getElementsByTagName("DRIVER").getLength();
        return amountOfCars;
    }

    private static void startSIM(List<Car> cars){
        runCars();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        File inputFile = new File(cmdInterface(in));

        while(!inputFile.exists()) {
            System.err.println("File not found!");
        	inputFile = new File(cmdInterface(in));	
        }

        setDoc(inputFile);

        // create car instances 
        for(int i = 0; i < getAmountOfCars(); i++){
            cars.add(new Car(doc));
        }
        course = Course.getInstance();

        course.setCars(cars);
        course.setSegments(doc);
        course.setTotalCourseLength();


        startSIM(cars);
    }
}