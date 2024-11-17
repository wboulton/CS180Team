import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;


public class servertest {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.write("Hi there");
            writer.println();
            writer.flush();
            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("What do you want to send to server?");
                String recieved = sc.nextLine();
                writer.write(recieved);
                writer.println();
                writer.flush();
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    writer.write("77288937499272"); // random quit code
                    writer.println();
                    writer.flush();
                }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
