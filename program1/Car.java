// Author: Esteban Retana
// Second latest commit: Added findDistanceInIntervals method
// Latest commit: Added printCarResults method
// Date: 9/12

// Description: Created a program that finds the total time to finish 3 miles in a car.
// That allows to create time breakpoints and to define when the speed changes. 
// The program uses an integral based approach to figure out the distance for every 30 seconds 
// and stores the data in ArrayLists


import java.util.ArrayList;

class Car{
    // stores the distances for each 30 sec interval
    private ArrayList<Double> results = new ArrayList<Double>();

    final int[] speedLimit = {20,60,30};
    // reference time interval
    final int interval = 30;
    // global changeable timeInterval
    double timeInterval;
    double totalTime;

    final int distanceBreakPoint = 1;

    final double totalDistance = 3.0;
    
    double accel;
    double accelMilesInSeconds;

    double currentSpeed;
    
    ArrayList<Double> accelTime = new ArrayList<Double>();
    ArrayList<Double> accelDistance = new ArrayList<Double>();

    ArrayList<Double> constantSpeedTime = new ArrayList<Double>();
    ArrayList<Double> constantSpeedDistance = new ArrayList<Double>();
    ArrayList<Double> speed = new ArrayList<Double>();

    double location;
    double startTime;
    double endTime;
    
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
        accelDistance.add((.5 * accelMilesInSeconds) * Math.pow(endTime, 2) - (.5 * accelMilesInSeconds) * Math.pow(startTime, 2));
    }
    // sets time for constantTime to reach near end of mile 
    public void setConstantSpeedTime(int speedL){
        double remainingDistance = 1;
        while(!accelDistance.isEmpty()){
            // gets remaining distance after acceleration
            remainingDistance -= accelDistance.remove(0);  
        }
        // gets time from remaining distance in constant speed
        constantSpeedTime.add(remainingDistance/((double)speedL/3600));
    }
    // find time and distance data for acceleration and constant velocity
    public void setRunningData(){
        setAccelTime(0, speedLimit[0]);
        setAccelDistance(0, speedLimit[0],0);
        
        setConstantSpeedTime(speedLimit[0]);

        resetTime();
        setAccelTime(speedLimit[0], speedLimit[1]);
        setAccelDistance(speedLimit[0], speedLimit[1],1);

        resetTime();
        setAccelTime(speedLimit[1], speedLimit[2]);
        setAccelDistance(speedLimit[1], speedLimit[2],2);

        setConstantSpeedTime(speedLimit[1]);

        setConstantSpeedTime(speedLimit[2]);
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
    public void findConstantSpeedLocation(int i){
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
            totalTime += num;
        }
        for(Double num : constantSpeedTime){
            totalTime += num;
        }
    }
    // figure out integral using interval in accel
    public void printDistanceInIntervals(){
        int i = 0;
        timeInterval = interval;
        int speedLimitSign = 0;
        double end = 0.0;
        while(Math.round(totalTime) >= 0.001){
            // stores distance once 30 sec lilmit is reached
            if(timeInterval == 0){
                results.add(location);
                speed.add(currentSpeed);
                timeInterval = interval;
            }
            // calculates acceleration and deceleration distances every 30 sec
            if(i == 0 || i == 2 || i == 3){
                // deceleration
                if(i == 3)
                    findAccelLocation(true);
                // acceleration
                else
                    findAccelLocation(false);
                
                if(accelTime.get(0) == 0){
                    accelTime.remove(0);
                    resetTime();
                    // add once completed
                    i++;
                }
            }
            // calculates constantSpeed distances every 30 sec
            if(i == 1 || i == 4 || i == 5){
                findConstantSpeedLocation(speedLimitSign);
                if(constantSpeedTime.get(0) == 0){
                    constantSpeedTime.remove(0);
                    resetTime();
                    speedLimitSign++;
                    // add once completed
                    i++;
                }
            }
        }
    }
    // displays data for each 30 sec interval
    public static void printCarResults(Car a, Car b, Car c){
        double tempSpeed = 0;
        double tempResult = 0;

        System.out.println("time\tCar a\t\t\tCar b\t\t\tCar c\n\tspeed\tlocation\tspeed\tlocation\tspeed\tlocation");
        int i = 0;
    
        while(true){
            if(i == 0)
                System.out.println("0\t0.0\t0.0\t\t0.0\t0.0\t\t0.0\t0.0");

            System.out.print((i+1)*30 + "\t");

            if(a.results.size() != 0){
                tempSpeed = a.speed.remove(0);
                tempResult = a.results.remove(0);
                System.out.printf("%.1f\t%.2f\t\t",tempSpeed , tempResult);
            }
            else{
                System.out.printf("%.1f\t%.2f\t\t",tempSpeed, tempResult);
            }
            if(b.results.size() != 0 && i >= 2){
                tempSpeed = b.speed.remove(0);
                tempResult = b.results.remove(0);
                System.out.printf("%.1f\t%.2f\t\t", tempSpeed, tempResult);
            }
            else if(b.results.size() == 0)
                System.out.printf("%.1f\t%.2f\t\t", tempSpeed, tempResult);
            else if(i < 2)
                System.out.print("0.0\t0.0\t\t");

            if(c.results.size() != 0 && i >= 4){
                
                System.out.printf("%.1f\t%.2f\t\t", c.speed.remove(0), c.results.remove(0));
            }
            else
                System.out.print("0.0\t0.0\t\t");

            if(a.results.size() == 0 && b.results.size() == 0 && c.results.size() == 0)
                break;
            i++;
            System.out.println();
        }
    }
    public static void main(String[] args) {
        Car A = new Car();
        Car B = new Car();
        Car C = new Car();

        A.setRunningData();
        A.findTotalTime();
        A.printDistanceInIntervals();

        B.setRunningData();
        B.findTotalTime();
        B.printDistanceInIntervals();

        C.setRunningData();
        C.findTotalTime();
        C.printDistanceInIntervals();

        printCarResults(A,B,C);
    }
}