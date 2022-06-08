/**
 *
 * @author jingda Kang
 * @id 1276802
 *
 **/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataManagement {
    private String p = "./src/main/java/dictionary.json";
    private JSONArray jsonArray;
    private HashMap<String, ArrayList> dic;


    public DataManagement(String path) {
        this.p = path;
        JSONObject jsonData = ReadJson(path);
        jsonArray = jsonData.getJSONArray("Content");
        dic = new HashMap<String, ArrayList>();
        for (int i=0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            dic.put(jsonObject.getString("word"), (ArrayList) jsonObject.getJSONArray("meaning").toList());
        }

    }

    public synchronized int queryOp(String word){

        if (dic.containsKey(word)){
            return RequestCode.SUCCESS;
        }else {
            return RequestCode.FAIL;
        }
    }

    public synchronized ArrayList getMeaningOp(String word){

        return (ArrayList) dic.get(word);
    }

    public synchronized boolean addWordandMeaningOp(String word, ArrayList meaning){

        HashMap newPair = new HashMap();
        newPair.put("word", word);
        newPair.put("meaning", meaning);
        JSONObject newObj = new JSONObject(newPair);
        jsonArray.put(newObj);
        JSONObject newjsonData = new JSONObject();
        newjsonData.put("type", "WordDictionary");
        newjsonData.put("Content", jsonArray);
        try {
            WriteJson(p, newjsonData);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean addMeaningOp(String word, ArrayList<String> meaning){
        ArrayList<String> meaningList = new ArrayList();
        meaningList = getMeaningOp(word);
        for (String n: meaning){
            if (!meaningList.contains(n)){
                meaningList.add(n);
            }
        }

        for (int i =0; i<jsonArray.length();i++){
            if (jsonArray.getJSONObject(i).getString("word").equals(word)){
                jsonArray.getJSONObject(i).put("meaning", new JSONArray(meaningList));
            }
        }

        JSONObject newjsonData = new JSONObject();
        newjsonData.put("type", "WordDictionary");
        newjsonData.put("Content", jsonArray);
        try {
            WriteJson(p, newjsonData);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean removeOp(String word){

        for (int i =0; i<jsonArray.length();i++){
            if (jsonArray.getJSONObject(i).getString("word").equals(word)){
                jsonArray.remove(i);
            }
        }

        JSONObject newjsonData = new JSONObject();
        newjsonData.put("type", "WordDictionary");
        newjsonData.put("Content", jsonArray);
        try {
            WriteJson(p, newjsonData);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public synchronized boolean updateMeaningOp(String word, ArrayList<String> meaning){


        for (int i =0; i<jsonArray.length();i++){
            if (jsonArray.getJSONObject(i).getString("word").equals(word)){
                jsonArray.getJSONObject(i).put("meaning", new JSONArray(meaning));
            }
        }

        JSONObject newjsonData = new JSONObject();
        newjsonData.put("type", "WordDictionary");
        newjsonData.put("Content", jsonArray);
        try {
            WriteJson(p, newjsonData);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject ReadJson(String Path){
        JSONObject dataJson = null;
        // read json file
        try {
            BufferedReader br = new BufferedReader(new FileReader(Path));
            String str=null;
            String data="";
            while((str=br.readLine())!=null) {
                data=data+str+"\n";
            }
            dataJson = new JSONObject(data);
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 读取原始json文件
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataJson;
    }


    public static void WriteJson(String Path, JSONObject dataJson) {
        //write json file
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(Path));
            String ws=dataJson.toString();
            System.out.println(ws);
            bw.write(ws);
            // bw.newLine();
            bw.flush();
            bw.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 读取原始json文件
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

class ResultPackage{
    private int requestCode;
    private ArrayList meaning;

    public ResultPackage(int requestCode, ArrayList meaning) {
        this.requestCode = requestCode;
        this.meaning = meaning;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public ArrayList getMeaning() {
        return meaning;
    }

    public void setMeaning(ArrayList meaning) {
        this.meaning = meaning;
    }
}


