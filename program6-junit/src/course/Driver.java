package course;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Driver {
    private Document doc;
    private String name;
    private String typeName;
    private DriverType driverType;

    // constructor
    Driver(String name, String typeName){
        this.name = name;
        this.typeName = typeName;
    }
    // getters
    public String getName(){
        return name;
    }
    public String getTypeName(){
        return typeName;
    }
    public DriverType getDriverType(){
        return driverType;
    }
    // setters
    // sets the document file form xml
    public void setDoc(Document doc){
        this.doc = doc;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTypeName(String typeName){
        this.typeName = typeName;
    }
    // sets the type of drivers
    public void setDriverType(String driverTypeName){
        NodeList nList = doc.getElementsByTagName("DRIVER_TYPE");

        for (int temp = 0; nList.item(temp).getChildNodes().getLength() > 1; temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String typeName = eElement.getElementsByTagName("TYPE_NAME").item(0).getTextContent().trim();

                if(driverTypeName.equals(typeName)){
                    double followTime = Double.parseDouble(eElement.getElementsByTagName("FOLLOW_TIME").item(0).getTextContent().trim());
                    double speedLimit = Double.parseDouble(eElement.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().trim());
                    double maxAccel = Double.parseDouble(eElement.getElementsByTagName("MAX_ACCELERATION").item(0).getTextContent().trim());
                    driverType = new DriverType(typeName, followTime, speedLimit, maxAccel);

                    break;
                }
                
            }
        }
    }
}