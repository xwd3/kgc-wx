package cn.kgc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlUtils {
    public static String loadURL(String urlStr){
        try {
            URL url= new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            String responseStr = ConvertToString(inputStream);
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String ConvertToString(InputStream inputStream){
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder result = new StringBuilder();
        String line = null;
        try{
            while((line = bufferedReader.readLine())!=null){
                result.append(line+"\n");
            }
        }catch (IOException ex){
            ex.printStackTrace();
            return result.toString();
        }
        return result.toString();
    }
}
