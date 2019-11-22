package course.test;

import course.Car;
import course.Coasting;
import course.Accelerating;
import course.Braking;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StatesTest {
    Car car = new Car();
    Coasting coast = new Coasting(car);
    Accelerating accel = new Accelerating(car);
    Braking brake = new Braking(car);
    double timeIncrement = 0.001;

    @Before
    public void setup(){
        car.setLocation(0.0);
        car.setAccel(20.00);
        car.setCarNumber(1);
        car.setState(car.getCoastingState());
    }
    @Test
    public void newPosTest(){
        double carLocation = car.getLocation();
        car.setState(coast);
        car.getState().newPos(timeIncrement);
        assertEquals(true,carLocation < car.getLocation());
        carLocation = car.getLocation();
        car.setState(accel);
        car.getState().newPos(timeIncrement);
        assertEquals(true,carLocation < car.getLocation());
        carLocation = car.getLocation();
        car.setState(brake);
        car.getState().newPos(timeIncrement);
        assertEquals(true,carLocation < car.getLocation());
    }
}
