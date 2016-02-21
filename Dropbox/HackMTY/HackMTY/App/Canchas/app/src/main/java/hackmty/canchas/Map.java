package hackmty.canchas;

import android.Manifest;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class Map extends FragmentActivity implements OnMapReadyCallback {


    private ArrayList<Cancha> canchas;

    private Marker your_location;

    private LatLng latLng;

    private GoogleMap gMap = null;

    private String urlend;

    private ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        ListView lv_list = (ListView) findViewById(R.id.list);
//
//        ListAdapter adapter = new ListAdapter(getApplicationContext(), R.id.list, canchas);
//
//        lv.setAdapter(adapter);

        adapter = new ListAdapter(getApplicationContext(), R.layout.row_canchalist, canchas);

//        if(canchas!=null)
//            lv_list.setAdapter(adapter);


        int num = getIntent().getIntExtra("num", 0);

        urlend = getIntent().getStringExtra("urlend");

        String my_lat = getIntent().getStringExtra("lat");
        String my_lng = getIntent().getStringExtra("lng");

        latLng = new LatLng(Double.parseDouble(my_lat), Double.parseDouble(my_lng));


        GoogleMapOptions mapOptions = new GoogleMapOptions();

        mapOptions.camera(new CameraPosition(latLng, 15, 0, 0));


        MapFragment mMapFragment = MapFragment.newInstance(mapOptions);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);






        canchas = new ArrayList<>();

        for (int i = 0; i < num; i++) {

            String id = getIntent().getStringExtra("id" + i);
            String name = getIntent().getStringExtra("name" + i);
            String lat = getIntent().getStringExtra("lat" + i);
            String lng = getIntent().getStringExtra("lng" + i);

            double dist = getIntent().getDoubleExtra("dist" + i, 0);

            Cancha c = new Cancha(id, name, lng, lat);
            c.setDist(dist);

            canchas.add(c);

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
    public void onMapReady(final GoogleMap googleMap) {

        gMap = googleMap;

        gMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });



        your_location = googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        for (Cancha c : canchas){
            googleMap.addMarker(new MarkerOptions().position(c.getLatLng()).title(c.getName()));
            Log.i("cancha", c.getName());
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        googleMap.setMyLocationEnabled(true);

    }


    public void switchView(View v){

        LinearLayout ll_map = (LinearLayout) findViewById(R.id.map);
        ListView lv_list = (ListView)findViewById(R.id.list);

        Button btn_sv = (Button) findViewById(R.id.switch_view);

        if(ll_map.getVisibility() == View.VISIBLE){

            ll_map.setVisibility(View.GONE);

//
//        ListAdapter adapter = new ListAdapter(getApplicationContext(), R.id.list, canchas);
//
//        lv.setAdapter(adapter);

//            if(adapter!=null)
//                adapter.clear();
            adapter = new ListAdapter(getApplicationContext(), R.layout.row_canchalist, canchas);
            lv_list.setAdapter(adapter);



            lv_list.setVisibility(View.VISIBLE);
            btn_sv.setText("mapa");


        } else {

            ll_map.setVisibility(View.VISIBLE);
            lv_list.setVisibility(View.GONE);
            btn_sv.setText("lista");


        }



    }



    public void refresh(View v){



        double lat = your_location.getPosition().latitude;
        double lng = your_location.getPosition().longitude;

        latLng = new LatLng(lat,lng);

        Log.i("debugmarker1", lat + " " + lng);

        String urler = "http://45.55.30.36/api/cancha?lat=" + lat + "&long=" + lng + urlend;

        new SearchAsync().execute(urler);




    }


    private class SearchAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(Map.this);
            progressDialog.setMessage("Buscando canchas disponibles...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {


            Log.v("url request", params[0]);

            try {
                return downloadUrl(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();

            if (s == null) {
                Log.e("REQUEST ERROR", "empty response");
            } else {


                Log.i("REQUEST SUCCESS", s);

                canchas.clear();

                try {
                    JSONArray arr = new JSONArray(s);


                    Location loc = new Location("");
                    loc.setLatitude(latLng.latitude);
                    loc.setLongitude(latLng.longitude);


                    Log.i("map_loc1", loc.toString());

                    for (int i = 0; i < arr.length(); i++){

                        JSONObject obj = arr.getJSONObject(i);

                        String o_id = obj.getString("_id");
                        String o_name = obj.getString("name");
                        String o_lng = obj.getJSONArray("location").get(0).toString();
                        String o_lat = obj.getJSONArray("location").get(1).toString();

                        Location loc2 = new Location("");
                        loc2.setLongitude((double) obj.getJSONArray("location").get(0));
                        loc2.setLatitude((double) obj.getJSONArray("location").get(1));


                        Log.i("map_loc2", loc2.toString());

                        double dist = loc.distanceTo(loc2);


                        Log.i("map_dist", dist+"");

                        Cancha cancha = new Cancha(o_id,o_name,o_lng,o_lat);
                        cancha.setDist(dist);
                        canchas.add(cancha);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(gMap != null) {


                    latLng = your_location.getPosition();

                    gMap.clear();

                    gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    your_location = gMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


                    for (Cancha c : canchas) {
                        gMap.addMarker(new MarkerOptions().position(c.getLatLng()).title(c.getName()));
                        Log.i("cancha", c.getName());

                    }

                }




//                i.putExtra("")

            }


            ListView lv_list = (ListView)findViewById(R.id.list);
            adapter = new ListAdapter(getApplicationContext(), R.layout.row_canchalist, canchas);
            lv_list.setAdapter(adapter);
//            adapter.clear();
//            adapter.addAll(canchas);
////            adapter = new ListAdapter(getApplicationContext(), R.layout.row_canchalist, canchas);
//
//            adapter.notifyDataSetChanged();

        }
    }


    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.

        try {
            URL url = new URL(myurl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG_TAG", "The response is: " + response);

            is = conn.getInputStream();

            // Convert the InputStream into a string
            return readIt(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String readIt(InputStream stream) throws IOException {



        BufferedReader streamReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        return responseStrBuilder.toString();



    }



}
