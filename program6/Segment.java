/**
 * Segment
 */
public class Segment {
    private int speedLimit;
    private double length;
    private int segmentNumber;
    private int lanes;

    Segment(Segment segment){
        this.segmentNumber = segment.segmentNumber;
        this.length = segment.length;
        this.speedLimit = segment.speedLimit;
        this.lanes = segment.lanes;
    }
    
    Segment(int segmentNumber, double length, int speedLimit, int lanes){
        this.segmentNumber = segmentNumber;
        this.length = length;
        this.speedLimit = speedLimit;
        this.lanes = lanes;
    }

    public int getSpeedLimit(){
        return speedLimit;
    }
    public double getLength(){
        return length;
    }
    public int getSegmentNumber(){
        return segmentNumber;
    }
    public int getLanes(){
        return lanes;
    }

    public void setSpeedLimit(int speedLimit){
        this.speedLimit = speedLimit;
    }
    public void setLength(double length){
        this.length = length;
    }
    public void setNumber(int segmentNumber){
        this.segmentNumber = segmentNumber;
    }
    public void setLanes(int lanes){
        this.lanes = lanes;
    }
}