/**
 * Braking
 */
public class Braking implements PositionState{
    Car car;
    public Braking(Car car){
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
        System.out.println("Already braking");
    }
}