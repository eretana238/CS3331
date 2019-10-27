import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

public class MyTableModel  extends AbstractTableModel {
	String [] columnNames = {"Segment Number", "Segment Length", "Segment Speed"};
	String[][] data = {{"something","nothing","nada"},{"something","nothing","nada"},{"something","nothing","nada"}};
	// Table data 
    MyTableModel(String[][] content){
		this.data = content;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
            return data.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	@Override
	public boolean isCellEditable (int row, int col) {
		return (true);
	}
	
	@Override 
	public void setValueAt (Object value, int row, int col) {
		data[row][col] = (String) value;
		fireTableCellUpdated (row, col);
	}

}
