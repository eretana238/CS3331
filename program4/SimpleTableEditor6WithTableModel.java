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

public class SimpleTableEditor6WithTableModel implements TableModelListener {

	public SimpleTableEditor6WithTableModel () {
		JFrame frame = new JFrame ("Editable table demo");
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu ("File");
		JMenuItem menuItemOpen = new JMenuItem ("Open");
		JMenuItem menuItemExit = new JMenuItem ("Exit");

		ActionListener al = new DemoActionListener();
		menuItemOpen.addActionListener(al);
		menuItemExit.addActionListener(al);

		menu.add(menuItemOpen);
		menu.add(menuItemExit);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);

		//Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JTable table = new JTable (new MyTableModel());
		table.getModel().addTableModelListener(this);

		// create a scroll pane and put table in the scroll pane
		JScrollPane scrollPane = new JScrollPane (table);
		table.setFillsViewportHeight(true);
		frame.add(scrollPane);

		// put window in center of screen
		frame.setLocationRelativeTo(null);		

		// Show frame
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		int row = e.getFirstRow();
		int col = e.getColumn();
		TableModel model = (TableModel)e.getSource();
		String colName = model.getColumnName(col);
		Object data = model.getValueAt(row,  col);
		System.out.println("new data is > " + data.toString());
	}
	
	
	public static void main(String[] args) {
		SimpleTableEditor6WithTableModel ste = new SimpleTableEditor6WithTableModel();
	}
}

