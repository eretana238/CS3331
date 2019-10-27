// Author: Esteban Retana

import java.awt.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Runner{
    final static int courseLength = 600;
    final static int maxRunners = 5;
    final static int timeInterval = 5;
    static int numOfObjects;
    String name;

    // interval
    double i;
    // total distance
    double d = 0.0;

    ArrayList<Double> arr = new ArrayList<Double>();

    double topSpeed;
    double accel;
    double totalTime;

    double startTime;
    double endTime;

    double accelDistance;
    double accelTime;

    double accelTime2;

    double decel;
    double decelTime;

    double topSpeedDistance1;
    double topSpeedTime1;

    double topSpeedDistance2;
    double topSpeedTime2;

    Runner(String name, double topSpeed, double accel){
        this.name = name;
        this.topSpeed = topSpeed;
        this.accel = accel;
        this.decel = accel;
        numOfObjects++;
    }

    // find time to accel to topSpeed
    public void setAccelTime(){
        accelTime = topSpeed/accel;
        accelTime2 = accelTime;
        decelTime = accelTime;
        accelDistance = .5*accel*(Math.pow(accelTime, 2));
        // accelDistance = .5*accel*accelTime);
    }
    // find time for first top speed
    public void setTopSpeed1(){
        topSpeedDistance1 = courseLength/2 - accelDistance*2;
        topSpeedTime1 = topSpeedDistance1 / topSpeed;
    }
    // find time for second top speed
    public void setTopSpeed2(){
        topSpeedDistance2 = courseLength/2 - accelDistance;
        topSpeedTime2 = topSpeedDistance2/topSpeed;
    }
    // find runner's course completion time
    public void setTotalTime(){
        setAccelTime();
        setTopSpeed1();
        setTopSpeed2();
        totalTime = (accelTime * 3) + topSpeedTime1 + topSpeedTime2;
    }
    // finds distance from first acceleration
    public void accel1Condition(){
        resetTime(accelTime);
        // accelTime runs out
        if(accelTime <= i){
            startTime = endTime;
            endTime += accelTime;

            // find out distance using integral
            d += .5*accel*(Math.pow(endTime,2)) - .5*accel*(Math.pow(startTime,2));

            i -= accelTime;
            accelTime = 0;  
        }
        // accelTime is still left
        else{
            startTime = endTime;
            endTime += i;

            // find out distance using integral
            d += .5*accel*(Math.pow(endTime,2)) - .5*accel*(Math.pow(startTime,2));
            accelTime -= timeInterval;

            i = 0;
        }
    }
    // finds distance from first top speed
    public void topSpeed1Condition(){
        resetTime(topSpeedTime1);
        if(topSpeedTime1<= i){
            startTime = endTime;
            endTime += timeInterval-topSpeedTime1;

            // find distance with non-changing velocity
            d += topSpeed * topSpeedTime1; 

            i -= topSpeedTime1;
            topSpeedTime1= 0; 
        }
        else{
            startTime = endTime;
            endTime += i;

            d += topSpeed * i;
            topSpeedTime1 -= i;

            i = 0;
        }
    }
    // finds distance from deceleration
    public void decelCondition(){
        resetTime(decelTime);
        if(decelTime <= i){

            d += .5 * decel * Math.pow(decelTime,2);

            i -= decelTime;
            decelTime = 0;  

        }
        else{
            endTime = decelTime;
            startTime = endTime - i;

            d += .5*accel*(Math.pow(endTime,2)) - .5*accel*(Math.pow(startTime,2));
            decelTime -= i;

            i = 0;
        }
    }
    // finds distance from seconds acceleration
    public void accel2Condition(){
        resetTime(accelTime2);
        // accelTime runs out
        if(accelTime2 <= i){
            startTime = endTime;
            endTime += accelTime2;

            
            d += .5*accel*(Math.pow(endTime,2)) - .5*accel*(Math.pow(startTime,2));

            i -= accelTime2;
            accelTime2 = 0;  
        }
        // accelTime2 is still left
        else{
            startTime = endTime;
            endTime += i;

            // find out distance using integral
            d += .5*accel*(Math.pow(endTime,2)) - .5*accel*(Math.pow(startTime,2));
            accelTime2 -= i;

            i = 0;
        }
    }
    // finds distance from second top speed
    public void topSpeed2Condition(){
        resetTime(topSpeedTime2);
        if(topSpeedTime2<= i){
            startTime = endTime;
            endTime += timeInterval-topSpeedTime2;

            // find distance with non-changing velocity
            d += topSpeed * topSpeedTime2; 

            i -= topSpeedTime2;
            topSpeedTime2 = 0; 
        }
        else{
            startTime = endTime;
            endTime += i;

            // find distance with non-changing velocity
            d += topSpeed * i;
            topSpeedTime2 -= i;

            i = 0;
        }
    }
    // resets time
    public void resetTime(double time){
        if(startTime > time || endTime > time){
            startTime = 0;
            endTime = 0;
        }
    }
    // prints approximate distance every five seconds
    public void distanceFromInterval(){
        while(totalTime > 0){
            i = timeInterval;
            if(i > totalTime){
                break;
            }
            // prevents negative time
            if(totalTime < 0)
                break;
            // loops until 5 second time interval is equal to zero
            while(i != 0){
                if(accelTime != 0){
                    accel1Condition();
                }
                else if(topSpeedTime1 != 0){
                    topSpeed1Condition();
                }
                else if(decelTime != 0){
                    decelCondition();
                }
                else if(accelTime2 != 0){
                    accel2Condition();
                }
                else if(topSpeedTime2 != 0){
                    topSpeed2Condition();
                }
            }
            arr.add(d);
            totalTime -= timeInterval;
        }
    }
    // finds the runner with the most time stamps
    public static int findSlowestRunner(Runner[] runners){
        int max = runners[0].arr.size();
        for(int i = 1; i < runners.length; i++){
            if(max < runners[i].arr.size())
                max = runners[i].arr.size();
        }
        return max;
    }
    // print runner name, topSpeed and acceleration
    public static void printRunnerInfoHeader(Runner person){
        person.setTotalTime();
        person.distanceFromInterval();
        System.out.println(person.name + "\t\t" + person.topSpeed + "\t\t\t" + person.accel);
    }
    // print runners distance data
    public static void printRunnersDistance(Runner[] runners){
        int slowest = findSlowestRunner(runners);
        // prints structure
        System.out.print("Time\t");
        for(int i = 0; i < runners.length; i++){
            System.out.print(runners[i].name + "\t");
        }
        System.out.println();
        // prints data for each runner
        for(int i = 0; i < slowest; i++){
            System.out.print(i*5+5 + "\t");
            for(int j = 0; j < runners.length; j++){
                if(runners[j].arr.size() > i+1)
                    System.out.printf("%.1f\t", runners[j].arr.get(i));
                else
                    System.out.print("\t");
            }
            System.out.println();
        }
    }
    // displays result
    public static void runnersInfo(Runner[] runners){
        System.out.println("Runner\t\tMax Speed (f/s)\t\tAcceleration(f/s/s)");
        // prints runner info
        for(int i = 0; i < runners.length; i++){
            printRunnerInfoHeader(runners[i]);
        }
        // empty line
        System.out.println();
        // prints distance every five seconds
        printRunnersDistance(runners);

    }
    public static void main(String[] args) {
        Runner person1 = new Runner("Nelly", 30.0, 8.0);
        Runner person2 = new Runner("Steve", 8.8, 3.0);
        Runner person3 = new Runner("Usain", 41.0, 11.0);
        Runner[] runners = {person1, person2, person3};
        runnersInfo(runners);
    }
}