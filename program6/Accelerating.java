/**
 * Accelerating
 */
public class Accelerating implements PositionState{
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
    public void decelForSegment(Segment segment) {
        System.out.println("decelerating for segment");
    }

    @Override
    public void decelForCarAheacd(Car front) {
        System.out.println("decelerating for car ahead");
    }

    @Override
    public void needToBrake() {
        System.out.println("Braking");
        car.setState(car.getBrakingState());
    }
}