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
    private double totalCourseLength;

    public Course(Car[] cars){
        this.cars = cars;
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
    public void setTotalCourseLength(){
        for(Map.Entry<Integer,Segment> segment: segments.entrySet()){
            totalCourseLength += segment.getValue().getLength();
        }
    }

    public HashMap<Integer,Segment> getSegments(){
        return this.segments;
    }

    // course gets the segment in which the car is in
    public int getCurrentSegment(Car car){
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
    // course gets the distance ran relative to the current course the car is in
    public double getRemainingDistanceOfSegment(Car car){
        double distanceFromSegment = car.getLocation();
        int currentSegment = 1;
        for(Map.Entry<Integer, Segment> segment: segments.entrySet()){
            if(distanceFromSegment < segment.getValue().getLength()){
                break;
            }
            distanceFromSegment -= segment.getValue().getLength();
        }
        distanceFromSegment -= 0.01;
        return segments.get(getCurrentSegment(car)).getLength() - distanceFromSegment;
    }

    public double getTotalCourseLength(){
        return this.totalCourseLength;
    }
    
    public boolean isSpeedLimitInRange(int carNumber, int nextSegment){
        // get time remaining to finish the segment, and compare time when decelerating current speed to next speed limit
        if(segments.get(nextSegment) == null) return false;

        double changeToSegmentSpeedTime = Math.abs(segments.get(nextSegment).getSpeedLimit() - cars[carNumber].getCurrentSpeed())/cars[carNumber].getAccel();
        double distanceToStartBraking = cars[carNumber].getCurrentSpeed() * changeToSegmentSpeedTime + .5 * (cars[carNumber].getAccel()/3600) * Math.pow(changeToSegmentSpeedTime, 2);
        
        return getRemainingDistanceOfSegment(cars[carNumber]) <= distanceToStartBraking; 
    }
    public boolean isCarAhead(int carNumber){
        // check if current car speed can reach the car in front
        return false;
    }
}