import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

class S {
    public static ArrayList<String> serverIPs;
    public final static int DEFAULT_PORT = 5025;
    private static S instance = null;
    static int indexOfThisServer;

    public static int receivedNonce;
    public static boolean received;

    public static boolean canSend;
    public static int nonceToSend;

    private S() throws UnknownHostException {
        serverIPs = new ArrayList<>();
        //serverIPs.add("192.168.10.105");
        //serverIPs.add("192.168.10.48");
        //serverIPs.add("192.168.1.239");
        //serverIPs.add("192.168.1.21");
        serverIPs.add("192.168.10.213");
        //serverIPs.add("192.168.56.1");
        indexOfThisServer = serverIPs.indexOf(getLocalIP());
        //serverIPs.remove(getLocalIP());

        receivedNonce = 0;
        received = false;
        canSend = false;
        nonceToSend = 0;
    }
    public static S getInstance() throws UnknownHostException {
            if(instance == null) {
                instance = new S();
            }
        return instance;
    }

    public static String getLocalIP() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        return ip.toString().split("/")[1];
    }


    public static void main(String[] args) throws UnknownHostException {
        S.getInstance();
        Blockchain bc = Blockchain.getInstance();
        int port = DEFAULT_PORT;
        ReadWrite rd = new ReadWrite();

        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                System.err.println("Error1");
            }
        }

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(5026);
            Socket newSock;
            System.out.println("Server Ips Size : " + serverIPs.size());
            for(int i = 0; i < serverIPs.size(); ++i){
                newSock = null;
                System.out.println("index of This Server : " + indexOfThisServer);
                System.out.println("index of my server: " + getLocalIP());
                if(indexOfThisServer > i){
                    System.out.println("here1");

                    newSock = serverSocket.accept();
                    ServerConnectionsThread sT = new ServerConnectionsThread(newSock);
                    sT.start();
                    System.out.println("Here 1 Tamamlandı.");
                    //newSock = new Socket("192.168.1.239", DEFAULT_PORT);
                }else if(indexOfThisServer < i){
                    System.out.println("here2");
                    System.out.println(serverIPs.get(i) + "Serveriyle bağlantı kurmaya çalışıyorum");
                    newSock = new Socket(serverIPs.get(i), 5026);
                    ServerConnectionsThread sT = new ServerConnectionsThread(newSock);
                    sT.start();
                    System.out.println("Here2 tamamlandı");
                }
                System.out.println("Server connection taraması bitti");
            }

            newSock = null;
            boolean stop = true;
            Boolean end = true;
            serverSocket = new ServerSocket(5025);

            while(stop == true) {
                System.out.println("Server TCP ready at the port: " + port + "..." );
                //Waiting for the connection with the client
                newSock = serverSocket.accept();
                System.out.println("accept");
                SocketThreads sT = new SocketThreads(newSock);
                sT.start();
            }
            //Thread th = new HandleConnectionThread(newSock);
            // th.start();
        }
        catch (IOException e) {
            System.err.println("Error2 " + e);
        }
    } // end main

}