package course.core;

public interface PositionState{
    public void newPos(double timeIncrement);
    public void needToBrake();
    public void decelForSegment(Segment segment, double timeIncrement);
    public void decelForCarAhead(Car front, double timeIncrement);
}