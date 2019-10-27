// Author: Esteban Retana
// Second latest commit: Added WRITE methods
// Latest commit: Added SHOW, CHANGE, PREV AND EXIT methods
// Date: 9/15

// Description: Used the java file provided and added several methods, each performing a specific command by the user. 
// Based on the commands given from a user, the program will find the method corresponding the command and navigate 
// through the list of nodes. Each method accesses the list of nodes by creating a NodeList from the head list and choosing the item index. Lastly,
// to prevent from any unusual/unexpected user input, several try and catches, and if statements tell the user to corrent their input.


import java.io.File;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLParser {

	static int currentElem = 0;
	// SHOW print the contents of the current element
	public static void SHOW(NodeList nodes){
		NodeList children = nodes.item(currentElem).getChildNodes();
		// traverse through children nodes
		for(int i = 0; i < children.getLength(); i++){
			Node item = children.item(i);
			// only prints node if not empty
			if(!item.getTextContent().trim().isEmpty()){
				System.out.print(item.getNodeName() + "=" + item.getTextContent().trim() + "  ");
			}
		}
		System.out.println();
	}
	// CHANGE <param> of the current element to value
	public static void CHANGE(NodeList nodes, String tag, String val){
		Node currentNode = nodes.item(currentElem);
		try {
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				Element nElement = (Element) currentNode;
				nElement.getElementsByTagName(tag).item(0).setTextContent(val);
			}
			SHOW(nodes);

		} catch (Exception e) {
			System.out.println("Element doesn't exit. Please specify a valid Element tag name.");
		}
	}
	// WRITE filename write the DOM object to an XML text file
	public static void WRITE(Document doc, String fileName){
		  // write the content into xml file
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(".\\" + fileName + ".xml"));
			transformer.transform(source, result);
		
			// Output to console for testing
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
			System.out.println();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// NEXT output the contents of the next element
	public static void NEXT(NodeList nodes){
		// traverse through children nodes
		for(int i = currentElem+1; i < nodes.getLength(); i++){
			// moves currentElement index to the nearest next element only when not empty
			if(!nodes.item(i).getTextContent().trim().isEmpty()){
				currentElem = i;
				break;
			}
		}
		SHOW(nodes);
	}
	// PREVIOUS output the contents of the previous element
	public static void PREV(NodeList nodes){
		// traverse through children nodes
		for(int i = currentElem-1; i < nodes.getLength(); i--){
			// moves currentElement index to the nearest previous element only when not empty
			if(!nodes.item(i).getTextContent().trim().isEmpty()){
				currentElem = i;
				break;
			}
		}
		SHOW(nodes);
	}
	// checks if the input string arry is the correct size
	public static boolean correctInput(String[] arr, int size){
		if(arr.length == size)
			return true;
		else if(arr.length < size)
			System.err.println("Missing arguments for " + arr[0] + " or " + arr[0].charAt(0) +". Please re-input");
		else
			System.err.println("Too many arguments for " + arr[0] + " or " + arr[0].charAt(0) +". Please re-input");
		
		return false;
	}
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int exitErrors = 0;
		try {
			File inputFile = new File("input.txt");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			// upper level nodes
			Node head = doc.getFirstChild();
			NodeList nodes = head.getChildNodes();
			// checks if the current element is null/empty. If so, it gets the next closest non-empty element
			while(!nodes.item(currentElem).hasChildNodes()){
				currentElem++;
			}
			// Display on startup 
			SHOW(nodes);

			// command line user input loop
			while(true){
				// gets input
				String command = in.nextLine();
				String[] arr = command.split(" ");
				
				// commands
				if(arr[0].equals("SHOW") || arr[0].equals("S")){
					if(correctInput(arr, 1)) SHOW(nodes);
				}
				else if(arr[0].equals("CHANGE") || arr[0].equals("C")){
					if(correctInput(arr, 3)) CHANGE(nodes, arr[1], arr[2]);
				}
				else if(arr[0].equals("WRITE") || arr[0].equals("W")){
					if(correctInput(arr, 2)) WRITE(doc, arr[1]);
				}
				else if(arr[0].equals("NEXT") || arr[0].equals("N")){
					if(correctInput(arr, 1)) NEXT(nodes);
				}
				else if(arr[0].equals("PREVIOUS") || arr[0].equals("P")){
					if(correctInput(arr, 1)) PREV(nodes);
				}
				else if(arr[0].equals("EXIT") || arr[0].equals("E")){
					if(correctInput(arr, 1)) break;
				}
				// Error output
				else{
					exitErrors++;
					if(exitErrors % 3 == 0)
						System.err.println("Cannot recognize command. If you wish to exit type \"EXIT\"");
					else
						System.err.println("Cannot recognize command. Please re-input.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

