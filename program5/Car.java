import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.swing.*;

class Car{
    // stores the distances for each 30 sec interval
    private ArrayList<Double> results = new ArrayList<Double>();
    private Driver driver;

    // reference time interval
    private final int interval = 30;
    // global changeable timeInterval
    private double timeInterval;
    private double totalTime;
    
    private double accel;
    private double accelMilesInSeconds;

    private double currentSpeed;
    private double maxSpeed;
    
    private ArrayList<Double> accelTime = new ArrayList<Double>();
    private ArrayList<Double> accelDistance = new ArrayList<Double>();

    private ArrayList<Double> constantSpeedTime = new ArrayList<Double>();
    private ArrayList<Double> constantSpeedDistance = new ArrayList<Double>();
    private ArrayList<Double> speed = new ArrayList<Double>();

    private double location;
    private double startTime;
    private double endTime;
    
    Car(Driver driver){
        this.driver = driver;
        this.accel = driver.getType().getMaxAccel();
        this.maxSpeed = driver.getType().getSpeedLimit();
        this.accelMilesInSeconds = this.accel/3600;
        this.location = 0.0;
        System.out.println(maxSpeed);
    }
    // resets start time and end time
    public void setRestartTime(){
        startTime = 0;
        endTime = 0;
    }
    // sets time for acceleration to reach speed limit or max speed
    public void setAccelTime(int prev, int speedL){
        if(maxSpeed < speedL){
            accelTime.add(Math.abs(maxSpeed- prev)/accel);
        }
        else{
            accelTime.add(Math.abs(speedL - prev)/accel);
        }
    }
    // sets distance for acceleration
    public void setAccelDistance(int i){
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
        if(maxSpeed < speedL){
            // gets time from remaining distance in constant speed
            constantSpeedTime.add(segmentLength/((double)maxSpeed/3600));            
        }
        else{
            constantSpeedTime.add(segmentLength/((double)speedL/3600));
        }
    }
    // find time and distance data for acceleration and constant velocity
    public void setRunningData(int[] speedLimit, double[] segmentLengths){
        for(int i = 1; i < speedLimit.length; i++){
            // transfer to acceltime and distance
            double length = segmentLengths[i-1];

            setRestartTime();
            // accelerate
            if(speedLimit[i-1] < speedLimit[i]){
                setAccelTime(speedLimit[i-1], speedLimit[i]);
                setAccelDistance(i-1);
            }
            // decelerate
            if(i+1 < speedLimit.length && speedLimit[i] > speedLimit[i+1]){
                setAccelTime(speedLimit[i], speedLimit[i+1]);
                setAccelDistance(i);
                setConstantSpeedTime(speedLimit[i], segmentLengths[i-1]);
            }
            // constant
            else{
                setConstantSpeedTime(speedLimit[i], segmentLengths[i-1]);
            }
        }
    }

    // figure out integral using interval in accel
    public void setAccelLocation(boolean decel){
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
    public void setConstantSpeedLocation(int i, int[] speedLimit){
        if(constantSpeedTime.get(0) > timeInterval){
            if(maxSpeed < speedLimit[i]){
                location += ((double)maxSpeed/3600) * timeInterval;
                totalTime -= timeInterval;
                currentSpeed = (double)maxSpeed;
            }
            else{
                location += ((double)speedLimit[i]/3600) * timeInterval;
                totalTime -= timeInterval;
                currentSpeed = (double)speedLimit[i];
            }
            constantSpeedTime.set(0, constantSpeedTime.get(0)-timeInterval);
            timeInterval = 0;
        }
        else{
            if(maxSpeed < speedLimit[i]){
                location += (maxSpeed/3600) * constantSpeedTime.get(0);
                timeInterval -= constantSpeedTime.get(0);
                totalTime -= constantSpeedTime.get(0);
                currentSpeed = (double)maxSpeed;
            }
            else{
                location += (speedLimit[i]/3600) * constantSpeedTime.get(0);
                timeInterval -= constantSpeedTime.get(0);
                totalTime -= constantSpeedTime.get(0);
                currentSpeed = (double)speedLimit[i];
            }
            constantSpeedTime.set(0, 0.0);
        }
    }
    // sums up accelTimes and constantSpeedTimes
    public void setTotalTime(){
        for(Double num : accelTime){
            totalTime += Math.abs(num);
        }
        for(Double num : constantSpeedTime){
            totalTime += num;
        }
    }
    public void setDistanceInIntervals(int[] speedLimit){
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
                    setAccelLocation(true);
                }
                // acceleration
                else{
                    setAccelLocation(false);
                }
                
                if(accelTime.get(0) == 0){
                    accelTime.remove(0);
                    setRestartTime();
                    i++;
                    // add once completed
                }
            }
            // calculates constantSpeed distances every 30 sec
            else{
                setConstantSpeedLocation(speedLimitSign, speedLimit);
                if(constantSpeedTime.get(0) == 0){
                    constantSpeedTime.remove(0);
                    setRestartTime();
                    speedLimitSign++;
                    i++;
                    // add once completed
                }
            }
        }
    }
    // figure out integral using interval in accel
    public void setDistanceInIntervals(Car car, double followTime, int[] speedLimit){
        // creates car follow
        if(results.size() > 2){
            double t = (car.results.get(results.size()-3)-results.get(results.size()-1))/accelMilesInSeconds;
            if(t <= followTime){
                this.driver = car.driver;
            }
        }  
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
                    setAccelLocation(true);
                }
                // acceleration
                else{
                    setAccelLocation(false);
                }
                
                if(accelTime.get(0) == 0){
                    accelTime.remove(0);
                    setRestartTime();
                    i++;
                    // add once completed
                }
            }
            // calculates constantSpeed distances every 30 sec
            else{
                setConstantSpeedLocation(speedLimitSign, speedLimit);
                if(constantSpeedTime.get(0) == 0){
                    constantSpeedTime.remove(0);
                    setRestartTime();
                    speedLimitSign++;
                    i++;
                    // add once completed
                }
            }
        }
    }
    
    public ArrayList<Double> getResults(){
        return results;
    }
    public ArrayList<Double> getSpeed(){
        return speed;
    }
}