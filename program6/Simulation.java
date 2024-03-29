// Author: Esteban Retana
// Second latest commit: Modified setConstantSpeedLocation for considering max driver speed
// Latest commit: 
// Date: 10/13

// Description: 

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

    private static int getMaxResultSize(){
        int max = -1;
        for(int i = 0; i < cars.size(); i++){
            if(max < cars.get(i).getResults().size())
                max = cars.get(i).getResults().size();
        }
        return max;
    }

    private static void printCarResults(){
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

    private static void setCarLaneNumber(Car car){
        int laneNumber = car.getCarNumber() % course.getSegments().get(course.getCurrentSegment(car)).getLanes();
        if(car.getLaneNumber() != laneNumber){
            car.setLaneNumber(laneNumber);
        }
    }

    private static void runCars(){
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
                System.out.println(car.getLocation() + " " + car.getCurrentSpeed());
                car.run(timeIncrement);     
                
                if(car.getElapsedTime() % 30.0 <= timeIncrement){
                    ArrayList<Double> result = new ArrayList<Double>();
                    result.add(car.getCurrentSpeed()*3600);
                    result.add(car.getLocation());
                    car.addResult(result);
                }
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

    private static int getAmountOfCars(){
        int amountOfCars = doc.getElementsByTagName("DRIVER").getLength();
        return amountOfCars;
    }

    private static void startSIM(List<Car> cars){
        runCars();
        // printCarResults();
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
        // circular course
        int laps = 2;
        course.createCircularCourse(laps);
        course.setTotalCourseLength();


        startSIM(cars);
    }
}