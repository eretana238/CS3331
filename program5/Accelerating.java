/**
 * Accelerating
 */
public class Accelerating implements PositionState{
    Car car;
    public Accelerating(Car car){
        this.car = car;
    }
    @Override
    public void newPos() {

    }

    @Override
    public void decelForSegment(Segment segment) {
        // TODO Auto-generated method stub
    }

    @Override
    public void decelForCarAheacd(Car front) {
        // TODO Auto-generated method stub
    }

    @Override
    public void needToBrake() {
        System.out.println("Braking");
        car.setState(car.getBrakingState());
    }
}