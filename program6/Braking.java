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
        System.out.println("Already braking");

    }

    @Override
    public void decelForCarAheacd(Car front) {
        System.out.println("Already braking");

    }

    @Override
    public void needToBrake() {
        System.out.println("Already braking");
    }
}