package com.example.friendlocation.friendlocation.PathDrawing;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

public class ReadTask extends AsyncTask<String, Void , String> {

    GoogleMap googleMap;
    public ReadTask(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    @Override
    protected String doInBackground(String... url) {
        String data = "";
        try {
            MapHttpConnection http = new MapHttpConnection();
            data = http.readUr(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        new ParserTask(googleMap).execute(result);
    }
}