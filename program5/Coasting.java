/**
 * Coasting
 */
public class Coasting implements PositionState{
    Car car;
    public Coasting(Car car){
        this.car = car;
    }
    @Override
    public void newPos() {
        // TODO Auto-generated method stub

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