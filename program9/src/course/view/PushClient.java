package course.view;

import java.io.IOException;
import java.net.Socket;

public class PushClient {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 8888);
        Runnable view = new CourseView();

        Thread viewThread = new Thread(view);
        viewThread.start();
    }
}
