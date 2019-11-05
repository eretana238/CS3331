// Author: Esteban Retana
// Second latest commit: Modified setConstantSpeedLocation for considering max driver speed
// Latest commit: Added 2nd method setDistanceInIntervals for car upfront
// Date: 10/13

// Description: Created a program that finds the location of a car in a track composed of different segments for every 30 sec intervals.
// The program uses an xml file and stores each segment's data to compute the location
// The program uses an integral based approach to figure out the distance for every 30 seconds and finds the location regardless
// of driver type (driving style) and speed.

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
    private ArrayList<String[]> segment = new ArrayList<String[]>();
    private ArrayList<String[]> driverType = new ArrayList<String[]>();
    private ArrayList<String[]> driver = new ArrayList<String[]>();

    // sets the course info
    public void setSegment(Document doc){
        NodeList nList = doc.getElementsByTagName("SEGMENT");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String[] segmentInfo = new String[3];
                segmentInfo[0] = eElement.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent().trim();
                segmentInfo[1] = eElement.getElementsByTagName("LENGTH").item(0).getTextContent().trim();
                segmentInfo[2] = eElement.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().trim();
                segment.add(segmentInfo);
            }
        }
    }
    // sets the type of drivers
    public void setDriverType(Document doc){
        NodeList nList = doc.getElementsByTagName("DRIVER_TYPE");

        for (int temp = 0; nList.item(temp).getChildNodes().getLength() > 1; temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String[] driverTypeInfo = new String[4];
                driverTypeInfo[0] = eElement.getElementsByTagName("TYPE_NAME").item(0).getTextContent().trim();
                driverTypeInfo[1] = eElement.getElementsByTagName("FOLLOW_TIME").item(0).getTextContent().trim();
                driverTypeInfo[2] = eElement.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().trim();
                driverTypeInfo[3] = eElement.getElementsByTagName("MAX_ACCELERATION").item(0).getTextContent().trim();

                driverType.add(driverTypeInfo);
            }
        }
    }
    // sets the driver
    public void setDriver(Document doc){
        NodeList nList = doc.getElementsByTagName("DRIVER");
            
            double[] segmentLengths = new double[nList.getLength()];
            int[] speedLimit = new int[nList.getLength()+1];
            speedLimit[0] = 0;

			for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    
                    String[] driverInfo = new String[2];
					driverInfo[0] = eElement.getElementsByTagName("NAME").item(0).getTextContent().trim();
                    driverInfo[1] = eElement.getElementsByTagName("DRIVER_TYPE").item(0).getTextContent().trim();

                    driver.add(driverInfo);
				}
            }
    }
    // user input and prompt
    private static String cmdInterface(Scanner in){
        System.out.println("Please input xml file name:");
        String fileName = in.nextLine();
        return fileName;
    }
    // gets the document file form xml
    private static Document getDoc(File inputFile){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // displays data for each 30 sec interval
    public static void printCarResults(Car a, Car b, Car c){
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

        System.out.println("time\tCar a\t\t\tCar b\t\t\tCar c\n\tspeed\tlocation\tspeed\tlocation\tspeed\tlocation");
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

    public static void main(String[] args) {
        // system input
        Scanner in = new Scanner(System.in);
        File inputFile = new File(cmdInterface(in));
        while(!inputFile.exists()) {
            System.err.println("File not found!");
        	inputFile = new File(cmdInterface(in));	
        }
        Document doc = getDoc(inputFile);

        Course course = new Course();

        course.setSegment(doc);
        course.setDriverType(doc);
        course.setDriver(doc);

        // defines drivertypes
        DriverType[] types = new DriverType[course.driverType.size()];
        for(int i = 0; i < types.length; i++){
            String[] info = course.driverType.get(i);
            types[i] = new DriverType(info[0], Double.parseDouble(info[1]), Double.parseDouble(info[2]), Double.parseDouble(info[3]));
        }

        // defines drivers
        Driver[] drivers = new Driver[3];
        for(int i = 0; i < drivers.length; i++){
            String[] info = course.driver.get(i);

            if(info[1].equals(types[i].getTypeName()))
                drivers[i] = new Driver(info[0], types[i]);
        }
        // defines course info with segments
        double[] segmentLength = new double[course.segment.size()];
        int[] speedLimit = new int[course.segment.size()+1];
        speedLimit[0] = 0;
        for(int i = 0; i < segmentLength.length; i++){
            try{
                int index = Integer.parseInt(course.segment.get(i)[0]);
                segmentLength[index-1] = Double.parseDouble(course.segment.get(i)[1]);
                speedLimit[index] = Integer.parseInt(course.segment.get(i)[2]);   
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        
        // create car instances 
        Car carA = new Car(drivers[0]);
        Car carB = new Car(drivers[1]);
        Car carC = new Car(drivers[2]);

        // find out the car data
        carA.setRunningData(speedLimit, segmentLength);
        carA.setTotalTime();
        carA.setDistanceInIntervals(speedLimit);

        carB.setRunningData(speedLimit, segmentLength);
        carB.setTotalTime();
        carB.setDistanceInIntervals(carA, drivers[1].getType().getFollowTime(), speedLimit);

        carC.setRunningData(speedLimit, segmentLength);
        carC.setTotalTime();
        carC.setDistanceInIntervals(carB, drivers[2].getType().getFollowTime(), speedLimit);

        printCarResults(carA, carB, carC);
    }
}