package course.test;

import course.Car;
import course.Course;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import static org.junit.Assert.assertEquals;

public class CarTest {
    Car car = new Car();
    Course course = Course.getInstance();
    @Before
    public void setUp() {
        File file = new File("./course/input.xml");
        Document doc;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            car.setDoc(doc);
            car.setCurrentSpeed(0.001);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void assignDriverTest(){
        assertEquals(false ,car.getDriver() != null);
        car.assignDriver();
        assertEquals(true ,car.getDriver() != null);
    }
    @Test
    public void isInSameLaneTest(){
        assertEquals(true, car.isInSameLane(0));
        assertEquals(false, car.isInSameLane(1));
    }
    @Test
    public void needsDeceleratingTest(){
        assertEquals(false, car.needsDecelerating(course, 0));
    }
    @Test
    public void needsAcceleratingTest(){
        assertEquals(false, car.needsAccelerating(course, 0, 0.001));
    }
    @Test
    public void needsConstantTest(){
        assertEquals(false, car.needsConstant(course, course.getSegments().get(0), 0.001));
    }
    @Test
    public void runTest(){
        double carLocation = car.getLocation();
        car.run(2.00);
        assertEquals(true,carLocation < car.getLocation());
    }
}
