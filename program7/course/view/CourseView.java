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

    public void paintComponent(Graphics g){
        Rectangle r = g.getClipBounds();
        g.clearRect(r.x,r.y, r.width, r.height);
        if(carLocations != null){
            g.setColor(Color.black);
            g.drawOval(25,25,300,300);
            drawCar(g, carLocations[0]);
        }
    }

    public void drawCar(Graphics g, double carLoc){
//        angle = (current location / totalCourseDistance) * 360 deg
//        radius = circumference / (2 * pi)

        double angle  = (carLoc / 3.0) * 360;

        double radius = 150;
        double xPos = 170 + Math.cos(angle)*radius;
        double yPos = 170 + Math.sin(angle)*radius;
        g.setColor(Color.CYAN);
        g.fillOval((int)xPos, (int)yPos, 10, 10);
    }

    public CourseView(){
        JFrame frame = new JFrame();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open xml");
        JMenuItem exit = new JMenuItem("Exit");



        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equalsIgnoreCase("Open xml")){
                    // opens filechooser to choose file to open
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                    int result = fileChooser.showOpenDialog(frame);

                    // proceed with new file
                    if(result == JFileChooser.APPROVE_OPTION){
                        File selectedFile = fileChooser.getSelectedFile();
                        pathName = selectedFile.getAbsolutePath();
                        sendPath(pathName);
                    }
                }
                else if(e.getActionCommand().equalsIgnoreCase("Exit")){
                    System.exit(0);
                }
            }
        };
        open.addActionListener(al);
        exit.addActionListener(al);

        menu.add(open);
        menu.add(exit);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.add(this);

        frame.setPreferredSize(new Dimension(400,400));

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
        if(segmentLocations.length == 0)
            this.segmentLocations = segmentLocations;
        if (courseLength == 0.0) this.courseLength = courseLength;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(20);
                if(running){
                    this.repaint();
                }
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
