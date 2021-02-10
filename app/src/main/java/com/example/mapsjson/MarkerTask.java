package com.example.mapsjson;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MarkerTask extends AsyncTask<GoogleMap, Void, String> {

    private static final String LOG_TAG = "MapsJSON";
    GoogleMap mMap;

    @Override
    protected String doInBackground(GoogleMap... mMapAux) {
        mMap = mMapAux[0];
        HttpURLConnection urlConnection = null;
        final StringBuilder json = new StringBuilder();
        try {
            URL url = new URL("https://run.mocky.io/v3/1495c059-7c37-48b3-ad2d-f26dc015079b");
            urlConnection = (HttpURLConnection) url.openConnection();
            int status = urlConnection.getResponseCode();
            if (status==200) {
                //Recuperem contingut de la URL, llegim el text "per paquets de bytes"
                InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    json.append(buff, 0, read);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connectant al servei", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return json.toString();
    }

    @Override
    protected void onPostExecute(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                LatLng latLng = new LatLng(jsonObj.getDouble("latitud"),
                        jsonObj.getDouble("longitud"));

                if (i == 0) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(15).build();

                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
                MarkerOptions options = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .position(latLng);
                mMap.addMarker(options).setTag(jsonObj);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error amb el JSON", e);
        }

    }
}
