import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

// t = Vi*t + 0.5*a*t*t
// vf = vi + a*t
// d = v * t

class Car{
    private Driver driver;
    private static int driverNumber;
    private PositionState state;

    private Document doc;
    
    private double accel;

    private double maxSpeed;

    private double location;


    Car(Document doc){
        setDoc(doc);
        assignDriver();
        this.state = new Accelerating();
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
            driverNumber++;
        }
    }

    // find location for 0.01 s intervals, return 
    public void setState(PositionState state){
        this.state = state;
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

    public PositionState getState(){
        return state;
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
}