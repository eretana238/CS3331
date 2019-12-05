package course.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PushClient implements Runnable{
    private InputStreamReader streamReader;
    private BufferedReader clientReader;
    private PrintWriter clientWriter;
    private Socket socket;
    private boolean initilized = false;

    private void initSocket(){
        try{
            socket = new Socket("127.0.0.1", 8888);
            streamReader = new InputStreamReader(socket.getInputStream());
            clientReader = new BufferedReader(streamReader);
            clientWriter = new PrintWriter(socket.getOutputStream());
            initilized = true;
        }
        catch(UnknownHostException e){
            System.err.println("UnknownHostException");
            e.printStackTrace();
        }
        catch (IOException e){
            System.err.println("IOException");
            e.printStackTrace();
        }

    }

    private boolean isInitilized(){
        return initilized;
    }

    private String getXMLPackage(){
        return "";
    }

    @Override
    public void run() {

    }


    public static void main(String[] args) throws InterruptedException {
        Runnable client = new PushClient();
        Thread clientThread = new Thread(client);
        clientThread.start();

        Runnable view = new CourseView();

        Thread viewThread = new Thread(view);
        viewThread.start();
    }
}
