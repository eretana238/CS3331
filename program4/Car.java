// Author: Esteban Retana
// Second latest commit: Keylistener overriding
// Latest commit: modified HandleXML writeXML
// Date: 10/02

// Description: Created an application using JFrame to modify data from the XML files. Its able to save, open and exit 
// the program in the menu options and alternatively close it by the "x". Created several classes where each does the 
// GUI, or the action listeners, or xml handling, which makes everything much neater. However, program lacks better key listening
// to save with just keys and xml data cant be transfered to the car class yet.

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

class Car{
    // stores the distances for each 30 sec interval
    private ArrayList<Double> results = new ArrayList<Double>();

    // reference time interval
    final int interval = 30;
    // global changeable timeInterval
    private double timeInterval;
    private double totalTime;
    
    private double accel;
    private double accelMilesInSeconds;

    private double currentSpeed;
    private String fileName;

    
    ArrayList<Double> accelTime = new ArrayList<Double>();
    ArrayList<Double> accelDistance = new ArrayList<Double>();

    ArrayList<Double> constantSpeedTime = new ArrayList<Double>();
    ArrayList<Double> constantSpeedDistance = new ArrayList<Double>();
    ArrayList<Double> speed = new ArrayList<Double>();

    private double location;
    double startTime;
    double endTime;

    static String[][] content;

    Car(){
        this.accel = 10.23;
        this.accelMilesInSeconds = 10.23/3600;
        this.location = 0.0;
    }
    // resets start time and end time
    public void resetTime(){
        startTime = 0;
        endTime = 0;
    }
    // sets time for acceleration to reach speed limit
    public void setAccelTime(int prev, int speedL){
        accelTime.add(Math.abs((speedL - prev)/accel));
    }
    // sets distance for acceleration
    public void setAccelDistance(double prevSpeed, int speedL, int i){
        startTime = endTime;
        endTime = accelTime.get(i);
        
        // finds integral of endTime and startTime
        accelDistance.add(Math.abs((.5 * accelMilesInSeconds) * Math.pow(endTime, 2) - (.5 * accelMilesInSeconds) * Math.pow(startTime, 2)));
    }
    // sets time for constantTime to reach near end of mile 
    public void setConstantSpeedTime(int speedL, double segmentLength){
        while(!accelDistance.isEmpty()){
            // gets remaining distance after acceleration
            segmentLength -= accelDistance.remove(0);  
        }
        // gets time from remaining distance in constant speed
        constantSpeedTime.add(segmentLength/((double)speedL/3600));
    }
    // find time and distance data for acceleration and constant velocity
    public void setCourseData(int[] speedLimit, double[] segmentLengths){
        for(int i = 1; i < speedLimit.length; i++){
            // transfer to acceltime and distance
            double length = segmentLengths[i-1];

            resetTime();
            // accelerate
            if(speedLimit[i-1] < speedLimit[i]){
                setAccelTime(speedLimit[i-1], speedLimit[i]);
                setAccelDistance(speedLimit[i-1], speedLimit[i], i-1);
            }
            // decelerate
            if(i+1 < speedLimit.length && speedLimit[i] > speedLimit[i+1]){
                setAccelTime(speedLimit[i], speedLimit[i+1]);
                setAccelDistance(speedLimit[i], speedLimit[i+1], i);
                setConstantSpeedTime(speedLimit[i], segmentLengths[i-1]);
            }
            // constant
            else{
                setConstantSpeedTime(speedLimit[i], segmentLengths[i-1]);
            }
        }
    }

    // figure out integral using interval in accel
    public void findAccelLocation(boolean decel){
        if(accelTime.get(0) > timeInterval){
            // deceleration codition
            if(decel){
                startTime = accelTime.get(0) - timeInterval;
                endTime = accelTime.get(0);
            }
            // acceleration condition
            else{
                startTime = endTime;
                endTime += timeInterval;
            }
            totalTime -= timeInterval;
            // set speed
            currentSpeed += accelMilesInSeconds * timeInterval;
            timeInterval = 0;
            accelTime.set(0, accelTime.get(0)-timeInterval);
        }
        else{
            // deceleration condition
            if(decel){
                startTime = 0;
                endTime = accelTime.get(0);
            }
            // acceleration condition
            else{
                startTime = 0;
                endTime = accelTime.get(0);
            }
            totalTime -= accelTime.get(0);
            // set speed
            currentSpeed = accelMilesInSeconds * accelTime.get(0);
            timeInterval -= accelTime.get(0);
            accelTime.set(0, 0.0);
        }
        // finds integral of endTime and startTime
        location += ((.5 * accelMilesInSeconds) * Math.pow(endTime, 2) - (.5 * accelMilesInSeconds) * Math.pow(startTime, 2));
    }
    // figure out distance using velocity and time
    public void findConstantSpeedLocation(int i, int[] speedLimit){
        if(constantSpeedTime.get(0) > timeInterval){
            location += ((double)speedLimit[i]/3600) * timeInterval;
            totalTime -= timeInterval;
            currentSpeed = (double)speedLimit[i];
            constantSpeedTime.set(0, constantSpeedTime.get(0)-timeInterval);
            timeInterval = 0;
        }
        else{
            location += (speedLimit[i]/3600) * constantSpeedTime.get(0);
            timeInterval -= constantSpeedTime.get(0);
            totalTime -= constantSpeedTime.get(0);
            currentSpeed = (double)speedLimit[i];
            constantSpeedTime.set(0, 0.0);
        }
    }
    // sums up accelTimes and constantSpeedTimes
    public void findTotalTime(){
        for(Double num : accelTime){
            totalTime += Math.abs(num);
        }
        for(Double num : constantSpeedTime){
            totalTime += num;
        }
    }
    // figure out integral using interval in accel
    public void printDistanceInIntervals(int[] speedLimit){
        // try to find out direction in intervals
        int i = 0;
        timeInterval = interval;
        // represents the speed that the car is supposed to go at
        int speedLimitSign = 1;
        double end = 0.0;
        while(Math.round(totalTime) >= 0.001){
            // stores distance once 30 sec lilmit is reached
            if(timeInterval == 0){
                results.add(location);
                speed.add(currentSpeed);
                timeInterval = interval;
            }
            // calculates acceleration and deceleration distances every 30 sec
            // i (index) represents acceleration or constant speed...
            // if i is odd, the car is accelerating. Or if i is even, the car is at constant speed
            if(i % 2 == 0){
                // deceleration
                if(speedLimit[speedLimitSign-1] > speedLimit[speedLimitSign]){
                    // remove
                    findAccelLocation(true);
                }
                // acceleration
                else{
                    findAccelLocation(false);
                }
                
                if(accelTime.get(0) == 0){
                    accelTime.remove(0);
                    resetTime();
                    i++;
                    // add once completed
                }
            }
            
            // calculates constantSpeed distances every 30 sec
            else{
                findConstantSpeedLocation(speedLimitSign, speedLimit);
                if(constantSpeedTime.get(0) == 0){
                    constantSpeedTime.remove(0);
                    resetTime();
                    speedLimitSign++;
                    i++;
                    // add once completed
                }
            }
        }
    }
    // displays data for each 30 sec interval
    public static void printCarResults(Car a, Car b, Car c){
        // holds the last speed value temporarily
        double[] tempSpeed = new double[2];
        // holds the last location value temporarily
        double[] tempResult = new double[2];

        System.out.println("time\tCar a\t\t\tCar b\t\t\tCar c\n\tspeed\tlocation\tspeed\tlocation\tspeed\tlocation");
        int i = 0;
    
        while(true){
            // initial print
            if(i == 0)
                System.out.println("0\t0.0\t0.00\t\t0.0\t0.00\t\t0.0\t0.00");
            // prints seconds in 30 sec intervals
            System.out.print((i+1)*30 + "\t");

            // car a is still in the course
            if(a.results.size() != 0){
                tempSpeed[0] = a.speed.remove(0);
                tempResult[0] = a.results.remove(0);
                System.out.printf("%.1f\t%.2f\t\t",tempSpeed[0] , tempResult[0]);
            }
            // car a finishes course
            else{
                System.out.printf("%.1f\t%.2f\t\t",tempSpeed[0], tempResult[0]);
            }
            // car b is still in the course
            if(b.results.size() != 0 && i >= 2){
                tempSpeed[1] = b.speed.remove(0);
                tempResult[1] = b.results.remove(0);
                System.out.printf("%.1f\t%.2f\t\t", tempSpeed[1], tempResult[1]);
            }
            // car b is finished
            else if(b.results.size() == 0)
                System.out.printf("%.1f\t%.2f\t\t", tempSpeed[1], tempResult[1]);
            // car b hasn't started the course
            else if(i < 2)
                System.out.print("0.0\t0.0\t\t");
            // car c is still in the course
            if(c.results.size() != 0 && i >= 4){
                
                System.out.printf("%.1f\t%.2f\t\t", c.speed.remove(0), c.results.remove(0));
            }
            // car c hasn't started the course
            else
                System.out.print("0.0\t0.00\t\t");
            // all cars have finished the course
            if(a.results.size() == 0 && b.results.size() == 0 && c.results.size() == 0)
                break;
            i++;
            System.out.println();
        }
    }
    
    public static void main(String[] args) {
        // file directory
        String dir = "in.xml";
        File inputFile = new File(dir);
        // organize xml data from file
        HandleXML xmlData = new HandleXML(dir);

        // start gui
        if(xmlData.content != null){
            Window frame = new Window(400, 400, xmlData.content, dir);
        }
        else{
            Window frame = new Window(400,400);
        }


            // // create car instances 
            // Car A = new Car();
            // Car B = new Car();
            // Car C = new Car();
            // // set cource data
            // A.setCourseData(speedLimit, segmentLengths);
            // A.findTotalTime();
            // A.printDistanceInIntervals(speedLimit);
            // // set cource data
            // B.setCourseData(speedLimit, segmentLengths);
            // B.findTotalTime();
            // B.printDistanceInIntervals(speedLimit);
            // // set cource data
            // C.setCourseData(speedLimit, segmentLengths);
            // C.findTotalTime();
            // C.printDistanceInIntervals(speedLimit);

            // printCarResults(A, B, C);
    }
}
