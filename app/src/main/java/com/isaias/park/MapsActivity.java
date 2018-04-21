package com.isaias.park;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {



    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        //verificacion de servicios actualizados de Google Maps
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }
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

        //tipo de mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //habilita la opcion de uso de controles
        UiSettings uiSettings = mMap.getUiSettings();

        //habilitamos controles de zoom en el mapa
        uiSettings.setZoomControlsEnabled(true);

        //control del boton mi localizacion
        uiSettings.setMyLocationButtonEnabled(true);


        // puntos de log lat
        LatLng punto1 = new LatLng(-17.798576, -63.190923);

        // marcador
        mMap.addMarker(new MarkerOptions().position(punto1).title("Cooperativa Fatima Obispo Peña 63, Santa Cruz de la Sierra").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(punto1).snippet("Este es"));


        //efectos de camara sobre los puntos
        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto1));

        //zoom para el marcador
        float zoomlevel = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto1, zoomlevel));


        //verifica y habilita la localizacion actual
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);


    }


    //Metodo para aniadir los puntos de parqueo
//    public void Puntos(GoogleMap googleMap){
//        mMap = googleMap;
//
//        // puntos de log lat
//        LatLng punto1 = new LatLng(-17.798576, -63.190923);
//
//        // marcador
//        mMap.addMarker(new MarkerOptions().position(punto1).title("Cooperativa Fatima Obispo Peña 63, Santa Cruz de la Sierra").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
//        mMap.addMarker(new MarkerOptions().position(punto1).snippet("Este es"));
//
//
//        //efectos de camara sobre los puntos
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto1));
//
//        //zoom para el marcador
//        float zoomlevel = 16;
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto1, zoomlevel));
//    }

}
