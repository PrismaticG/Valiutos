package com.example.uni0;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DataLoader extends AsyncTask<String, Void, String> {

    public interface DataCallback {
        void onDataLoaded(ArrayList<String> data);
    }

    private final DataCallback callback;

    public DataLoader(DataCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... urls) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();
        } catch (Exception e) {
            Log.e("DataLoader", "Error fetching data: " + e.getMessage());
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        Parser parser = new Parser();
        ArrayList<String> currencyData = parser.parseXML(result);
        callback.onDataLoaded(currencyData);
    }
}
