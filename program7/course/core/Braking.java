package course.core;

public class Braking implements PositionState {
    Car car;
    public Braking(Car car){
        this.car = car;
    }
    // decelerates speed and computes new location with timeIncrement
    @Override
    public void newPos(double timeIncrement) {
        double newSpeed = car.getCurrentSpeed() - (car.getAccel()/3600) * timeIncrement;
        car.setCurrentSpeed(newSpeed);
        double newLocation = car.getLocation() + car.getCurrentSpeed() * timeIncrement;
        car.setLocation(newLocation);
    }

    @Override
    public void decelForSegment(Segment segment, double timeIncrement) {
        System.out.println("Already braking");

    }

    @Override
    public void decelForCarAhead(Car front, double timeIncrement) {
        System.out.println("Already braking");

    }

    @Override
    public void needToBrake() {
        System.out.println("Already braking");
    }
}