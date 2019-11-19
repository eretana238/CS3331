
public class Accelerating implements PositionState {
    Car car;
    public Accelerating(Car car){
        this.car = car;
    }
    @Override
    public void newPos(double timeIncrement) {
        double newSpeed = car.getCurrentSpeed() + (car.getAccel()/3600) * timeIncrement;
        car.setCurrentSpeed(newSpeed);
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
        System.out.println("Braking");
        car.setState(car.getBrakingState());
    }
}