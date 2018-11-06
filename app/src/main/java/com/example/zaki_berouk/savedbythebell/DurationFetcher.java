package com.example.zaki_berouk.savedbythebell;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DurationFetcher extends AsyncTask<String, Void, Long> {
    private static final String API_KEY = "AIzaSyB4Ug3HPkzhQh4ZUPo8KQQkhR5KgoOaifg";


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Long doInBackground(String... params) {
        String urlString = "https://maps.googleapis.com/maps/api/directions/json?key=" + API_KEY;
        urlString += "&origin=" + params[0];
        urlString += "&destination=" + params[1].replace(" ", "+");
        urlString += "&departure_time=" + params[2];

        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlString);
            Log.d("App", "doInBackground: " + url);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            try{
                JSONObject jsonObject = new JSONObject(stringBuffer.toString()).getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
                Long result = jsonObject.getJSONObject("duration_in_traffic").getLong("value");
                return result;
            } catch (Exception e){
                return Long.valueOf(1);
            }

        } catch (Exception ex) {
            Log.e("App", "DurationFetcher", ex);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}