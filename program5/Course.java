import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Course {
    private HashMap<Integer,Segment> segments = new HashMap<Integer,Segment>();
    private Document doc;
    // private static int segmentNumber = 1;
    

    // course gets the segment in which the car is in
    public int getCarLocationSegment(Car car){
        for(Map.entry<Integer, Segment> entry : segments.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
    // course gets the location of the car every 30 secs
    public void getCarLocationsInIntervals(Car car){
        double[] locations;

    }
    // sets the course info
    public void setSegments(Document doc){
        NodeList nList = doc.getElementsByTagName("SEGMENT");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                int segmentNumber = Integer.parseInt(eElement.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent().trim());
                double segmentLength = Double.parseDouble(eElement.getElementsByTagName("LENGTH").item(0).getTextContent().trim());
                int segmentSpeed = Integer.parseInt(eElement.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().trim());

                Segment seg = new Segment(segmentNumber, segmentLength, segmentSpeed);
                segments.put(seg.getSegmentNumber(), seg);
            }
        }
    }

    // displays data for each 30 sec interval
    public void printCarResults(Car a, Car b, Car c){
        ArrayList<Double> aResults = a.getResults();
        ArrayList<Double> bResults = b.getResults();
        ArrayList<Double> cResults = c.getResults();

        ArrayList<Double> aSpeed = a.getSpeed();
        ArrayList<Double> bSpeed = b.getSpeed();
        ArrayList<Double> cSpeed = c.getSpeed();

        // holds the last speed value temporarily
        double[] tempSpeed = new double[3];
        // holds the last location value temporarily
        double[] tempResult = new double[3];

        String aTypeName = a.getDriver().getDriverType().getTypeName();
        String bTypeName = b.getDriver().getDriverType().getTypeName();
        String cTypeName = c.getDriver().getDriverType().getTypeName();

        System.out.println("time\tCar a " + aTypeName + "\tCar b " + bTypeName + "\t\tCar c " + cTypeName + "\n\tspeed\tlocation\tspeed\tlocation\tspeed\tlocation");
        int i = 0;
    
        while(true){
            // initial print
            if(i == 0)
                System.out.println("0\t0.0\t0.00\t\t0.0\t0.00\t\t0.0\t0.00");
            // prints seconds in 30 sec intervals
            System.out.print((i+1)*30 + "\t");

            // car a is still in the course
            if(aResults.size() != 0){
                tempSpeed[0] = aSpeed.remove(0);
                tempResult[0] = aResults.remove(0);
                System.out.printf("%.1f\t%.2f\t\t",tempSpeed[0] , tempResult[0]);
            }
            // car a finishes course
            else{
                System.out.printf("%.1f\t%.2f\t\t",tempSpeed[0], tempResult[0]);
            }

            // car b is still in the course
            if(bResults.size() != 0 && i >= 2){
                tempSpeed[1] = bSpeed.remove(0);
                tempResult[1] = bResults.remove(0);
                System.out.printf("%.1f\t%.2f\t\t", tempSpeed[1], tempResult[1]);
            }
            // car b is finished
            else if(bResults.size() == 0)
                System.out.printf("%.1f\t%.2f\t\t", tempSpeed[1], tempResult[1]);
            // car b hasn't started the course
            else if(i < 2)
                System.out.print("0.0\t0.0\t\t");

            // car c is still in the course
            if(cResults.size() != 0 && i >= 4){
                tempSpeed[2] = cSpeed.remove(0);
                tempResult[2] = cResults.remove(0);
                System.out.printf("%.1f\t%.2f\t\t", cSpeed.remove(0), cResults.remove(0));
            }
            // car c is finished
            else if(cResults.size() == 0)
                System.out.printf("%.1f\t%.2f\t\t", tempSpeed[2], tempResult[2]);
            // car c hasn't started the course
            else
                System.out.print("0.0\t0.00\t\t");
            // all cars have finished the course
            if(aResults.size() == 0 && bResults.size() == 0 && cResults.size() == 0)
                break;
            i++;
            System.out.println();
        }
    }
}