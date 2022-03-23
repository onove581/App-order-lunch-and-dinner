package com.example.landdserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.landdserver.Common.Common;
import com.example.landdserver.Common.Dire;
import com.example.landdserver.Remote.IGeoCoordinates;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//
//public class TrackingOrder extends FragmentActivity implements OnMapReadyCallback,
//GoogleApiClient.ConnectionCallbacks,
//    GoogleApiClient.OnConnectionFailedListener,LocationListener {
//    private GoogleMap mMap;
//    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
//    private final static int LOCATION_PERMISSION_REQUEST = 1001;
//    private Location mLastLocation;
//
//    private GoogleApiClient googleApiClient;
//    private LocationRequest locationRequest;
//
//    private static int UPDATE_INTERVAL = 1000;
//    private static int FASTEST_INTERVAL = 5000;
//    private static int DISPLACEMENT = 10;
//    private IGeoCoordinates mService;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tracking_order);
//        mService= Common.getGeoCodeServide();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestRunTimePermission();
//        } else {
//            if (checkPlayServices()) {
//                buildGoogleApiClient();
//                createLocationRequest();
//            }
//        }
//        displaylocation();
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//
//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        mLastLocation = location;
//        displaylocation();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        displaylocation();
//        starLocationUpdate();
//
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (googleApiClient != null) {
//            googleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        checkPlayServices();
//    }
//
//
//    private void starLocationUpdate() {
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                mLastLocation = location;
//                displaylocation();
//            }
//        });
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//googleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//    private void requestRunTimePermission() {
//        ActivityCompat.requestPermissions(this, new String[]
//                {
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                }, LOCATION_PERMISSION_REQUEST);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case LOCATION_PERMISSION_REQUEST:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (checkPlayServices()) {
//                        buildGoogleApiClient();
//                        createLocationRequest();
//                        displaylocation();
//
//                    }
//                }
//                break;
//
//        }
//    }
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if(resultCode != ConnectionResult.SUCCESS)
//        {
//            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
//            {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            }
//            else
//            {
//                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }
//    protected synchronized void buildGoogleApiClient() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//
//        googleApiClient.connect();
//    }
//
//    private void createLocationRequest() {
//        locationRequest = new LocationRequest();
//        locationRequest.setInterval(UPDATE_INTERVAL);
//        locationRequest.setFastestInterval(FASTEST_INTERVAL);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setSmallestDisplacement(DISPLACEMENT);
//    }
//    private void displaylocation() {
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            requestRunTimePermission();
//        }
//        else {
//            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            if(mLastLocation != null) {
//                double latitude = mLastLocation.getLatitude();
//                double longitude = mLastLocation.getLongitude();
//                LatLng yourLocation = new LatLng(latitude, longitude);
//                mMap.addMarker(new MarkerOptions().position(yourLocation).title("Your Location"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
//            drawnroute(yourLocation,Common.currentRequest.getAddress());
//            }
//            else
//            {
////                Toast.makeText(this, "Could not get the last location!", Toast.LENGTH_SHORT).show();
//                Log.d("Debug","Couldn't get the location");
//            }
//        }
//    }
//
//    private void drawnroute(LatLng yourLocation, String address) {
//        mService.getGeoCode(address).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//               try {
//                   JSONObject jsonObject=new JSONObject(response.body().toString());
//                   String lat=((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("localtion").get("lat").toString();
//                   String lng=((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("localtion").get("lng").toString();
//                   LatLng orderloacation=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
//                   Bitmap bitmaps= BitmapFactory.decodeResource(getResources(),R.drawable.box);
//               bitmaps=Common.scaleBitmap(bitmaps,70,70);
//               MarkerOptions maker=new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmaps))
//                       .title("Order of"+Common.currentRequest.getPhone()).position(orderloacation);
//               mMap.addMarker(maker);
//
//
//               mService.getDirections(yourLocation.latitude+","+yourLocation.longitude,orderloacation.latitude+","+orderloacation.longitude).enqueue(new Callback<String>() {
//                   @Override
//                   public void onResponse(Call<String> call, Response<String> response) {
//new ParserTask().execute(response.body().toString());
//                   }
//
//                   @Override
//                   public void onFailure(Call<String> call, Throwable t) {
//
//                   }
//               });
//               }catch (JSONException e)
//
//               {
//                   e.printStackTrace();
//               }
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }
//    private class ParserTask extends AsyncTask<String,Integer, List<List<HashMap<String,String>>>>
//    {
//        ProgressDialog mg=new ProgressDialog(TrackingOrder.this);
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mg.setMessage("Pls...");
//            mg.show();
//        }
//
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
//            JSONObject object;
//            List<List<HashMap<String,String>>> routes=null;
//            try {
//                object=new JSONObject(strings[0]);
//                Dire parser=new Dire();
//                routes=parser.parse(object);
//            }catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
//            mg.dismiss();
//            ArrayList point=null;
//            PolylineOptions polylineOptions=null;
//            for (int i=0;i<lists.size();i++)
//            {
//                point=new ArrayList();
//                polylineOptions=new PolylineOptions();
//                List<HashMap<String,String>> path=lists.get(i);
//                for (int j=0;j<path.size();j++)
//                {
//                    HashMap<String,String> p=path.get(j);
//                    double lat=Double.parseDouble(p.get("lat"));
//                    double lng=Double.parseDouble(p.get("lng"));
//                    LatLng posti=new LatLng(lat,lng);
//                    point.add(posti);
//                }
//                polylineOptions.addAll(point);
//                polylineOptions.width(12);
//                polylineOptions.color(Color.RED);
//                polylineOptions.geodesic(true);
//            }
//            mMap.addPolyline(polylineOptions);
//        }
//    }
//
//}

public class TrackingOrder extends FragmentActivity implements OnMapReadyCallback,
GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private GoogleMap mMap;
        private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private final static int LOCATION_PERMISSION_REQUEST = 1001;
        private Location mLastLocation;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    private static int UPDATE_INTERVAL = 1000;
    private static int FASTEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;
        private IGeoCoordinates mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);
        mService= Common.getGeoCodeServide();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestRunTimePermission();
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
            }
        }
    }

    private void requestRunTimePermission() {
        ActivityCompat.requestPermissions(this, new String[]
                {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, LOCATION_PERMISSION_REQUEST);
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }
        protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }
        private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displaylocation();

                    }
                }
                break;

        }
    }
        private void displaylocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestRunTimePermission();
        }
        else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(mLastLocation != null) {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                LatLng yourLocation = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(yourLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            //drawnroute(yourLocation,"41 Đ. Lê Duẩn, Hải Châu 1, Hải Châu, Đà Nẵng 550000");
                LatLng nhahang=new LatLng(16.071860580945888, 108.22013490741297);
                mMap.addMarker(new MarkerOptions().position(nhahang).title("Cua hang").icon(BitmapDescriptorFactory.fromResource(R.drawable.order)));
                mMap.addPolyline(new PolylineOptions().add(
yourLocation,nhahang
                       // new LatLng(16.071860580945888, 108.22013490741297)

                ).width(10).color(Color.RED));
            }
            else
            {
//                Toast.makeText(this, "Could not get the last location!", Toast.LENGTH_SHORT).show();
                Log.d("Debug","Couldn't get the location");
            }
        }
    }
//    private void drawnroute(LatLng yourLocation, String address) {
//        mService.getGeoCode(address).enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//               try {
//                   JSONObject jsonObject=new JSONObject(response.body().toString());
//                   String lat=((JSONArray)jsonObject.get("results")).
//                           getJSONObject(0).getJSONObject("geometry").
//                           getJSONObject("location")
//                           .get("lat").toString();
//                   String lng=((JSONArray)jsonObject.get("results")).
//                           getJSONObject(0).getJSONObject("geometry").
//                           getJSONObject("location").get("lng").toString();
//                   LatLng orderloacation=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
//                   Bitmap bitmaps= BitmapFactory.decodeResource(getResources(),R.drawable.order);
//               bitmaps=Common.scaleBitmap(bitmaps,70,70);
//               MarkerOptions maker=new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmaps))
//                       .title("Order of"+Common.currentRequest.getPhone()).position(orderloacation);
//               mMap.addMarker(maker);
//                   //mMap.addMarker(new MarkerOptions().position(orderloacation).icon(BitmapDescriptorFactory.fromResource(R.drawable.order)));
//
//
////               mService.getDirections(yourLocation.latitude+","+yourLocation.longitude,orderloacation.latitude+","+orderloacation.longitude).enqueue(new Callback<String>() {
////                   @Override
////                   public void onResponse(Call<String> call, Response<String> response) {
////new ParserTask().execute(response.body().toString());
////                   }
////
////                   @Override
////                   public void onFailure(Call<String> call, Throwable t) {
////
////                   }
////               });
//               }catch (JSONException e)
//
//               {
//                   e.printStackTrace();
//               }
////
//            }
////
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
//    }
    private void starLocationUpdate() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = location;
                displaylocation();
            }
        });
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        displaylocation();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displaylocation();
        starLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
        @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
}