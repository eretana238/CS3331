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
    public void changeCarState(double timeIncrement){
        while(true){
            // run each car for timeIncrement
            for(int i = 0; i < cars.length; i++){
                Car car = cars[i];
                Segment currentSegment = segments.get(getCarLocationSegment(car));
                Segment nextSegment = segments.get(currentSegment.getSegmentNumber()+1);
                
                // i*60 <= car.getElapsedTime() makes sure that each car starts 1 minute apart from each other
                if((car.getCurrentSpeed() != car.getMaxSpeed() || car.getCurrentSpeed() != currentSegment.getSpeedLimit()) && car.getState().getClass().getName() != "Accelerating" && car.getElapsedTime() >= i*60) car.accel();

                
                else if(car.getCurrentSpeed() == car.getMaxSpeed() || car.getCurrentSpeed() == currentSegment.getSpeedLimit() && car.getState().getClass().getName() != "Coasting" && car.getElapsedTime() >= i*60) car.coast();
                
            
                else if(nextSegment.getSpeedLimit() < currentSegment.getSpeedLimit() && isSpeedLimitInRange(i,nextSegment) && car.getElapsedTime() >= i*60){
                    
                }

                else if(nextSegment.getSpeedLimit() < currentSegment.getSpeedLimit() && isCarAhead(i) && car.getElapsedTime() >= i*60){
                    
                }

                car.run(timeIncrement);    
            }
        }
    }
    public boolean isSpeedLimitInRange(int carNumber, Segment nextSegment){
        // check if the car negative decel can reach next speed limit
        return false;
    }
    public boolean isCarAhead(int carNumber){
        // check if current car speed can reach the car in front
        return false;
    }
}