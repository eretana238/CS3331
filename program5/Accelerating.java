/**
 * Accelerating
 */
public class Accelerating extends PositionState{
    public Accelerating(Car car){
         
    }
    @Override
    public void newPos() {
        System.out.println("getting position");

    }

    @Override
    public void decelForSegment(Segment current, Segment next) {
        System.out.println("getting deceleration for segment");

    }

    @Override
    public void decelForCarAheacd(Car current, Car next) {
        System.out.println("getting deceleration for car ahead");

    }
}