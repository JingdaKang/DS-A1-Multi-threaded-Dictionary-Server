/**
 *
 * @author jingda Kang
 * @id 1276802
 *
 **/

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.net.ServerSocketFactory;
import java.net.*;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Locale;


public class DicClient {

    private static String serverAddress = "localhost";
    private static int port = 2022;



    public static void main(String[] args) {
        Locale.setDefault(new Locale("USA"));

        DicClient dicClient = new DicClient();


        try {
            dicClient.setPort(args[1]);
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Wrong port number! Using default port 2022.");
        }

        DicClientGUI gui = new DicClientGUI(dicClient);

    }

    public void setPort(String num){

        try {
            port = Integer.parseInt(num);
            if (port < 1024 || port > 65535){
                throw new PortUnreachableException();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (PortUnreachableException e){
            System.out.println("Port number should be in range [1024, 65535]. Using default port 2022.");
        }

    }



    public ResultPackage query(String word){

        ResultPackage result = new ResultPackage(RequestCode.FAIL, null);
        try {
            Socket clientSocket = new Socket(serverAddress, port);
            DataInputStream is = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

            JSONObject queryObj = new JSONObject();
            queryObj.put("RequestCode", RequestCode.QUERY);
            queryObj.put("word", word);
            try {
                os.writeUTF(queryObj.toString());
                os.flush();
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject repObj = new JSONObject(is.readUTF());
                int resultCode = repObj.getInt("resultCode");
                result = new ResultPackage(resultCode, (ArrayList) repObj.getJSONArray("meaning").toList());

            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            is.close();
            os.close();

        } catch (ConnectException e){
            int resultCode = RequestCode.FAIL_CONNECTION;
            result = new ResultPackage(resultCode, null);

        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    public ResultPackage add(WordAndMeanings pair){

        ResultPackage result = new ResultPackage(RequestCode.FAIL, null);
        try {
            Socket clientSocket = new Socket(serverAddress, port);
            DataInputStream is = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

            JSONObject addObj = new JSONObject();
            addObj.put("RequestCode", RequestCode.ADD);
            addObj.put("word", pair.getWord());
            addObj.put("meaning", pair.getMeanings());
            //send
            try {
                os.writeUTF(addObj.toString());
                os.flush();
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //receive
            try {
                JSONObject repObj = new JSONObject(is.readUTF());
                int resultCode = repObj.getInt("resultCode");
                result = new ResultPackage(resultCode, null);

            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            is.close();
            os.close();

        } catch (ConnectException e){
            int resultCode = RequestCode.FAIL_CONNECTION;
            result = new ResultPackage(resultCode, null);

        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    public ResultPackage remove(String word){

        ResultPackage result = new ResultPackage(RequestCode.FAIL, null);
        try {
            Socket clientSocket = new Socket(serverAddress, port);
            DataInputStream is = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

            JSONObject removeObj = new JSONObject();
            removeObj.put("RequestCode", RequestCode.REMOVE);
            removeObj.put("word", word);
            //send
            try {
                os.writeUTF(removeObj.toString());
                os.flush();
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //receive
            try {
                JSONObject repObj = new JSONObject(is.readUTF());
                int resultCode = repObj.getInt("resultCode");
                result = new ResultPackage(resultCode, null);

            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            is.close();
            os.close();

        } catch (ConnectException e){
            int resultCode = RequestCode.FAIL_CONNECTION;
            result = new ResultPackage(resultCode, null);

        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    public ResultPackage update(WordAndMeanings pair){

        ResultPackage result = new ResultPackage(RequestCode.FAIL, null);
        try {
            Socket clientSocket = new Socket(serverAddress, port);
            DataInputStream is = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());

            JSONObject updateObj = new JSONObject();
            updateObj.put("RequestCode", RequestCode.UPDATE);
            updateObj.put("word", pair.getWord());
            updateObj.put("meaning", pair.getMeanings());
            //send
            try {
                os.writeUTF(updateObj.toString());
                os.flush();
            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //receive
            try {
                JSONObject repObj = new JSONObject(is.readUTF());
                int resultCode = repObj.getInt("resultCode");
                result = new ResultPackage(resultCode, null);

            }catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            is.close();
            os.close();

        } catch (ConnectException e){
            int resultCode = RequestCode.FAIL_CONNECTION;
            result = new ResultPackage(resultCode, null);

        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }


}
