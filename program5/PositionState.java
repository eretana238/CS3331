abstract class PositionState{
    public abstract void newPos(Car car);
    public void needToBrake(Car car){
        car.setState(new Braking());
    }
    public abstract void decelForSegment(Car car, Segment current, Segment next);
    public abstract void decelForCarAheacd(Car current, Car next);
}