/**
 * Segment
 */
public class Segment {
    private int speedLimit;
    private double length;
    private int segmentNumber;

    Segment(int segmentNumber, double length, int speedLimit){
        this.segmentNumber = segmentNumber;
        this.length = length;
        this.speedLimit = speedLimit;
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

    public void setSpeedLimit(int speedLimit){
        this.speedLimit = speedLimit;
    }
    public void setLength(double length){
        this.length = length;
    }
    public void setNumber(int segmentNumber){
        this.segmentNumber = segmentNumber;
    }
}