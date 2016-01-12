package com.engivi.enric.recursos_euskadi;


import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetalleActivityAlojamientos extends Activity {

    TextView txtNombre, txtTipo, txtDescripcion, txtDireccion, txtTelefono, txtWeb, txtAccessibilidad;
    private MapView myOpenMapView;
    LocationManager locationManager;
    MyLocationNewOverlay myLocationOverlay = null;
    ArrayList<OverlayItem> overlayItemArray;

    //Datos Geocoder
    Geocoder myGeocoder;
    final static int MAX_RESULT = 1;
    String[] coordenadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_elemento);

        txtNombre = (TextView) findViewById(R.id.txtViewNombre);
        txtTipo = (TextView) findViewById(R.id.txtViewTipo);
        txtDescripcion = (TextView) findViewById(R.id.txtViewDescripcion);
        txtDireccion = (TextView) findViewById(R.id.txtViewDireccion);
        txtTelefono = (TextView) findViewById(R.id.txtViewTelefono);
        txtWeb = (TextView) findViewById(R.id.txtViewWeb);
        txtAccessibilidad = (TextView) findViewById(R.id.txtViewAccessibilidad);

        myOpenMapView = (MapView)findViewById(R.id.idMapa);
        //myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setMultiTouchControls(true);
        myOpenMapView.getController().setZoom(12);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //for demo, getLastKnownLocation from GPS only, not from NETWORK
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastLocation != null){
            updateLoc(lastLocation);
        }




    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            // Obtenemos el id del lugar que hay que mostrar
            Bundle extras = getIntent().getExtras();
            String stringNombre = extras.getString("nombre");
            txtNombre.setText(stringNombre);
            String stringTipo = extras.getString("tipo");
            txtTipo.setText(stringTipo);
            String stringDescripcion = extras.getString("descripcion");
            txtDescripcion.setText(stringDescripcion);
            String stringDireccion = extras.getString("direccion");
            txtDireccion.setText(stringDireccion);
            String stringDireccionMapa = extras.getString("direccionMapa");
            if (stringDireccionMapa.equals("")){
                stringDireccionMapa = stringDireccion;
            }
            String stringTelefono = extras.getString("telefono");
            txtTelefono.setText(stringTelefono);
            String stringWeb = extras.getString("web");
            txtWeb.setText(stringWeb);
            String stringAccessibilidad = extras.getString("accessibilidad");

            if (stringAccessibilidad.length()>0){
                stringAccessibilidad = "Accesible a personas con discapacidad";
                txtAccessibilidad.setText(stringAccessibilidad);
            }else {
                stringAccessibilidad = "NO accesible a personas con discapacidad";
                txtAccessibilidad.setText(stringAccessibilidad);

            }

            // GEOCODING
            myGeocoder = new Geocoder(this);

            String [] resultado;
            resultado  = searchFromLocationName(stringDireccionMapa);
            double doubleLatitud = Double.parseDouble(resultado[0]);
            double doubleLongitud = Double.parseDouble(resultado[1]);


            // Create an ArrayList with overlays to display objects on map
            overlayItemArray = new ArrayList<OverlayItem>();

            // Create som init objects
            GeoPoint localizacionRecurso = new GeoPoint(doubleLatitud, doubleLongitud);
            OverlayItem linkopingItem = new OverlayItem("Title", "Description",
                    localizacionRecurso);

            // Add the init objects to the ArrayList overlayItemArray
            overlayItemArray.add(linkopingItem);

            // Add the Array to the IconOverlay
            ItemizedIconOverlay<OverlayItem> itemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(this, overlayItemArray, null);

            // Add the overlay to the MapView
            myOpenMapView.getOverlays().add(itemizedIconOverlay);
            myOpenMapView.getController().setCenter(localizacionRecurso);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        locationManager.removeUpdates(myLocationListener);
    }


    private void updateLoc(Location loc){
        GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
        //myOpenMapView.getController().setCenter(locGeoPoint);
        myOpenMapView.invalidate();

        GpsMyLocationProvider gpsLocationProvider = new GpsMyLocationProvider(this);
        //gpsLocationProvider.setLocationUpdateMinTime(1000);
        //gpsLocationProvider.setLocationUpdateMinDistance(6000);

        myLocationOverlay = new MyLocationNewOverlay(this,new GpsMyLocationProvider(this),myOpenMapView);
        myLocationOverlay.enableMyLocation(gpsLocationProvider);
        myOpenMapView.getOverlays().add(myLocationOverlay);


    }

    private LocationListener myLocationListener;

    {
        myLocationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                updateLoc(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }

        };
    }

    // FUNCTION GEOCODING

    public String[] searchFromLocationName(String name){
        try {
            List<Address> result
                    = myGeocoder.getFromLocationName(name, MAX_RESULT);

            if ((result == null)||(result.isEmpty())){
                Toast.makeText(DetalleActivityAlojamientos.this,
                        R.string.noMapaResultado,
                        Toast.LENGTH_LONG).show();
            }else{
                //String stringResult = "";
                for (int i =0; i < result.size(); i++){
                    String AddressLine = "";
                    int maxAddressLineIndex = result.get(i).getMaxAddressLineIndex();
                    if (maxAddressLineIndex != -1){
                        for (int j = 0; j <= result.get(i).getMaxAddressLineIndex(); j++){
                            AddressLine += result.get(i).getAddressLine(j) + "\n";
                        }
                    }


                    coordenadas = new String[2];
                    coordenadas[0] = "" + result.get(i).getLatitude();
                    coordenadas[1] = "" + result.get(i).getLongitude();
                }

            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(DetalleActivityAlojamientos.this,
                    R.string.noInternet,
                    Toast.LENGTH_LONG).show();
        }
        return coordenadas;
    }
}
