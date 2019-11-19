import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

// d = Vi*t + 0.5*a*t*t
// vf = vi + a*t
// d = v * t

class Car{
    private Driver driver;
    private static int driverNumber;
    private int carNumber;
    private int laneNumber;

    private ArrayList<ArrayList<Double>> results = new ArrayList<ArrayList<Double>>();

    private PositionState accelerating;
    private PositionState coasting;
    private PositionState braking;

    private PositionState state;

    private Document doc;
    
    private double accel;

    private double maxSpeed;
    private double currentSpeed;

    private double location;
    
    private double elapsedTime;

    Car(Document doc){
        setDoc(doc);
        assignDriver();
        accelerating = new Accelerating(this);
        coasting = new Coasting(this);
        braking = new Braking(this);
        this.state = coasting;
        this.accel = driver.getDriverType().getMaxAccel();
        this.maxSpeed = driver.getDriverType().getSpeedLimit();
    }

    // sets the driver
    public void assignDriver(){
        NodeList nList = doc.getElementsByTagName("DRIVER");
            
        Node nNode = nList.item(driverNumber);

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) nNode;
            
            String name = eElement.getElementsByTagName("NAME").item(0).getTextContent().trim();
            String driverTypeName = eElement.getElementsByTagName("DRIVER_TYPE").item(0).getTextContent().trim();
            driver = new Driver(name, driverTypeName);
            driver.setDoc(this.doc);
            driver.setDriverType(driverTypeName);
            setCarNumber(driverNumber);
            driverNumber++;
        }
    }
    public boolean isInSameLane(int lane){
        return laneNumber == lane;
    }

    public boolean needsDecelerating(Course course, int currentSegment){
        if(state.getClass().getName() == "Braking") return false;
        if(currentSpeed > course.getSegments().get(currentSegment).getSpeedLimit()/3600) return true;
        // determine if the coures.getSegements().size()-1
        if(currentSegment < course.getSegments().size()-1){
            double currentSpeedLimit = course.getSegments().get(currentSegment).getSpeedLimit();
            double nextSpeedLimit = course.getSegments().get(currentSegment+1).getSpeedLimit();
            boolean decreasingSpeedLimits = currentSpeedLimit > nextSpeedLimit;
            return (course.isCarAhead(carNumber) || decreasingSpeedLimits) && currentSpeed > nextSpeedLimit/3600;
        }
        
        return course.isCarAhead(carNumber);
    }
    public boolean needsAccelerating(Course course, int currentSegment, double timeIncrement){
        // checks if speed is the same as next segment and if car is close to ending segment
        // prevents accelerating when segment is about to end
        if(state.getClass().getName() == "Accelerating") return false;

        double threshold = timeIncrement/100;
        ArrayList<Segment> segments = course.getSegments();
        
        if(segments.get(currentSegment).getSegmentNumber() < segments.size()){
            double speedDiff = getCurrentSpeed() - (double)segments.get(currentSegment+1).getSpeedLimit()/3600;
            boolean isCarSpeedEqual = speedDiff < threshold && speedDiff > -threshold;
            boolean isCarCloseToSegment = course.isSpeedLimitInRange(carNumber, currentSegment+1);

            threshold = -threshold;
            boolean isSpeedLessThanMaxSpeed = getCurrentSpeed() - getMaxSpeed()/3600 < threshold;
            boolean isSpeedLessThanSpeedLimit = getCurrentSpeed() - (double)segments.get(currentSegment).getSpeedLimit()/3600 < threshold;

            return this.state.getClass().toString() != "Braking" && (!isCarSpeedEqual || !isCarCloseToSegment) && isSpeedLessThanMaxSpeed && isSpeedLessThanSpeedLimit;    

        }
        else{
            // checks if car current speed is below current segment speed limit
            boolean isSpeedLessThanMaxSpeed = getCurrentSpeed() - getMaxSpeed()/3600 < threshold;
            boolean isSpeedLessThanSpeedLimit = getCurrentSpeed()- (double)segments.get(currentSegment).getSpeedLimit()/3600 < threshold;

            return isSpeedLessThanMaxSpeed && isSpeedLessThanSpeedLimit;
        }
    }
    public boolean needsConstant(Course course, Segment currentSegment, double timeIncrement){
        if(state.getClass().getName() == "Coasting") return false;

        double threshold = timeIncrement/700;
        double speedDiffMax = getCurrentSpeed() - getMaxSpeed()/3600;
        boolean isSpeedEqualToMaxSpeed =  Math.abs(speedDiffMax) < threshold;
        
        double speedDiffLimit = getCurrentSpeed() - (double)currentSegment.getSpeedLimit()/3600;
        boolean isSpeedEqualToSpeedLimit = Math.abs(speedDiffLimit) < threshold;

        return (isSpeedEqualToMaxSpeed || isSpeedEqualToSpeedLimit);
    }

    public void run(double timeIncrement){
        elapsedTime += timeIncrement;
        // run state
        state.newPos(timeIncrement);
    }

    public void setState(PositionState state){
        this.state = state;
    }
    public void accel(){
        this.state = accelerating;
    }
    public void coast(){
        this.state = coasting;
    }
    public void brake(){
        this.state = braking;
    }
    // sets the document file form xml
    public void setDoc(Document doc){
        this.doc = doc;
    }
    public void setAccel(double accel){
        this.accel = accel;
    }
    public void setMaxSpeed(double maxSpeed){
        this.maxSpeed = maxSpeed;
    }
    public void setLocation(double location){
        this.location = location;
    }
    public void setCurrentSpeed(double currentSpeed){
        this.currentSpeed = currentSpeed;
    }
    public void setElapsedTime(double elapsedTime){
        this.elapsedTime = elapsedTime;
    }
    public void setCarNumber(int carNumber){
        this.carNumber = carNumber;
    }
    public void setLaneNumber(int laneNumber){
        this.laneNumber = laneNumber;
    }
    public void addResult(ArrayList<Double> result){
        this.results.add(result);
    }

    public PositionState getState(){
        return state;
    }
    public PositionState getAcceleratingState(){
        return this.accelerating;
    }
    public PositionState getCoastingState(){
        return this.coasting;
    }
    public PositionState getBrakingState(){
        return this.braking;
    }
    public Driver getDriver(){
        return driver;
    }
    public double getAccel(){
        return accel;
    }
    public double getMaxSpeed(){
        return maxSpeed;
    }
    public double getLocation(){
        return location;
    }
    public double getCurrentSpeed(){
        return currentSpeed;
    }
    public double getElapsedTime(){
        return elapsedTime;
    }
    public int getCarNumber(){
        return carNumber;
    }
    public int getLaneNumber(){
        return laneNumber;
    }
    public ArrayList<ArrayList<Double>> getResults(){
        return this.results;
    }
}