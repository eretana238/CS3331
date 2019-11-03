import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Course {
    private HashMap<Integer,Segment> segments = new HashMap<Integer,Segment>();
    private Document doc;
    private Car[] cars;
    private int carNumber;
    private double amountTraveled;

    public Course(Car[] cars){
        this.cars = cars;
    }

    // course gets the segment in which the car is in
    public int getCarLocationSegment(Car car){
        double totalDistance = 0;
        for(Map.Entry<Integer, Segment> segment: segments.entrySet()){
            totalDistance += segment.getValue().getLength();
            // check if current car location for every milisencond is less than the time
            if(car.getLocation() <= totalDistance) 
                return segment.getValue().getSegmentNumber();
        }
        // returns -1 if car finished course
        return -1;
    }
    // sets the course info
    public void setSegments(Document doc){
        NodeList nList = doc.getElementsByTagName("SEGMENT");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                int segmentNumber = Integer.parseInt(eElement.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent().trim());
                double segmentLength = Double.parseDouble(eElement.getElementsByTagName("LENGTH").item(0).getTextContent().trim());
                int segmentSpeed = Integer.parseInt(eElement.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().trim());

                Segment seg = new Segment(segmentNumber, segmentLength, segmentSpeed);
                segments.put(seg.getSegmentNumber(), seg);
            }
        }
    }
    public void setDocument(Document doc){
        this.doc = doc;
    }
    public void changeCarState(Car car, double timeIncrement){
        // if getCarLocationSegment + 1 is less than current segment and exact decel to new segment speed is true, decelerate
        // if getCarLocationSegment + 1 is greater than current segment and location is at start of new segment, accelerate
        Segment currentSegment = segments.get(getCarLocationSegment(car));
        Segment nextSegment = segments.get(currentSegment.getSegmentNumber()+1);
        // TODO: accel
        // if car speed != car max speed or car speed != speed limit
        if(car.getCurrentSpeed() != car.getMaxSpeed() || car.getCurrentSpeed() != currentSegment.getSpeedLimit()){

        }
        // TODO: coast
        // if car speed == car max speed or car speed == speed limit
        if(car.getCurrentSpeed() == car.getMaxSpeed() || car.getCurrentSpeed() == currentSegment.getSpeedLimit()){

        }
        // TODO: decel
        // if next segment length < current segment and decel to next segment speed == true
        if(car.getCurrentSpeed() < currentSegment.getLength() && isSpeed)


    }
    public boolean isSpeedLimitClose(){
        
        return false;
    }
    public boolean isCarAhead(){
        // check if current car speed can reach 
        return false;
    }
}