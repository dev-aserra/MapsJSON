package com.example.mapsjson;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    protected GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker m) {
                return null;
            }

            @Override
            public View getInfoContents(Marker m) {
                JSONObject jsonObject = (JSONObject) m.getTag();
                View v = getLayoutInflater().inflate(R.layout.infowindow_layout, null);
                String nom = "", url = "";

                ImageView img = v.findViewById(R.id.info_window_imagen);
                try {
                    nom = jsonObject.getString("nombre");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                switch (nom) {
                    case "Jabugo":
                        url = "https://media-cdn.tripadvisor.com/media/photo-s/06/19/ee/93/la-nueva-imagen.jpg";
                        break;
                    case "Fonda Europa":
                        url = "https://www.visitgranollers.com/wp-content/uploads/2018/10/LaFondaEuropa.jpg";
                        break;
                    case "Mint":
                        url = "https://media-cdn.tripadvisor.com/media/photo-s/11/27/a6/4b/terraza-interior.jpg";
                        break;
                    case "Magrana":
                        url = "https://www.visitgranollers.com/wp-content/uploads/2018/10/LaMagrana.jpg";
                        break;
                    case "Cuynes":
                        url = "https://media-cdn.tripadvisor.com/media/photo-s/0d/45/71/d5/el-local.jpg";
                        break;
                }

                Glide
                        .with(v.getContext())
                        .load(url)
                        .fitCenter()
                        .dontAnimate()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                e.printStackTrace();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if(!dataSource.equals(DataSource.MEMORY_CACHE)) m.showInfoWindow();
                                return false;
                            }
                        }).into(img);

                TextView mNom = v.findViewById(R.id.info_window_nombre);
                mNom.setText(nom);

                TextView mDescripcio = v.findViewById(R.id.info_window_descripcion);
                try {
                    mDescripcio.setText(jsonObject.getString("descripcion"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView mTipus = v.findViewById(R.id.info_window_tipo);
                try {
                    mTipus.setText(jsonObject.getString("tipo"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView mCiutat = v.findViewById(R.id.info_window_ciudad);
                try {
                    mCiutat.setText(jsonObject.getString("ciudad"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return v;
            }
        });
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        new MarkerTask().execute(mMap);
    }
}