import java.util.ArrayList;

public class Accelerating implements PositionState {
    Car car;
    public Accelerating(Car car){
        this.car = car;
    }
    private void carUpdate(double timeIncrement){
        double elapsedTime = car.getElapsedTime() + timeIncrement;
        car.setElapsedTime(elapsedTime);
        double newSpeed = car.getCurrentSpeed() - (car.getAccel()/3600) * timeIncrement;
        car.setCurrentSpeed(newSpeed);
        double newLocation = car.getLocation() + car.getCurrentSpeed() * timeIncrement;
        car.setLocation(newLocation);
        if(car.getElapsedTime() % 30.0 <= timeIncrement){
            ArrayList<Double> result = new ArrayList<Double>();
            result.add(car.getCurrentSpeed()*3600);
            result.add(car.getLocation());
            car.addResult(result);   
        }
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
        carUpdate(timeIncrement);
        car.setState(car.getCoastingState());
    }

    @Override
    public void decelForCarAhead(Car front, double timeIncrement) {
        carUpdate(timeIncrement);
    }

    @Override
    public void needToBrake() {
        System.out.println("Braking");
        car.setState(car.getBrakingState());
    }
}