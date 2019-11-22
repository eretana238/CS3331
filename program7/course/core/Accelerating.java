package course.core;

public class Accelerating implements PositionState {
    Car car;
    public Accelerating(Car car){
        this.car = car;
    }
    // creates new speed and new location based on new speed
    @Override
    public void newPos(double timeIncrement) {
        double newSpeed = car.getCurrentSpeed() + (car.getAccel()/3600) * timeIncrement;
        car.setCurrentSpeed(newSpeed);
        double newLocation = car.getLocation() + car.getCurrentSpeed() * timeIncrement;
        car.setLocation(newLocation);
    }
    // changes state to breaking
    @Override
    public void decelForSegment(Segment segment, double timeIncrement) {
        car.setState(car.getBrakingState());
    }

    @Override
    public void decelForCarAhead(Car front, double timeIncrement) {
        car.setState(car.getBrakingState());
    }
    @Override
    public void needToBrake() {
        car.setState(car.getBrakingState());
    }
}