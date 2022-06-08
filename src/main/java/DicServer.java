/**
 * @author jingda Kang
 * @id 1276802
 **/

import javax.net.ServerSocketFactory;
import java.net.*;
import java.io.*;


public class DicServer {

    //set a default port number, could also be set by user
    private static int port = 2022;

    private static String path = "./src/main/java/dictionary.json";
    //record the number of requests
    private static int numOfrequests = 0;
    //an instance to be used


    public static void main(String[] args) {
        try {
            DicServer dicServer = new DicServer();
            try {
                dicServer.setPort(args[0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Wrong port number ! Using default port 2022.");
            }
            try {
                dicServer.loadDic(args[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Wrong dictionary file! Using default dictionary file.");
            }


            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port);
            System.out.println("The server is running..");

            while (true) {

                System.out.println("Waiting for connections...");
                Socket client = serverSocket.accept();
                numOfrequests++;

                Thread dicThread = new Thread(new ConnectionThread(dicServer, client, path));
                dicThread.start();
                System.out.println(numOfrequests+" requests have been processed!");

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setPort(String num) {

        try {
            port = Integer.parseInt(num);
            if (port < 1024 || port > 65535) {
                throw new PortUnreachableException();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (PortUnreachableException e) {
            System.out.println("Port number should be in range [1024, 65535]. Using default port 2022.");
        }

    }

    public void loadDic(String p) {
        try {
            path = p;
            File dicFile = new File(path);
            if (!dicFile.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Using default dictionary file instead.");
        }
    }


}


