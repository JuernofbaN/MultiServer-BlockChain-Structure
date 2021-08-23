import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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
                if(inputStrings[0].equals("getBalance")){
                    int balance = bc.getBalance(inputStrings[1], "VCloud");
                    os.println(balance);
                    os.flush();
                }else if(inputStrings[0].equals("getTransactions")) {
                    String transactions = bc.getTransactions(inputStrings[1]);
                    os.println(transactions);
                    os.flush();
                }else{
                        bc.addBlock(inputStrings[0],inputStrings[1],inputStrings[2],Integer.parseInt(inputStrings[3]));
                        System.out.println("Received -> " + inputLine);
                        rd.writeBlockChain(bc);
                        os.println("Transaction is verified.");
                        os.flush();
                    }
                }
            } catch (UnknownHostException unknownHostException) {
            unknownHostException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
