public interface PositionState{
    public void newPos(double timeIncrement);
    public void needToBrake();
    public void decelForSegment(Segment segment);
    public void decelForCarAheacd(Car front);
}