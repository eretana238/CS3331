package course.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class PushServer implements Runnable{
    private double[] carLocations;
    private ServerSocket ss;
    private Socket s;
    private InputStreamReader streamReader;
    private BufferedReader reader;

    public void initSocket(){
        try{
            ss = new ServerSocket(8888);

            s = ss.accept();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    public static void main(String[] args) throws IOException {
        Runnable pushServer = PushServer();
        System.out.println("Client connected");
        Runnable sim = new Simulation();

        Thread simulationThread = new Thread(sim);

        simulationThread.start();

    }
}
