import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;


public class DemoActionListener implements ActionListener, ItemListener, TableModelListener{
	private JFrame frame;
	// Document doc;
	private String dir;
	HandleXML xmlData;

	DemoActionListener(JFrame frame, Document doc, String dir){
		this.frame = frame;
		this.dir = dir;
		xmlData = new HandleXML(dir);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println("itemStateChanged " + e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed " + e.getActionCommand());
		if (e.getActionCommand().equalsIgnoreCase("Exit")) {
			System.exit(0);
		}
		// performs open
		if (e.getActionCommand().equalsIgnoreCase("Open")) {
			// opens filechooser to choose file to open
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int result = fileChooser.showOpenDialog(frame);

			// proceed with new file
			if(result == JFileChooser.APPROVE_OPTION){
				File selectedFile = fileChooser.getSelectedFile();
				dir = selectedFile.getAbsolutePath();
				

				frame.setVisible(false);
				frame.dispose();

				String[][] content = xmlData.readXML(dir);

				if(xmlData.content != null){
					Window frame = new Window(400, 400, content, dir);
				}
				else{
					Window frame = new Window(400,400);
				}
			}
		}
		// performs save
		if(e.getActionCommand().equalsIgnoreCase("Save")){
			xmlData.writeXML(dir);
		}
		// performs save as, similar to open
		if(e.getActionCommand().equalsIgnoreCase("Save As")){
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			fileChooser.setDialogTitle("Specify a file to save");
			int status = fileChooser.showSaveDialog(frame);

			if(status == JFileChooser.APPROVE_OPTION){
				File selectedFile = fileChooser.getSelectedFile();
				dir = selectedFile.getAbsolutePath();
			}
			// save xml file call
			xmlData.writeXML(dir);

		}
		if(e.getActionCommand().equalsIgnoreCase("Create")){

		}
		if(e.getActionCommand().equalsIgnoreCase("Delete")){

		}
	}
	@Override
    public void tableChanged(TableModelEvent e) {
        // TODO Auto-generated method stub
        int row = e.getFirstRow();
        int col = e.getColumn();
        TableModel model = (TableModel) e.getSource();
        String colName = model.getColumnName(col);
        Object data = model.getValueAt(row, col);
        xmlData.content[row][col] = data.toString();
    }
}
