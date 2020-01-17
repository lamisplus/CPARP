package org.fhi360.lamis.mobile.cparp.webservice;

import android.content.Context;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aalozie on 7/10/2016.
 */
public class HttpPostRequest  {
    private Context context;
    public HttpPostRequest(Context context){
        this.context = context;
    }
     public String postData(String serverUrl, String content) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        String result = "";
        try {
            URL url = new URL(serverUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            //Set request header
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            //Upload a request body
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            writer.write(content);
            writer.flush();
            writer.close();
            //Read the response
            int statusCode = urlConnection.getResponseCode();
            if(statusCode == 200) {
                String line = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                if(reader != null) reader.close();
            }else{
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }
}
