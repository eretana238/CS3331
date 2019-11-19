public class Coasting implements PositionState{
    Car car;
    public Coasting(Car car){
        this.car = car;
    }
    @Override
    public void newPos(double timeIncrement) {
        double newLocation = car.getLocation() + car.getCurrentSpeed() * timeIncrement;
        car.setLocation(newLocation);
    }

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