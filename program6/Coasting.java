/**
 * Coasting
 */
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