import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Course {
    private Map<Integer,Segment> segments = new HashMap<Integer,Segment>();
    private Document doc;
    private List<Car> cars = new ArrayList<Car>();
    private double totalCourseLength;

    private Course(){}

    private static class CourseHolder{
        public static final Course INSTANCE = new Course();
    }
    public static Course getInstance(){
        return CourseHolder.INSTANCE;
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
                int segmentLanes = Integer.parseInt(eElement.getElementsByTagName("LANES").item(0).getTextContent().trim());

                Segment seg = new Segment(segmentNumber, segmentLength, segmentSpeed, segmentLanes);
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
    public void setCars(List<Car> cars){
        this.cars = cars;
    }

    public Map<Integer,Segment> getSegments(){
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
    // public int setNewLane()

    // course gets the distance ran relative to the current course the car is in
    public double getRemainingDistanceOfSegment(Car car){
        double distanceFromSegment = car.getLocation();
        for(Map.Entry<Integer, Segment> segment: segments.entrySet()){
            if(distanceFromSegment < segment.getValue().getLength()){
                break;
            }
            distanceFromSegment -= segment.getValue().getLength();
        }
        return segments.get(getCurrentSegment(car)).getLength() - distanceFromSegment;
    }

    public double getTotalCourseLength(){
        return this.totalCourseLength;
    }
    
    public boolean isSpeedLimitInRange(int carNumber, int nextSegment){
        // get time remaining to finish the segment, and compare time when decelerating current speed to next speed limit
        if(segments.get(nextSegment) == null) return false;

        double changeToSegmentSpeedTime = Math.abs(segments.get(nextSegment).getSpeedLimit() - cars.get(carNumber).getCurrentSpeed()*3600)/cars.get(carNumber).getAccel();
        double distanceToStartBraking = cars.get(carNumber).getCurrentSpeed() * changeToSegmentSpeedTime - .5 * (cars.get(carNumber).getAccel()/3600) * Math.pow(changeToSegmentSpeedTime, 2);
        
        double remainingDist = getRemainingDistanceOfSegment(cars.get(carNumber)) - distanceToStartBraking;
        boolean startDecel =  remainingDist <= distanceToStartBraking; 
        return !isSameSpeed(carNumber, nextSegment) && startDecel; 
    }

    public boolean isSameSpeed(int carNumber, int nextSegment){
        double threshold = 0.0001;
        // temp
        double speedLimit = (double)segments.get(nextSegment).getSpeedLimit()/3600;
        double currentSpeed = cars.get(carNumber).getCurrentSpeed(); 
        if(Math.abs(speedLimit - currentSpeed) <= threshold){
            cars.get(carNumber).setState(cars.get(carNumber).getCoastingState());
            return true;
        }
        return false;
    }       
    // gets all the cars that are in the same lane
    public ArrayList<Car> getCarsInSameLane(Car car){
        ArrayList<Car> carsInLane = new ArrayList<Car>();
        for(int i = 0; i < cars.size(); i++){
            if(cars.get(i).getLaneNumber() == car.getLaneNumber() && cars.get(i) != car){
                carsInLane.add(cars.get(i));
            }
        }
        return carsInLane;
    }

    public boolean isCarAhead(int carNumber){
        // check if current car speed can reach the car in front
        ArrayList<Car> carsInLane = getCarsInSameLane(cars.get(carNumber));
        for(int i = 0; i < carsInLane.size(); i++){
            if(carNumber != i && cars.get(i).getLocation() > cars.get(carNumber).getLocation()){
                double distance = cars.get(carNumber).getCurrentSpeed() * cars.get(carNumber).getDriver().getDriverType().getFollowTime();
                
            }
        }
        return false;
    }
}