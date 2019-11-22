package course.test;

import course.Simulation;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class SimulationTest {
    static final Simulation sim = new Simulation();
    @Before
    public void setUp() {
        File file = new File("./course/input.xml");
        sim.setDoc(file);
        sim.initSIM();
    }
    @Test
    public void getAmountOfCarsTest(){ assertEquals(3,sim.getAmountOfCars()); }
    @Test
    public void startSIMTest(){
        sim.startSIM();
        assertEquals(true, sim.allCarsFinished());
        assertEquals(false, sim.allCarsFinished());
    }
    @Test
    public void allCarsFinishedTest(){ assertEquals(true, sim.allCarsFinished()); }
    @Test
    public void getMaxResultSizeTest(){
        sim.initSIM();
        sim.startSIM();
        assertEquals(true, sim.getMaxResultSize() > 1);
    }
}
