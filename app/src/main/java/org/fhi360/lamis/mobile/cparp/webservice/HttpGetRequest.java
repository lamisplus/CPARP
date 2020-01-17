package org.fhi360.lamis.mobile.cparp.webservice;

import android.content.Context;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aalozie on 7/11/2016.
 */
public class HttpGetRequest {
    private Context context;
    public  HttpGetRequest(Context context){
        this.context = context;
    }
    public String getData(String serverUrl) {
        HttpURLConnection urlConnection = null;
        String result = "";
        try {
            URL url = new URL(serverUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            //Set request header
            urlConnection.setRequestProperty("Content-Type", "text/plain");
            urlConnection.setRequestMethod("GET");

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                String line = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                if (reader != null) reader.close();
            }else{
                }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }
}
