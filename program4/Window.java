import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Dimension;

import org.w3c.dom.Document;

class Window implements ActionListener{
    Document doc;
    HandleXML xmlData;
    String dir;
    String[][] content;
    int xSize = 0;
    int ySize = 0;

    // creates application when no xml data is obtained
    public Window(int xSize, int ySize) {
        xmlData = new HandleXML(dir);
        this.xSize = xSize;
        this.ySize = ySize;
        JFrame frame = new JFrame("Window");
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItemOpen = new JMenuItem("Open");
        JMenuItem menuItemSave = new JMenuItem("Save");
        JMenuItem menuItemSaveAs = new JMenuItem("Save As");
        JMenuItem menuItemExit = new JMenuItem("Exit");

        ActionListener al = new DemoActionListener(frame, doc, dir);
        menuItemOpen.addActionListener(al);
        menuItemSave.addActionListener(al);
        menuItemSaveAs.addActionListener(al);
        menuItemExit.addActionListener(al);

        menu.add(menuItemOpen);
        menu.add(menuItemSave);
        menu.add(menuItemSaveAs);
        menu.add(menuItemExit);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.pack();
        frame.setVisible(true);
    }
    // creates application when xml data is obtained from a correct directory
    public Window(int xSize, int ySize, String[][] content, String dir) {
        xmlData = new HandleXML(dir);
        this.xSize = xSize;
        this.ySize = ySize;
        this.content = content;
        this.dir = dir;

        JFrame frame = new JFrame("Window");
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItemOpen = new JMenuItem("Open");
        JMenuItem menuItemSave = new JMenuItem("Save");
        JMenuItem menuItemSaveAs = new JMenuItem("Save As");
        JMenuItem menuItemExit = new JMenuItem("Exit");

        JButton createButton = new JButton("Create");
        createButton.setBounds(100,300,100,40);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(200,300,100,40);

        ActionListener al = new DemoActionListener(frame, doc, dir);
        menuItemOpen.addActionListener(al);
        menuItemSave.addActionListener(al);
        menuItemSaveAs.addActionListener(al);
        menuItemExit.addActionListener(al);

        createButton.addActionListener(al);
        deleteButton.addActionListener(al);

        menu.add(menuItemOpen);
        menu.add(menuItemSave);
        menu.add(menuItemSaveAs);
        menu.add(menuItemExit);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.add(createButton);
        frame.add(deleteButton);

        // listening for the ctrl s command
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e){}
            @Override
            public void keyReleased(KeyEvent e){}

            @Override
            public void keyPressed(KeyEvent e){
                // save equivalent
                if ((e.getKeyCode() == KeyEvent.VK_S) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
                    xmlData.writeXML(dir);
                    System.out.println("Save");
                }
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTable table = new JTable(new MyTableModel(content));
        table.setMaximumSize(new Dimension(400,300));
        table.getModel().addTableModelListener(table);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        frame.add(scrollPane);
        frame.setLocationRelativeTo(null);

        frame.setSize(xSize, ySize);

        frame.pack();
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        // Nothing
    }
}