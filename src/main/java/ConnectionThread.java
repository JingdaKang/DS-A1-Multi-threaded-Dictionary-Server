/**
 * @author jingda Kang
 * @id 1276802
 **/

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.net.ServerSocketFactory;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import jdk.nashorn.internal.scripts.JS;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class ConnectionThread implements Runnable {

    DicServer server;
    Socket client;
    String path;
    DataManagement dm;

    public ConnectionThread(DicServer server, Socket client, String path) {
        this.server = server;
        this.client = client;
        this.path = path;
        this.dm = new DataManagement(path);

    }

    @Override
    public void run() {
        try {

            DataInputStream is = new DataInputStream(client.getInputStream());
            DataOutputStream os = new DataOutputStream(client.getOutputStream());
            //System.out.println(System.getProperty("user.dir"));

            JSONObject inputObj = new JSONObject(is.readUTF());
            int requestCode = inputObj.getInt("RequestCode");
            int resultCode;
            ArrayList meaning = new ArrayList();

            switch (requestCode) {
                case RequestCode.QUERY:
                    if (dm.queryOp(inputObj.getString("word")) == RequestCode.SUCCESS) {
                        meaning = dm.getMeaningOp(inputObj.getString("word"));
                        resultCode = RequestCode.SUCCESS;
                    } else {
                        resultCode = RequestCode.FAIL;
                        meaning = null;
                    }

                    JSONObject resObj_q = new JSONObject();
                    resObj_q.put("resultCode", resultCode);
                    resObj_q.put("meaning", meaning);
                    os.writeUTF(resObj_q.toString());
                    os.flush();
                    break;
                case RequestCode.ADD:
                    //if the word doesn't exist in the dic
                    if (dm.queryOp(inputObj.getString("word")) == RequestCode.FAIL) {
                        boolean result = dm.addWordandMeaningOp(inputObj.getString("word"), (ArrayList) inputObj.getJSONArray("meaning").toList());
                        if (result == true) {
                            resultCode = RequestCode.SUCCESS;
                        } else resultCode = RequestCode.FAIL;

                    } else {//word exists
                        System.out.println("bababa");
                        meaning = dm.getMeaningOp(inputObj.getString("word"));
                        int count = 0;
                        for (Object input : (ArrayList) inputObj.getJSONArray("meaning").toList()) {
                            if (meaning.contains(input.toString())) {
                                count++;
                            }
                        }
                        //all meanings exist
                        if (count == ((ArrayList) inputObj.getJSONArray("meaning").toList()).size()) {
                            resultCode = RequestCode.FAIL;
                        } else {
                            boolean result = dm.addMeaningOp(inputObj.getString("word"), (ArrayList) inputObj.getJSONArray("meaning").toList());
                            if (result == true) {
                                resultCode = RequestCode.SUCCESS_WORD_EXIST;
                            } else resultCode = RequestCode.FAIL;
                        }
                    }
                    JSONObject resObj_a = new JSONObject();
                    resObj_a.put("resultCode", resultCode);
                    os.writeUTF(resObj_a.toString());
                    os.flush();
                    break;

                case RequestCode.REMOVE:
                    if (dm.queryOp(inputObj.getString("word")) == RequestCode.SUCCESS) {
                        boolean result = dm.removeOp(inputObj.getString("word"));
                        if (result == true) {
                            resultCode = RequestCode.SUCCESS;
                        } else resultCode = RequestCode.FAIL;
                    } else {
                        resultCode = RequestCode.FAIL;
                    }

                    JSONObject resObj_r = new JSONObject();
                    resObj_r.put("resultCode", resultCode);
                    os.writeUTF(resObj_r.toString());
                    os.flush();
                    break;
                case RequestCode.UPDATE:
                    //word already exist
                    boolean result = dm.updateMeaningOp(inputObj.getString("word"), (ArrayList) inputObj.getJSONArray("meaning").toList());
                    if (result == true) {
                        resultCode = RequestCode.SUCCESS;
                    } else resultCode = RequestCode.FAIL;


                    JSONObject resObj_u = new JSONObject();
                    resObj_u.put("resultCode", resultCode);
                    os.writeUTF(resObj_u.toString());
                    os.flush();
                    break;

            }

        } catch (IOException e) {
            System.out.println("Info transmission failed.");
        }

    }


}