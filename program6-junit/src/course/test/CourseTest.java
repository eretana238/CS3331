package course.test;

import course.Car;
import course.Course;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CourseTest {

    static final Course course = Course.getInstance();
    static ArrayList<Car> cars = new ArrayList<Car>();
    static Document doc;
    @Before
    public void setUp() {
        File file = new File("./course/input.xml");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Car car1 = new Car();
        car1.setLocation(0.0);
        car1.setCarNumber(1);

        Car car2 = new Car();
        car2.setLocation(1.0);
        car2.setCarNumber(2);
        cars.add(car1);
        cars.add(car2);

        course.setCars(cars);
    }
    @Test
    public void setSegmentsTest(){
        course.setSegments(doc);
        assertEquals(3, course.getSegments().size());
    }
    @Test
    public void setTotalCourseLengthTest(){
        course.setTotalCourseLength();
        assertEquals(3.0, course.getTotalCourseLength(), 0.001);
    }
    @Test
    public void createCircularCourseTest(){
        course.createCircularCourse(2);
        course.setTotalCourseLength();
        assertEquals(6.0, course.getTotalCourseLength(), 0.001);
    }
    @Test
    public void isSpeedLimitInRangeTest(){
        assertEquals(false, course.isSpeedLimitInRange(cars.get(0).getCarNumber(), 1));
        cars.get(0).setLocation(0.98999);
        cars.get(0).setCurrentSpeed(0.00833);
        assertEquals(true, course.isSpeedLimitInRange(cars.get(0).getCarNumber(), 1));
    }
    @Test
    public void isSameSpeedTest(){
        assertEquals(true, course.isSameSpeed(0,1));
        assertEquals(false, course.isSameSpeed(1, 2));
    }
    @Test
    public void isCarAheadTest(){
        assertEquals(false, course.isCarAhead(0));
    }
    @Test
    public void getCarsInSameLaneTest(){
        course.getSegments().get(0).setLanes(100);
        assertEquals(0, course.getCarsInSameLane(cars.get(0)).size());
        course.getSegments().get(0).setLanes(1);
        assertEquals(0, course.getCarsInSameLane(cars.get(0)).size());
    }
    @Test
    public void getCurrentSegmentTest(){
        cars.get(0).setLocation(0.0);
        assertEquals(1, course.getCurrentSegment(cars.get(0)));
        cars.get(0).setLocation(1.0);
        assertEquals(2, course.getCurrentSegment(cars.get(0)));
        cars.get(0).setLocation(0.999);
        assertEquals(1, course.getCurrentSegment(cars.get(0)));

    }
    @Test
    public void getRemainingDistanceOfSegmentTest(){
        cars.get(0).setLocation(5.0);
        assertEquals(1.0, course.getRemainingDistanceOfSegment(cars.get(0)), 0.001);
    }

}
