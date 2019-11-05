public interface PositionState{
    public void newPos();
    public void needToBrake();
    public void decelForSegment(Segment segment);
    public void decelForCarAheacd(Car front);
}