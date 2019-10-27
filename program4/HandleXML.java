import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class HandleXML {
    String[][] content;
    Document doc;
    String dir;

    public HandleXML(String dir){
        this.dir = dir;
        this.content = readXML(dir);
    }
    
    public String[][] readXML(String dir){
        try {
            // handle xml
            File inputFile = new File(dir);
            if(inputFile.exists()){
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(inputFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("SEGMENT");
                
                String[][] tempContent = new String[nList.getLength()][3];
                content = tempContent;
                
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        String segment = eElement.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent().trim();
                        String segmentLength = eElement.getElementsByTagName("LENGTH").item(0).getTextContent().trim();
                        String speedLimit = eElement.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().trim();
                        String[] arr = {segment, segmentLength, speedLimit};

                        int segmentNumber = Integer.parseInt(segment);
                        content[segmentNumber-1][0] = arr[0];
                        content[segmentNumber-1][1] = arr[1];
                        content[segmentNumber-1][2] = arr[2];
                    }
                }
            }
        }
        catch (Exception e) {
            // e.printStackTrace();
        }

        return content;
    } 

	public boolean writeXML(String dir){
        
        try {
            Element root = doc.getDocumentElement();
            
            for(int i = 0; i < content.length; i++){
                Element segment = doc.createElement("SEGMENT");
                root.appendChild(segment);
    
                Element segment_number = doc.createElement("SEGMENT_NUMBER");
                segment_number.appendChild(doc.createTextNode(content[i][0]));
                segment.appendChild(segment_number);    

                Element segment_length = doc.createElement("LENGTH");
                segment_length.appendChild(doc.createTextNode(content[i][1]));
                segment.appendChild(segment_length);    

                Element segment_speed = doc.createElement("SPEED_LIMIT");
                segment_speed.appendChild(doc.createTextNode(content[i][2]));
                segment.appendChild(segment_speed);    
            }
        
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            // insert directory
            StreamResult result = new StreamResult(new File(dir));
            transformer.transform(source, result);
        
            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
            System.out.println();
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}