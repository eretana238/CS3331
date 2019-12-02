package course.view;

import course.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class CourseView extends JComponent implements Runnable{
    private String pathName;
    private Controller controller;
    private double[] carLocations;
    private double[] segmentLocations;
    private double courseLength = 0.0;
    private boolean running = false;
    private boolean stop = false;

    public void paint(Graphics g){
        Rectangle r = g.getClipBounds();
        g.clearRect(r.x,r.y, r.width, r.height);

        int xPos = 85;
        int size = 300;
        if(carLocations != null){
            g.setColor(Color.black);
            g.drawOval(xPos-5,25,size,size);
            fillLocation(g, segmentLocations, Color.black, 7);
            fillLocation(g, carLocations, Color.red, 12);
        }
    }

    public void fillLocation(Graphics g, double[] locations, Color color, int size){
        for(double loc : locations){
            int x = 225;
            int y = 170;
            double[] l = drawLocation(loc, x, y);
            g.setColor(color);
            g.fillOval((int)l[0], (int)l[1], size, size);
        }
    }

    public double[] drawLocation(double carLoc, int x, int y){
//        angle = (current location / totalCourseDistance) * 360 deg
//        radius = circumference / (2 * pi)
        double radians  = Math.toRadians((carLoc / courseLength) * 360);
        double radius = 150;

        double[] locations = new double[2];
        locations[0] = x + Math.cos(radians)*radius;
        locations[1] = y + Math.sin(radians)*radius;

        return locations;
    }

    public CourseView(){
        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open xml");
        JMenuItem exit = new JMenuItem("Exit");

        JPanel buttonsPanel = new JPanel();

        JButton START = new JButton("Start");
        JButton PAUSE = new JButton("Pause");
        JButton CONTINUE = new JButton("Continue");
        JButton STOP = new JButton("Stop");

        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equalsIgnoreCase("open xml")){
                    // opens filechooser to choose file to open
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                    int result = fileChooser.showOpenDialog(frame);

                    // proceed with new file
                    if(result == JFileChooser.APPROVE_OPTION){
                        File selectedFile = fileChooser.getSelectedFile();
                        pathName = selectedFile.getAbsolutePath();
                    }
                }
                else if(e.getActionCommand().equalsIgnoreCase("exit")){
                    System.exit(0);
                }
            }
        };

        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equalsIgnoreCase("start")){
                    if(!controller.getRunningState() && !running) sendPath(pathName);
                }
                else if(e.getActionCommand().equalsIgnoreCase("pause")){
                    controller.setRunningState(false);
                }
                else if(e.getActionCommand().equalsIgnoreCase("continue")){
                    controller.setRunningState(true);
                }
                else if(e.getActionCommand().equalsIgnoreCase("stop")){
                    controller.setRunningState(false);
                    running = false;
                    stop = true;
                }
            }
        };
        open.addActionListener(al);
        exit.addActionListener(al);

        START.addActionListener(buttonListener);
        PAUSE.addActionListener(buttonListener);
        CONTINUE.addActionListener(buttonListener);
        STOP.addActionListener(buttonListener);

        menu.add(open);
        menu.add(exit);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        buttonsPanel.add(START);
        buttonsPanel.add(PAUSE);
        buttonsPanel.add(CONTINUE);
        buttonsPanel.add(STOP);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 400;      //make this component tall
        c.ipadx = 200;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        frame.add(this, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100;      //make this component tall
        c.ipadx = 200;
        c.weightx = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        frame.add(buttonsPanel, c);

        frame.setPreferredSize(new Dimension(500,500));

        // put window in center of screen
        frame.setLocationRelativeTo(null);

        // Show frame
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public void toggleRunning(){
        this.running = !running;
    }
    public void sendPath(String pathName){
        controller.setPathName(pathName);
    }

    public void update(double[] carLocations, double[] segmentLocations, double courseLength){
        if(!running) toggleRunning();
        this.carLocations = carLocations;
        this.segmentLocations = segmentLocations;
        this.courseLength = courseLength;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(0);
                if(running && !stop){
                    this.repaint();
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
