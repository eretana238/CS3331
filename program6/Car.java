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

    private PositionState accelerating;
    private PositionState coasting;
    private PositionState braking;

    private PositionState state;

    private Document doc;
    
    private double accel;

    private double maxSpeed;
    private double currentSpeed;

    private double location;
    private double locationInSegment;
    
    private int currentSegment;
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

    public void run(double timeIncrement){
        elapsedTime += timeIncrement;
        // run state
        state.newPos(timeIncrement);

        if(elapsedTime % 30.0 <= timeIncrement){
            System.out.printf("%.0f\t%.2f\t%.2f\t\t", elapsedTime, currentSpeed*3600, location);        
        }
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
}