/**
 * Accelerating
 */
public class Accelerating implements PositionState{
    Car car;
    public Accelerating(Car car){
        this.car = car;
    }
    @Override
    public void newPos(double timeIncrement) {
        double newSpeed = car.getCurrentSpeed() + (car.getAccel()/3600) * timeIncrement;
        car.setCurrentSpeed(newSpeed);
        double newLocation = car.getLocation() + car.getCurrentSpeed() * timeIncrement;
        car.setLocation(newLocation);
    }

    @Override
    public void decelForSegment(Segment segment, double timeIncrement) {
        double elapsedTime = car.getElapsedTime() + timeIncrement;
        car.setElapsedTime(elapsedTime);
        double newSpeed = car.getCurrentSpeed() - (car.getAccel()/3600) * timeIncrement;
        car.setCurrentSpeed(newSpeed);
        double newLocation = car.getLocation() + car.getCurrentSpeed() * timeIncrement;
        car.setLocation(newLocation);
        if(elapsedTime % 30.0 <= timeIncrement){
            System.out.printf("%.0f\t%.2f\t%.2f\t\t", elapsedTime, newSpeed*3600, newLocation);        
        }
    }

    @Override
    public void decelForCarAhead(Car front, double timeIncrement) {
        double elapsedTime = car.getElapsedTime() + timeIncrement;
        car.setElapsedTime(elapsedTime);
        double newSpeed = car.getCurrentSpeed() - (car.getAccel()/3600) * timeIncrement;
        car.setCurrentSpeed(newSpeed);
        double newLocation = car.getLocation() + car.getCurrentSpeed() * timeIncrement;
        car.setLocation(newLocation);
    }

    @Override
    public void needToBrake() {
        System.out.println("Braking");
        car.setState(car.getBrakingState());
    }
}