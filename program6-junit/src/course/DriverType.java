package course;

public class DriverType {

    private String typeName;
    private double followTime;
    private double speedLimit;
    private double maxAccel;

    DriverType(String typeName, double followTime, double speedLimit, double maxAccel){
        this.typeName = typeName;
        this.followTime = followTime;
        this.speedLimit = speedLimit;
        this.maxAccel = maxAccel;
    }

    // getters
    public String getTypeName(){
        return typeName;
    }
    public double getFollowTime(){
        return followTime;
    }
    public double getSpeedLimit(){
        return speedLimit;
    }
    public double getMaxAccel(){
        return maxAccel;
    }

    // setters
    public void setTypeName(String typeName){
        this.typeName = typeName;
    }
    public void setFollowTime(double followTime){
        this.followTime = followTime;
    }
    public void setSpeedLimit(double speedLimit){
        this.speedLimit = speedLimit;
    }
    public void setMaxAccel(double maxAccel){
        this.maxAccel = maxAccel;
    }
}