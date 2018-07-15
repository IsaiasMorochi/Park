package com.isaias.park;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private LatLng userLocation;
    private Marker mSelectedMarker;
    private Marker marker;
    private boolean mLocationPermissionGranted;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private DatabaseReference databaseReference;
    private static final String PAR = "NOMBRE  ";
    private static final String TAG = "ERROR  ";
    private static final LatLng inicio = new LatLng(-17.783264, -63.182061);


    /**
     * Constructor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //definicion de referencia a la BD
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //verificacion de servicios Google Maps
        statusServices();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setFilter(new AutocompleteFilter.Builder().setCountry("BO").build());

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                final LatLng latLngLoc = place.getLatLng();

                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(latLngLoc).title(place.getName().toString()));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this, "" + status.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Verifica el Estado de los servicios de Google Maps
     */
    private void statusServices() {
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
     * Inicializacion del Mapa Google Maps
     *
     * @param googleMap
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

        //adjuntamos los marcadores
        addMarkersToMap();

        //efectos de camara sobre los puntos
        mMap.moveCamera(CameraUpdateFactory.newLatLng(inicio));

        //zoom para el marcador
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio, 14));

        //permisos para obtener ubicacion del usuario
        mylocation();

        //Seleccion de Icono
        selectIcon();

        //Obtener la Ubicacion actual del dispositivo y establecer la posicion actual en el mapa
        getDeviceLocation();

        //Activa la capa MyLocation y el control del mapa
        updateLocationUI();
    }

    /**
     * Habilita la capa de Localizacion
     */
    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            //entra por aqui para permitir obtener nuestra ubicacioin
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            mLocationPermissionGranted = true;
        }
    }

    /**
     * Se habilitan los permisos para Acceder a la Ubicacion
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                }
            }
        }
    }

    /**
     * Acutaliza la localizacion del dispositivo
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                //Activa los controles para el botono my location
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Agregamos los marcadores, desde la BD de firebase
     */
    private void addMarkersToMap() {
        databaseReference.child("Parqueos");
        databaseReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get parqueo values
                        // Parqueos p = dataSnapshot.getValue(Parqueos.class);
                        // Log.e(PAR, p.NOMBRE);

                        for (DataSnapshot templateSnapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot snap : templateSnapshot.getChildren()) {

                                // Log.e(PAR, String.valueOf(snap.child("NOMBRE").getValue()));
                                Log.e(PAR, String.valueOf(snap));

                                String nom, dir;
                                double xlog, ylat;
                                float col;
                                nom = String.valueOf(snap.child("NOMBRE").getValue());
                                dir = String.valueOf(snap.child("DIRECCION").getValue());

                                try {

                                    xlog = Double.parseDouble(String.valueOf(snap.child("xlongitud").getValue()));
                                    ylat = Double.parseDouble(String.valueOf(snap.child("ylatitud").getValue()));

                                    //color del icono, estado de disponibilidida del parqueo
                                    col = colorIcon(String.valueOf(snap.child("Estado").getValue()));

                                    // Anadimos el marcador
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(ylat, xlog))
                                            .title(nom)
                                            .snippet(dir)
                                            .icon(BitmapDescriptorFactory.defaultMarker(col)));

                                } catch (Exception e) {
                                    System.out.println("El error es: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "Conexion Internet Lenta", databaseError.toException());
                        Toast.makeText(MapsActivity.this, "Conexion Internet Lenta.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * Determina la ubicacion del Dipositivo
     */
    private void mylocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            if (!mMap.isMyLocationEnabled())
                mMap.setMyLocationEnabled(true);

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (myLocation == null) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                String provider = lm.getBestProvider(criteria, true);
                myLocation = lm.getLastKnownLocation(provider);
            }

            if (myLocation != null) {
                //localizacion actual del usuario, punto origen
                userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            }
        }
    }

    /**
     * Metodo que Verfica el Estado de Seleccion del Icono
     */
    public void selectIcon() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // The user has re-tapped on the marker which was already showing an info window.
                if (marker.equals(mSelectedMarker)) {
                    // Retorna true indicando que consumismos el evento
                    mesjoff();

                    mMap.clear();
                    addMarkersToMap();
                    mSelectedMarker = null;
                    return true;
                }
                //registra el marcador
                mSelectedMarker = marker;
                mSelectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                mesjok();

                //traza su ruta
                //Establecemos nuestro destino y origen,
                // mandamos ejecutar una tarea de descarga en segundo plano.
                String url = obtenerDireccionesURL(userLocation, marker.getPosition());
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);

                //retorna falso indicando que no consume el evento
                return false;
            }
        });
    }

    /**
     * Determina el color del Icono segun la disponibilidad
     *
     * @param x
     * @return
     */
    private float colorIcon(String x) {
        float color;
        if (x.equals("Ocupado")) {
            color = 240.0f;  //OCUPADO = AZUL
        } else {
            color = 120.00f; //LIBRE = VERDE
        }
        return color;
    }

    /**
     * Mensaje
     */
    private void mesjoff() {
        Toast.makeText(this, "Limpiando ruta", Toast.LENGTH_SHORT).show();
    }

    /**
     * Mensaje
     */
    private void mesjok() {
        Toast.makeText(this, "Dibujando ruta", Toast.LENGTH_SHORT).show();
    }


    /**
     * En este metodo lo que hacemos es construir la url que mandaremos al web service de google maps
     * para obtener una serie de puntos con la ruta a seguir, o pintar en este caso.
     *
     * @param origin punto de partida
     * @param dest   punto de destino
     * @return devuelve la ruta
     */
    private String obtenerDireccionesURL(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     * Ruta
     *
     * @param strUrl
     * @return
     * @throws IOException
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creamos una conexion http
            urlConnection = (HttpURLConnection) url.openConnection();
            // Conectamos
            urlConnection.connect();
            // Leemos desde URL
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * Recordando que esta ruta que obtenemos con el metodo obtenerDireccionesURL
     * lo mandamos a una tarea de descarga en segundo plano
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("ERROR AL OBTENER INFO DEL WS", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    /**
     * Conectar con el web service y obtener el resultado, lo solicitamos en formato JSON,
     * despues mandamos llamar una tarea asincrona para interpretar el resultado,
     * mismo que le pasamos como parametro
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.rgb(0, 0, 255));
            }
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
        }
    }

    /**
     * Por ultimo lo que hacemos aqui es interpretar el resultado del web service de Google Maps
     * al que le mandamos nuestros puntos, crear los puntos que conforman la ruta desde el punto A hasta el punto B,
     * darles un ancho y un color para para al final agregarlo a todo al mapa como una poyline.
     */
    public class DirectionsJSONParser {

        public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;

            try {
                jRoutes = jObject.getJSONArray("routes");
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    List path = new ArrayList<HashMap<String, String>>();

                    for (int j = 0; j < jLegs.length(); j++) {
                        jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                        for (int k = 0; k < jSteps.length(); k++) {
                            String polyline = "";
                            polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list = decodePoly(polyline);

                            for (int l = 0; l < list.size(); l++) {
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("lat", Double.toString((list.get(l)).latitude));
                                hm.put("lng", Double.toString((list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
            }

            return routes;
        }

        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }

}
