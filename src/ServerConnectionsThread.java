import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerConnectionsThread extends Thread {
    Socket newSock;
    public ServerConnectionsThread(Socket parameter) {
        // store parameter for later user
        newSock = parameter;
    }

    public void run()
    {
        try {
            OutputStream os = null;
            os = newSock.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            InputStream is = null;
            PrintStream ps = new PrintStream(newSock.getOutputStream()); // SEND DATA
            PrintWriter osP = new PrintWriter(newSock.getOutputStream(), true);
            BufferedReader br = new BufferedReader(new InputStreamReader(newSock.getInputStream())); // READ DATA
            S s;
            while (true) {
                s = S.getInstance();
                if(s.canSend){
                    System.out.println("Diger Servere Yollama booleanları aktif: " + s.nonceToSend);
                    s.canSend = false;
                    ps.println(s.nonceToSend);
                    //osP.println(s.nonceToSend);
                    //osP.flush();
                }else{
                    ps.println("-1");
                }
                String str, str1;
                System.out.println("String bekliyo Diger SErverdan" );

                str = br.readLine();
                System.out.println("String KISMI GEÇTİ" );

                if(!str.equals("-1")){
                        System.out.println("Coming message from another SERVER:" + str);
                        s.receivedNonce = Integer.parseInt(str);
                        str1 = "I received your nonce; " + s.receivedNonce;
                        System.out.println(str1);
                        s.received = true;
                        //ps.println(str1);
                        continue;
                    }
            }
        }catch (Exception e){
            System.out.println("Exception is caught in ServerConnectionsThread");
            e.printStackTrace();
        }
    }
}
