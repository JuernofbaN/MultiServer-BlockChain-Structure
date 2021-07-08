import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketThreads extends Thread{
    Socket newSock;
    public SocketThreads(Socket parameter) {
        // store parameter for later user
        newSock = parameter;
    }
    public void run()
    {
        try {
            // Displaying the thread that is running
            System.out.println(
                    "Thread " + Thread.currentThread().getId()
                            + " is running");
            Blockchain bc = Blockchain.getInstance();
            ReadWrite rd = new ReadWrite();
            boolean end = true;
            while(end == true){
                System.out.println("qq");
                BufferedReader is = new BufferedReader(new InputStreamReader(newSock.getInputStream()));
                PrintWriter os = new PrintWriter(newSock.getOutputStream(), true);
                String inputLine = is.readLine();
                String[] inputStrings = inputLine.split(":"); // String fromAd, String toAdd, String token, int amount
                bc.addBlock(inputStrings[0],inputStrings[1],inputStrings[2],Integer.parseInt(inputStrings[3]));
                System.out.println("Received -> " + inputLine);
                rd.writeBlockChain(bc);
                os.println("Transaction is verified.");
                os.flush();

                if(inputLine.equals("END")) {
                    System.out.println("ciclo uscita");
                    end = false;
                    is.close();
                    os.close();
                    newSock.close();
                }

                if(inputLine.equals("STOP")) {
                    os.println( "Server aborted!");
                    is.close();
                    os.close();
                    newSock.close();
                    end = false;
                }

            }
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}
